package ru.practicum.ewm.statistic;

import org.springframework.http.HttpStatus;
import ru.practicum.ewm.dto.StatsRequestDto;
import ru.practicum.ewm.dto.StatsResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface IStatisticService {
    /**
     * Добавляет статистику в базу данных
     *
     * @param statsRequestDto Данные для добавления
     * @return HttpStatus
     */
    HttpStatus addStat(StatsRequestDto statsRequestDto);

    /**
     * Возвращает статистику по запросам
     *
     * @param start  Время начала выборки
     * @param end    Время конца выборки
     * @param uris   Список uri для фильтрации
     * @param unique Флаг, указывающий на необходимость фильтрации по уникальным ip
     * @return Список статистик
     */
    List<StatsResponseDto> getStats(LocalDateTime start,
                                    LocalDateTime end,
                                    Set<String> uris,
                                    boolean unique);
}
