package meuprojeto.repository;

import meuprojeto.model.EquipeMembro;
import meuprojeto.model.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EquipeMemberRepository extends JpaRepository<EquipeMembro, Long> {
    List<EquipeMembro> findByEquipe(Equipe equipe);
}