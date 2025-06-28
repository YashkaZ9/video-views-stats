package com.yashkaz.videoviewsstats.controller;

import com.yashkaz.videoviewsstats.model.VideoStatistics;
import com.yashkaz.videoviewsstats.service.AggregationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AggregationController.class)
class AggregationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AggregationService aggregationService;

    @Test
    void whenPostEvent_thenAccepted() throws Exception {
        String eventJson = "{\"videoId\":\"video1\",\"userId\":1,\"duration\":120}";

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isAccepted());
    }

    @Test
    void whenGetStats_thenReturnStats() throws Exception {
        VideoStatistics stats = new VideoStatistics("video1", 10L, 1000L);
        when(aggregationService.getStatistics("video1")).thenReturn(stats);

        mockMvc.perform(get("/api/events/statistics/video1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uniqueViewers").value(10));
    }
}