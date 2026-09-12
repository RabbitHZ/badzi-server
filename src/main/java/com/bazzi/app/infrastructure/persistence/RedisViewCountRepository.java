package com.bazzi.app.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisViewCountRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String TODAY_KEY_PREFIX = "today:";
    private static final String TOTAL_KEY_PREFIX = "total:";
    private static final Duration TODAY_TTL = Duration.ofHours(24);

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

    //오늘 조회수 증가 (키 최초 생성 시 TTL 24h 설정)
    public void incrementTodayViewCount(String username) {
        try {
            String todayKey = TODAY_KEY_PREFIX + username;
            Long count = redisTemplate.opsForValue().increment(todayKey, 1);
            if (count != null && count == 1) {
                redisTemplate.expire(todayKey, TODAY_TTL);
            }
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

    //오늘, 전체 조회수 초기화 (today는 TTL 24h 유지, total은 TTL 없음)
    public void resetViewCount(String username) {
        try {
            String todayKey = TODAY_KEY_PREFIX + username;
            String totalKey = TOTAL_KEY_PREFIX + username;
            redisTemplate.opsForValue().set(todayKey, "0", TODAY_TTL);
            redisTemplate.opsForValue().set(totalKey, "0");
        } catch (Exception e){
            throw new RuntimeException("조회수 초기화 중 오류 발생: " + e.getMessage(), e);
        }
    }

    //오늘 조회수 초기화
    public void updateTotalWithToday(String username){
        try{
            String todayKey = TODAY_KEY_PREFIX + username;
            redisTemplate.opsForValue().set(todayKey, "0");
        } catch (Exception e){
            throw new RuntimeException("오늘 조회수 초기화 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
