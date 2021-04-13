package com.example.avaloq.challenge.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class DiceSimulationPrimaryKey implements Serializable {

    private int numberOfDice;
    private int numberOfDiceSides;
}
