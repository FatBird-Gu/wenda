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
    private static final String PREFLIX_UV = "uv";
    private static final String PREFLIX_DAU = "dau";
    private static final String PREFLIX_POST = "post";

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

    // 单体uv
    public static String getUVKey(String date){
        return PREFLIX_UV+SPLIT+date;
    }

    // 区间uv
    public static String getUVKey(String start, String end){
        return PREFLIX_UV+SPLIT+start+SPLIT+end;
    }
    // 活跃用户dau
    public static String getDAUKey(String date){
        return PREFLIX_DAU+SPLIT+date;
    }
    // 区间活跃用户dau
    public static String getDAUKey(String start, String end){
        return PREFLIX_DAU+SPLIT+start+SPLIT+end;
    }

    // 帖子分数
    public static String getPostScoreKey(){
        return PREFLIX_POST+SPLIT+"score";
    }
}
