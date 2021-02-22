package com.backbase.assignment.kalah.game.entity;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pit entity.
 * 
 * @author @vanish
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pit {
	private Integer pitIndex;
	private Integer pitValue;
}
