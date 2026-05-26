package meuprojeto.controller;

import jakarta.servlet.http.HttpSession;
import meuprojeto.model.Projeto;
import meuprojeto.model.Usuario;
import meuprojeto.service.ProjetoService;
import meuprojeto.service.EquipeService;
import meuprojeto.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MenuController {

    private final ProjetoService projetoService;
    private final EquipeService equipeService;
    private final UsuarioService usuarioService;

    public MenuController(ProjetoService projetoService, 
                         EquipeService equipeService,
                         UsuarioService usuarioService) {
        this.projetoService = projetoService;
        this.equipeService = equipeService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String menu(Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        // Adicionar estatísticas apenas para ADMIN/GERENTE
        if (usuarioLogado.getPerfil() == Usuario.Perfil.ADMINISTRADOR || 
            usuarioLogado.getPerfil() == Usuario.Perfil.GERENTE) {
            
            // Busca todas as listas e conta via Stream (sem precisar de métodos extras nos Services)
            List<Projeto> todosProjetos = projetoService.findAll();
            List<Usuario> todosUsuarios = usuarioService.findAll();
            List<meuprojeto.model.Equipe> todasEquipes = equipeService.findAll();
            
            Map<String, Long> stats = new HashMap<>();
            stats.put("totalProjetos", (long) todosProjetos.size());
            stats.put("emAndamento", todosProjetos.stream()
                .filter(p -> p.getStatus() == Projeto.Status.EM_ANDAMENTO)
                .count());
            stats.put("equipesAtivas", (long) todasEquipes.size());
            stats.put("colaboradores", todosUsuarios.stream()
                .filter(u -> u.getPerfil() == Usuario.Perfil.COLABORADOR)
                .count());
            
            model.addAttribute("stats", stats);
        }

        return "menu";
    }
}