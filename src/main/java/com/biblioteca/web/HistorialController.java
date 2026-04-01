package com.biblioteca.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.biblioteca.servicio.BibliotecaServicio;

@Controller
public class HistorialController {

    private final BibliotecaServicio bibliotecaServicio;

    public HistorialController(BibliotecaServicio bibliotecaServicio) {
        this.bibliotecaServicio = bibliotecaServicio;
    }

    @GetMapping("/historial")
    public String verHistorial(Model model) {
        model.addAttribute("titulo", "Historial de Movimientos — Biblioteca");
        model.addAttribute("pilaDevoluciones", bibliotecaServicio.obtenerHistorialPila());
        model.addAttribute("colaPrestamos", bibliotecaServicio.obtenerRegistroCola());
        
        return "historial"; 
    }
    
}