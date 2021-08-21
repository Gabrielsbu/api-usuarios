package com.electr.users.domain.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity(name = "eletrodomesticos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class Eletrodomestico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eletroId;
    private String avatar;
    private String nome;
    private Integer quantidade;
    private Integer tempoEmMinuto;
    private Integer potencia;
    private Integer diasPorMes;

    @Column(precision=10, scale=2)
    private Double valorPorMes;

    @Column(precision=10, scale=2)
    private Double kwhPorMes;

    @ManyToOne(fetch = FetchType.LAZY)
    private Simulacao simulacao;

    private LocalDateTime createEletrodomesticoAt;
    private LocalDateTime updateEletrodomesticoAt;

    @PrePersist
    public void prePersist(){
        this.createEletrodomesticoAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updateEletrodomesticoAt = LocalDateTime.now();
    }
}
