package com.concertapp.dao;

import com.concertapp.model.Artist;

import java.util.List;

public interface ArtistDao {
    Integer getOrCreateArtistId(String artistName);

    List<Artist> getAllArtists();

    String getArtistName(int artistId);


    List<Artist> searchArtistsByName(String searchTerm);
}
