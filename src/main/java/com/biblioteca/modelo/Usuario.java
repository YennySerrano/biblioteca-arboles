package com.biblioteca.modelo;

public class Usuario {

    private static final int MAX_LIBROS = 2;

    private final String nombre; 
    private final String id;
    private final Libro[] prestamosActivos;
    private int numPrestamos;

    public Usuario(String nombre, String id) {
        this.nombre = nombre;
        this.id = id;
        this.prestamosActivos = new Libro[MAX_LIBROS];
        this.numPrestamos = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public boolean puedeTomarOtroLibro() {
        return numPrestamos < MAX_LIBROS;
    }

    public int getNumPrestamosActivos() {
        return numPrestamos;
    }

    public void registrarPrestamo(Libro libro) {
        if (numPrestamos < MAX_LIBROS && libro != null) {
            prestamosActivos[numPrestamos++] = libro;
        }
    }

    public void liberarPrestamo(Libro libro) {
        if (libro == null) return;
        for (int i = 0; i < numPrestamos; i++) {
            if (prestamosActivos[i] == libro) {
                prestamosActivos[i] = prestamosActivos[numPrestamos - 1];
                prestamosActivos[numPrestamos - 1] = null;
                numPrestamos--;
                return;
            }
        }
    }
}