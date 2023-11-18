package ru.practicum.ewm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.dto.StatsResponseDto;
import ru.practicum.ewm.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class StatsServiceClient {

    private final WebClient webClient;

    public StatsServiceClient(@Value("${stats.server.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public HttpStatus addHit(StatsRequestDto statsRequestDto) {
        return Objects.requireNonNull(webClient.post()
                        .uri("/hit")
                        .bodyValue(statsRequestDto)
                        .retrieve()
                        .toBodilessEntity()
                        .block())
                .getStatusCode();
    }

    public ResponseEntity<StatsResponseDto> getStats(LocalDateTime start,
                                                     LocalDateTime end,
                                                     Set<String> uris,
                                                     boolean unique) {
        String encodedStart = DateTimeUtil.encodeDateTimeToString(start);
        String encodedEnd = DateTimeUtil.encodeDateTimeToString(end);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/stats")
                        .queryParam("start", encodedStart)
                        .queryParam("end", encodedEnd)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .toEntity(StatsResponseDto.class)
                .block();
    }
}