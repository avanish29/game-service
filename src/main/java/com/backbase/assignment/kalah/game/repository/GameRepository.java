package com.backbase.assignment.kalah.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backbase.assignment.kalah.game.entity.Game;

/**
 * Repository to manage {@link Game} instances.
 * 
 * @author @vanish
 */
public interface GameRepository extends JpaRepository<Game, Long> {

}
