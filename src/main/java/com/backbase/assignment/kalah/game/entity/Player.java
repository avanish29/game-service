package com.backbase.assignment.kalah.game.entity;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Player entity.
 * 
 * @author @vanish
 */
@Entity
@Table(name = "player")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Player {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long playerId;

	private String name;

	private int score;

	@ManyToOne
	@JoinColumn(name = "game_id", nullable = false)
	private Game game;

	@ElementCollection
	private List<Pit> pits;
}
