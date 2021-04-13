package com.example.avaloq.challenge;

import com.example.avaloq.challenge.model.Dice;
import com.example.avaloq.challenge.model.DiceDistribution;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiceDistributionRepository extends CrudRepository<DiceDistribution, Long> {

    List<DiceDistribution> findByDiceNumberAndSideNumber(int diceNumber, int sideNumber);
    List<Dice.DiceAndSidesNumber> findDistinctBy();
    long countByDiceNumberAndSideNumber(int diceNumber, int sideNumber);
    @Query("select sum(d.rollNumber) from DiceDistribution d where d.diceNumber=?1 and d.sideNumber=?2")
    long sumTotalRollsByDiceNumberAndSideNumber(int diceNumber, int sideNumber);
}
