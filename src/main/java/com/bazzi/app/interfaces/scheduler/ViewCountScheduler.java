package com.bazzi.app.interfaces.scheduler;

import com.bazzi.app.application.service.ViewCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ViewCountScheduler {

    private final ViewCountService viewCountService;

    private final StringRedisTemplate redisTemplate;

    // 매일 자정(00:00)에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateAllViewCounts() {
        // Redis에서 today:*로 시작하는 모든 키를 가져옴
        Set<String> todayKeys = redisTemplate.keys("today:*");
        if (todayKeys != null) {
            for (String key : todayKeys) {
                String username = key.substring(key.indexOf(":") + 1);
                viewCountService.updateTotalWithToday(username);
            }
        }
    }
}
