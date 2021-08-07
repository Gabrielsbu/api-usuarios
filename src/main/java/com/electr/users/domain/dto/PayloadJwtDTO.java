package com.electr.users.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayloadJwtDTO {
    private String iss;
    private String sub;
    private List<String> Authorities;
    private Long iat;
    private Long exp;
}
