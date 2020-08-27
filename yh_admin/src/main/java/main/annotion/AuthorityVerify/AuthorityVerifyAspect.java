package main.annotion.AuthorityVerify;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import main.base.enums.EMenuType;
import main.base.enums.EStatus;
import main.base.global.ECode;
import main.commons.entity.Admin;
import main.commons.entity.CategoryMenu;
import main.commons.entity.Role;
import main.global.MessageConf;
import main.global.RedisConf;
import main.global.SQLConf;
import main.global.SysConf;
import main.util.utils.JsonUtils;
import main.util.utils.RedisUtil;
import main.util.utils.ResultUtil;
import main.util.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**权限校验切面
 * @author hzh
 * @since 2020-08-07
 */
public class AuthorityVerifyAspect {

    @Autowired
    CategoryMenuService categoryMenuService;

    @Autowired
    RoleService roleService;

    @Autowired
    AdminService adminService;

    @Autowired
    RedisUtil redisUtil;

    //定义切入点
    @Pointcut(value = "@annotation(authorityVerify)")
    public void pointcut(AuthorityVerify authorityVerify) {

    }
    //连接点
    @Around(value = "pointcut(authorityVerify)")
    public Object doAround(ProceedingJoinPoint joinPoint,AuthorityVerify authorityVerify) throws Throwable {
        //1.获取请求
        ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        //2.请求路径
        String url=request.getRequestURI();
        //3.解析请求者的和用户名
        String adminUid=request.getAttribute(SysConf.ADMIN_UID).toString();
        //4.获取权限列表,先从redis中获取权限列表数据,不存在则到mysql中查询
        String visitUrl = redisUtil.get(RedisConf.ADMIN_VISIT_MENU + RedisConf.SEGMENTATION + adminUid);
        List<String> urlList = new ArrayList<>();
        if (StringUtils.isNotEmpty(visitUrl)) {
            //若存在,则转化成list
            urlList = JsonUtils.jsonToList(visitUrl, String.class);
        }else{
            //不存在,则查询数据库
            Admin admin = adminService.getById(adminUid);

            String roleUid = admin.getRoleUid();

            Role role = roleService.getById(roleUid);

            String caetgoryMenuUids = role.getCategoryMenuUids();

            String[] uids = caetgoryMenuUids.replace("[", "").replace("]", "").replace("\"", "").split(",");

            List<String> categoryMenuUids = new ArrayList<>(Arrays.asList(uids));

            // 这里只需要查询访问的按钮
            QueryWrapper<CategoryMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.in(SQLConf.UID, categoryMenuUids);
            queryWrapper.eq(SQLConf.MENU_TYPE, EMenuType.BUTTON);
            queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
            List<CategoryMenu> buttonList = categoryMenuService.list(queryWrapper);

            for (CategoryMenu item : buttonList) {
                if (StringUtils.isNotEmpty(item.getUrl())) {
                    urlList.add(item.getUrl());
                }
            }
            // 将访问URL存储到Redis中
            redisUtil.setEx(RedisConf.ADMIN_VISIT_MENU + SysConf.REDIS_SEGMENTATION + adminUid, JsonUtils.objectToJson(urlList).toString(), 1, TimeUnit.HOURS);
        }
        //5.判断该角色是否能够访问该接口
        boolean flag=false;
        for (String item : urlList) {
            if (url.equals(item)) {
                flag = true;
                System.out.println("用户拥有操作权限，访问的路径: {}，拥有的权限接口：{}"+url+"  "+item);
                break;
            }
        }
        if (!flag) {
            System.out.println("用户不具有操作权限，访问的路径: "+url );
            return ResultUtil.result(ECode.NO_OPERATION_AUTHORITY, MessageConf.RESTAPI_NO_PRIVILEGE);
        }

        //继续执行请求任务
        return joinPoint.proceed();
    }
}
