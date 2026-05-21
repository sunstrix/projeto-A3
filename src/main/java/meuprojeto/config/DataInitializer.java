package meuprojeto.config;

import meuprojeto.model.Usuario;
import meuprojeto.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository) {
        return args -> {
            // Verificar se já existem usuários
            if (usuarioRepository.findAll().isEmpty()) {
                
                // Criar usuário administrador
                Usuario admin = new Usuario(
                    "Administrador",
                    "11144477735",
                    "admin@empresa.com",
                    "TI",
                    "admin",
                    "admin",
                    Usuario.Perfil.ADMINISTRADOR
                );
                usuarioRepository.save(admin);
                
                // Criar usuário gerente
                Usuario gerente = new Usuario(
                    "Gerente Projetos",
                    "87654321596",
                    "gerente@empresa.com",
                    "Gerente",
                    "gerente",
                    "gerente",
                    Usuario.Perfil.GERENTE
                );
                usuarioRepository.save(gerente);
                
                // Criar usuário colaborador
                Usuario colaborador = new Usuario(
                    "Colaborador",
                    "39053344705",
                    "colaborador@empresa.com",
                    "Analista",
                    "colaborador",
                    "123456",
                    Usuario.Perfil.COLABORADOR
                );
                usuarioRepository.save(colaborador);
                
                System.out.println("✅ Usuários de teste criados com sucesso!");
            }
        };
    }
}