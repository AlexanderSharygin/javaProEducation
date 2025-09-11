package pro.java.education.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDto(Long id, @NotBlank String name) {
}
