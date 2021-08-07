package com.electr.users.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(setterPrefix = "set")
public class LoginDTO {

    private String email;
    private String senha;
}
