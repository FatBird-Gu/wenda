package com.nowcoder.wenda.util;

public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PRELIX_ENTITY_LIKE = "like:entity";
    private static final String PREFLIX_USER_LIKE = "like:user";
    private static final String PREFLIX_FOLLOWER = "follower";
    private static final String PREFLIX_FOLLOWEE = "followee";
    private static final String PREFLIX_KAPTCHA = "kaptcha";
    private static final String PREFLIX_TICKET = "ticket";
    private static final String PREFLIX_USER = "user";


    // 生成某个实体的赞
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId){
        return PRELIX_ENTITY_LIKE+SPLIT+entityType + SPLIT + entityId;
    }

    // 某个用户的赞
    // like:user:userId->int
    public static String getUserLikeKey(int userId){
        return PREFLIX_USER_LIKE+SPLIT+userId;
    }

    //某个用户的关注实体
    // followee:userid:entityType ->Zset(entityId, now) A 关注 B
    public static String getFolloweeKey(int userId, int entityType){
        return PREFLIX_FOLLOWEE+SPLIT+userId+SPLIT+entityType;
    }

    // 某个用户拥有的粉丝
    // follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(int entityType, int entityId){
        return PREFLIX_FOLLOWER+SPLIT+entityType+SPLIT+entityId;
    }

    // 登录验证码
    public static String getKaptchaKey(String owner){
        return PREFLIX_KAPTCHA + SPLIT + owner;
    }
    //登录凭证
    public static String getTicketKey(String ticket){
        return PREFLIX_TICKET + SPLIT + ticket;
    }

    //用户
    public static String getUserKey(int userId){
        return PREFLIX_USER + SPLIT + userId;
    }
}
