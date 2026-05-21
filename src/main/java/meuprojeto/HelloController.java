package com.sunstrix.projetoA3; // Use o mesmo pacote que já está no seu projeto

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String dizerOla() {
        return "<h1>Olá, Spring Boot! Meu projeto A3 está no ar! 🚀</h1>";
    }
}