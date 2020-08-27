package com.moxi.hyblog.xo.mapper;

import com.moxi.hyblog.commons.entity.CategoryMenu;
import com.moxi.hyblog.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.ArrayList;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年11月23日10:41:04
 */
public interface CategoryMenuMapper extends SuperMapper<CategoryMenu> {
    @SelectProvider(type = CategoryProvider.class,method = "getAuthorities")
    public String[] getAuthorities(@Param("categoryMenuUids") ArrayList<String>list);



    public class CategoryProvider{
        public String getAuthorities(@Param("categoryMenuUids") ArrayList<String>list){
            StringBuilder sb=new StringBuilder();
            sb.append("SELECT url FROM t_category_menu WHERE uid IN (");
            for (int i = 0; i <list.size() ; i++) {
                sb.append("'"+list.get(i)+"'");
                if(i!=list.size()-1)sb.append(",");
            }
            sb.append(") and menu_type=1 and status=1");
            return sb.toString();
        }
    }

}
