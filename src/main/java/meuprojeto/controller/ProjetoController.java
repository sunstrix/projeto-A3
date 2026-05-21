package meuprojeto.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import meuprojeto.model.Projeto;
import meuprojeto.model.Usuario;
import meuprojeto.service.ProjetoService;
import meuprojeto.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;
    private final UsuarioService usuarioService;

    public ProjetoController(ProjetoService projetoService, UsuarioService usuarioService) {
        this.projetoService = projetoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        
        if (usuarioLogado.getPerfil().equals(Usuario.Perfil.GERENTE)) {
            model.addAttribute("projetos", projetoService.findByGerente(usuarioLogado));
        } else if (usuarioLogado.getPerfil().equals(Usuario.Perfil.ADMINISTRADOR)) {
            model.addAttribute("projetos", projetoService.findAll());
        } else {
            model.addAttribute("projetos", projetoService.findAll());
        }
        
        return "projeto/list";
    }

    @GetMapping("/novo")
    public String novo(Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        
        if (!usuarioLogado.getPerfil().equals(Usuario.Perfil.GERENTE) && 
            !usuarioLogado.getPerfil().equals(Usuario.Perfil.ADMINISTRADOR)) {
            throw new RuntimeException("Apenas gerentes podem criar projetos");
        }

        model.addAttribute("projeto", new Projeto());
        model.addAttribute("gerentes", usuarioService.findAll().stream()
            .filter(u -> u.getPerfil().equals(Usuario.Perfil.GERENTE))
            .toList());
        model.addAttribute("status", Projeto.Status.values());
        return "projeto/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Projeto projeto, 
                        BindingResult result, 
                        Model model, 
                        HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        
        if (!usuarioLogado.getPerfil().equals(Usuario.Perfil.GERENTE) && 
            !usuarioLogado.getPerfil().equals(Usuario.Perfil.ADMINISTRADOR)) {
            throw new RuntimeException("Apenas gerentes podem criar projetos");
        }

        if (result.hasErrors()) {
            model.addAttribute("gerentes", usuarioService.findAll().stream()
                .filter(u -> u.getPerfil().equals(Usuario.Perfil.GERENTE))
                .toList());
            model.addAttribute("status", Projeto.Status.values());
            return "projeto/form";
        }

        try {
            projetoService.save(projeto);
            return "redirect:/projetos";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("gerentes", usuarioService.findAll().stream()
                .filter(u -> u.getPerfil().equals(Usuario.Perfil.GERENTE))
                .toList());
            model.addAttribute("status", Projeto.Status.values());
            return "projeto/form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Projeto projeto = projetoService.findById(id);

        // Apenas o gerente responsável ou admin pode editar
        if (!projeto.getGerente().getId().equals(usuarioLogado.getId()) && 
            !usuarioLogado.getPerfil().equals(Usuario.Perfil.ADMINISTRADOR)) {
            throw new RuntimeException("Apenas o gerente responsável pode editar este projeto");
        }

        model.addAttribute("projeto", projeto);
        model.addAttribute("gerentes", usuarioService.findAll().stream()
            .filter(u -> u.getPerfil().equals(Usuario.Perfil.GERENTE))
            .toList());
        model.addAttribute("status", Projeto.Status.values());
        return "projeto/form";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, 
                           @Valid @ModelAttribute Projeto projeto, 
                           BindingResult result, 
                           Model model, 
                           HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Projeto projetoExistente = projetoService.findById(id);

        if (!projetoExistente.getGerente().getId().equals(usuarioLogado.getId()) && 
            !usuarioLogado.getPerfil().equals(Usuario.Perfil.ADMINISTRADOR)) {
            throw new RuntimeException("Apenas o gerente responsável pode editar este projeto");
        }

        if (result.hasErrors()) {
            model.addAttribute("gerentes", usuarioService.findAll().stream()
                .filter(u -> u.getPerfil().equals(Usuario.Perfil.GERENTE))
                .toList());
            model.addAttribute("status", Projeto.Status.values());
            return "projeto/form";
        }

        try {
            projetoService.update(id, projeto);
            return "redirect:/projetos";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("gerentes", usuarioService.findAll().stream()
                .filter(u -> u.getPerfil().equals(Usuario.Perfil.GERENTE))
                .toList());
            model.addAttribute("status", Projeto.Status.values());
            return "projeto/form";
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Projeto projeto = projetoService.findById(id);

        if (!projeto.getGerente().getId().equals(usuarioLogado.getId()) && 
            !usuarioLogado.getPerfil().equals(Usuario.Perfil.ADMINISTRADOR)) {
            throw new RuntimeException("Apenas o gerente responsável pode deletar este projeto");
        }

        projetoService.deleteById(id);
        return "redirect:/projetos";
    }
}