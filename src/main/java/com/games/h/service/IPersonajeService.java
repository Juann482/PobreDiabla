package com.games.h.service;

import java.util.List;
import java.util.Optional;

import com.games.h.model.Juego;
import com.games.h.model.Personaje;
import com.games.h.model.Usuario;

public interface IPersonajeService {
	
	List<Personaje> findAll();
    Optional<Personaje> get(Integer id);
    Personaje save(Personaje personaje);
    void delete(Integer id);
    Optional<Personaje> findByPuesto(Integer puesto);
    void guardarConAjuste(Personaje personaje); // guarda y reajusta puestos
    void reajustarPuestos(); // recalcula puestos de todos (usar después de delete)
	Optional<Personaje> findById(Integer id);
	Personaje update(Personaje personaje);
	List<Personaje> buscar(String nombre, String juego, Integer puesto, Integer estrellas);

}
