package com.games.h.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.games.h.model.Personaje;

@Repository
public interface IPersonajeRepository extends JpaRepository<Personaje, Integer>{

	Personaje findByNombre(String nombre);

	Personaje findByCalificacion(Integer calificacion);

	//List<Personaje> findAllByOrderByCalificacionDesc();

	List<Personaje> findByPuestoGreaterThanEqualOrderByPuestoAsc(Integer puesto);

	List<Personaje> findAllByOrderByPuestoAsc();

	Optional<Personaje> findByPuesto(Integer puesto);

}
