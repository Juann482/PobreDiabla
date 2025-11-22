package com.games.h.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "Personaje")
public class Personaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    // Ranking asignado por el service
    private Integer puesto;

    // Tres valores (1..100)
    private Integer valor1; // 20%
    private Integer valor2; // 35%
    private Integer valor3; // 45%

    // Total calculado (0..100)
    private Double total;

    private String caracteristica;
    private String imagen;

    @ManyToOne
    private Juego juego;

    public Personaje() {}

    public Personaje(Integer id, String nombre, Integer puesto,
                     Integer valor1, Integer valor2, Integer valor3,
                     Double total, String caracteristica, String imagen, Juego juego) {
        this.id = id;
        this.nombre = nombre;
        this.puesto = puesto;
        this.valor1 = valor1;
        this.valor2 = valor2;
        this.valor3 = valor3;
        this.total = total;
        this.caracteristica = caracteristica;
        this.imagen = imagen;
        this.juego = juego;
    }

    // ------------------ Getters y Setters ------------------

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getPuesto() { return puesto; }
    public void setPuesto(Integer puesto) { this.puesto = puesto; }

    public Integer getValor1() { return valor1; }
    public void setValor1(Integer valor1) { this.valor1 = valor1; }

    public Integer getValor2() { return valor2; }
    public void setValor2(Integer valor2) { this.valor2 = valor2; }

    public Integer getValor3() { return valor3; }
    public void setValor3(Integer valor3) { this.valor3 = valor3; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getCaracteristica() { return caracteristica; }
    public void setCaracteristica(String caracteristica) { this.caracteristica = caracteristica; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Juego getJuego() { return juego; }
    public void setJuego(Juego juego) { this.juego = juego; }

    // ------------------ Helpers ------------------

    /**
     * Recalcula el total con los porcentajes definidos.
     * Debe llamarse SIEMPRE desde el service antes de guardar o actualizar.
     */
    public void recalcularTotal() {
        double v1 = (valor1 == null) ? 0 : valor1;
        double v2 = (valor2 == null) ? 0 : valor2;
        double v3 = (valor3 == null) ? 0 : valor3;

        this.total = v1 * 0.20 + v2 * 0.35 + v3 * 0.45;
    }

    @Transient
    @JsonIgnore
    public int getEstrellas() {
        double pct = (total == null) ? 0 : total;
        int stars = (int) Math.ceil((pct / 100.0) * 5.0);
        if (stars < 1) stars = 1;
        if (stars > 5) stars = 5;
        return stars;
    }

    @Transient
    public int getTotalRounded() {
        return (total == null) ? 0 : (int) Math.round(total);
    }

    @Transient
    public boolean isEstrellaOro() {
        return total != null && total >= 80;
    }
}
