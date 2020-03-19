package com.nowcoder.wenda.util;

public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PRELIX_ENTITY_LIKE = "like:entity";
    // 生成某个实体的赞
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId){
        return PRELIX_ENTITY_LIKE+SPLIT+entityType + SPLIT + entityId;
    }
}
