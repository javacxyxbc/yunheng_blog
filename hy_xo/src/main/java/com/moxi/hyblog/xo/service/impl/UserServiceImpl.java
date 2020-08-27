package com.moxi.hyblog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.moxi.hyblog.utils.*;
import com.moxi.hyblog.commons.entity.User;
import com.moxi.hyblog.commons.feign.PictureFeignClient;
import com.moxi.hyblog.xo.global.MessageConf;
import com.moxi.hyblog.xo.global.SQLConf;
import com.moxi.hyblog.xo.global.SysConf;
import com.moxi.hyblog.xo.utils.WebUtil;
import com.moxi.hyblog.xo.vo.UserVO;
import com.moxi.hyblog.xo.mapper.UserMapper;
import com.moxi.hyblog.xo.service.SysParamsService;
import com.moxi.hyblog.xo.service.UserService;
import com.moxi.hyblog.base.enums.EStatus;
import com.moxi.hyblog.base.global.BaseSQLConf;
import com.moxi.hyblog.base.global.BaseSysConf;
import com.moxi.hyblog.base.holder.RequestHolder;
import com.moxi.hyblog.base.serviceImpl.SuperServiceImpl;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
@Service
public class UserServiceImpl extends SuperServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    WebUtil webUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SysParamsService sysParamsService;
    @Autowired
    private PictureFeignClient pictureFeignClient;


    public Map<String, User> getListByIds(List<String> uids) {
            //avatar,uid,nickname
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.in(SQLConf.UID,uids);
        queryWrapper.eq(SQLConf.STATUS,EStatus.ENABLE);
        queryWrapper.select(SQLConf.AVATAR,SQLConf.UID,SQLConf.NICK_NAME);
        List<User> list= userService.list(queryWrapper);
        Map<String,User> userInfos=new HashMap<>();
        for(User user:list){
            if(StringUtils.isNotEmpty(user.getUid())){
                userInfos.put(user.getUid(),user);
            }
        }
        return userInfos;
    }

    @Override
    public User insertUserInfo(HttpServletRequest request, String response) {
        Map<String, Object> map = JsonUtils.jsonToMap(response);
        boolean exist = false;
        User user = new User();
        Map<String, Object> data = JsonUtils.jsonToMap(JsonUtils.objectToJson(map.get("data")));
        if (data.get("uuid") != null && data.get("source") != null) {
            if (getUserBySourceAnduuid(data.get("source").toString(), data.get("uuid").toString()) != null) {
                user = getUserBySourceAnduuid(data.get("source").toString(), data.get("uuid").toString());
                exist = true;
            }
        } else {
            System.out.println("未获取到uuid或source");
            return null;
        }

        if (data.get("email") != null) {
            user.setEmail(data.get("email").toString());
        }
        if (data.get("avatar") != null) {
            user.setAvatar(data.get("avatar").toString());
        }
        if (data.get("nickname") != null) {
            user.setNickName(data.get("nickname").toString());
        }
        user.setLoginCount(user.getLoginCount() + 1);
        user.setLastLoginTime(new Date());
        user.setLastLoginIp(IpUtils.getIpAddr(request));
        if (exist) {
            user.updateById();
            System.out.println("updata");
        } else {
            /*初始化*/
            user.setUuid(data.get("uuid").toString());
            user.setSource(data.get("source").toString());
            user.setUserName("mg".concat(user.getSource()).concat(user.getUuid()));
            Integer randNum = (int) (Math.random() * (999999) + 1);//产生(0,999999]之间的随机数
            String workPassWord = String.format("%06d", randNum);//进行六位数补全
            user.setPassWord(workPassWord);
            user.insert();
        }
        return user;
    }

    @Override
    public User getUserBySourceAnduuid(String source, String uuid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BaseSQLConf.UUID, uuid).eq(BaseSQLConf.SOURCE, source);
        return userService.getOne(queryWrapper);

    }

    @Override
    public Integer getUserCount(int status) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BaseSQLConf.STATUS, status);
        return userService.count(queryWrapper);
    }

    @Override
    public User serRequestInfo(User user) {
        HttpServletRequest request = RequestHolder.getRequest();
        Map<String, String> map = IpUtils.getOsAndBrowserInfo(request);
        String os = map.get("OS");
        String browser = map.get("BROWSER");
        String ip = IpUtils.getIpAddr(request);
        user.setLastLoginIp(ip);
        user.setOs(os);
        user.setBrowser(browser);
        user.setLastLoginTime(new Date());

        //从Redis中获取IP来源
        String jsonResult = stringRedisTemplate.opsForValue().get("IP_SOURCE:" + ip);
        if (StringUtils.isEmpty(jsonResult)) {
            String addresses = IpUtils.getAddresses("ip=" + ip, "utf-8");
            if (StringUtils.isNotEmpty(addresses)) {
                user.setIpSource(addresses);
                stringRedisTemplate.opsForValue().set("IP_SOURCE" + BaseSysConf.REDIS_SEGMENTATION + ip, addresses, 24, TimeUnit.HOURS);
            }
        } else {
            user.setIpSource(jsonResult);
        }
        return user;
    }

    @Override
    public List<User> getUserListByIds(List<String> ids) {
        List<User> userList = new ArrayList<>();
        if (ids == null || ids.size() == 0) {
            return userList;
        }

        Collection<User> userCollection = userService.listByIds(ids);
        userCollection.forEach(item -> {
            userList.add(item);
        });
        return userList;
    }

    @Override
    public IPage<User> getPageList(UserVO userVO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 查询用户名
        if (StringUtils.isNotEmpty(userVO.getKeyword()) && !StringUtils.isEmpty(userVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.USER_NAME, userVO.getKeyword().trim()).or().like(SQLConf.NICK_NAME, userVO.getKeyword().trim());
        }
        if (StringUtils.isNotEmpty(userVO.getSource()) && !StringUtils.isEmpty(userVO.getSource().trim())) {
            queryWrapper.eq(SQLConf.SOURCE, userVO.getSource().trim());
        }
        if (userVO.getCommentStatus() != null) {
            queryWrapper.eq(SQLConf.COMMENT_STATUS, userVO.getCommentStatus());
        }
        queryWrapper.select(User.class, i -> !i.getProperty().equals(SQLConf.PASS_WORD));
        Page<User> page = new Page<>();
        page.setCurrent(userVO.getCurrentPage());
        page.setSize(userVO.getPageSize());
        queryWrapper.ne(SQLConf.STATUS, EStatus.DISABLED);

        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        IPage<User> pageList = userService.page(page, queryWrapper);
        return pageList;
    }

    @Override
    public String editUser(UserVO userVO) {
        User user = userService.getById(userVO.getUid());
        user.setEmail(userVO.getEmail());
        user.setStartEmailNotification(userVO.getStartEmailNotification());
        user.setOccupation(userVO.getOccupation());
        user.setGender(userVO.getGender());
        user.setQqNumber(userVO.getQqNumber());
        user.setSummary(userVO.getSummary());
        user.setBirthday(userVO.getBirthday());
        user.setAvatar(userVO.getAvatar());
        user.setNickName(userVO.getNickName());
        user.setUserTag(userVO.getUserTag());
        user.setCommentStatus(userVO.getCommentStatus());
        user.setUpdateTime(new Date());
        user.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String deleteUser(UserVO userVO) {
        User user = userService.getById(userVO.getUid());
        user.setStatus(EStatus.DISABLED);
        user.setUpdateTime(new Date());
        user.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
    }

    @Override
    public String resetUserPassword(UserVO userVO) {
        String defaultPassword = sysParamsService.getSysParamsValueByKey(SysConf.SYS_DEFAULT_PASSWORD);
        if (StringUtils.isEmpty(defaultPassword)) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.PLEASE_CONFIGURE_A_PASSWORD);
        }
        User user = userService.getById(userVO.getUid());
        user.setPassWord(MD5Utils.string2MD5(defaultPassword));
        user.setUpdateTime(new Date());
        user.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }
}
