package com.biblioteca.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.biblioteca.servicio.BibliotecaServicio;

@Controller
public class UsuarioController {

    private final BibliotecaServicio bibliotecaServicio;

    public UsuarioController(BibliotecaServicio bibliotecaServicio) {
        this.bibliotecaServicio = bibliotecaServicio;
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("titulo", "Usuarios — Biblioteca");
        model.addAttribute("usuarios", bibliotecaServicio.listarUsuarios());
        return "usuarios";
    }

    @PostMapping("/usuarios")
    public String agregarUsuario(
            @RequestParam String nombre,
            @RequestParam String id,
            RedirectAttributes redirectAttributes) {
        
        String err = bibliotecaServicio.agregarUsuario(nombre, id);
        
        if (err != null) {
            redirectAttributes.addFlashAttribute("error", err);
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado correctamente.");
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/usuarios/eliminar")
    public String eliminarUsuario(
            @RequestParam String id,
            RedirectAttributes redirectAttributes) {
        
        String err = bibliotecaServicio.eliminarUsuario(id);
        if (err != null) {
            redirectAttributes.addFlashAttribute("error", err);
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado. Los libros que tenía prestados quedaron disponibles.");
        }
        return "redirect:/usuarios";
    }
}