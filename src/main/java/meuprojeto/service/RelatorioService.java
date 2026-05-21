package meuprojeto.service;

import meuprojeto.model.Projeto;
import meuprojeto.repository.ProjetoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    private final ProjetoRepository projetoRepository;

    public RelatorioService(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> gerarRelatorioDashboard() {
        List<Projeto> projetos = projetoRepository.findByAtivoTrue();
        
        Map<String, Object> relatorio = new HashMap<>();
        
        long totalProjetos = projetos.size();
        long projetosAtivos = projetos.stream()
            .filter(p -> p.getStatus().equals(Projeto.Status.EM_ANDAMENTO))
            .count();
        long projetosConcluidos = projetos.stream()
            .filter(p -> p.getStatus().equals(Projeto.Status.CONCLUIDO))
            .count();
        long projetosCancelados = projetos.stream()
            .filter(p -> p.getStatus().equals(Projeto.Status.CANCELADO))
            .count();

        relatorio.put("totalProjetos", totalProjetos);
        relatorio.put("projetosAtivos", projetosAtivos);
        relatorio.put("projetosConcluidos", projetosConcluidos);
        relatorio.put("projetosCancelados", projetosCancelados);
        relatorio.put("projetos", projetos);

        return relatorio;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> gerarRelatorioDesempenho(Long projetoId) {
        Projeto projeto = projetoRepository.findById(projetoId)
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        Map<String, Object> relatorio = new HashMap<>();
        
        long diasTotais = ChronoUnit.DAYS.between(projeto.getDataInicio(), projeto.getDataTerminoPrevista());
        long diasDecorridos = ChronoUnit.DAYS.between(projeto.getDataInicio(), LocalDate.now());
        double percentualProgresso = (double) diasDecorridos / diasTotais * 100;

        relatorio.put("projeto", projeto);
        relatorio.put("diasTotais", diasTotais);
        relatorio.put("diasDecorridos", diasDecorridos);
        relatorio.put("percentualProgresso", Math.min(percentualProgresso, 100));
        relatorio.put("statusAtual", projeto.getStatus().getDescricao());

        return relatorio;
    }
}