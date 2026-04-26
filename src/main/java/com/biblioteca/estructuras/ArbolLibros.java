package com.biblioteca.estructuras;

import com.biblioteca.modelo.Libro;

public class ArbolLibros {

    private NodoLibro raiz;

    // INSERTAR
    public void insertar(Libro libro) {
        raiz = insertarRec(raiz, libro);
    }

    private NodoLibro insertarRec(NodoLibro nodo, Libro libro) {
        if (nodo == null) {
            return new NodoLibro(libro);
        }

        if (libro.getTitulo().compareToIgnoreCase(nodo.libro.getTitulo()) < 0) {
            nodo.izquierda = insertarRec(nodo.izquierda, libro);
        } else {
            nodo.derecha = insertarRec(nodo.derecha, libro);
        }

        return nodo;
    }

    // BUSCAR
    public Libro buscar(String titulo) {
        return buscarRec(raiz, titulo);
    }

    private Libro buscarRec(NodoLibro nodo, String titulo) {
        if (nodo == null) return null;

        if (titulo.equalsIgnoreCase(nodo.libro.getTitulo())) {
            return nodo.libro;
        }

        if (titulo.compareToIgnoreCase(nodo.libro.getTitulo()) < 0) {
            return buscarRec(nodo.izquierda, titulo);
        } else {
            return buscarRec(nodo.derecha, titulo);
        }
    }
}