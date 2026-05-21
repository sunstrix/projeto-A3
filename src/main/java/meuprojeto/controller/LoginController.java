package meuprojeto.controller;

import jakarta.servlet.http.HttpSession;
import meuprojeto.model.Usuario;
import meuprojeto.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login/autenticar")
    public String autenticar(@RequestParam String login, 
                            @RequestParam String senha, 
                            HttpSession session, 
                            Model model) {
        try {
            var usuario = usuarioService.findByLogin(login);
            
            if (usuario.isPresent() && usuario.get().getSenha().equals(senha) && usuario.get().getAtivo()) {
                session.setAttribute("usuarioLogado", usuario.get());
                
                // Redireciona conforme o perfil
                if (usuario.get().getPerfil().equals(Usuario.Perfil.ADMINISTRADOR)) {
                    return "redirect:/usuarios";
                } else if (usuario.get().getPerfil().equals(Usuario.Perfil.GERENTE)) {
                    return "redirect:/projetos";
                } else {
                    return "redirect:/projetos";
                }
            } else {
                model.addAttribute("erro", "Login ou senha inválidos");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao autenticar");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}