package com.backbase.assignment.kalah.game.service;

import com.backbase.assignment.kalah.game.entity.Game;
import com.backbase.assignment.kalah.game.entity.Pit;
import com.backbase.assignment.kalah.game.exception.GameTerminatedException;
import com.backbase.assignment.kalah.game.exception.IllegalPitNumberException;
import com.backbase.assignment.kalah.game.util.GameConstant;

import lombok.NonNull;

/**
 * Game move handler to update game entity and pits.
 * 
 * @author @vanish
 */
final class GameMoveHandler {
	private final Game game;
	private final int playerIndex;
	private final int pitIndex;

	/**
	 * Default constructor
	 * 
	 * @param game     - Instance of game entity.
	 * @param pitIndex - Pit index passed in rest path.
	 */
	GameMoveHandler(@NonNull final Game game, final int pitIndex) {
		this.validateMove(game, pitIndex);
		this.game = game;
		this.playerIndex = game.getNextPlayer();
		this.pitIndex = pitIndex > GameConstant.NUMBER_OF_PITS ? pitIndex - (GameConstant.NUMBER_OF_PITS + 2)
				: pitIndex - 1;

		// check if selected pit is empty or not.
		int totalStonesInSelectedPit = game.getPlayers().get(playerIndex).getPits().get(this.pitIndex).getPitValue();
		if (totalStonesInSelectedPit <= 0) {
			throw new IllegalPitNumberException("There is no stone to move in selected pit.");
		}
	}

	/**
	 * Method move the stone from selected pit to next pits
	 * 
	 * @return - updated instance of game.
	 */
	Game moveStone() {
		// Empty the selected pit before moving to next pit
		int totalStonesInSelectedPit = this.game.getPlayers().get(playerIndex).getPits().get(this.pitIndex)
				.getPitValue();
		this.game.getPlayers().get(playerIndex).getPits().get(this.pitIndex).setPitValue(0);
		this.game.setLastStoneOnPlayersBigPit(false);

		int i = 1;
		int pitValue = 0;
		Integer nextPitIndex;
		Integer nextPlayerIndex = playerIndex;
		do {
			// Evaluate the next pit index
			nextPitIndex = (this.pitIndex + i) % GameConstant.NUMBER_OF_PITS;
			// Evaluate the next player index
			nextPlayerIndex = (nextPitIndex == 0) ? (nextPlayerIndex == 0 ? 1 : 0) : nextPlayerIndex;
			// If pit index is 7 or 14 and next player is not same is player, then put stone
			// in score pit.
			if (nextPitIndex == 0 && nextPlayerIndex != playerIndex) {
				pitValue = this.game.getPlayers().get(playerIndex).getScore();
				this.game.getPlayers().get(playerIndex).setScore(pitValue + 1);

				// Return if there are no more stones
				if (--totalStonesInSelectedPit <= 0) {
					this.game.setLastStoneOnPlayersBigPit(true);
					this.game.setNextPlayer(playerIndex);
					break;
				}
			}
			// Add stone in next pit
			pitValue = this.game.getPlayers().get(nextPlayerIndex).getPits().get(nextPitIndex).getPitValue();
			this.game.getPlayers().get(nextPlayerIndex).getPits().get(nextPitIndex).setPitValue(pitValue + 1);
			this.game.setNextPlayer(playerIndex == 0 ? 1 : 0);

			i++;
		} while (--totalStonesInSelectedPit > 0);
		checkEndOfGame();
		return this.game;
	}

	/**
	 * Method check if game is ended or not
	 */
	void checkEndOfGame() {
		Integer playerIndx = null;

		// Check if there is any player with all pits empty
		if (this.game.getPlayers().get(0).getPits().stream().mapToInt(Pit::getPitValue).sum() == 0) {
			playerIndx = 0;
		} else if (this.game.getPlayers().get(1).getPits().stream().mapToInt(Pit::getPitValue).sum() == 0) {
			playerIndx = 1;
		}

		// If there is, sums all stones left to opponent score
		if (playerIndx != null) {
			// Get all stones left from opponent...
			int opponentIndex = playerIndex == 0 ? 1 : 0;
			int allStonesLeft = 0;
			for (int i = 0; i < GameConstant.NUMBER_OF_PITS; i++) {
				allStonesLeft += this.game.getPlayers().get(opponentIndex).getPits().get(i).getPitValue();
				this.game.getPlayers().get(opponentIndex).getPits().get(i).setPitValue(0);
			}

			// And add to opponent's score
			this.game.getPlayers().get(opponentIndex)
					.setScore(this.game.getPlayers().get(opponentIndex).getScore() + allStonesLeft);

			// Calculate winner
			if (this.game.getPlayers().get(playerIndex).getScore() > this.game.getPlayers().get(opponentIndex)
					.getScore()) {
				this.game.setWinner(playerIndex);
			} else if (this.game.getPlayers().get(playerIndex).getScore() < this.game.getPlayers().get(opponentIndex)
					.getScore()) {
				this.game.setWinner(opponentIndex);
			} else {
				this.game.setWinner(2);
			}
		}
	}

	/**
	 * Validate the move
	 * 
	 * @param game     - Instance of game entity.
	 * @param pitIndex - Pit index passed in rest path.
	 */
	void validateMove(final Game game, final int pitIndex) {
		validatePitIndex(pitIndex);
		validatePlayerChance(game, pitIndex);
		validateGameStatus(game);
	}

	/**
	 * Validate the correct player chance.
	 * 
	 * @param game     - Instance of game entity.
	 * @param pitIndex - Pit index passed in rest path.
	 */
	void validatePlayerChance(final Game game, final int pitIndex) {
		if (game.getNextPlayer() == 0 ? (pitIndex > GameConstant.NUMBER_OF_PITS)
				: (pitIndex < GameConstant.NUMBER_OF_PITS)) {
			throw new IllegalPitNumberException(String.format(
					"It's player %s turn, please select index range from %s to %s inclusive.",
					game.getNextPlayer() == 0 ? "one" : "two",
					game.getNextPlayer() == 0 ? "1" : GameConstant.NUMBER_OF_PITS + 2,
					game.getNextPlayer() == 0 ? GameConstant.NUMBER_OF_PITS : GameConstant.NUMBER_OF_TOTAL_PITS - 1));
		}
	}

	/**
	 * Validate the pit index is greater than 0 and not greater than 14, also
	 * selected pit should not the kalah.
	 * 
	 * @param pitIndex - Pit index passed in rest path.
	 */
	void validatePitIndex(final int pitIndex) {
		if (playerIndex < 0 || playerIndex >= 2 || pitIndex < 1 || pitIndex > GameConstant.NUMBER_OF_TOTAL_PITS) {
			throw new IllegalPitNumberException("Either player or pit index is not valid.");
		}
		if (pitIndex == (GameConstant.NUMBER_OF_PITS + 1) || pitIndex == GameConstant.NUMBER_OF_TOTAL_PITS) {
			throw new IllegalPitNumberException("Selected index is for Kalah, You can not select/move kalah.");
		}
	}

	/**
	 * Validate the current state of game.
	 * 
	 * @param game - Instance of game entity.
	 */
	void validateGameStatus(final Game game) {
		if (game.getWinner() != null) {
			throw new GameTerminatedException(
					String.format("Game end with winner '%s'", (game.getWinner() == 0) ? "Player 1 wins!"
							: ((game.getWinner() == 1) ? "Player 2 wins!" : "It's a match")));
		}
	}
}
