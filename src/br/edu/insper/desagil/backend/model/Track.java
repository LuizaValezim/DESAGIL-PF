package br.edu.insper.desagil.backend.model;

public class Track {
	private Artist artist;
	private String name;
	private int duration;
	
	public Track(Artist artist, String name, int duration) {
		this.artist = artist;
		this.name = name;
		this.duration = duration;
	}

	public Artist getArtist() {
		return artist;
	}

	public String getName() {
		return name;
	}

	public int getDuration() {
		return duration;
	}
	
	public String getDurationString() {
		
		int seconds = this.duration;
		int minutes = 0;
		
		for (int i=0; i < 30; i++) {
			if (seconds >= 60) {
				minutes = minutes + 1;
				seconds = seconds - 60;
			}
			else {
				break;
			}
		}
		
		if (minutes >= 10 && seconds >= 10) {
			return minutes + ":" + seconds;
		}
		
		else if (minutes >= 10 && seconds < 10) {
			return minutes + ":" + "0" + seconds; 
		}
		
		else if (minutes < 10 && seconds >= 10) {
			return minutes + ":" + seconds; 
		}
		
		else {
			return minutes + ":" + "0" + seconds; 
		}
	
	}
	
	public String getFullArtistName() {
		return this.getArtist().getName();
	}
}
