package com.example.avaloq.challenge.model;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class Dice {
     int numberOfDice;
     int numberOfDiceSides;

     public interface NumberOfDiceAndSides {
         int getNumberOfDice();
         int getNumberOfDiceSides();
     }
}
