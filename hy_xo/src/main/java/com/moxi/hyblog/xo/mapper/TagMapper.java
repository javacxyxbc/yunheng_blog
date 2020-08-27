package com.moxi.hyblog.xo.mapper;

import com.moxi.hyblog.commons.entity.Tag;
import com.moxi.hyblog.base.mapper.SuperMapper;
import com.moxi.hyblog.utils.StringUtils;
import com.moxi.hyblog.xo.global.SysConf;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * <p>
 * 标签表 Mapper 接口
 * </p>
 *
 */
/**
 * @author hzh
 * @since 2020-08-07
 */
public interface TagMapper extends SuperMapper<Tag> {
    //根据uids获取tag标签
    @SelectProvider(type = TagProvider.class,method ="getTagsByUid")
    public List<Tag>  getTagsByUid(@Param("tag_uid") String uid);



    public class TagProvider{
        public String getTagsByUid(@Param("tag_uid") String uid){
            List<String> uids= StringUtils.changeStringToString(uid,SysConf.FILE_SEGMENTATION);
            StringBuilder sb =new StringBuilder();
            sb.append("SELECT t1.uid,t1.content FROM t_tag t1 WHERE t1.uid IN (");
            for (int i = 0; i < uids.size() ; i++) {
                sb.append("'"+uids.get(i)+"'");
                if(i!=uids.size()-1)sb.append(",");
            }
            sb.append(")");
            return sb.toString();
        }
    }
}
