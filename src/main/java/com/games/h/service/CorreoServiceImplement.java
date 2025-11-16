package com.games.h.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.games.h.model.Correos;
import com.games.h.repository.ICorreosRepository;

@Service
public class CorreoServiceImplement implements ICorreoService{

	@Autowired
	private ICorreosRepository correoRepository;
	
	@Override
	public Optional<Correos> get(Integer id) {
		return correoRepository.findById(id);
	}

	@Override
	public Optional<Correos> findByNombre(String nombre) {
		return correoRepository.findByNombre(nombre);
	}

	@Override
	public List<Correos> findByEmail(String email) {
		return correoRepository.findByEmail(email);
	}

	@Override
	public void delete(Integer id) {
		correoRepository.deleteById(id);
	}

	@Override
	public Correos save(Correos correos) {
		return correoRepository.save(correos);
	}

	

	@Override
	public List<Correos> findAll() {
		return correoRepository.findAll();
	}

}
