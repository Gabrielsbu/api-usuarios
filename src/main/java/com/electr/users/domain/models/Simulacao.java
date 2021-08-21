package com.electr.users.domain.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "simulacao")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Simulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long simulacaoId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @OneToMany(mappedBy = "simulacao", fetch = FetchType.LAZY)
    private List<Eletrodomestico> eletrodomesticos = new ArrayList<>();

    @Column(precision=10, scale=2)
    private Double totalValorPorMes;

    @Column(precision=10, scale=2)
    private Double totalKwhPorMes;

    private LocalDateTime createSimulationAt;
    private LocalDateTime updateSimulationAt;

    @PrePersist
    public void prePersist(){
        this.createSimulationAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updateSimulationAt = LocalDateTime.now();
    }
}