package com.example.avaloq.challenge.model;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class Dice {
     int diceNumber;
     int sideNumber;

     public interface DiceAndSidesNumber {
         int getDiceNumber();
         int getSideNumber();
     }
}
