package com.concertapp.controller;

import com.concertapp.dao.ArtistDao;
import com.concertapp.model.Artist;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
@CrossOrigin
public class ArtistController {

    private final ArtistDao artistDao;

    public ArtistController(ArtistDao artistDao) {
        this.artistDao = artistDao;
    }

    @GetMapping
    public List<Artist> getAllArtists() {
        return artistDao.getAllArtists();
    }

    @GetMapping("/search")
    public List<Artist> searchArtists(@RequestParam String name) {
        return artistDao.searchArtistsByName(name);
    }
}
