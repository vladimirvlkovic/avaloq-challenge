package com.example.avaloq.challenge.model;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class Dice {
    private int numberOfDice;
    private int numberOfDiceSides;
}
