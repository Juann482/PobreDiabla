package com.games.h.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.games.h.model.Personaje;

public interface IPersonajeRepository extends JpaRepository<Personaje, Integer> {
	
    Optional<Personaje> findByPuesto(Integer puesto);
    List<Personaje> findAllByOrderByTotalDesc();
}
