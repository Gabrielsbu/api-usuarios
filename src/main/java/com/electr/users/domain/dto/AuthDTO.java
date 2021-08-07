package com.electr.users.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(setterPrefix = "set")
public class AuthDTO {

    private String nome;
    private String role;
    private String email;
    private String senha;
}
