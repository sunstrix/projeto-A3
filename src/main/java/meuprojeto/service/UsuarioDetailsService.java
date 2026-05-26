package meuprojeto.service;

import meuprojeto.model.Usuario;
import meuprojeto.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // Busca usuário pelo login no banco
        Optional<Usuario> usuarioOpt = usuarioRepository.findByLogin(login);
        
        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + login);
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Converte o perfil para uma autoridade do Spring Security
        String role = "ROLE_" + usuario.getPerfil().name();
        
        // Retorna o UserDetails com senha (já deve estar com BCrypt no banco)
        return User.builder()
            .username(usuario.getLogin())
            .password(usuario.getSenha()) // ⚠️ Deve estar com BCrypt no banco!
            .roles(role.replace("ROLE_", "")) // Spring espera sem o prefixo ROLE_
            .disabled(!usuario.getAtivo())
            .authorities(new SimpleGrantedAuthority(role))
            .build();
    }
}