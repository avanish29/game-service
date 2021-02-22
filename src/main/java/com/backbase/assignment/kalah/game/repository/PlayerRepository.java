package com.backbase.assignment.kalah.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backbase.assignment.kalah.game.entity.Player;

/**
 * Repository to manage {@link Player} instances.
 * 
 * @author @vanish
 */
public interface PlayerRepository extends JpaRepository<Player, Long> {

}
