package com.games.h.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    
    @Override
    public List<Personaje> buscar(String nombre, String juego, Integer puesto, Integer estrellas) {
        
        System.out.println("🔍 Buscando personajes con parámetros:");
        System.out.println("   - nombre: " + nombre);
        System.out.println("   - juego: " + juego);
        System.out.println("   - puesto: " + puesto);
        System.out.println("   - estrellas: " + estrellas);

        List<Personaje> resultados = repo.findAll().stream()
            // FILTRAR por nombre
            .filter(p -> nombre == null || nombre.isEmpty() || 
                    (p.getNombre() != null && 
                     p.getNombre().toLowerCase().contains(nombre.toLowerCase())))
            
            // FILTRAR por juego (relación ManyToOne)
            .filter(p -> juego == null || juego.isEmpty() || 
                    (p.getJuego() != null && 
                     p.getJuego().getNombre() != null &&
                     p.getJuego().getNombre().toLowerCase().contains(juego.toLowerCase())))
            
            // FILTRAR por puesto (TOP)
            .filter(p -> puesto == null || (p.getPuesto() != null && p.getPuesto().equals(puesto)))
            
            // FILTRAR por estrellas (usando el método getEstrellas() transiente)
            .filter(p -> estrellas == null || p.getEstrellas() == estrellas)
            
            .collect(Collectors.toList());

        System.out.println("✅ Resultados encontrados: " + resultados.size());
        
        // Debug: mostrar detalles de cada resultado
        resultados.forEach(p -> {
            System.out.println("   - " + p.getNombre() + 
                             " | Puesto: " + p.getPuesto() + 
                             " | Total: " + p.getTotal() +
                             " | Estrellas: " + p.getEstrellas());
        });
        
        return resultados;
    }
    

}
