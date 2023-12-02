package ru.practicum.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.dto.StatsResponseDto;
import ru.practicum.ewm.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface StatsRepository extends JpaRepository<Stats, Long> {

    //@formatter:off
    @Query("SELECT new ru.practicum.ewm.dto.StatsResponseDto(s.app, s.uri, count(distinct s.ip)) "
            + "FROM Stats as s "
            + "WHERE s.timestamp between :start and :end "
            + "AND ((:uris) is null or s.uri in :uris) "
            + "GROUP BY s.app, s.uri "
            + "ORDER BY count(distinct s.ip) DESC")
    //@formatter:on
    List<StatsResponseDto> findAllStatsUnique(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end,
                                              @Param("uris") Set<String> uris);

    //@formatter:off
    @Query("SELECT new ru.practicum.ewm.dto.StatsResponseDto(s.app, s.uri, count(s.ip)) "
            + "FROM Stats as s "
            + "WHERE s.timestamp between :start and :end "
            + "AND ((:uris) is null or s.uri in :uris) "
            + "GROUP BY s.app, s.uri "
            + "ORDER BY count(s.ip) DESC")
    //@formatter:on
    List<StatsResponseDto> findAllStats(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end,
                                        @Param("uris") Set<String> uris);
}
