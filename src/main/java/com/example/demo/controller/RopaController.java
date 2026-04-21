package com.example.demo.controller;

import com.example.demo.model.Prenda;
import com.example.demo.repository.PrendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class RopaController {

    @Autowired
    private PrendaRepository repository;

    private void cargarEstadisticas(Model model, List<Prenda> lista) {
        int totalUnidades = lista.stream().mapToInt(p -> p.getCantidad() != null ? p.getCantidad() : 0).sum();
        double valorTotal = lista.stream().mapToDouble(p -> (p.getPrecio() != null ? p.getPrecio() : 0.0) * (p.getCantidad() != null ? p.getCantidad() : 0)).sum();
        model.addAttribute("totalUnidades", totalUnidades);
        model.addAttribute("valorTotal", valorTotal);
        model.addAttribute("totalModelos", lista.size());
    }

    @GetMapping("/")
    public String home(Model model) {
        cargarEstadisticas(model, repository.findAll());
        return "index";
    }

    @GetMapping("/ropa")
    public String listar(Model model) {
        List<Prenda> lista = repository.findAll();
        model.addAttribute("prendas", lista);
        model.addAttribute("prenda", new Prenda());
        cargarEstadisticas(model, lista);
        return "inventario";
    }

    @PostMapping("/ropa/guardar")
    public String guardar(@ModelAttribute Prenda prenda) {
        repository.save(prenda);
        return "redirect:/ropa";
    }

    @GetMapping("/ropa/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/ropa";
    }
}