package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationDtoIn {
    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
    private String title;
    private Set<Long> events;
    private Boolean pinned;
}
