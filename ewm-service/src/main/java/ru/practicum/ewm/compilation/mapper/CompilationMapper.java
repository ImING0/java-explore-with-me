package ru.practicum.ewm.compilation.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.compilation.dto.CompilationDtoOut;
import ru.practicum.ewm.compilation.dto.NewCompilationDtoIn;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.mapper.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class CompilationMapper {
    public Compilation toCompilation(NewCompilationDtoIn compilationDtoIn) {
        return Compilation.builder()
                .title(compilationDtoIn.getTitle())
                .pinned(compilationDtoIn.isPinned())
                .build();
    }

    public CompilationDtoOut toCompilationDtoOut(Compilation compilation) {
        return CompilationDtoOut.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(compilation.getEvents() != null ? compilation.getEvents()
                        .stream()
                        .map(EventMapper::toEventShortDtoOut)
                        .collect(Collectors.toList()) : List.of())
                .pinned(compilation.getPinned())
                .build();
    }
}
