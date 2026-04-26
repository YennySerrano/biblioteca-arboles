package com.biblioteca.estructuras;

public class ArbolCategorias {

    private NodoCategoria raiz;

    // 🔹 INSERTAR
    public void insertar(String categoria) {
        raiz = insertarRec(raiz, categoria);
    }

    private NodoCategoria insertarRec(NodoCategoria nodo, String categoria) {

        if (nodo == null) {
            return new NodoCategoria(categoria);
        }

        if (categoria.compareToIgnoreCase(nodo.categoria) < 0) {
            nodo.izquierda = insertarRec(nodo.izquierda, categoria);
        } else {
            nodo.derecha = insertarRec(nodo.derecha, categoria);
        }

        return nodo;
    }

    // 🔹 BUSCAR
    public boolean buscar(String categoria) {
        return buscarRec(raiz, categoria);
    }

    private boolean buscarRec(NodoCategoria nodo, String categoria) {

        if (nodo == null) return false;

        if (categoria.equalsIgnoreCase(nodo.categoria)) {
            return true;
        }

        if (categoria.compareToIgnoreCase(nodo.categoria) < 0) {
            return buscarRec(nodo.izquierda, categoria);
        } else {
            return buscarRec(nodo.derecha, categoria);
        }
    }
}