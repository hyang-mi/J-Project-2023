package com.mjuAppSW.appName.domain.Room;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepository{
    @PersistenceContext
    EntityManager em;

    public void save(Room room){
        em.persist(room);
    }
}
