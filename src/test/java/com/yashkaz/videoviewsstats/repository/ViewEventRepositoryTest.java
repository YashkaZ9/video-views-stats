package com.yashkaz.videoviewsstats.repository;

import com.yashkaz.videoviewsstats.model.VideoStatistics;
import com.yashkaz.videoviewsstats.model.ViewEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ViewEventRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ViewEventRepository repository;

    @Test
    void whenGetStats_thenReturnCorrect() {
        ViewEvent event1 = new ViewEvent("video1", 1L, 120L, Instant.now());
        ViewEvent event2 = new ViewEvent("video1", 2L, 180L, Instant.now());
        entityManager.persist(event1);
        entityManager.persist(event2);
        entityManager.flush();

        VideoStatistics stats = repository.getVideoStats("video1").orElseThrow();

        assertEquals(2L, stats.getUniqueViewers());
        assertEquals(300L, stats.getTotalDuration());
    }
}