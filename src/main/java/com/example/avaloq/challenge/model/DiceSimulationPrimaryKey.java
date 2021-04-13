package com.example.avaloq.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiceSimulationPrimaryKey implements Serializable {

    private int numberOfDice;
    private int numberOfDiceSides;
}
