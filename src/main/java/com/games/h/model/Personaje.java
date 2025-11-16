package com.games.h.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Personaje")
public class Personaje {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	private Integer calificacion;
	private String caracteristica;
	private String imagen;
	
	@ManyToOne
	private Juego juego;

	public Personaje() {}

	public Personaje(Integer id, String nombre, Integer calificacion, String caracteristica, String imagen,
			Juego juego) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.calificacion = calificacion;
		this.caracteristica = caracteristica;
		this.imagen = imagen;
		this.juego = juego;
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

	public Integer getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(Integer calificacion) {
		this.calificacion = calificacion;
	}

	public String getCaracteristica() {
		return caracteristica;
	}

	public void setCaracteristica(String caracteristica) {
		this.caracteristica = caracteristica;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Juego getJuego() {
		return juego;
	}

	public void setJuego(Juego juego) {
		this.juego = juego;
	}

	@Override
	public String toString() {
		return "Personaje [id=" + id + ", nombre=" + nombre + ", calificacion=" + calificacion + ", caracteristica="
				+ caracteristica + ", imagen=" + imagen + ", juego=" + juego + "]";
	}
	
	
}
