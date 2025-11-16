package com.games.h.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.games.h.model.Personaje;
import com.games.h.repository.IPersonajeRepository;

@Service
public class PersonajeServiceImplement implements IPersonajeService {

    @Autowired
    private IPersonajeRepository personajeRepository;

    @Override
    public Optional<Personaje> get(Integer id) {
        return personajeRepository.findById(id);
    }

    @Override
    public Personaje findByNombre(String nombre) {
        return personajeRepository.findByNombre(nombre);
    }

    @Override
    public Personaje findByCalificacion(Integer calificacion) {
        return personajeRepository.findByCalificacion(calificacion);
    }

    @Override
    public Personaje save(Personaje personaje) {
        return personajeRepository.save(personaje);
    }

    @Override
    public void delete(Integer id) {
        personajeRepository.deleteById(id);
    }

    @Override
    public void update(Personaje personaje) {
        // El update en Spring Data JPA es exactamente igual que save()
        personajeRepository.save(personaje);
    }

    @Override
    public List<Personaje> findAll() {
        return personajeRepository.findAll();
    }
}
