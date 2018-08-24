package com.telega.eatter.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "subscriber")
@Data
public class Subscriber {

    @Id
    private Long id;

}
