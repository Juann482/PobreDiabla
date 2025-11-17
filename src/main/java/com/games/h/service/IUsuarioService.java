package com.games.h.service;

import java.util.Optional;

import com.games.h.model.Usuario;

public interface IUsuarioService {
	
	Optional<Usuario> findById (Integer id);

}
