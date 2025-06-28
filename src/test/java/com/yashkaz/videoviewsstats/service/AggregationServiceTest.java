package com.yashkaz.videoviewsstats.service;

import com.yashkaz.videoviewsstats.model.VideoStatistics;
import com.yashkaz.videoviewsstats.model.ViewEvent;
import com.yashkaz.videoviewsstats.repository.ViewEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AggregationServiceTest {
    @Mock
    private ViewEventRepository repository;

    @InjectMocks
    private AggregationService service;

    @Test
    void whenProcessEvent_thenUpdateCounters() {
        ViewEvent event = new ViewEvent("video1", 1L, 120L, Instant.now());

        service.processEvent(event);

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            assertEquals(1L, service.getUniqueUsers().get("video1").get());
            assertEquals(120L, service.getTotalDuration().get("video1").get());
        });
    }

    @Test
    void whenGetStatistics_thenUseCache() {
        when(repository.getVideoStats("video1"))
                .thenReturn(Optional.of(new VideoStatistics("video1", 10L, 1000L)));

        VideoStatistics stats1 = service.getStatistics("video1");
        VideoStatistics stats2 = service.getStatistics("video1");

        verify(repository, times(1)).getVideoStats("video1");
        assertEquals(10L, stats1.getUniqueViewers());
        assertEquals(10L, stats2.getUniqueViewers());
    }
}