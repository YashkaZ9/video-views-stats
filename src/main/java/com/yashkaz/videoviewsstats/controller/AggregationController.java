package com.yashkaz.videoviewsstats.controller;

import com.yashkaz.videoviewsstats.model.VideoStatistics;
import com.yashkaz.videoviewsstats.model.ViewEvent;
import com.yashkaz.videoviewsstats.service.AggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class AggregationController {
    private final AggregationService aggregationService;

    @PostMapping
    public ResponseEntity<String> handleEvent(@RequestBody ViewEvent event) {
        event.setTimestamp(Instant.now());
        aggregationService.processEvent(event);
        return ResponseEntity.accepted().body("Event accepted");
    }

    @GetMapping("/statistics/{videoId}")
    public ResponseEntity<VideoStatistics> getStatistics(@PathVariable String videoId) {
        return ResponseEntity.ok(aggregationService.getStatistics(videoId));
    }
}
