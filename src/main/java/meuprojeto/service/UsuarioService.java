package meuprojeto.service;

import meuprojeto.exception.AppException;
import meuprojeto.model.Usuario;
import meuprojeto.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new AppException("Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByLogin(String login) {
        return usuarioRepository.findByLogin(login);
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        // Valida CPF
        if (!CpfValidator.isValid(usuario.getCpf())) {
            throw new AppException("CPF inválido");
        }

        // Verifica CPF duplicado
        if (usuarioRepository.findByCpf(usuario.getCpf()).isPresent()) {
            throw new AppException("CPF já cadastrado");
        }

        // Verifica email duplicado
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new AppException("Email já cadastrado");
        }

        // Verifica login duplicado
        if (usuarioRepository.findByLogin(usuario.getLogin()).isPresent()) {
            throw new AppException("Login já cadastrado");
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario update(Long id, Usuario usuario) {
        Usuario existing = findById(id);
        
        existing.setNome(usuario.getNome());
        existing.setEmail(usuario.getEmail());
        existing.setCargo(usuario.getCargo());
        existing.setPerfil(usuario.getPerfil());
        existing.setSenha(usuario.getSenha());
        
        return usuarioRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        Usuario usuario = findById(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }
}