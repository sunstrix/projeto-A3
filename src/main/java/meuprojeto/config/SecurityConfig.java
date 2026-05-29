package meuprojeto.config;

import meuprojeto.service.UsuarioDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private final UsuarioDetailsService usuarioDetailsService;

    public SecurityConfig(UsuarioDetailsService usuarioDetailsService) {
        this.usuarioDetailsService = usuarioDetailsService;
    }

    // ⚠️ ATENÇÃO: NoOpPasswordEncoder permite senhas em texto puro
    // ✅ Use APENAS para desenvolvimento/testes
    // 🔐 Para produção: substitua por new BCryptPasswordEncoder()
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // ✅ Provider que usa nosso UserDetailsService + encoder configurado
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ✅ Ignorar CSRF apenas para o endpoint do Kanban (drag-and-drop)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/projetos/atualizar-status"))
            )
            // Configurar autenticação com nosso provider
            .authenticationProvider(authenticationProvider())
            // Configurar autorização de requisições
            .authorizeHttpRequests(auth -> auth
                // Permitir acesso público
                .requestMatchers(
                    "/login", 
                    "/css/**", 
                    "/js/**", 
                    "/images/**", 
                    "/", 
                    "/error"
                ).permitAll()
                // Todas as outras requisições exigem autenticação
                .anyRequest().authenticated()
            )
            // Configurar login com formulário personalizado
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            // Configurar logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
        
        return http.build();
    }
}