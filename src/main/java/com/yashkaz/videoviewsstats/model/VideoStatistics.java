package com.yashkaz.videoviewsstats.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class VideoStatistics {
    private String videoId;
    private Long uniqueViewers;
    private Long totalDuration;
}