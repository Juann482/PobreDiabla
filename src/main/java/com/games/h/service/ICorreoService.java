package com.games.h.service;

import java.util.List;
import java.util.Optional;

import com.games.h.model.Correos;

public interface ICorreoService {

	Optional<Correos> get(Integer id);

	Optional<Correos> findByNombre(String nombre);
	
	List<Correos> findByEmail(String correo);

	void delete(Integer id);

	Correos save(Correos correos);
	
	List<Correos> findAll();

}
