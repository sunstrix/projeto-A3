package meuprojeto.repository;

import meuprojeto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String login);
    Optional<Usuario> findByCpf(String cpf);
    Optional<Usuario> findByEmail(String email);
}