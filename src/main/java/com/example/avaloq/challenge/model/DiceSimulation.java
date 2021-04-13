package com.example.avaloq.challenge.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
public class DiceSimulation {
    @EmbeddedId
    private DiceSimulationPrimaryKey diceSimulationPrimaryKey;
    private int totalRollsMade;
    private int numberOfSimulations;
    @ElementCollection
    @CollectionTable(name = "total_sum_count")
    @MapKeyColumn(name = "total_sum")
    @Column(name = "count")
    private Map<Integer,Integer> diceTotalSumCount = new HashMap<>(); ;
}
