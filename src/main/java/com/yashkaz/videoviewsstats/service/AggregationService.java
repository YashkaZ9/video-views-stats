package com.yashkaz.videoviewsstats.service;

import com.yashkaz.videoviewsstats.model.VideoStatistics;
import com.yashkaz.videoviewsstats.model.ViewEvent;
import com.yashkaz.videoviewsstats.repository.ViewEventRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Getter
public class AggregationService {
    private final ViewEventRepository repository;
    private final ConcurrentMap<String, AtomicLong> uniqueUsers = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, AtomicLong> totalDuration = new ConcurrentHashMap<>();
    private final BlockingQueue<ViewEvent> eventQueue = new LinkedBlockingQueue<>(5000);
    private final ScheduledExecutorService batchScheduler = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    public AggregationService(ViewEventRepository repository) {
        this.repository = repository;
        this.batchScheduler.scheduleAtFixedRate(this::processBatch, 1, 1, TimeUnit.SECONDS);
    }

    @Async("eventTaskExecutor")
    public void processEvent(ViewEvent event) {
        uniqueUsers
                .computeIfAbsent(event.getVideoId(), k -> new AtomicLong(0))
                .incrementAndGet();

        totalDuration
                .computeIfAbsent(event.getVideoId(), k -> new AtomicLong(0))
                .addAndGet(event.getDuration());

        eventQueue.offer(event);
    }

    private void processBatch() {
        List<ViewEvent> batch = new ArrayList<>(1000);
        eventQueue.drainTo(batch, 1000);
        if (!batch.isEmpty()) {
            repository.saveAll(batch);
        }
    }

    @Cacheable(value = "videoStats", key = "#videoId")
    public VideoStatistics getStatistics(String videoId) {
        return repository.getVideoStats(videoId)
                .orElse(new VideoStatistics(videoId, 0L, 0L));
    }
}
