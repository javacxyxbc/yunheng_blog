package com.moxi.hyblog.commons.entity;

import java.util.List;

/**
 * @author hzh
 * @version 1.0
 * @date 2020/8/21 21:19
 */

public class archived {
    private int year;
    private List<Blog> articles;
    public archived(int year,List<Blog> articles){
        this.year=year;
        this.articles=articles;
    }
}
