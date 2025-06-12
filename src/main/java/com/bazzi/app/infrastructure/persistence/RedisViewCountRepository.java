package com.bazzi.app.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisViewCountRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String TODAY_KEY_PREFIX = "today:";
    private static final String TOTAL_KEY_PREFIX = "total:";

    //오늘 조회수 조회
    public int getTodayViewCount(String username) {
        try{
            String todayKey = TODAY_KEY_PREFIX + username;
            String today = redisTemplate.opsForValue().get(todayKey);
            return Integer.parseInt(today != null ? today : "0");
        } catch (Exception e){
            throw new RuntimeException("오늘 조회수를 가져오는 중 오류 발생: " + e.getMessage(), e);
        }
    }

    //전체 조회수 조회
    public int getTotalViewCount(String username) {
        try {
            String totalKey = TOTAL_KEY_PREFIX + username;
            String total = redisTemplate.opsForValue().get(totalKey);
            return Integer.parseInt(total != null ? total : "0");
        } catch (Exception e){
            throw new RuntimeException("전체 조회수를 가져오는 중 오류 발생: " + e.getMessage(), e);
        }
    }

    //오늘 조회수 증가
    public void incrementTodayViewCount(String username) {
        try {
            String todayKey = TODAY_KEY_PREFIX + username;
            redisTemplate.opsForValue().increment(todayKey, 1);
        } catch (Exception e){
            throw new RuntimeException("오늘 조회수 증가 중 오류 발생: " + e.getMessage(), e);
        }
    }

    //전체 조회수 증가
    public void incrementTotalViewCount(String username) {
        try {
            String totalKey = TOTAL_KEY_PREFIX + username;
            redisTemplate.opsForValue().increment(totalKey, 1);
        } catch (Exception e){
            throw new RuntimeException("전체 조회수 증가 중 오류 발생: " + e.getMessage(), e);
        }
    }

    //오늘, 전체 조회수 초기화
    public void resetViewCount(String username) {
        try {
            String todayKey = TODAY_KEY_PREFIX + username;
            String totalKey = TOTAL_KEY_PREFIX + username;
            redisTemplate.opsForValue().set(todayKey, "0");
            redisTemplate.opsForValue().set(totalKey, "0");
        } catch (Exception e){
            throw new RuntimeException("조회수 초기화 중 오류 발생: " + e.getMessage(), e);
        }
    }

    //오늘 조회수, 전체 조회수에 업데이트
    public void updateTotalWithToday(String username){
        try{
            String todayKey = TODAY_KEY_PREFIX + username;
            String totalKey = TOTAL_KEY_PREFIX + username;
            String todayCount = redisTemplate.opsForValue().get(todayKey);
            int today = Integer.parseInt(todayCount != null ? todayCount : "0");
            if (today > 0) {
                redisTemplate.opsForValue().increment(totalKey, today);
                redisTemplate.opsForValue().set(todayKey, "0"); // 오늘 조회수 초기화
            }
        } catch (Exception e){
            throw new RuntimeException("오늘 조회수, 전체 조회수에 업데이트 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
