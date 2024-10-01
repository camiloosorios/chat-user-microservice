package com.bosorio.app.user_microservice.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePasswordDTO {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
