package com.electr.users.domain.models;

import lombok.*;

import javax.persistence.*;

@Entity(name = "solidarity_roles")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    private String roleLabel;

    @Column(name = "role_authorities")
    private String authorities;

    private String roleLabelPt;
}
