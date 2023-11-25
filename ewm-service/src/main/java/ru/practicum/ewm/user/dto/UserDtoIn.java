package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoIn {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 250, message = "Name must be between 2 and 250 characters")
    private String name;
    @NotBlank(message = "Email is mandatory")
    @Size(min = 6, max = 254, message = "Email must be between 6 and 254 characters")
    @Email(message = "Email should be valid")
    private String email;
}
