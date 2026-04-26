package com.biblioteca.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.servicio.BibliotecaServicio;

@Controller
public class LibroController {

    private final BibliotecaServicio bibliotecaServicio;

    public LibroController(BibliotecaServicio bibliotecaServicio) {
        this.bibliotecaServicio = bibliotecaServicio;
    }

    @GetMapping("/libros")
    public String libros(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("titulo", "Libros — Biblioteca");
        model.addAttribute("libros", bibliotecaServicio.listarLibrosFiltrados(q));
        model.addAttribute("usuarios", bibliotecaServicio.listarUsuarios()); 
        
        model.addAttribute("q", q != null ? q : "");
        return "libros";
    }

    @PostMapping("/libros")
    public String agregarLibro(
            @RequestParam String titulo,
            @RequestParam String autor,
            RedirectAttributes redirectAttributes) {
        String err = bibliotecaServicio.agregarLibro(titulo, autor);
        if (err != null) {
            redirectAttributes.addFlashAttribute("error", err);
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Libro registrado correctamente.");
        }
        return "redirect:/libros";
    }

    @PostMapping("/libros/prestar")
    public String prestarLibro(
            @RequestParam String tituloLibro,
            @RequestParam String usuarioId,
            RedirectAttributes redirectAttributes) {
        
        Libro libro = bibliotecaServicio.buscarLibroPorTitulo(tituloLibro);
        Usuario usuario = bibliotecaServicio.buscarUsuarioPorId(usuarioId);

        if (libro == null || usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Libro o Usuario no encontrado.");
        } else if (!libro.isDisponible()) {
            redirectAttributes.addFlashAttribute("error", "El libro ya está prestado.");
        } else if (!usuario.puedeTomarOtroLibro()) {
            redirectAttributes.addFlashAttribute("error", "El usuario ya tiene el máximo de libros permitidos.");
        } else {
            libro.prestarA(usuario);
            usuario.registrarPrestamo(libro);
            redirectAttributes.addFlashAttribute("mensaje", "Préstamo realizado a: " + usuario.getNombre());
        }
        return "redirect:/libros";
    }

    @PostMapping("/libros/devolver")
    public String devolverLibro(
        @RequestParam String tituloLibro,
        RedirectAttributes redirectAttributes) {

    String err = bibliotecaServicio.procesarDevolucion(tituloLibro);

    if (err != null) {
        redirectAttributes.addFlashAttribute("error", err);
    } else {
        redirectAttributes.addFlashAttribute("mensaje", "El libro '" + tituloLibro + "' ha sido devuelto y registrado en el historial.");
    }
    
    return "redirect:/libros";
}
}