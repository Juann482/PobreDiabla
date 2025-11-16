package com.games.h.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
 @Table(name = "Juegos")
public class Juego {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	private String imagen;
	private Integer calificacion;
	private String enlaceAlbum;
	private String enlaceDrive;
	private LocalDate fechaRegistro;
	
	@OneToMany(mappedBy = "juego")
	private List<Personaje> personaje = new ArrayList<>();
	
	@ManyToOne
	private Correos correo;
	
	@ManyToOne
	private Usuario usuario;

	public Juego() {}

	public Juego(Integer id, String nombre, String imagen, Integer calificacion, String enlaceAlbum, String enlaceDrive,
			LocalDate fechaRegistro, List<Personaje> personaje, Correos correo, Usuario usuario) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.imagen = imagen;
		this.calificacion = calificacion;
		this.enlaceAlbum = enlaceAlbum;
		this.enlaceDrive = enlaceDrive;
		this.fechaRegistro = fechaRegistro;
		this.personaje = personaje;
		this.correo = correo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Integer getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(Integer calificacion) {
		this.calificacion = calificacion;
	}

	public String getEnlaceAlbum() {
		return enlaceAlbum;
	}

	public void setEnlaceAlbum(String enlaceAlbum) {
		this.enlaceAlbum = enlaceAlbum;
	}

	public String getEnlaceDrive() {
		return enlaceDrive;
	}

	public void setEnlaceDrive(String enlaceDrive) {
		this.enlaceDrive = enlaceDrive;
	}

	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public List<Personaje> getPersonaje() {
		return personaje;
	}

	public void setPersonaje(List<Personaje> personaje) {
		this.personaje = personaje;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Correos getCorreo() {
		return correo;
	}

	public void setCorreo(Correos correo) {
		this.correo = correo;
	}

	@Override
	public String toString() {
		return "Juego [id=" + id + ", nombre=" + nombre + ", imagen=" + imagen + ", calificacion=" + calificacion
				+ ", enlaceAlbum=" + enlaceAlbum + ", enlaceDrive=" + enlaceDrive + ", fechaRegistro=" + fechaRegistro
				+ ", personaje=" + personaje + ", correo=" + correo + ", usuario=" + usuario + "]";
	}
	
}
