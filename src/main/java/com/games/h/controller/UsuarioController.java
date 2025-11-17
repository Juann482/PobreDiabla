package com.games.h.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

//import org.apache.tomcat.util.http.fileupload.UploadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.games.h.model.Juego;
import com.games.h.model.Personaje;
import com.games.h.model.Usuario;
import com.games.h.model.Correos;
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

		model.addAttribute("juegos", juegoService.findAll());

		return "usuario/juegos";
	}

	@GetMapping("/JuegosRegistro")
	public String JuegosRegistro(Juego juego, Model model) {

		model.addAttribute("correo", correoService.findAll());
		return "usuario/registroJUEGOS";
	}
	
	@GetMapping("/EditJuego/{id}")
	public String EditarJuego(@PathVariable Integer id, Juego juego, Model model) {
		
		Juego j = juegoService.get(id).get();
		model.addAttribute("game", j);
		model.addAttribute("correo", correoService.findAll());
		return "usuario/editarJUEGOS";
	}
	
	@PostMapping("/SendJuegoEd")
	public String EnvioActJ(Juego juego) {
		
		Juego jg = juegoService.get(juego.getId())
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + juego.getId()));
		
		jg.setEnlaceAlbum(juego.getEnlaceAlbum());
		jg.setEnlaceDrive(juego.getEnlaceDrive());
		jg.setNombre(juego.getNombre());
		jg.setPuesto(juego.getPuesto());
		jg.setCalificacion(juego.getCalificacion());
		jg.setCorreo(juego.getCorreo());
		
		Integer nuevoPuesto = juego.getPuesto();
		  // Buscar si otro personaje ya tiene ese puesto
	    Optional<Juego> ocupante = juegoService.findByPuesto(nuevoPuesto);

	    if (ocupante.isPresent() && ocupante.get().getId() != juego.getId()) {
	        // El personaje que estaba en ese puesto pasa al puesto que tenía el editado
	        Juego otro = ocupante.get();
	        otro.setPuesto(juegoService.findById(juego.getId()).get().getPuesto());
	        juegoService.save(otro);
	    }
	    juegoService.save(juego);
		return "redirect:/usuario/Juegos";
	    
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
	
	@GetMapping("/DeleteJuego/{id}")
    public String DeleteJuego(@PathVariable Integer id) {
		
        Juego c = juegoService.get(id).orElseThrow(() -> new RuntimeException("Juego no encontrado"));
        juegoService.delete(id);
        LOGGER.warn("Juego eliminado: {}", c);
        
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
	
	@GetMapping("/EditPerson/{id}")
	public String EditPerson(@PathVariable Integer id, Personaje person, Model model) {
		
		Personaje p = personajeService.get(id).get();
		model.addAttribute("person", p);
		model.addAttribute("juego", juegoService.findAll());
		return "usuario/editarPERSONAJE";
	}
	
	@PostMapping("/EnviarEditP")
	public String EnviarNP(Personaje person) {
		
		Personaje pr = personajeService.get(person.getId())
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + person.getId()));
		
		pr.setNombre(person.getNombre());
		pr.setPuesto(person.getPuesto());
		pr.setCaracteristica(person.getCaracteristica());
		pr.setJuego(person.getJuego());
		pr.setCalificacion(person.getCalificacion());
		
		Integer nuevoPuesto = person.getPuesto();

	    // Buscar si otro personaje ya tiene ese puesto
	    Optional<Personaje> ocupante = personajeService.findByPuesto(nuevoPuesto);

	    if (ocupante.isPresent() && ocupante.get().getId() != person.getId()) {
	        // El personaje que estaba en ese puesto pasa al puesto que tenía el editado
	        Personaje otro = ocupante.get();
	        otro.setPuesto(personajeService.findById(person.getId()).get().getPuesto());
	        personajeService.save(otro);
	    }

		
		personajeService.save(pr);
		LOGGER.warn("Personaje actualizado; {}", pr);
		return "redirect:/usuario/Personajes";
	}
	
	@GetMapping("/DeletePerson/{id}")
    public String DeletePerson(@PathVariable Integer id) {
		
        Personaje u = personajeService.get(id).orElseThrow(() -> new RuntimeException("Personaje no encontrado"));
        personajeService.delete(id);
        LOGGER.warn("Personaje eliminado: {}", u);
        
        return "redirect:/usuario/Personajes";
    }
	
	//============================== CORREOS ===================================
	
	@GetMapping("/Correos")
	public String Correos(Model model) {
		
		model.addAttribute("correo", correoService.findAll());
		
		return "usuario/correos";
	}
	
	@PostMapping("/GuardarCorreo")
	public String CorreoSave(Correos correo) {
		correoService.save(correo);
		return "redirect:/usuario/Correos";
	}
	
	@GetMapping("/DeleteCorreo/{id}")
    public String DeleteCorreo(@PathVariable Integer id) {
		
        Correos c = correoService.get(id).orElseThrow(() -> new RuntimeException("Correo no encontrado"));
        correoService.delete(id);
        LOGGER.warn("Correo eliminado: {}", c);
        
        return "redirect:/usuario/Personajes";
    }
}
