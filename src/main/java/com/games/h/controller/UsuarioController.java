package com.games.h.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	@PostMapping("/SendJuegoEd")
	public String crearJuego(Juego juego) {

		Juego original = juegoService.get(juego.getId()).orElseThrow(() -> new RuntimeException("Juego no encontrado"));

		Integer puestoAnterior = original.getPuesto();

		original.setNombre(juego.getNombre());
		original.setCalificacion(juego.getCalificacion());
		original.setEnlaceAlbum(juego.getEnlaceAlbum());
		original.setEnlaceDrive(juego.getEnlaceDrive());
		original.setPuesto(juego.getPuesto());

		juegoService.guardarEditadoConAjuste(original, puestoAnterior);

		return "redirect:/usuario/Juegos";
	}

	@GetMapping("/EditJuego/{id}")
	public String EditarJuego(@PathVariable Integer id, Juego juego, Model model) {

		Juego j = juegoService.get(id).get();
		model.addAttribute("game", j);
		model.addAttribute("correo", correoService.findAll());
		return "usuario/editarJUEGOS";
	}

	@PostMapping("/JuegoSave")
	public String JuevoSave(Juego juego, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {

		Usuario u = usuarioService.findById(1).get();
		juego.setFechaRegistro(LocalDate.now());
		juego.setUsuario(u);

		if (juego.getId() == null) {
			String nombreImagen = upload.saveImages(file, juego.getNombre());
			juego.setImagen(nombreImagen);
		}

		juegoService.guardarNuevoConAjuste(juego);

		return "redirect:/usuario/JuegosRegistro";
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
	public String Personajes(@RequestParam(required = false) String nombre,
	        @RequestParam(required = false) String juego,
	        @RequestParam(required = false) Integer puesto,
	        @RequestParam(required = false) Integer estrellas, Model model) {
		
		System.out.println("🎯🎯🎯 CONTROLADOR Personajes EJECUTADO 🎯🎯🎯");

		List<Personaje> personajes;
		
		// Si hay parámetros de búsqueda, usar el servicio de búsqueda
		if ((nombre != null && !nombre.isEmpty()) || 
		    (juego != null && !juego.isEmpty()) || 
		    puesto != null || 
		    estrellas != null) {
			
			personajes = personajeService.buscar(nombre, juego, puesto, estrellas);
			System.out.println("Búsqueda filtrada - Resultados: " + personajes.size());
			
		} else {
			// Si no hay parámetros, cargar todos y ordenar por total
			personajes = personajeService.findAll();
			personajes.sort(Comparator.comparing(Personaje::getTotal,
					Comparator.nullsLast(Double::compare)).reversed());
			System.out.println("Cargando todos los personajes: " + personajes.size());
		}

		model.addAttribute("personajes", personajes);
		return "usuario/personajes";
	}

	@GetMapping("/PersonajeRegistro")
	public String PersonajeRegistro(Personaje personaje, Model model) {
		model.addAttribute("juego", juegoService.findAll());
		return "usuario/registroPERSONAJE";
	}

	@PostMapping("/PersonSave")
	public String PersonSave(Personaje person, @RequestParam("img") MultipartFile file,
			RedirectAttributes redirectAttributes) throws IOException {

		// Nombre vacío => ???
		if (person.getNombre() == null || person.getNombre().trim().isEmpty()) {
			person.setNombre("???");
		}

		// Característica vacía => "Sin descripción."
		if (person.getCaracteristica() == null || person.getCaracteristica().trim().isEmpty()) {
			person.setCaracteristica("Sin descripción.");
		}

		// Imagen solo en nuevos
		if (person.getId() == null) {
			String nombreImagen = upload.saveImages(file, person.getNombre());
			person.setImagen(nombreImagen);
		}

		personajeService.guardarConAjuste(person);

		redirectAttributes.addFlashAttribute("mensaje", "guardado");
		return "redirect:/usuario/PersonajeRegistro";
	}

	@GetMapping("/EditPerson/{id}")
	public String EditPerson(@PathVariable Integer id, Personaje person, Model model) {

		Personaje p = personajeService.get(id).orElseThrow(() -> new RuntimeException("Personaje no encontrado"));

		model.addAttribute("person", p);
		model.addAttribute("juego", juegoService.findAll());
		return "usuario/editarPERSONAJE";
	}

	@PostMapping("/EnviarEditP")
	public String EnviarNP(Personaje person, RedirectAttributes redirectAttributes) {

		Personaje pr = personajeService.get(person.getId())
				.orElseThrow(() -> new RuntimeException("Personaje no encontrado con ID: " + person.getId()));

		// Valores
		pr.setValor1(person.getValor1());
		pr.setValor2(person.getValor2());
		pr.setValor3(person.getValor3());
		
		pr.setNombre(person.getNombre());
		pr.setTotal(person.getTotal());

		// Descripción
		pr.setCaracteristica(
				(person.getCaracteristica() == null || person.getCaracteristica().trim().isEmpty()) ? "Sin descripción."
						: person.getCaracteristica());


		// Guardar + reajustar puestos
		personajeService.guardarConAjuste(pr);

		redirectAttributes.addFlashAttribute("mensaje", "editado");
		LOGGER.warn("Personaje actualizado: {}", pr);

		return "redirect:/usuario/Personajes";
	}

	@GetMapping("/DeletePerson/{id}")
	public String DeletePerson(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

		personajeService.delete(id);
		personajeService.reajustarPuestos(); // recalcular ranking

		redirectAttributes.addFlashAttribute("mensaje", "eliminado");

		return "redirect:/usuario/Personajes";
	}

	// ============================== CORREOS ===================================

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

		return "redirect:/usuario/Correos";
	}
	
	//================== Busqueda AJAX ===========
	
	@GetMapping("/buscarPersonajes")
	@ResponseBody
	public List<Personaje> buscarPersonajes(
	        @RequestParam(required = false) String nombre,
	        @RequestParam(required = false) String juego,
	        @RequestParam(required = false) Integer puesto,
	        @RequestParam(required = false) Integer estrellas) {

		System.out.println("🎯 Búsqueda AJAX recibida:");
	    System.out.println("   - nombre: " + nombre);
	    System.out.println("   - juego: " + juego);
	    System.out.println("   - puesto: " + puesto);
	    System.out.println("   - estrellas: " + estrellas);

		
	    List<Personaje> resultados = personajeService.buscar(nombre, juego, puesto, estrellas);
	    System.out.println("Búsqueda AJAX - Parámetros: nombre=" + nombre + ", juego=" + juego + 
	                      ", puesto=" + puesto + ", estrellas=" + estrellas);
	    System.out.println("📊 Total de resultados: " + resultados.size());
	    
	    // Debug detallado
	    if (!resultados.isEmpty()) {
	        System.out.println("👥 Primer personaje encontrado:");
	        Personaje primer = resultados.get(0);
	        System.out.println("   - ID: " + primer.getId());
	        System.out.println("   - Nombre: " + primer.getNombre());
	        System.out.println("   - Puesto: " + primer.getPuesto());
	        System.out.println("   - Total: " + primer.getTotal());
	        System.out.println("   - Estrellas: " + primer.getEstrellas());
	        System.out.println("   - Juego: " + (primer.getJuego() != null ? primer.getJuego().getNombre() : "null"));
	    } 
	    return resultados;
	}

}