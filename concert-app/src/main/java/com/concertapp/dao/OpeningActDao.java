package com.concertapp.dao;

import java.util.List;

public interface OpeningActDao {

    void insertOpeningActs(int concertId, List<Integer> actIds);

    List<Integer> getOpeningActIds(int concertId);
}
