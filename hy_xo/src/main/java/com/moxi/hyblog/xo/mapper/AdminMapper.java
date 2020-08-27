package com.moxi.hyblog.xo.mapper;

import com.moxi.hyblog.commons.entity.Admin;
import com.moxi.hyblog.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
public interface AdminMapper extends SuperMapper<Admin> {

    /**
     * 通过uid获取管理员
     *
     * @return
     */

    public Admin getAdminByUid(@Param("uid") String uid);

    /**
     * 获取admin的权限字符串
     */
    @Select("SELECT c.str AS str  FROM (SELECT b.category_menu_uids AS str\n" +
            "FROM t_admin a JOIN t_role b \n" +
            "ON a.role_uid=b.uid WHERE a.uid=#{uid})c;\n" +
            ";\n")
    public String getAuthority(@Param("uid")String uid);
}
