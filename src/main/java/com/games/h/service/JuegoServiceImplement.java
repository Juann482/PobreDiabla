package com.games.h.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.games.h.model.Juego;
import com.games.h.repository.IJuegoRepository;

import jakarta.transaction.Transactional;

@Service
public class JuegoServiceImplement implements IJuegoService {

	@Autowired
	private IJuegoRepository juegoRepository;

	@Override
	public List<Juego> findAll() {
		return juegoRepository.findAll(Sort.by("puesto"));
	}

	@Override
	public Optional<Juego> get(Integer id) {
		return juegoRepository.findById(id);
	}

	@Override
	public Juego save(Juego juego) {
		return juegoRepository.save(juego);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		
		Juego eliminado = juegoRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Juego no encontrado"));

	    Integer puestoEliminado = eliminado.getPuesto();
	    
	 // 1. Eliminar el juego
	    juegoRepository.deleteById(id);

	    // 2. Mover hacia arriba todos los juegos con puesto mayor
	    List<Juego> lista = juegoRepository.findByPuestoGreaterThanOrderByPuestoAsc(puestoEliminado);
	    for (Juego j : lista) {
	        j.setPuesto(j.getPuesto() - 1);
	    }
	    
	    juegoRepository.saveAll(lista);
	}

	@Override
	public Optional<Juego> findByPuesto(Integer puesto) {
		return juegoRepository.findByPuesto(puesto);
	}

	/**
	 * ============================================ CREAR JUEGO (Insertar nuevo con
	 * ajuste) ============================================
	 */
	@Override
	public void guardarNuevoConAjuste(Juego nuevo) {

		List<Juego> mover = juegoRepository.findByPuestoGreaterThanEqualOrderByPuestoAsc(nuevo.getPuesto());

		for (Juego j : mover) {
			j.setPuesto(j.getPuesto() + 1);
		}

		juegoRepository.saveAll(mover);
		juegoRepository.save(nuevo);
	}

	/**
	 * ============================================ EDITAR JUEGO (Reacomodando
	 * puestos) ============================================
	 */
	@Override
	public void guardarEditadoConAjuste(Juego editado, Integer puestoAnterior) {

		Integer nuevoPuesto = editado.getPuesto();

		if (nuevoPuesto.equals(puestoAnterior)) {
			// No cambió de posición, solo guardar datos
			juegoRepository.save(editado);
			return;
		}

		List<Juego> lista = juegoRepository.findAll();

		for (Juego j : lista) {
			if (j.getId().equals(editado.getId()))
				continue;

			// Si se mueve hacia ARRIBA (ej: de 9 → 4)
			if (nuevoPuesto < puestoAnterior) {
				if (j.getPuesto() >= nuevoPuesto && j.getPuesto() < puestoAnterior) {
					j.setPuesto(j.getPuesto() + 1);
				}
			}

			// Si se mueve hacia ABAJO (ej: de 3 → 10)
			else {
				if (j.getPuesto() <= nuevoPuesto && j.getPuesto() > puestoAnterior) {
					j.setPuesto(j.getPuesto() - 1);
				}
			}
		}

		juegoRepository.saveAll(lista);
		juegoRepository.save(editado);
	}
}
