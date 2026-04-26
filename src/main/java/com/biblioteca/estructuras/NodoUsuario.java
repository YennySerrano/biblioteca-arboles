package com.biblioteca.estructuras;

import com.biblioteca.modelo.Usuario;

public class NodoUsuario {

    public Usuario usuario;
    public NodoUsuario izquierda;
    public NodoUsuario derecha;

    public NodoUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}