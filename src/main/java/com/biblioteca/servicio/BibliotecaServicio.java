package com.biblioteca.servicio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Usuario;

import jakarta.annotation.PostConstruct;

@Service
public class BibliotecaServicio {
   //LISTAS
    private final List<Libro> libros = new ArrayList<>();
    private final List<Usuario> usuarios = new ArrayList<>();

    // ESTRUCTURA DE DATOS
    private final Stack<String> historialDevoluciones = new Stack<>();
    private final Queue<String> registroPrestamos = new LinkedList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @PostConstruct
    public void iniciarDatos() {
        registrarLibroInterno("Cien años de soledad", "García Márquez");
        registrarLibroInterno("Don Quijote", "Cervantes");
        registrarLibroInterno("El Principito", "Saint-Exupéry");

        Usuario felix = registrarUsuarioInterno("Felix Santafe", "1111111111");
        Usuario yenny = registrarUsuarioInterno("Yenny Serrano", "1000000000");
        
        Libro dq = buscarLibroPorTitulo("Don Quijote");
        if (felix != null && dq != null) {
            prestarLibro(dq.getTitulo(), felix.getId());
        }
    }

    // USUARIO 
    public String agregarUsuario(String nombre, String id) {
        String n = (nombre != null) ? nombre.trim() : "";
        String i = (id != null) ? id.trim() : "";
        if (n.isEmpty() || i.isEmpty()) return "Nombre e id son obligatorios.";
        if (buscarUsuarioPorId(i) != null) return "Ya existe ese ID.";
        usuarios.add(new Usuario(n, i));
        return null;
    }

    public String eliminarUsuario(String id) {
        Usuario u = buscarUsuarioPorId(id);
        if (u == null) return "Usuario no encontrado.";
        libros.stream().filter(l -> l.getPrestadoA() == u).forEach(l -> {
            l.devolver();
            u.liberarPrestamo(l);
        });
        usuarios.remove(u);
        return null;
    }
   //LISTA USUARIO
    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public Usuario buscarUsuarioPorId(String id) {
        for (Usuario u : usuarios) {
            if (u.getId().equalsIgnoreCase(id)) return u;
        }
        return null;
    }

    public String agregarLibro(String titulo, String autor) {
        String t = (titulo != null) ? titulo.trim() : "";
        String a = (autor != null) ? autor.trim() : "";
        if (t.isEmpty() || a.isEmpty()) return "Datos obligatorios.";
        if (buscarLibroPorTitulo(t) != null) return "Ya existe el libro.";
        libros.add(new Libro(t, a));
        return null;
    }
    //LISTA LIBRO
    public List<Libro> listarLibrosFiltrados(String consulta) {
        if (consulta == null || consulta.isBlank()) return new ArrayList<>(libros);
        String q = consulta.toLowerCase(Locale.ROOT).trim();
        return libros.stream()
                .filter(l -> l.getTitulo().toLowerCase(Locale.ROOT).contains(q) || 
                             l.getAutor().toLowerCase(Locale.ROOT).contains(q))
                .collect(Collectors.toList());
    }

    public Libro buscarLibroPorTitulo(String titulo) {
        for (Libro l : libros) {
            if (l.getTitulo().equalsIgnoreCase(titulo)) return l;
        }
        return null;
    }

    //  PILA  Y COLA PRESTAMO Y DEVOLUCION DE LIBRO

    public String prestarLibro(String tituloLibro, String usuarioId) {
        Libro libro = buscarLibroPorTitulo(tituloLibro);
        Usuario usuario = buscarUsuarioPorId(usuarioId);

        if (libro == null || usuario == null) return "Error en datos.";
        if (!libro.isDisponible()) return "No disponible.";
        if (!usuario.puedeTomarOtroLibro()) return "Límite alcanzado.";

        libro.prestarA(usuario);
        usuario.registrarPrestamo(libro);

        // Registro en COLA 
        String fecha = LocalDateTime.now().format(formatter);
        registroPrestamos.add(usuario.getNombre() + " | " + fecha + " | " + libro.getTitulo() + " | PRESTAMO");
        return null;
    }
    //DEVOLUCION
    public String procesarDevolucion(String tituloLibro) {
        Libro libro = buscarLibroPorTitulo(tituloLibro);
        if (libro == null || libro.isDisponible()) return "No está prestado.";

        Usuario usuario = libro.getPrestadoA();
        String nombre = (usuario != null) ? usuario.getNombre() : "Anónimo";
        
        if (usuario != null) usuario.liberarPrestamo(libro);
        libro.devolver();

        // Registro en PILA 
        String fecha = LocalDateTime.now().format(formatter);
        historialDevoluciones.push(nombre + " | " + fecha + " | " + libro.getTitulo() + " | DEVOLUCION");
        return null;
    }

    //  HISTORIAL 
    public List<String> obtenerHistorialPila() {
        return new ArrayList<>(historialDevoluciones);
    }

    public List<String> obtenerRegistroCola() {
        return new ArrayList<>(registroPrestamos);
    }

    
    private void registrarLibroInterno(String t, String a) {
        libros.add(new Libro(t, a));
    }

    private Usuario registrarUsuarioInterno(String n, String i) {
        Usuario u = new Usuario(n, i);
        usuarios.add(u);
        return u;
    }
}