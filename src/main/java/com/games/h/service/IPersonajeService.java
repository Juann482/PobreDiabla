package com.games.h.service;

import java.util.List;
import java.util.Optional;

import com.games.h.model.Personaje;

public interface IPersonajeService {
	
	Optional<Personaje> get (Integer id);
	
	Personaje findByNombre(String nombre);
	
	Personaje findByCalificacion(Integer calificacion);
	
	Personaje save(Personaje personaje);
	
	void delete(Integer id);
	
	void update(Personaje personaje);
	
	List<Personaje> findAll();

}
