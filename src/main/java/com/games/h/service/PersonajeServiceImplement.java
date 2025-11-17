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
        return personajeRepository.findAllByOrderByPuestoAsc();
    }

	/*@Override
	public List<Personaje> findAllOrderByCalificacion() {
		return personajeRepository.findAllByOrderByCalificacionDesc();
	}*/

	@Override
	public void guardarConAjuste(Personaje nuevo) {
		
		// 1. Revisar si el puesto ya existe
        List<Personaje> personajesConPuestoMayorIgual =
                personajeRepository.findByPuestoGreaterThanEqualOrderByPuestoAsc(nuevo.getPuesto());

        // 2. Recorrer puesto hacia abajo
        for(Personaje p : personajesConPuestoMayorIgual){
            p.setPuesto(p.getPuesto() + 1);
        }

        // 3. Guardar los cambios
        personajeRepository.saveAll(personajesConPuestoMayorIgual);
        personajeRepository.save(nuevo);
    }
		
}
