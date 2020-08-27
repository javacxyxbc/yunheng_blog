package com.moxi.hyblog.picture.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.hyblog.base.entity.SuperEntity;


/**
 * <p>
 *
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-17
 */
@TableName("t_file_sort")
public class FileSort extends SuperEntity<FileSort> {

    private static final long serialVersionUID = 1L;

    private String projectName;

    private String sortName;

    private String url;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
