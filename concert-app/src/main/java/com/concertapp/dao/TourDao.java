package com.concertapp.dao;

import com.concertapp.model.Tour;

import java.util.List;

public interface TourDao {
    Integer getOrCreateTourId(String name);

    List<Tour> getAllTours();

    Tour getTourById(int tourId);

    String getTourName(int tourId);

}
