package com.telega.eatter.entity;

import lombok.Data;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Entity
@Table(name="GameResult")
@Data
public class GameResults {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
    private Long GameResultId;

    @Column
    private String subscriberId;

    @Column
    private Double answer;

    @Column
    private Long GameId;
}
