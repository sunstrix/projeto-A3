package meuprojeto.service;

import meuprojeto.exception.AppException;
import meuprojeto.model.Equipe;
import meuprojeto.model.EquipeMembro;
import meuprojeto.model.Usuario;
import meuprojeto.repository.EquipeRepository;
import meuprojeto.repository.EquipeMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipeService {

    private final EquipeRepository equipeRepository;
    private final EquipeMemberRepository equipeMemberRepository;
    private final UsuarioService usuarioService;

    public EquipeService(EquipeRepository equipeRepository, EquipeMemberRepository equipeMemberRepository, UsuarioService usuarioService) {
        this.equipeRepository = equipeRepository;
        this.equipeMemberRepository = equipeMemberRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional(readOnly = true)
    public List<Equipe> findAll() {
        return equipeRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public Equipe findById(Long id) {
        return equipeRepository.findById(id)
            .orElseThrow(() -> new AppException("Equipe não encontrada"));
    }

    @Transactional
    public Equipe save(Equipe equipe) {
        Usuario lider = usuarioService.findById(equipe.getLider().getId());
        
        if (!lider.getPerfil().equals(Usuario.Perfil.GERENTE)) {
            throw new AppException("Líder da equipe deve ter perfil de Gerente");
        }

        return equipeRepository.save(equipe);
    }

    @Transactional
    public Equipe update(Long id, Equipe equipe) {
        Equipe existing = findById(id);
        
        existing.setNome(equipe.getNome());
        existing.setDescricao(equipe.getDescricao());
        
        return equipeRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        Equipe equipe = findById(id);
        equipe.setAtivo(false);
        equipeRepository.save(equipe);
    }

    @Transactional
    public void addMembro(Long equipeId, Long usuarioId, String funcao) {
        Equipe equipe = findById(equipeId);
        Usuario usuario = usuarioService.findById(usuarioId);

        EquipeMembro membro = new EquipeMembro(equipe, usuario, funcao);
        equipeMemberRepository.save(membro);
    }

    @Transactional
    public void removeMembro(Long membroId) {
        EquipeMembro membro = equipeMemberRepository.findById(membroId)
            .orElseThrow(() -> new AppException("Membro não encontrado"));
        membro.setAtivo(false);
        equipeMemberRepository.save(membro);
    }
}