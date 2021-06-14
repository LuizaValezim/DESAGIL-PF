package br.edu.insper.desagil.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrackTest {
	private Track trackzona;
	private Artist anira;
	private ArrayList<Artist> colabs;
	
	@BeforeEach
	void setUp() {
		anira = new Artist("Anitta");
		colabs = new ArrayList<>();
	}

	@Test
	void testZeroSeconds() {
		trackzona = new Track(anira, "bubutantan", 0);
		
		assertEquals("0:00", trackzona.getDurationString());
	}

	@Test
	void testFiveSeconds() {
		trackzona = new Track(anira, "bubutantan", 5);
		assertEquals("0:05", trackzona.getDurationString());
	}

	@Test
	void testTwentyFiveSeconds() {
		trackzona = new Track(anira, "bubutantan", 25);
		assertEquals("0:25", trackzona.getDurationString());
	}

	@Test
	void testOneMinuteZeroSeconds() {
		trackzona = new Track(anira, "bubutantan", 60);
		assertEquals("1:00", trackzona.getDurationString());
	}

	@Test
	void testOneMinuteFiveSeconds() {
		trackzona = new Track(anira, "bubutantan", 65);
		assertEquals("1:05", trackzona.getDurationString());
	}

	@Test
	void testOneMinuteTwentyFiveSeconds() {
		trackzona = new Track(anira, "bubutantan", 85);
		assertEquals("1:25", trackzona.getDurationString());
	}

	@Test
	void testTwoMinutesZeroSeconds() {
		trackzona = new Track(anira, "bubutantan", 120);
		assertEquals("2:00", trackzona.getDurationString());
	}

	@Test
	void testTwoMinutesFiveSeconds() {
		trackzona = new Track(anira, "bubutantan", 125);
		assertEquals("2:05", trackzona.getDurationString());
	}

	@Test
	void testTwoMinutesTwentyFiveSeconds() {
		trackzona = new Track(anira, "bubutantan", 145);
		assertEquals("2:25", trackzona.getDurationString());
	}

	@Test
	void testOneCollaborator() {
		
		colabs.add(new Artist("Becky G"));
		
		CollaborationTrack baile = new CollaborationTrack(anira,"show das poderosas", 60, colabs);
		
		assertEquals("Anitta (feat. Becky G)", baile.getFullArtistName());
	}

	@Test
	void testTwoCollaborators() {
		
		colabs.add(new Artist("Ludmilla"));
		colabs.add(new Artist("Snoop Dog"));
		
		CollaborationTrack baile = new CollaborationTrack(anira,"show das poderosas", 60, colabs);
		
		assertEquals("Anitta (feat. Ludmilla, Snoop Dog)", baile.getFullArtistName());
	}
}
