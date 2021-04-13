package com.example.avaloq.challenge.model;

import lombok.Data;

@Data
public class DiceSimulation extends Dice{

    private long totalNumberOfRolls;
    private long simulationNumber;
}
