package meuprojeto.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import meuprojeto.model.Equipe;
import meuprojeto.model.Usuario;
import meuprojeto.service.EquipeService;
import meuprojeto.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/equipes")
public class EquipeController {

    private final EquipeService equipeService;
    private final UsuarioService usuarioService;

    public EquipeController(EquipeService equipeService, UsuarioService usuarioService) {
        this.equipeService = equipeService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        model.addAttribute("equipes", equipeService.findAll());
        return "equipe/list";
    }

    @GetMapping("/novo")
    public String novo(Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        
        if (!usuarioLogado.getPerfil().equals(Usuario.Perfil.GERENTE) && 
            !usuarioLogado.getPerfil().equals(Usuario.Perfil.ADMINISTRADOR)) {
            throw new RuntimeException("Apenas gerentes podem criar equipes");
        }

        model.addAttribute("equipe", new Equipe());
        model.addAttribute("lideres", usuarioService.findAll().stream()
            .filter(u -> u.getPerfil().equals(Usuario.Perfil.GERENTE))
            .toList());
        return "equipe/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Equipe equipe, 
                        BindingResult result, 
                        Model model, 
                        HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("lideres", usuarioService.findAll().stream()
                .filter(u -> u.getPerfil().equals(Usuario.Perfil.GERENTE))
                .toList());
            return "equipe/form";
        }

        try {
            equipeService.save(equipe);
            return "redirect:/equipes";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("lideres", usuarioService.findAll().stream()
                .filter(u -> u.getPerfil().equals(Usuario.Perfil.GERENTE))
                .toList());
            return "equipe/form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        model.addAttribute("equipe", equipeService.findById(id));
        model.addAttribute("lideres", usuarioService.findAll().stream()
            .filter(u -> u.getPerfil().equals(Usuario.Perfil.GERENTE))
            .toList());
        return "equipe/form";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, 
                           @Valid @ModelAttribute Equipe equipe, 
                           BindingResult result, 
                           Model model, 
                           HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("lideres", usuarioService.findAll().stream()
                .filter(u -> u.getPerfil().equals(Usuario.Perfil.GERENTE))
                .toList());
            return "equipe/form";
        }

        try {
            equipeService.update(id, equipe);
            return "redirect:/equipes";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("lideres", usuarioService.findAll().stream()
                .filter(u -> u.getPerfil().equals(Usuario.Perfil.GERENTE))
                .toList());
            return "equipe/form";
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, HttpSession session) {
        equipeService.deleteById(id);
        return "redirect:/equipes";
    }
}