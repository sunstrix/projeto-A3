package meuprojeto.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns("/", "/login", "/login/autenticar", "/css/**", "/js/**", "/images/**");
    }

    public static class LoginInterceptor implements HandlerInterceptor {
        
        @Override
        public boolean preHandle(jakarta.servlet.http.HttpServletRequest request, 
                                 jakarta.servlet.http.HttpServletResponse response, 
                                 Object handler) throws Exception {
            HttpSession session = request.getSession(false);
            
            if (session == null || session.getAttribute("usuarioLogado") == null) {
                response.sendRedirect("/login");
                return false;
            }
            
            return true;
        }
    }
}