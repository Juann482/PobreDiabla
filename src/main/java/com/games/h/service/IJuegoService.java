package com.games.h.service;

import java.util.List;
import java.util.Optional;

import com.games.h.model.Correos;
import com.games.h.model.Juego;
import com.games.h.model.Personaje;

public interface IJuegoService {
	
	Optional<Juego> get(Integer id);
	
	Juego findByNombre(String nombre);
	
	Juego findByCalificacion(Integer calificacion);
	
	Juego findByEnlaceAlbum(String EnlaceAlbum);
	
	List<Juego> findAllOrderByCalificacion();
	
	void guardarConAjusteJJ(Juego nuevo);
	
	void delete(Integer id);
	
	Juego save(Juego juego);
	
	void update(Juego juego);
	
	List<Correos> findByCorreo(Correos correo);
	
	List<Juego> findAll();
	
	Optional<Juego> findByPuesto(Integer Puesto);

	Optional<Juego> findById(Integer id);

}
