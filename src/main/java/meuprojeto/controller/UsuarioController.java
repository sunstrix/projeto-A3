package meuprojeto.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import meuprojeto.model.Usuario;
import meuprojeto.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        verificarPermissao(session, Usuario.Perfil.ADMINISTRADOR);
        model.addAttribute("usuarios", usuarioService.findAll());
        return "usuario/list";
    }

    @GetMapping("/novo")
    public String novo(Model model, HttpSession session) {
        verificarPermissao(session, Usuario.Perfil.ADMINISTRADOR);
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("perfis", Usuario.Perfil.values());
        return "usuario/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Usuario usuario, 
                        BindingResult result, 
                        Model model, 
                        HttpSession session) {
        verificarPermissao(session, Usuario.Perfil.ADMINISTRADOR);
        
        if (result.hasErrors()) {
            model.addAttribute("perfis", Usuario.Perfil.values());
            return "usuario/form";
        }

        try {
            usuarioService.save(usuario);
            return "redirect:/usuarios";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("perfis", Usuario.Perfil.values());
            return "usuario/form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        verificarPermissao(session, Usuario.Perfil.ADMINISTRADOR);
        model.addAttribute("usuario", usuarioService.findById(id));
        model.addAttribute("perfis", Usuario.Perfil.values());
        return "usuario/form";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, 
                           @Valid @ModelAttribute Usuario usuario, 
                           BindingResult result, 
                           Model model, 
                           HttpSession session) {
        verificarPermissao(session, Usuario.Perfil.ADMINISTRADOR);
        
        if (result.hasErrors()) {
            model.addAttribute("perfis", Usuario.Perfil.values());
            return "usuario/form";
        }

        try {
            usuarioService.update(id, usuario);
            return "redirect:/usuarios";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("perfis", Usuario.Perfil.values());
            return "usuario/form";
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, HttpSession session) {
        verificarPermissao(session, Usuario.Perfil.ADMINISTRADOR);
        usuarioService.deleteById(id);
        return "redirect:/usuarios";
    }

    private void verificarPermissao(HttpSession session, Usuario.Perfil perfilRequerido) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        
        if (usuarioLogado == null || !usuarioLogado.getPerfil().equals(perfilRequerido)) {
            throw new RuntimeException("Acesso negado");
        }
    }
}