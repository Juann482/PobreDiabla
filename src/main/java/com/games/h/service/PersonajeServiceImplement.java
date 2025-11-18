package com.games.h.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.games.h.model.Juego;
import com.games.h.model.Personaje;
import com.games.h.repository.IPersonajeRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonajeServiceImplement implements IPersonajeService {

    @Autowired
    private IPersonajeRepository personajeRepository;

 // -----------------------------
    // Obtener todos ordenados
    // -----------------------------
    @Override
    public List<Personaje> findAll() {
        return personajeRepository.findAllByOrderByPuestoAsc();
    }

    // -----------------------------
    // Crear personaje (sin repetir puesto)
    // -----------------------------
    @Override
    public Personaje save(Personaje personaje) {

        // Si ya existe alguien con ese puesto → mover hacia abajo
        if (personajeRepository.existsByPuesto(personaje.getPuesto())) {
            desplazarHaciaAbajo(personaje.getPuesto());
        }

        return personajeRepository.save(personaje);
    }

    // -----------------------------
    // Editar personaje
    // -----------------------------
    @Override
    public Personaje update(Personaje personaje) {
        Personaje original = personajeRepository.findById(personaje.getId())
                .orElseThrow(() -> new RuntimeException("Personaje no encontrado"));

        Integer puestoOriginal = original.getPuesto();
        Integer nuevoPuesto = personaje.getPuesto();

        // Si cambió el puesto
        if (!puestoOriginal.equals(nuevoPuesto)) {

            // Si el nuevo puesto está ocupado → desplazar hacia abajo
            if (personajeRepository.existsByPuesto(nuevoPuesto)) {
                desplazarHaciaAbajo(nuevoPuesto);
            }
        }

        return personajeRepository.save(personaje);
    }

    // -----------------------------
    // Eliminar personaje (suben todos los de abajo)
    // -----------------------------
    @Override
    @Transactional
    public void delete(Integer id) {

        Personaje eliminado = personajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personaje no encontrado"));

        Integer puestoEliminado = eliminado.getPuesto();

        personajeRepository.delete(eliminado);

        // Todos los que tienen puesto mayor suben
        List<Personaje> lista = personajeRepository
                .findByPuestoGreaterThanOrderByPuestoAsc(puestoEliminado);

        for (Personaje p : lista) {
            p.setPuesto(p.getPuesto() - 1);
        }

        personajeRepository.saveAll(lista);
    }

    // -----------------------------
    // FUNCION EXTRA → desplazamiento hacia abajo
    // -----------------------------
    private void desplazarHaciaAbajo(Integer puestoInicial) {

        List<Personaje> lista = personajeRepository.findAllByOrderByPuestoAsc();

        // Recorre de abajo hacia arriba para evitar conflictos
        for (int i = lista.size() - 1; i >= 0; i--) {
            Personaje p = lista.get(i);

            if (p.getPuesto() >= puestoInicial) {
                p.setPuesto(p.getPuesto() + 1);
            }
        }

        personajeRepository.saveAll(lista);
    }

	@Override
	public Optional<Personaje> findById(Integer id) {
		return personajeRepository.findById(id);
	}
}
