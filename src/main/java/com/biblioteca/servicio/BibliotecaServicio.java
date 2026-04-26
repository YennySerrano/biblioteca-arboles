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

import com.biblioteca.estructuras.ArbolLibros;
import com.biblioteca.estructuras.ArbolUsuarios; // 🔥 NUEVO
import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Usuario;

import jakarta.annotation.PostConstruct;

@Service
public class BibliotecaServicio {

    // =========================
    // LISTAS
    // =========================
    private final List<Libro> libros = new ArrayList<>();
    private final List<Usuario> usuarios = new ArrayList<>();

    // =========================
    // ÁRBOLES
    // =========================
    private final ArbolUsuarios arbolUsuarios = new ArbolUsuarios();
    private final ArbolLibros arbolLibros = new ArbolLibros(); // 🔥 NUEVO

    // =========================
    // ESTRUCTURAS
    // =========================
    private final Stack<String> historialDevoluciones = new Stack<>();
    private final Queue<String> registroPrestamos = new LinkedList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @PostConstruct
    public void iniciarDatos() {

        registrarLibroInterno("Cien años de soledad", "García Márquez");
        registrarLibroInterno("Don Quijote", "Cervantes");
        registrarLibroInterno("El Principito", "Saint-Exupéry");

        Usuario Jhon = registrarUsuarioInterno("Jhon Caballero", "1111111111");
        Usuario yenny = registrarUsuarioInterno("Yenny Serrano", "1000000000");

        Libro dq = buscarLibroPorTitulo("Don Quijote");

        if (Jhon != null && dq != null) {
            prestarLibro(dq.getTitulo(), Jhon.getId());
        }
    }

    // =========================
    // USUARIOS
    // =========================

    public String agregarUsuario(String nombre, String id) {

        String n = (nombre != null) ? nombre.trim() : "";
        String i = (id != null) ? id.trim() : "";

        if (n.isEmpty() || i.isEmpty()) return "Nombre e id son obligatorios.";
        if (buscarUsuarioPorId(i) != null) return "Ya existe ese ID.";

        Usuario nuevo = new Usuario(n, i);
        usuarios.add(nuevo);

        arbolUsuarios.insertar(nuevo); // 🔥 ÁRBOL USUARIOS

        return null;
    }

    public String eliminarUsuario(String id) {

        Usuario u = buscarUsuarioPorId(id);
        if (u == null) return "Usuario no encontrado.";

        libros.stream()
                .filter(l -> l.getPrestadoA() == u)
                .forEach(l -> {
                    l.devolver();
                    u.liberarPrestamo(l);
                });

        usuarios.remove(u);

        return null;
    }

    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public Usuario buscarUsuarioPorId(String id) {
        return arbolUsuarios.buscar(id);
    }

    // =========================
    // LIBROS
    // =========================

    public String agregarLibro(String titulo, String autor) {

        String t = (titulo != null) ? titulo.trim() : "";
        String a = (autor != null) ? autor.trim() : "";

        if (t.isEmpty() || a.isEmpty()) return "Datos obligatorios.";
        if (buscarLibroPorTitulo(t) != null) return "Ya existe el libro.";

        Libro nuevo = new Libro(t, a);

        libros.add(nuevo);
        arbolLibros.insertar(nuevo); // 🔥 ÁRBOL LIBROS

        return null;
    }

    public List<Libro> listarLibrosFiltrados(String consulta) {

        if (consulta == null || consulta.isBlank()) return new ArrayList<>(libros);

        String q = consulta.toLowerCase(Locale.ROOT).trim();

        return libros.stream()
                .filter(l -> l.getTitulo().toLowerCase(Locale.ROOT).contains(q)
                        || l.getAutor().toLowerCase(Locale.ROOT).contains(q))
                .collect(Collectors.toList());
    }

    // 🔥 AHORA USA ÁRBOL DE LIBROS
    public Libro buscarLibroPorTitulo(String titulo) {
        return arbolLibros.buscar(titulo);
    }

    // =========================
    // PRÉSTAMOS
    // =========================

    public String prestarLibro(String tituloLibro, String usuarioId) {

        Libro libro = buscarLibroPorTitulo(tituloLibro);
        Usuario usuario = buscarUsuarioPorId(usuarioId);

        if (libro == null || usuario == null) return "Error en datos.";
        if (!libro.isDisponible()) return "No disponible.";
        if (!usuario.puedeTomarOtroLibro()) return "Límite alcanzado.";

        libro.prestarA(usuario);
        usuario.registrarPrestamo(libro);

        String fecha = LocalDateTime.now().format(formatter);
        registroPrestamos.add(
                usuario.getNombre() + " | " + fecha + " | " + libro.getTitulo() + " | PRESTAMO"
        );

        return null;
    }

    // =========================
    // DEVOLUCIÓN
    // =========================

    public String procesarDevolucion(String tituloLibro) {

        Libro libro = buscarLibroPorTitulo(tituloLibro);

        if (libro == null || libro.isDisponible()) return "No está prestado.";

        Usuario usuario = libro.getPrestadoA();
        String nombre = (usuario != null) ? usuario.getNombre() : "Anónimo";

        if (usuario != null) usuario.liberarPrestamo(libro);
        libro.devolver();

        String fecha = LocalDateTime.now().format(formatter);
        historialDevoluciones.push(
                nombre + " | " + fecha + " | " + libro.getTitulo() + " | DEVOLUCION"
        );

        return null;
    }

    // =========================
    // HISTORIAL
    // =========================

    public List<String> obtenerHistorialPila() {
        return new ArrayList<>(historialDevoluciones);
    }

    public List<String> obtenerRegistroCola() {
        return new ArrayList<>(registroPrestamos);
    }

    // =========================
    // INTERNOS
    // =========================

    private void registrarLibroInterno(String t, String a) {

        Libro libro = new Libro(t, a);

        libros.add(libro);
        arbolLibros.insertar(libro); // 🔥 IMPORTANTE
    }

    private Usuario registrarUsuarioInterno(String n, String i) {

        Usuario u = new Usuario(n, i);

        usuarios.add(u);
        arbolUsuarios.insertar(u);

        return u;
    }
}