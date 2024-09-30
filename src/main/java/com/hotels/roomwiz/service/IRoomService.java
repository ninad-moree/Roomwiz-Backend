package com.hotels.roomwiz.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import org.springframework.web.multipart.MultipartFile;

import com.hotels.roomwiz.model.Room;

public interface IRoomService {
    Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) throws IOException, SQLException;;
}
