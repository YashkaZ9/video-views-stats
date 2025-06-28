package com.yashkaz.videoviewsstats;

import com.yashkaz.videoviewsstats.model.VideoStatistics;
import com.yashkaz.videoviewsstats.repository.ViewEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AggregationIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ViewEventRepository repository;

    @Test
    void fullProcessingTest() throws Exception {
        // Отправка 1000 событий
        for (int i = 0; i < 1000; i++) {
            String eventJson = String.format("{\"videoId\":\"video2\",\"userId\":%d,\"duration\":%d}", i, 100 + i);

            mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(eventJson));
        }

        // Ожидаем обработки
        await().atMost(3, TimeUnit.SECONDS).until(() ->
                repository.count() >= 1000
        );

        // Проверка агрегации
        VideoStatistics stats = repository.getVideoStats("video2").orElseThrow();
        assertEquals(1000L, stats.getUniqueViewers());
        assertEquals(1000 * 100 + 999 * 1000 / 2, stats.getTotalDuration());
    }
}