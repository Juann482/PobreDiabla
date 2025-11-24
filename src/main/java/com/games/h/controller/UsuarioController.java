package com.games.h.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

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
	public String personajes(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(required = false) String buscar,
	        @RequestParam(required = false) Integer minTotal,
	        @RequestParam(required = false) Integer maxTotal,
	        @RequestParam(required = false) Integer minPuesto,
	        @RequestParam(required = false) Integer maxPuesto,
	        @RequestParam(required = false) String ordenar,
	        Model model) {

	    int pageSize = 15;

	    // ▼ 1. Obtener personajes base
	    List<Personaje> personajes = personajeService.findAll();

	    // ▼ 2. FILTRO: Buscar por texto
	    if (buscar != null && !buscar.trim().isEmpty()) {
	        String filtro = buscar.toLowerCase();
	        personajes = personajes.stream()
	                .filter(p ->
	                        (p.getNombre() != null && p.getNombre().toLowerCase().contains(filtro)) ||
	                        (p.getJuego() != null && p.getJuego().getNombre().toLowerCase().contains(filtro))
	                )
	                .toList();
	    }

	    // ▼ 3. FILTRO: Totales
	    if (minTotal != null)
	        personajes = personajes.stream()
	                .filter(p -> p.getTotal() >= minTotal)
	                .toList();

	    if (maxTotal != null)
	        personajes = personajes.stream()
	                .filter(p -> p.getTotal() <= maxTotal)
	                .toList();

	    // ▼ 4. FILTRO: Puestos
	    if (minPuesto != null)
	        personajes = personajes.stream()
	                .filter(p -> p.getPuesto() >= minPuesto)
	                .toList();

	    if (maxPuesto != null)
	        personajes = personajes.stream()
	                .filter(p -> p.getPuesto() <= maxPuesto)
	                .toList();

	    // ▼ 5. ORDENAR SIEMPRE AL FINAL
	    Comparator<Personaje> comp = Comparator.comparing(Personaje::getTotal);

	    if (ordenar == null || ordenar.isEmpty()) {
	        // ✔ Orden por defecto = total DESC
	        comp = Comparator.comparing(Personaje::getTotal,
	                Comparator.nullsLast(Double::compare)).reversed();
	    } else {
	        switch (ordenar) {
	            case "totalAsc":
	                comp = Comparator.comparing(Personaje::getTotal);
	                break;

	            case "totalDesc":
	                comp = Comparator.comparing(Personaje::getTotal).reversed();
	                break;

	            case "puestoAsc":
	                comp = Comparator.comparing(Personaje::getPuesto);
	                break;

	            case "puestoDesc":
	                comp = Comparator.comparing(Personaje::getPuesto).reversed();
	                break;

	            case "nombreAsc":
	                comp = Comparator.comparing(Personaje::getNombre, String.CASE_INSENSITIVE_ORDER);
	                break;

	            case "nombreDesc":
	                comp = Comparator.comparing(Personaje::getNombre, String.CASE_INSENSITIVE_ORDER).reversed();
	                break;
	        }
	    }

	    personajes = personajes.stream()
	            .sorted(comp)
	            .toList();

	    // ▼ 6. Paginación
	    int start = Math.min(page * pageSize, personajes.size());
	    int end = Math.min(start + pageSize, personajes.size());

	    List<Personaje> pageList = personajes.subList(start, end);

	    int totalPages = (int) Math.ceil((double) personajes.size() / pageSize);

	    // ▼ 7. Enviar datos
	    model.addAttribute("personajes", pageList);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("buscar", buscar);
	    model.addAttribute("minTotal", minTotal);
	    model.addAttribute("maxTotal", maxTotal);
	    model.addAttribute("minPuesto", minPuesto);
	    model.addAttribute("maxPuesto", maxPuesto);
	    model.addAttribute("ordenar", ordenar);

	    return "usuario/personajes";
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
	
	@GetMapping("/PersonajeRegistro")
	public String PersonajeRegistro(Personaje personaje, Model model) {
		
	    model.addAttribute("juego", juegoService.findAll());
	    
	    return "usuario/registroPERSONAJE";

	}
	
	@PostMapping("/PersonSave")
	public String PersonSave(Personaje person,
	                         @RequestParam("img") MultipartFile file,
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
}
