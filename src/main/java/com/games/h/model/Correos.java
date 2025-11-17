package com.games.h.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "correo")
public class Correos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	private String email;
	private String contraseña;
	
	@OneToMany(mappedBy = "correo")
	private List<Juego> juegos = new ArrayList<>();

	public Correos() {}

	public Correos(Integer id, String nombre, String email, String contraseña, List<Juego> juegos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.contraseña = contraseña;
		this.juegos = juegos;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public List<Juego> getJuegos() {
		return juegos;
	}

	public void setJuegos(List<Juego> juegos) {
		this.juegos = juegos;
	}

	@Override
	public String toString() {
		return "emails [id=" + id + ", nombre=" + nombre + ", email=" + email + ", contraseña=" + contraseña
				+ ", juegos=" + juegos + "]";
	}
	
	
	
	
	
	
}
