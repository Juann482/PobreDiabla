package com.games.h.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.games.h.model.Correos;
import com.games.h.model.Juego;
import com.games.h.model.Personaje;
import com.games.h.repository.IJuegoRepository;

@Service
public class JuegoServiceImplement implements IJuegoService {

    @Autowired
    private IJuegoRepository juegoRepository;

    @Override
    public Optional<Juego> get(Integer id) {
        return juegoRepository.findById(id);
    }

    @Override
    public Juego findByNombre(String nombre) {
        return juegoRepository.findByNombre(nombre);
    }
    
    @Override
    public Personaje findByEnlaceAlbum(String enlaceAlbum) {
        return juegoRepository.findByEnlaceAlbum(enlaceAlbum);
    }

    @Override
    public Juego findByCalificacion(Integer calificacion) {
        return juegoRepository.findByCalificacion(calificacion);
    }

    @Override
    public void delete(Integer id) {
        juegoRepository.deleteById(id);
    }

    @Override
    public Juego save(Juego juego) {
        return juegoRepository.save(juego);
    }

    @Override
    public void update(Juego juego) {
        // Igual que save(), JPA actualiza automáticamente
        juegoRepository.save(juego);
    }

    @Override
    public List<Correos> findByCorreo(Correos correo) {
        return juegoRepository.findByCorreo(correo);
    }

    @Override
    public List<Juego> findAll() {
        return juegoRepository.findAll();
    }

	@Override
	public List<Juego> findAllOrderByCalificacion() {
		return juegoRepository.findAllByOrderByCalificacionDesc();
	}
}
