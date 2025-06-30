package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
	List<Room> findByProduit_Id(Long id);
	Optional<Room> findByName(String name);
	

}
