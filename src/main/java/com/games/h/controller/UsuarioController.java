package com.games.h.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.games.h.model.Juego;
import com.games.h.service.ICorreoService;
import com.games.h.service.IJuegoService;
import com.games.h.service.IPersonajeService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(UsuarioController.class);
	
	//=========================================================
	
	@Autowired
	private IJuegoService juegoService;
	
	@Autowired
	private ICorreoService correoService;
	
	@Autowired
	private IPersonajeService personajeService;
	
	//=========================================================
	
	@GetMapping("/Juegos")
	public String juegos(Juego juego, org.springframework.ui.Model model) {
		
		model.addAttribute("juegos", juegoService);
		return "usuario/juegos";
	}

	
}
