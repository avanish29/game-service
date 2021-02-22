package com.backbase.assignment.kalah.game.repository;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.backbase.assignment.kalah.game.entity.Game;
import com.backbase.assignment.kalah.game.entity.Player;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class PlayerRepositoryTest {
	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Test
	void testSavePlayer() {
		Game game = new Game();
		Game created = gameRepository.save(game);
		game.getPlayers().forEach(player -> player.setGame(created));
		List<Player> createdPlayers = playerRepository.saveAll(game.getPlayers());

		Assertions.assertNotNull(createdPlayers);
		Assertions.assertEquals(2, createdPlayers.size());
	}
}
