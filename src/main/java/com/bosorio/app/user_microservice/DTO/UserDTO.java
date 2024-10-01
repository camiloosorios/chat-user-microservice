package com.bosorio.app.user_microservice.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    private String name;

    private String username;

    private String email;

}
