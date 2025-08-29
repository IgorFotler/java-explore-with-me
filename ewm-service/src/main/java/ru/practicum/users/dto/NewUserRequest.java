package ru.practicum.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {

    @NotBlank
    @Size(min = 2, max = 250)
    String name;

    @NotBlank
    @Size(min = 6, max = 254)
    @Email
    String email;
}