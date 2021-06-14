package br.edu.insper.desagil.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlaylistTest {
	private static double DELTA = 0.000001;
	private Playlist vibez;

	@BeforeEach
	void setUp() {
		vibez = new Playlist(1);
	}

	@Test
	void testRoundDownToZero() {
		
		vibez.putRating("João", 1);
		vibez.putRating("Joã", 2);
		vibez.putRating("Jo", 3);
		vibez.putRating("J", 3);
		
		assertEquals(2.0, vibez.averageRatings(), DELTA);
		
	}

	@Test
	void testRoundUpToHalf() {
		vibez.putRating("Maria", 1);
		vibez.putRating("Mari", 2);
		vibez.putRating("Mar", 1);
		
		assertEquals(1.5, vibez.averageRatings(), DELTA);
		
	}

	@Test
	void testRoundDownToHalf() {
		vibez.putRating("Hashi", 1);
		vibez.putRating("Hash", 2);
		vibez.putRating("Has", 2);
		
		assertEquals(1.5, vibez.averageRatings(), DELTA);
	}

	@Test
	void testRoundUpToOne() {
		vibez.putRating("Insper", 1);
		vibez.putRating("Inspe", 1);
		vibez.putRating("Insp", 2);
		vibez.putRating("Ins", 3);
		
		assertEquals(2.0, vibez.averageRatings(), DELTA);
	}
}
