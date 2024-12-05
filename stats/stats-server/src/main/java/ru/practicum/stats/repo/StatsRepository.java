package ru.practicum.stats.repo;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.statsdto.HitObject;
import ru.practicum.statsdto.HitObjectProjection;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@EntityScan(basePackages = "ru.practicum.statsdto")
public interface StatsRepository extends JpaRepository<HitObject, Long> {

    @Query(value = "SELECT h.app AS app, h.uri AS uri, COUNT(h.ip) AS hit FROM HitObject h " +
            "WHERE uri IN :uris AND h.timestamp BETWEEN :start AND :end GROUP BY h.app, h.uri ORDER BY hit DESC")
    List<HitObjectProjection> getStatsWithUries(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT h.app AS app, h.uri AS uri, COUNT(h.ip) AS hit " +
            "FROM HitObject h WHERE h.timestamp BETWEEN :start AND :end GROUP BY h.app, h.uri ORDER BY hit DESC")
    List<HitObjectProjection> getStats(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT h.app AS app, h.uri AS uri, COUNT(DISTINCT h.ip) AS hit FROM HitObject h " +
            "WHERE h.uri IN :uris AND h.timestamp BETWEEN :start AND :end GROUP BY h.app, h.uri ORDER BY hit DESC")
    List<HitObjectProjection> getUniqueStatsWithUries(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT h.app AS app, h.uri AS uri, COUNT(DISTINCT h.ip) AS hit FROM HitObject h " +
            "WHERE h.timestamp BETWEEN :start AND :end GROUP BY h.app, h.uri ORDER BY hit DESC")
    List<HitObjectProjection> getUniqueStats(LocalDateTime start, LocalDateTime end);

}
