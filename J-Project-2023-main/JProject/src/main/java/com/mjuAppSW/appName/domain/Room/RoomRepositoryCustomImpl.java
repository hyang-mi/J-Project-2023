package com.mjuAppSW.appName.domain.Room;

import jakarta.persistence.EntityManager;

public class RoomRepositoryCustomImpl implements RoomRepositoryCustom{

    private final EntityManager em;

    public RoomRepositoryCustomImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Room save(Room room) {
        return null;
    }
}
