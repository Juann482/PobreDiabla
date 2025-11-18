package com.games.h.service;

import java.util.List;
import java.util.Optional;

import com.games.h.model.Correos;
import com.games.h.model.Juego;
import com.games.h.model.Personaje;

public interface IJuegoService {
	
	List<Juego> findAll();
	
    Optional<Juego> get(Integer id);
    
    Juego save(Juego juego);
    
    void delete(Integer id);
    
    Optional<Juego> findByPuesto(Integer puesto);

    void guardarNuevoConAjuste(Juego nuevo);
    
    void guardarEditadoConAjuste(Juego editado, Integer puestoAnterior);

}
