package com.backbase.assignment.kalah.game.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backbase.assignment.kalah.game.domain.GameBoardView;
import com.backbase.assignment.kalah.game.domain.GameView;
import com.backbase.assignment.kalah.game.entity.Game;
import com.backbase.assignment.kalah.game.entity.Player;
import com.backbase.assignment.kalah.game.exception.GameNotFoundException;
import com.backbase.assignment.kalah.game.mapper.EntityToViewMapper;
import com.backbase.assignment.kalah.game.repository.GameRepository;
import com.backbase.assignment.kalah.game.repository.PlayerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GameService {
	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private EntityToViewMapper mapper;

	@Transactional
	public GameBoardView createGame() {
		log.debug("Creating new game.");
		Game newGameEntity = new Game();
		List<Player> players = newGameEntity.getPlayers();
		final Game gameEntity = gameRepository.save(newGameEntity);
		players.forEach(player -> player.setGame(gameEntity));
		playerRepository.saveAll(players);
		log.debug("New game entity is created successfully with id={}.", gameEntity.getGameId());
		return mapper.toGameBoard(findGameByID(gameEntity.getGameId()));
	}

	public GameView findById(final Long gameId) {
		log.debug("Finding game by Id : {}", gameId);
		Game gameEntity = findGameByID(gameId);
		return mapper.toGameView(gameEntity);
	}

	@Transactional
	public GameView makeMove(final Long gameId, int pitIndex) {
		log.debug("Start moving pit with index : {} for game Id : {}", pitIndex, gameId);
		Game gameEntity = findGameByID(gameId);
		gameEntity = gameRepository.save(new GameMoveHandler(gameEntity, pitIndex).moveStone());
		return mapper.toGameView(gameEntity);
	}

	Game findGameByID(final Long gameId) {
		return gameRepository.findById(gameId).orElseThrow(
				() -> new GameNotFoundException(String.format("Game with ID '%s' does not exists!", gameId)));
	}
}
