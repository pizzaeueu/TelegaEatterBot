package com.telega.eatter.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="game")
@Data
public class Game {

    @Id
    private Long id;

    @Column
    private Long ownerTelegramId;

}
