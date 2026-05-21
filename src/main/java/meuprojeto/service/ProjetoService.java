package meuprojeto.service;

import meuprojeto.exception.AppException;
import meuprojeto.model.Projeto;
import meuprojeto.model.Usuario;
import meuprojeto.repository.ProjetoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final UsuarioService usuarioService;

    public ProjetoService(ProjetoRepository projetoRepository, UsuarioService usuarioService) {
        this.projetoRepository = projetoRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional(readOnly = true)
    public List<Projeto> findAll() {
        return projetoRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public Projeto findById(Long id) {
        return projetoRepository.findById(id)
            .orElseThrow(() -> new AppException("Projeto não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Projeto> findByGerente(Usuario gerente) {
        return projetoRepository.findByGerente(gerente);
    }

    @Transactional
    public Projeto save(Projeto projeto) {
        // Valida datas
        if (projeto.getDataInicio().isAfter(projeto.getDataTerminoPrevista())) {
            throw new AppException("Data de início não pode ser após data de término");
        }

        if (projeto.getDataInicio().isBefore(LocalDate.now())) {
            throw new AppException("Data de início não pode ser no passado");
        }

        // Valida gerente
        Usuario gerente = usuarioService.findById(projeto.getGerente().getId());
        if (!gerente.getPerfil().equals(Usuario.Perfil.GERENTE)) {
            throw new AppException("Gerente deve ter perfil de Gerente");
        }

        return projetoRepository.save(projeto);
    }

    @Transactional
    public Projeto update(Long id, Projeto projeto) {
        Projeto existing = findById(id);
        
        existing.setNome(projeto.getNome());
        existing.setDescricao(projeto.getDescricao());
        existing.setDataInicio(projeto.getDataInicio());
        existing.setDataTerminoPrevista(projeto.getDataTerminoPrevista());
        existing.setStatus(projeto.getStatus());
        
        if (projeto.getStatus().equals(Projeto.Status.CONCLUIDO)) {
            existing.setDataTerminoReal(LocalDate.now());
        }

        return projetoRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        Projeto projeto = findById(id);
        projeto.setAtivo(false);
        projetoRepository.save(projeto);
    }
}