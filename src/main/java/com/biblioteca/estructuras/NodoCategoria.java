package com.biblioteca.estructuras;

public class NodoCategoria {

    public String categoria;
    public NodoCategoria izquierda;
    public NodoCategoria derecha;

    public NodoCategoria(String categoria) {
        this.categoria = categoria;
    }
}