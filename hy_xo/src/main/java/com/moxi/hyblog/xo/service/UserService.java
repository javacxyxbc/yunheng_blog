package com.moxi.hyblog.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moxi.hyblog.commons.entity.User;
import com.moxi.hyblog.xo.vo.UserVO;
import com.moxi.hyblog.base.service.SuperService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
public interface UserService extends SuperService<User> {

    /**
     * 通过uids获取获取用户的基本信息
     * @param uids
     * @return
     */
    public Map<String, User> getListByIds(List<String> uids);

    /**
     * 记录用户信息
     *
     * @param response
     */
    User insertUserInfo(HttpServletRequest request, String response);

    /**
     * 通过source uuid获取用户类
     *
     * @param source
     * @param uuid
     * @return
     */
    User getUserBySourceAnduuid(String source, String uuid);

    /**
     * 获取用户数
     *
     * @param status
     * @return
     */
    public Integer getUserCount(int status);

    /**
     * 设置Request相关，如浏览器，IP，IP来源
     *
     * @param user
     * @return
     */
    public User serRequestInfo(User user);

    /**
     * 通过ids获取用户列表
     *
     * @param ids
     * @return
     */
    public List<User> getUserListByIds(List<String> ids);

    /**
     * 获取用户列表
     *
     * @param userVO
     * @return
     */
    public IPage<User> getPageList(UserVO userVO);


    /**
     * 编辑用户
     *
     * @param userVO
     */
    public String editUser(UserVO userVO);

    /**
     * 删除用户
     *
     * @param userVO
     */
    public String deleteUser(UserVO userVO);

    /**
     * 重置用户密码
     *
     * @param userVO
     * @return
     */
    public String resetUserPassword(UserVO userVO);


}
