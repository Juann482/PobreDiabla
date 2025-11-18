package com.games.h.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.games.h.model.Personaje;

@Repository
public interface IPersonajeRepository extends JpaRepository<Personaje, Integer>{

	boolean existsByPuesto(Integer puesto);

    List<Personaje> findByPuestoGreaterThanOrderByPuestoAsc(Integer puesto);

    List<Personaje> findAllByOrderByPuestoAsc();

}
