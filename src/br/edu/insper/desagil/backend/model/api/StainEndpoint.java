//
// NÃO MODIFIQUE ESTE ARQUIVO!
//
// Este arquivo não precisa ser modificado. Se pensa
// em fazer isso, sua resposta está na direção errada.
//

package br.edu.insper.desagil.backend.model.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.edu.insper.desagil.backend.core.Endpoint;
import br.edu.insper.desagil.backend.core.exception.APIException;
import br.edu.insper.desagil.backend.core.exception.NotFoundException;
import br.edu.insper.desagil.backend.model.Artist;
import br.edu.insper.desagil.backend.model.CollaborationTrack;
import br.edu.insper.desagil.backend.model.Playlist;
import br.edu.insper.desagil.backend.model.Stain;

public class StainEndpoint extends Endpoint<Stain> {
	private Map<String, Stain> stains;

	public StainEndpoint() {
		super("/playlist");

		Artist artist = new Artist("Anitta");

		Playlist playlist = new Playlist(5);
		playlist.addTrack(new Track(artist, "Atencion", 160));
		playlist.addTrack(new CollaborationTrack(artist, Arrays.asList(new Artist("Becky G")), "Banana", 195));
		playlist.addTrack(new CollaborationTrack(artist, Arrays.asList(new Artist("Ludmilla"), new Artist("Snoop Dog")), "Onda Diferente", 160));
		playlist.putRating("anitta", 6);
		playlist.putRating("ludmilla", 0);

		stains = new HashMap<>();
		stains.put(Integer.toString(playlist.getId()), new Stain(playlist));
	}

	@Override
	public Stain get(Map<String, String> args) throws APIException {
		String id = extract(args, "id");
		if (!stains.containsKey(id)) {
			throw new NotFoundException("Key " + id + " not found");
		}
		return stains.get(id);
	}
}
