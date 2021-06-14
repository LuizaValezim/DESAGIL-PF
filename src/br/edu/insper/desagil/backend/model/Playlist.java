package br.edu.insper.desagil.backend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Playlist {
	private int id;
	private ArrayList<Track> tracks;
	private HashMap<String,Integer> ratings;
	
	public Playlist(int id) {
		this.id = id;
		this.tracks = new ArrayList<>();
		this.ratings = new HashMap<>();
	}

	public int getId() {
		return id;
	}

	public ArrayList<Track> getTracks() {
		return tracks;
	}

	public HashMap<String, Integer> getRatings() {
		return ratings;
	}
	
		
	public void addTrack(Track track) {
		this.tracks.add(track);
	}
	
	public void putRating(String user, int rating) {
		this.ratings.put(user, rating);
	}
	
	public double averageRatings() {
		
		Collection<Integer> ratings = this.ratings.values();
		
		double average = 0.0;
		
		for (int rating : ratings) {
			average = average + rating;
		}
		
		average = average / ratings.size();
		
		int inteiro = (int) average;
		double fracao = average - inteiro;
		
		if (fracao < 0.26) {
			fracao = 0.0;
		}
		
		else if(fracao >= 0.26 && fracao < 0.74) {
			fracao = 0.5;
		}
		
		else {
			fracao = 1.0;
		}
		
		return inteiro + fracao;
	}
	
}
