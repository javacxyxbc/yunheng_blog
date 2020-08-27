package com.moxi.hyblog.admin.restapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.moxi.hyblog.admin.global.MessageConf;
import com.moxi.hyblog.admin.global.RedisConf;
import com.moxi.hyblog.admin.global.SQLConf;
import com.moxi.hyblog.admin.global.SysConf;
import com.moxi.hyblog.utils.*;
import com.moxi.hyblog.commons.config.jwt.Audience;
import com.moxi.hyblog.commons.config.jwt.JwtHelper;
import com.moxi.hyblog.commons.entity.Admin;
import com.moxi.hyblog.commons.entity.CategoryMenu;
import com.moxi.hyblog.commons.entity.Role;
import com.moxi.hyblog.commons.feign.PictureFeignClient;
import com.moxi.hyblog.xo.service.AdminService;
import com.moxi.hyblog.xo.service.CategoryMenuService;
import com.moxi.hyblog.xo.service.RoleService;
import com.moxi.hyblog.xo.utils.WebUtil;
import com.moxi.hyblog.base.enums.EMenuType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 登录管理RestApi(为了更好地使用security放行把登录管理放在AuthRestApi中)
 * </p>
 *
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
@RestController
@RequestMapping("/auth")
@Api(value = "登录管理相关接口", tags = {"登录管理相关接口"})
@Slf4j
//ok
public class LoginRestApi {

    @Autowired
    WebUtil webUtil;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private CategoryMenuService categoryMenuService;

    @Autowired
    private Audience audience;

    @Value(value = "${tokenHead}")
    private String tokenHead;

    @Value(value = "${isRememberMeExpiresSecond}")
    private int longExpiresSecond;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PictureFeignClient pictureFeignClient;

    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping("/login")
    //1.参数验证
    //2.判断密码错误多少次
    //3.判断哪种类型的登录(邮箱,手机,用户名)
    //4.获取用户角色和权限,
    //5.创建token token:uid+name+clientId,过期时间,加密方式
    //6.登录相关操作
    //7.返回token
    public String login(HttpServletRequest request,
                        @ApiParam(name = "username", value = "用户名或邮箱或手机号", required = false) @RequestParam(name = "username", required = false) String username,
                        @ApiParam(name = "password", value = "密码", required = false) @RequestParam(name = "password", required = false) String password,
                        @ApiParam(name = "isRememberMe", value = "是否记住账号密码", required = false) @RequestParam(name = "isRememberMe", required = false, defaultValue = "0") int isRememberMe) {

        //1.参数验证
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResultUtil.result(SysConf.ERROR, "账号或密码不能为空");
        }

        //2.查看登录错误次数
        String ip = IpUtils.getIpAddr(request);
        String limitCount = redisUtil.get(RedisConf.LOGIN_LIMIT + RedisConf.SEGMENTATION + ip);
        if (StringUtils.isNotEmpty(limitCount)) {
            Integer tempLimitCount = Integer.valueOf(limitCount);
            if (tempLimitCount >= 5) {
                return ResultUtil.result(SysConf.ERROR, "密码输错次数过多,已被锁定30分钟");
            }
        }


        //3.验证密码是否正确
        Boolean isEmail = CheckUtils.checkEmail(username);
        Boolean isMobile = CheckUtils.checkMobileNumber(username);
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if (isEmail) {
            queryWrapper.eq(SQLConf.EMAIL, username);
        } else if (isMobile) {
            queryWrapper.eq(SQLConf.MOBILE, username);
        } else {
            queryWrapper.eq(SQLConf.USER_NAME, username);
        }

        Admin admin = adminService.getOne(queryWrapper);
        if (admin == null) {
            // 设置错误登录次数
            return ResultUtil.result(SysConf.ERROR, String.format(MessageConf.LOGIN_ERROR, setLoginCommit(request)));
        }
        //验证密码
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean isPassword = encoder.matches(password, admin.getPassWord());
        if (!isPassword) {
            //密码错误，返回提示
            return ResultUtil.result(SysConf.ERROR, String.format(MessageConf.LOGIN_ERROR, setLoginCommit(request)));
        }


        //4.获取用户角色和权限
        List<String> roleUids = new ArrayList<>();
        roleUids.add(admin.getRoleUid());
        List<Role> roles = (List<Role>) roleService.listByIds(roleUids);

        if (roles.size() <= 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.NO_ROLE);
        }
        String roleNames = null;
        for (Role role : roles) {
            roleNames += (role.getRoleName() + ",");
        }
        String roleName = roleNames.substring(0, roleNames.length() - 2);
        long expiration = isRememberMe == 1 ? longExpiresSecond : audience.getExpiresSecond();
        //5.创建token
        String jwtToken = jwtHelper.createJWT(admin.getUserName(),
                admin.getUid(),
                roleName.toString(),
                audience.getClientId(),
                audience.getName(),
                expiration * 1000,
                audience.getBase64Secret());
        String token = tokenHead + jwtToken;
        Map<String, Object> result = new HashMap<>();
        result.put(SysConf.TOKEN, token);

        //进行登录相关操作
        Integer count = admin.getLoginCount() + 1;
        admin.setLoginCount(count);
        admin.setLastLoginIp(IpUtils.getIpAddr(request));
        admin.setLastLoginTime(new Date());
        admin.updateById();

        // 设置token到validCode，用于记录登录用户
        admin.setValidCode(token);
        admin.setRole(roles.get(0));
        // 添加在线用户到Redis中
        adminService.addOnlineAdmin(admin);

        return ResultUtil.result(SysConf.SUCCESS, result);
    }




    @ApiOperation(value = "获取当前用户的菜单", notes = "获取当前用户的菜单", response = String.class)
    @GetMapping(value = "/getMenu")
    public String getMenu(HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();
        Collection<CategoryMenu> categoryMenuList = new ArrayList<>();
        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());

        List<String> roleUid = new ArrayList<>();
        roleUid.add(admin.getRoleUid());
        Collection<Role> roleList = roleService.listByIds(roleUid);

        List<String> categoryMenuUids = new ArrayList<>();

        roleList.forEach(item -> {
            String caetgoryMenuUids = item.getCategoryMenuUids();
            String[] uids = caetgoryMenuUids.replace("[", "").replace("]", "").replace("\"", "").split(",");
            for (int a = 0; a < uids.length; a++) {
                categoryMenuUids.add(uids[a]);
            }
        });
        categoryMenuList = categoryMenuService.listByIds(categoryMenuUids);

        // 从三级级分类中查询出 二级分类
        List<CategoryMenu> buttonList = new ArrayList<>();
        Set<String> secondMenuUidList = new HashSet<>();
        categoryMenuList.forEach(item -> {
            // 查询二级分类
            if (item.getMenuType() == EMenuType.MENU && item.getMenuLevel() == SysConf.TWO) {
                secondMenuUidList.add(item.getUid());
            }
            // 从三级分类中，得到二级分类
            if (item.getMenuType() == EMenuType.BUTTON && StringUtils.isNotEmpty(item.getParentUid())) {
                // 找出二级菜单
                secondMenuUidList.add(item.getParentUid());
                // 找出全部按钮
                buttonList.add(item);
            }
        });

        Collection<CategoryMenu> childCategoryMenuList = new ArrayList<>();
        Collection<CategoryMenu> parentCategoryMenuList = new ArrayList<>();
        List<String> parentCategoryMenuUids = new ArrayList<>();

        if (secondMenuUidList.size() > 0) {
            childCategoryMenuList = categoryMenuService.listByIds(secondMenuUidList);
        }

        childCategoryMenuList.forEach(item -> {
            //选出所有的二级分类
            if (item.getMenuLevel() == SysConf.TWO) {

                if (StringUtils.isNotEmpty(item.getParentUid())) {
                    parentCategoryMenuUids.add(item.getParentUid());
                }
            }
        });

        if (parentCategoryMenuUids.size() > 0) {
            parentCategoryMenuList = categoryMenuService.listByIds(parentCategoryMenuUids);
        }

        List<CategoryMenu> list = new ArrayList<>(parentCategoryMenuList);

        //对parent进行排序
        Collections.sort(list);
        map.put(SysConf.PARENT_LIST, list);
        map.put(SysConf.SON_LIST, childCategoryMenuList);
        map.put(SysConf.BUTTON_LIST, buttonList);
        return ResultUtil.result(SysConf.SUCCESS, map);
    }

    @ApiOperation(value = "用户信息", notes = "用户信息", response = String.class)
    @GetMapping(value = "/info")
    public String info(HttpServletRequest request,
                       @ApiParam(name = "token", value = "token令牌", required = false) @RequestParam(name = "token", required = false) String token) {

        Map<String, Object> map = new HashMap<>();
        if (request.getAttribute(SysConf.ADMIN_UID) == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }
        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());
        map.put(SysConf.TOKEN, token);
        map.put(SysConf.AVATAR,admin.getAvatar());

        List<String> roleUid = new ArrayList<>();
        roleUid.add(admin.getRoleUid());
        Collection<Role> roleList = roleService.listByIds(roleUid);
        map.put(SysConf.ROLES, roleList);
        return ResultUtil.result(SysConf.SUCCESS, map);
    }


    @ApiOperation(value = "退出登录", notes = "退出登录", response = String.class)
    @PostMapping(value = "/logout")
    public String logout() {
        ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attribute.getRequest();
        String token = request.getAttribute(SysConf.TOKEN).toString();
        redisUtil.delete(RedisConf.LOGIN_TOKEN_KEY + RedisConf.SEGMENTATION + token);
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }

    /**
     * 设置登录限制，返回剩余次数
     * 密码错误五次，将会锁定10分钟
     *
     * @param request
     */
    private Integer setLoginCommit(HttpServletRequest request) {
        String ip = IpUtils.getIpAddr(request);
        String count = redisUtil.get(RedisConf.LOGIN_LIMIT + RedisConf.SEGMENTATION + ip);
        Integer surplusCount = 5;
        if (StringUtils.isNotEmpty(count)) {
            Integer countTemp = Integer.valueOf(count) + 1;
            surplusCount = surplusCount - countTemp;
            redisUtil.setEx(RedisConf.LOGIN_LIMIT + RedisConf.SEGMENTATION + ip, String.valueOf(countTemp), 10, TimeUnit.MINUTES);
        } else {
            surplusCount = surplusCount - 1;
            redisUtil.setEx(RedisConf.LOGIN_LIMIT + RedisConf.SEGMENTATION + ip, "1", 30, TimeUnit.MINUTES);
        }

        return surplusCount;
    }

}
