package com.moxi.hyblog.xo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author hzh
 * @version 1.0
 * @date 2020/8/25 19:44
 */
@Service
public class RedisBitUtil {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    //用户点赞
    public boolean thumbUp(String key,int userId){
        if(getBit(key,userId)){
            //如果已经点赞,则取消点赞
            return false;
        }else{
            setBit(key,(long)userId,true);
            return true;
        }
    }

    //判断是否点过赞
    public boolean isThumbUp(String key,int userId){
        return getBit(key,userId);
    }
    //bitmap操作
    /**
     * setbit 设置一个值
     *
     * @param key
     * @param offset
     * @param value
     */
    public void setBit(String key, Long offset, Boolean value) {

        redisTemplate.execute((RedisCallback<Boolean>) con -> con.setBit(key.getBytes(),offset , value));
    }

    /**
     * getBit
     * 获取指定offset的值
     *
     * @param key
     * @param offset
     * @return
     */

    public Boolean getBit(String key, Integer offset) {

        return redisTemplate.execute((RedisCallback<Boolean>) con -> con.getBit(key.getBytes(), offset));

    }

    /**
     * bitcount
     * @param key
     * @return
     */
    public Integer bitCount(String key) {
        return redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes())).intValue();
    }

    public Long bitOp(RedisStringCommands.BitOperation op, String saveKey, String... desKey) {
        byte[][] bytes = new byte[desKey.length][];
        for (int i = 0; i < desKey.length; i++) {
            bytes[i] = desKey[i].getBytes();
        }
        return redisTemplate.execute((RedisCallback<Long>) con -> con.bitOp(op, saveKey.getBytes(), bytes));
    }
}
