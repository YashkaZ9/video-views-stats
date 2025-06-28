package com.yashkaz.videoviewsstats.repository;

import com.yashkaz.videoviewsstats.model.VideoStatistics;
import com.yashkaz.videoviewsstats.model.ViewEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViewEventRepository extends JpaRepository<ViewEvent, Long> {
    @Query("""
            SELECT v.videoId, COUNT(DISTINCT v.userId), SUM(v.duration)
            FROM ViewEvent v
            WHERE v.videoId = :videoId
            GROUP BY v.videoId
            """)
    Optional<VideoStatistics> getVideoStats(@Param("videoId") String videoId);
}
