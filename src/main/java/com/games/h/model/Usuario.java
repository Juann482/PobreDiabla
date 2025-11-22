package com.games.h.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	private Integer id;
	private String nombre;
	private String email;
	private String password;
	private String rol;
	
	@OneToMany(mappedBy = "usuario")
	@JsonIgnore
	private List<Juego> juego;

	public Usuario() {}

	public Usuario(Integer id, String nombre, String email, String password, String rol, List<Juego> juego) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.password = password;
		this.rol = rol;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public List<Juego> getJuego() {
		return juego;
	}

	public void setJuego(List<Juego> juego) {
		this.juego = juego;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", email=" + email + ", password=" + password + ", rol="
				+ rol + ", juego=" + juego + "]";
	}	
	
}
