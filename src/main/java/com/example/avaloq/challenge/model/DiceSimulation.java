package com.example.avaloq.challenge.model;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.ArrayList;

@Data
@Entity
public class DiceSimulation {
    @EmbeddedId
    private DiceSimulationPrimaryKey diceSimulationPrimaryKey;
    private int totalRollsMade;
    private int numberOfSimulations;
    private ArrayList<DiceTotalSumCount> diceTotalSumCount;
}
