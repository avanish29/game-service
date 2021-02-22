package com.backbase.assignment.kalah.game.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.backbase.assignment.kalah.game.entity.Game;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class GameRepositoryTest {
	@Autowired
	private GameRepository repository;

	@Test
	void testSaveGame() {
		Game game = new Game();
		Game created = repository.save(game);

		Assertions.assertNotNull(created);
		Assertions.assertEquals(1, created.getGameId());
		Assertions.assertEquals(null, created.getWinner());
	}
}
