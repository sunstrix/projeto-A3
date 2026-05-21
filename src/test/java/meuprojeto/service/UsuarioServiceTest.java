package meuprojeto.service;

import meuprojeto.model.Usuario;
import meuprojeto.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        usuarioService = new UsuarioService(usuarioRepository);
    }

    @Test
    void findAll_returnsList() {
        Usuario usuario = new Usuario(
            "João Silva",
            "12345678901",
            "joao@example.com",
            "Desenvolvedor",
            "joao",
            "senha123",
            Usuario.Perfil.COLABORADOR
        );
        
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        List<Usuario> usuarios = usuarioService.findAll();
        
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("João Silva", usuarios.get(0).getNome());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void findByLogin_returnsUsuario() {
        Usuario usuario = new Usuario(
            "Maria Silva",
            "98765432100",
            "maria@example.com",
            "Gerente",
            "maria",
            "senha456",
            Usuario.Perfil.GERENTE
        );
        
        when(usuarioRepository.findByLogin("maria")).thenReturn(Optional.of(usuario));
        Optional<Usuario> result = usuarioService.findByLogin("maria");
        
        assertTrue(result.isPresent());
        assertEquals("Maria Silva", result.get().getNome());
        verify(usuarioRepository, times(1)).findByLogin("maria");
    }

    @Test
    void save_withValidUsuario_shouldSave() {
        Usuario usuario = new Usuario(
            "Pedro Santos",
            "11122233344",
            "pedro@example.com",
            "Analista",
            "pedro",
            "senha789",
            Usuario.Perfil.COLABORADOR
        );
        
        when(usuarioRepository.findByCpf("11122233344")).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail("pedro@example.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findByLogin("pedro")).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        
        Usuario resultado = usuarioService.save(usuario);
        
        assertNotNull(resultado);
        assertEquals("Pedro Santos", resultado.getNome());
        verify(usuarioRepository, times(1)).save(usuario);
    }
}