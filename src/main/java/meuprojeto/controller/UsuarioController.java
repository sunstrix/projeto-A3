package meuprojeto.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import meuprojeto.model.Usuario;
import meuprojeto.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @ModelAttribute("usuario")
    public Usuario getUsuario() {
        return new Usuario();
    }
    
    @GetMapping
    public String listUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/list";
    }
    
    @GetMapping("/novo")
    public String novoUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
        return "usuarios/form";
    }
    
    @PostMapping
    public String saveUsuario(@Valid @ModelAttribute("usuario") Usuario usuario, 
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "usuarios/form";
        }
        
        usuarioService.save(usuario);
        return "redirect:/usuarios";
    }
    
    @GetMapping("/{id}")
    public String showUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.findById(id);
        model.addAttribute("usuario", usuario);
        return "usuarios/form";
    }
    
    @GetMapping("/editar/{id}")
    public String editUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.findById(id);
        model.addAttribute("usuario", usuario);
        return "usuarios/form";
    }
    
    @GetMapping("/deletar/{id}")
    public String deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return "redirect:/usuarios";
    }
}