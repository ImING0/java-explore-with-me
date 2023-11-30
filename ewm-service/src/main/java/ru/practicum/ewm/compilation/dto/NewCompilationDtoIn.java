package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDtoIn {
    @NotNull
    private Set<Long> events;
    @Builder.Default
    private Boolean pinned = false;
    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
    private String title;
}
