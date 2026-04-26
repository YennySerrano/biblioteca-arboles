package com.biblioteca.estructuras;

import com.biblioteca.modelo.Libro;

public class NodoLibro {

    public Libro libro;
    public NodoLibro izquierda;
    public NodoLibro derecha;

    public NodoLibro(Libro libro) {
        this.libro = libro;
    }
}