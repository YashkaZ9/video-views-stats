package com.yashkaz.videoviewsstats.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
public class ViewEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String videoId;
    private Long userId;
    private Long duration;
    private Instant timestamp;

    public ViewEvent(String videoId, Long userId, Long duration, Instant timestamp) {
        this.videoId = videoId;
        this.userId = userId;
        this.duration = duration;
        this.timestamp = timestamp;
    }
}