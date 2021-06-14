package br.edu.insper.desagil.backend.model;

import java.util.ArrayList;

public class CollaborationTrack extends Track{
	private ArrayList<Artist> collaborators;

	public CollaborationTrack(Artist artist, String name, int duration, ArrayList<Artist> collaborators) {
		super(artist, name, duration);
		this.collaborators = collaborators;
	}
	
	@Override
	public String getFullArtistName() {
		
		ArrayList<String> fullNames = new ArrayList<>();
		
		for (Artist artist : this.collaborators) {
			fullNames.add(artist.getName());
		}
		
		return super.getArtist().getName() + " (feat. " + String.join(", ", fullNames) + ")";
	}
}
