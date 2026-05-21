package meuprojeto.controller;

import jakarta.servlet.http.HttpSession;
import meuprojeto.model.Usuario;
import meuprojeto.service.RelatorioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        model.addAttribute("relatorio", relatorioService.gerarRelatorioDashboard());
        return "relatorio/dashboard";
    }

    @GetMapping("/desempenho/{id}")
    public String desempenho(@PathVariable Long id, Model model, HttpSession session) {
        model.addAttribute("relatorio", relatorioService.gerarRelatorioDesempenho(id));
        return "relatorio/desempenho";
    }
}