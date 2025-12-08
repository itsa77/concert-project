package com.concertapp.dao;

import com.concertapp.model.Festival;

import java.util.List;

public interface FestivalDao {
    Integer getOrCreateFestivalId(String name);

    List<Festival> getAllFestivals();

    Festival getFestivalById(int id);

    String getFestivalName(int festivalId);
}
