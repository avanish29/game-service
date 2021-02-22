package com.backbase.assignment.kalah.game.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.backbase.assignment.kalah.game.util.GameConstant;

import lombok.Data;

/**
 * A game entity.
 * 
 * @author @vanish
 */
@Entity
@Table(name = "game")
@Data
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long gameId;

	private Integer nextPlayer;

	private Integer winner;

	private boolean lastStoneOnPlayersBigPit = false;

	@OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
	private List<Player> players;

	public Game() {
		this.winner = null;
		this.nextPlayer = Integer.valueOf(0);

		this.lastStoneOnPlayersBigPit = false;

		this.players = new ArrayList<>(GameConstant.NUMBER_OF_PLAYERS);
		List<Pit> initialPitForPlayerOne = IntStream.range(0, GameConstant.NUMBER_OF_PITS)
				.mapToObj(pitIndex -> new Pit(pitIndex, GameConstant.NUMBER_OF_STONES_PER_PIT))
				.collect(Collectors.toList());
		List<Pit> initialPitForPlayerTwo = IntStream.range(0, GameConstant.NUMBER_OF_PITS)
				.mapToObj(pitIndex -> new Pit(pitIndex, GameConstant.NUMBER_OF_STONES_PER_PIT))
				.collect(Collectors.toList());
		this.players.add(Player.builder().name("Player One").pits(initialPitForPlayerOne).build());
		this.players.add(Player.builder().name("Player Two").pits(initialPitForPlayerTwo).build());
	}
}
