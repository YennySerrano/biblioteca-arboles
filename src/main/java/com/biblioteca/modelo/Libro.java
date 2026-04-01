package com.biblioteca.modelo;

public class Libro {

    private final String titulo; 
    private final String autor;
    private boolean disponible;
    private Usuario prestadoA;

    public Libro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
        this.disponible = true;
        this.prestadoA = null;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public Usuario getPrestadoA() {
        return prestadoA;
    }

    public String getTextoPrestatario() {
        if (disponible || prestadoA == null) {
            return "-";
        }
        return prestadoA.getNombre();
    }

    public void prestarA(Usuario usuario) {
        if (disponible) {
            this.disponible = false;
            this.prestadoA = usuario;
        }
    }

    public void devolver() {
        this.disponible = true;
        this.prestadoA = null;
    }
}