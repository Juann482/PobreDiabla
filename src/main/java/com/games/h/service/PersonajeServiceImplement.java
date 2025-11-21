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
    private IPersonajeRepository repo;

    @Override
    public List<Personaje> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Personaje> get(Integer id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Personaje> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Personaje> findByPuesto(Integer puesto) {
        return repo.findByPuesto(puesto);
    }

    /**
     * Guarda un personaje nuevo:
     * - Recalcula total con la fórmula de la entidad
     * - Lo guarda
     * - Reajusta los puestos de todos
     */
    @Override
    public Personaje save(Personaje personaje) {

        personaje.recalcularTotal();
        Personaje saved = repo.save(personaje);

        reajustarPuestos();
        return saved;
    }

    /**
     * Actualiza un personaje existente:
     * - Recalcula total
     * - Guarda
     * - Reajusta puestos
     */
    @Override
    public Personaje update(Personaje personaje) {


        personaje.recalcularTotal();
        Personaje saved = repo.save(personaje);

        reajustarPuestos();
        return saved;
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
        reajustarPuestos();
    }

    /**
     * Reasigna puestos a todos:
     * total alto = puesto 1
     */
    @Override
    public void reajustarPuestos() {
        List<Personaje> ordenados = repo.findAllByOrderByTotalDesc();
        int pos = 1;
        for (Personaje p : ordenados) {
            p.setPuesto(pos++);
            repo.save(p);
        }
    }

    @Override
    public void guardarConAjuste(Personaje personaje) {
        // Este método ahora solo llama a save(), ya que save incluye toda la lógica
        save(personaje);
    }
}
