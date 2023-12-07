package ru.practicum.ewm.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoIn {
    @NotBlank
    @Size(min = 1, max = 10_000, message = "Comment text length must be between 0 and 10_000")
    private String text;
}
