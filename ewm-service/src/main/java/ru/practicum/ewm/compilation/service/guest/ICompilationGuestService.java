package ru.practicum.ewm.compilation.service.guest;

import ru.practicum.ewm.compilation.dto.CompilationDtoOut;

import java.util.List;

public interface ICompilationGuestService {
    List<CompilationDtoOut> getAllByParams(Boolean pinned,
                                           Integer from,
                                           Integer size);

    CompilationDtoOut getById(Long compId);
}
