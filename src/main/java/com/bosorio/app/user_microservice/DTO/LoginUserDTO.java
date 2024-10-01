package com.bosorio.app.user_microservice.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LoginUserDTO {

    @NotEmpty(message ="Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;
}
