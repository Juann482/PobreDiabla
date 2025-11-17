package com.games.h.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.games.h.model.Correos;
import com.games.h.model.Juego;
import com.games.h.model.Personaje;

@Repository
public interface IJuegoRepository extends JpaRepository<Juego, Integer>{

	List<Correos> findByCorreo(Correos correo);

	Juego findByNombre(String nombre);

	Juego findByCalificacion(Integer calificacion);

	Juego findByEnlaceAlbum(String enlaceAlbum);

	List<Juego> findAllByOrderByCalificacionDesc();

	List<Juego> findByPuestoGreaterThanEqualOrderByPuestoAsc(Integer puesto);

	List<Juego> findByPuesto(Integer puesto);

	List<Juego> findAllByOrderByPuestoAsc();

}
