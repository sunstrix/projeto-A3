--- src/main/java/meuprojeto/controller/AdminController.java (??)
package meuprojeto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "dashboard-admin";
    }

    @GetMapping("/admin/usuarios")
    public String usuarios() {
        return "gerenciar-usuarios";
    }

    @GetMapping("/admin/projetos")
    public String projetos() {
        return "gerenciar-projetos";
    }

    @GetMapping("/admin/equipes")
    public String equipes() {
        return "gerenciar-equipes";
    }
}

+++ src/main/java/meuprojeto/controller/AdminController.java (???)
package meuprojeto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/")
    public String home() {
        return "redirect:/usuarios";
    }

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "dashboard-admin";
    }

    @GetMapping("/admin/usuarios")
    public String usuarios() {
        return "gerenciar-usuarios";
    }

    @GetMapping("/admin/projetos")
    public String projetos() {
        return "gerenciar-projetos";
    }

    @GetMapping("/admin/equipes")
    public String equipes() {
        return "gerenciar-equipes";
    }
}