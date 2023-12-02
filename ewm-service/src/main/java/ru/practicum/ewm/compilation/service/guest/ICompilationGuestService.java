package ru.practicum.ewm.compilation.service.guest;

import ru.practicum.ewm.compilation.dto.CompilationDtoOut;

import java.util.List;

/**
 * Сервис для получения подборок событий для всех пользователей
 */
public interface ICompilationGuestService {
    /**
     * Получить все подборки событий по параметрам
     *
     * @param pinned признак закрепленности
     * @param from   с какого элемента начинать выборку
     * @param size   сколько элементов выбирать
     * @return список подборок событий
     */
    List<CompilationDtoOut> getAllByParams(Boolean pinned,
                                           Integer from,
                                           Integer size);

    /**
     * Получить подборку событий по идентификатору
     *
     * @param compId идентификатор подборки
     * @return подборка событий
     */
    CompilationDtoOut getById(Long compId);
}
