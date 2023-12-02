package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.dto.event.EventShortDtoOut;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDtoOut {
    private Long id;
    private String title;
    private List<EventShortDtoOut> events;
    private Boolean pinned;
}
