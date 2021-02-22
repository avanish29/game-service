package com.backbase.assignment.kalah.game.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backbase.assignment.kalah.game.domain.GameBoardView;
import com.backbase.assignment.kalah.game.domain.GameView;
import com.backbase.assignment.kalah.game.service.GameService;
import com.backbase.assignment.kalah.game.util.GameConstant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring Web {@link RestController} used to generate a REST API.
 *
 * @author @vanish
 */
@RestController
@RequestMapping(GameConstant.GAME_API_PATH)
@Slf4j
@Api(value = "The game API", tags = { "kalah" })
public class GameController {
	@Autowired
	private GameService gameService;

	/**
	 * REST resource to create new game.
	 * 
	 * @return - Created {@link GameBoardView} instance.
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Create a game")
	public GameBoardView initializeGameBoard() {
		log.info("Request to create new game.");
		return gameService.createGame();
	}

	/**
	 * REST resource to retrieves a {@link GameView} by its id.
	 * 
	 * @param gameId - gameId must not be {@literal null}
	 * @return - The instance of {@link GameView} with the given id.
	 */
	@GetMapping(GameConstant.GAME_BY_ID_API_PATH)
	@ApiOperation(value = "Retrieves a snigle game by ID")
	public GameView findById(@PathVariable Long gameId) {
		log.info("Request to load game with Id : {}.", gameId);
		return gameService.findById(gameId);
	}

	/**
	 * REST resource to move pit to right direction for a game.
	 * 
	 * @param gameId - gameId must not be {@literal null}
	 * @param pitId  - pitId must not be {@literal null}
	 * @return - game status after move.
	 */
	@PutMapping(GameConstant.MOVE_PIT_BY_GAME_ID_API_PATH)
	@ApiOperation(value = "Make a move")
	public GameView makeMove(@PathVariable Long gameId, @PathVariable int pitId) {
		log.info("Request to move pit index : {} for game Id : {}.", pitId, gameId);
		return gameService.makeMove(gameId, pitId);
	}
}
