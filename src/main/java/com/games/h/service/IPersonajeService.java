package com.games.h.service;

import java.util.List;
import java.util.Optional;

import com.games.h.model.Juego;
import com.games.h.model.Personaje;
import com.games.h.model.Usuario;

public interface IPersonajeService {
	
	Personaje save(Personaje personaje);
	
	void delete(Integer id);
	
	Personaje update(Personaje personaje);
	
	List<Personaje> findAll();

	Optional<Personaje> findById(Integer id);

}
