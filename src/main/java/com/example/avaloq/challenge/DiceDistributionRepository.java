package com.example.avaloq.challenge;

import com.example.avaloq.challenge.model.Dice;
import com.example.avaloq.challenge.model.DiceDistribution;
import com.example.avaloq.challenge.model.DiceSimulationPrimaryKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiceDistributionRepository extends CrudRepository<DiceDistribution, Long> {

    List<DiceDistribution> findByNumberOfDiceAndNumberOfDiceSides(int numberOfDice, int numberOfDiceSides);
    List<Dice.NumberOfDiceAndSides> findDistinctBy();
    long countByNumberOfDiceAndNumberOfDiceSides(int numberOfDice, int numberOfDiceSides);
    @Query("select sum(d.numberOfRolls) from DiceDistribution d where d.numberOfDice=?1 and d.numberOfDiceSides=?2")
    long sumTotalRollsByNumberOfDiceAndNumberOfDiceSides(int numberOfDice, int numberOfDiceSides);
}
