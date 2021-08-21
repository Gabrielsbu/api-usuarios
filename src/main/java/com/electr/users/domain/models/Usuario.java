package com.electr.users.domain.models;

import com.electr.users.domain.enums.StatusUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuarioId;
    private String nome;
    private String email;
    private String senha;
    private String avatar;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Simulacao> simulacoes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private StatusUser status;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(columnDefinition = "TEXT")
    private String token;

    private LocalDateTime createAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
