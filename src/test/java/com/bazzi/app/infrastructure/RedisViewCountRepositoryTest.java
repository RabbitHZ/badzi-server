package com.bazzi.app.infrastructure;

import com.bazzi.app.infrastructure.persistence.RedisViewCountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisViewCountRepositoryTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOps;

    private RedisViewCountRepository repository;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        repository = new RedisViewCountRepository(redisTemplate);
    }

    @Test
    void today키_최초_increment시_TTL_설정됨() {
        when(valueOps.increment("today:user1", 1)).thenReturn(1L);

        repository.incrementTodayViewCount("user1");

        verify(redisTemplate).expire("today:user1", Duration.ofHours(24));
    }

    @Test
    void today키_두번째_increment시_TTL_재설정_안함() {
        when(valueOps.increment("today:user1", 1)).thenReturn(2L);

        repository.incrementTodayViewCount("user1");

        verify(redisTemplate, never()).expire(eq("today:user1"), any(Duration.class));
    }

    @Test
    void total키_increment시_TTL_설정안함() {
        repository.incrementTotalViewCount("user1");

        verify(redisTemplate, never()).expire(eq("total:user1"), any(Duration.class));
    }

    @Test
    void reset시_today에_TTL_포함해서_set() {
        repository.resetViewCount("user1");

        verify(valueOps).set("today:user1", "0", Duration.ofHours(24));
        verify(valueOps).set("total:user1", "0");
    }

    @Test
    void getTodayViewCount_값없으면_0() {
        when(valueOps.get("today:user1")).thenReturn(null);
        assertThat(repository.getTodayViewCount("user1")).isEqualTo(0);
    }

    @Test
    void getTodayViewCount_값있으면_파싱() {
        when(valueOps.get("today:user1")).thenReturn("42");
        assertThat(repository.getTodayViewCount("user1")).isEqualTo(42);
    }
}
