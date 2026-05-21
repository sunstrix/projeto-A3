package meuprojeto.repository;

import meuprojeto.model.Projeto;
import meuprojeto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
    List<Projeto> findByGerente(Usuario gerente);
    List<Projeto> findByAtivoTrue();
}