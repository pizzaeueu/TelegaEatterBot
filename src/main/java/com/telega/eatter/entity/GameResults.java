package com.telega.eatter.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="GameResult")
@Data
public class GameResults {
    @Id
    private Long GameResultId;

    @Column
    private Long subscriberId;

    @Column
    private Long answer;

    @Column
    private Long GameId;
}
