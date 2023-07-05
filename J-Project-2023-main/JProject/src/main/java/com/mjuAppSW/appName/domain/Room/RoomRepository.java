package com.mjuAppSW.appName.domain.Room;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


//public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom{}

@Repository
public class RoomRepository{
    @PersistenceContext
    EntityManager em;

    public void save(Room room){
        em.persist(room);
    }
}
