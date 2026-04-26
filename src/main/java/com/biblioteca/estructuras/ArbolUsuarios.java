package com.biblioteca.estructuras;

import com.biblioteca.modelo.Usuario;

public class ArbolUsuarios {

    private NodoUsuario raiz;

    // 🔹 INSERTAR USUARIO
    public void insertar(Usuario usuario) {
        raiz = insertarRec(raiz, usuario);
    }

    private NodoUsuario insertarRec(NodoUsuario nodo, Usuario usuario) {
        if (nodo == null) {
            return new NodoUsuario(usuario);
        }

        if (usuario.getId().compareToIgnoreCase(nodo.usuario.getId()) < 0) {
            nodo.izquierda = insertarRec(nodo.izquierda, usuario);
        } else {
            nodo.derecha = insertarRec(nodo.derecha, usuario);
        }

        return nodo;
    }

    // 🔹 BUSCAR USUARIO
    public Usuario buscar(String id) {
        return buscarRec(raiz, id);
    }

    private Usuario buscarRec(NodoUsuario nodo, String id) {
        if (nodo == null) return null;

        if (id.equalsIgnoreCase(nodo.usuario.getId())) {
            return nodo.usuario;
        }

        if (id.compareToIgnoreCase(nodo.usuario.getId()) < 0) {
            return buscarRec(nodo.izquierda, id);
        } else {
            return buscarRec(nodo.derecha, id);
        }
    }
}