package com.games.h.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.apache.tomcat.util.http.fileupload.UploadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.games.h.model.Juego;
import com.games.h.model.Personaje;
import com.games.h.model.Usuario;
import com.games.h.service.ICorreoService;
import com.games.h.service.IJuegoService;
import com.games.h.service.IPersonajeService;
import com.games.h.service.IUsuarioService;
import com.games.h.service.UploadFileService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(UsuarioController.class);

	// =========================================================

	@Autowired
	private IJuegoService juegoService;

	@Autowired
	private ICorreoService correoService;

	@Autowired
	private IPersonajeService personajeService;
	
	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private UploadFileService upload;

	// ============================ JUEGOS =============================

	@GetMapping("/Juegos")
	public String juegos(Juego juego, Model model) {

		model.addAttribute("juegos", juegoService.findAllOrderByCalificacion());

		return "usuario/juegos";
	}

	@GetMapping("/JuegosRegistro")
	public String JuegosRegistro(Juego juego, Model model) {

		model.addAttribute("correo", correoService.findAll());
		return "usuario/registroJUEGOS";
	}
	
	@PostMapping("/JuegoSave")
	public String JuevoSave(Juego juego, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
		
		Usuario u = usuarioService.findById(1).get();
		juego.setFechaRegistro(LocalDate.now());
		juego.setUsuario(u);
		if (juego.getId() == null) {
			String nombreImagen = upload.saveImages(file, juego.getNombre());
			juego.setImagen(nombreImagen);
		}
		juegoService.save(juego);
		return "redirect:/usuario/Juegos";
	}
	
	// ========================= PERSONAJE ============================

	@GetMapping("/Personajes")
	public String Personajes(Personaje personaje, Model model) {
		
		model.addAttribute("personajes", personajeService.findAll());
		
		return "usuario/personajes";
	}
	
	@GetMapping("/PersonajeRegistro")
	public String PersonajeRegistro(Personaje personaje, Model model) {
		
		model.addAttribute("juego", juegoService.findAll());
		
		return "usuario/registroPERSONAJE";
	}
	
	@PostMapping("/PersonSave")
	public String PersonSave(Personaje person, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
		
		Usuario u = usuarioService.findById(1).get();
		if (person.getId() == null) {
			String nombreImagen = upload.saveImages(file, person.getNombre());
			person.setImagen(nombreImagen);
		}
		personajeService.guardarConAjuste(person);
		return "redirect:/usuario/Personajes";
	}
}
