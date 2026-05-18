package meuprojeto.controller;

import meuprojeto.model.Usuario;
import meuprojeto.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String list(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/list"; // template Thymeleaf
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/form";
    }

    @PostMapping("/salvar")
    public String salvar(Usuario usuario) {
        usuarioService.save(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario u = usuarioService.findById(id);
        model.addAttribute("usuario", u);
        return "usuarios/form";
    }
}
