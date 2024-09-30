package com.hotels.roomwiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotels.roomwiz.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
