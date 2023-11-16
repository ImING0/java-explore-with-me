package ru.practicum.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.dto.StatsResponseDto;
import ru.practicum.ewm.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {

    @Query("SELECT new ru.practicum.ewm.dto.StatsResponseDto(s.app, s.uri, count(distinct s.ip))"
            + "FROM Stats as s "
            + "WHERE s.timeStamp between :start and :end and s.uri in :uris "
            + "group by s.app, s.uri "
            + "order by count (distinct s.id) desc ")
    List<StatsResponseDto> findAllStatsUnique(
            List<String> uris, LocalDateTime start, LocalDateTime end);
}
