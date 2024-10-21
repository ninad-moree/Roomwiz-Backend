package com.hotels.roomwiz.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hotels.roomwiz.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("select distinct r.roomType from Room r")
    List<String> findDistinctRoomTypes();

    @Query("select r from Room r " + "where r.roomType like %:roomType%" + "and r.id not in (" + " select br.room.id from BookedRoom br " + 
            " where ((br.checkInDate <= :checkOutDate) and (br.checkOutDate >= :checkInDate))" + ")"
    )
    List<Room> findAvailableRoomsByDatesAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}
