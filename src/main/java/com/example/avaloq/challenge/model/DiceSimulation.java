package com.example.avaloq.challenge.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
public class DiceSimulation {
    @Id
    @GeneratedValue
    private long id;
    private int numberOfDice;
    private int numberOfDiceSides;
    private int totalRollsMade;
    @ElementCollection
    @CollectionTable(name = "total_sum_count")
    @MapKeyColumn(name = "total_sum")
    @Column(name = "count")
    private Map<Integer,Integer> diceDistribution = new HashMap<>(); ;
}
