# Projeto A3

Sistema de gestão de projetos multi-perfis, com autenticação, hierarquia, dashboard e integração a banco de dados relacional, desenvolvido em **Java 21** e **Spring Boot**.

---

## 🆕 **Resumo das Alterações Recentes**

**1. Integração com Banco de Dados Persistente (PostgreSQL)**
- **Alteração:** Agora o projeto suporta banco de dados real, com configuração para PostgreSQL via perfil `prod`.
- **Configuração:** Novo `application-prod.properties` e uso de variáveis de ambiente para acesso ao banco.
- **pom.xml:** Adicionada dependência do driver do PostgreSQL.

**2. Correção de Templates Thymeleaf**
- **Problema:** Erros de sintaxe em condicionais e formatação de datas causavam erro 500.
- **Correção:** Refatoradas expressões `th:classappend`, padronização dos ternários e datas com `#temporals` (compatível Java 21).
- **Arquivos:** `usuario/list.html`, `projeto/list.html`, `relatorio/dashboard.html`.

**3. Configurações de Inicialização**
- **Scripts SQL:** Desligado modo auto (delegado ao Hibernate o controle de tabelas), evitando erro de schema e redundância.
- **Porta:** Mantida em 8080.
- **Perfis:** Separação clara para `dev` (H2 em memória) e `prod` (PostgreSQL).

---

## 💡 **O que é a Central de Projetos Multi-Perfil?**

Sistema web para empresas centralizarem projetos, equipes e acesso baseado em perfis (Administrador, Gerente, Colaborador).

---

## 🚥 **Características aplicadas**

- **Java 21**
- **Spring Boot** (Web, Thymeleaf, Data JPA)
- **Banco:** H2 (dev) **/ PostgreSQL (prod)**
- **Spring Profiles:** dev e prod, fáceis de alternar
- **Flyway** para migrações (optativo)
- **Estrutura professional**: controllers, model, repository, service, config, interceptor
- **Sem Lombok** (códigos manuais, fácil manutenção)
- **GitHub Actions:** build/test no CI
- **Autenticação e autorização** por perfil
- **Dashboard com relatórios**
- **Testes unitários (JUnit)**

---

## 📦 **Dependências-chave no pom.xml**

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

## 📂 **Estrutura do Projeto**

```
projeto-A3/
├── pom.xml
├── src/
│   └── main/
│       ├── java/meuprojeto/
│       │   ├── MeuProjetoApplication.java
│       │   ├── config/ (segurança, inicialização)
│       │   ├── controller/ (Login, Usuario, Projeto, Equipe, Relatorio)
│       │   ├── model/ (Usuario, Projeto, Equipe, ...)
│       │   ├── repository/
│       │   ├── service/
│       │   └── exception/
│       └── resources/
│           ├── application.properties          (desenvolvimento/H2)
│           ├── application-prod.properties     (produção/PostgreSQL)
│           └── templates/
│               ├── usuario/list.html
│               ├── projeto/list.html
│               ├── equipe/list.html
│               └── relatorio/dashboard.html
```

---

## 🚀 **Como Executar**  

**Desenvolvimento (H2):**
```bash
mvn clean install -DskipTests
mvn spring-boot:run
# ou
java -jar target/projeto-A3-0.0.1-SNAPSHOT.jar
```

**Produção (PostgreSQL):**
```bash
# Configure variáveis de ambiente:
# No PowerShell (Windows)
$env:SPRING_PROFILES_ACTIVE="prod"
$env:DB_URL="jdbc:postgresql://localhost:5432/nome_do_banco"
$env:DB_USERNAME="seu_usuario"
$env:DB_PASSWORD="sua_senha"

# Execute normalmente:
mvn spring-boot:run
# ou
java -jar target/projeto-A3-0.0.1-SNAPSHOT.jar
```

---

## 👤 **Perfis de Acesso e Credenciais de Teste**

| Login        | Senha    | Perfil         | Permissões                                      |
|--------------|----------|---------------|-------------------------------------------------|
| admin        | admin    | Administrador | Gerenciar usuários, ver todos os projetos       |
| gerente      | gerente  | Gerente       | Criar/editar projetos, montar equipes           |
| colaborador  | 123456   | Colaborador   | Visualizar projetos e relatórios                |

> Você pode cadastrar mais usuários com perfis e CPFs válidos pelo próprio sistema (tela de administração).

---

## 🗂 **Funcionalidades principais**

- Gestão de Usuários _(CRUD, ativar/desativar, perfis e validação de CPF)_
- Gestão de Projetos _(vínculo com gerente, datas, status, equipes)_
- Gestão de Equipes _(criar/remover membros, líderes)_
- Dashboard e Relatórios
- Segurança: login/senha, sessão, controle por perfil

---

## 📝 **Exemplos de Telas (URLs Importantes)**

- `/` — Home
- `/login` — Login
- `/usuarios` — Listagem/Gestão de usuários (admin)
- `/projetos` — Gestão de projetos
- `/equipes` — Gestão de equipes
- `/relatorios/dashboard` — Dashboard de indicadores

---

## ⚙️ **Comandos úteis para o desenvolvedor**

- Buildando sem rodar testes:
  ```bash
  mvn clean package -DskipTests
  ```
- Rodar aplicando perfil desejado:
  ```bash
  mvn spring-boot:run -Dspring-boot.run.profiles=dev
  mvn spring-boot:run -Dspring-boot.run.profiles=prod
  ```
- Ajuste de portas:
  ```
  spring:
    server.port=8080
  ```

---

## ☝️ **Notas técnicas**

- **Templates Thymeleaf:** sintaxe compatível com Spring Boot 3+/Java 21
- **Banco de dados:** as tabelas são criadas automaticamente pelo Hibernate
- **Migração para PostgreSQL:** alterne facilmente os perfis e faça deploy real

---

## 🤝 **Licença & Contribuição**

Abra issues, contribua e adapte à sua realidade.

Projeto de estudos e demonstração – Maio/2026

---

**Qualquer dúvida ou melhoria que queira implementar, abra uma _issue_ ou me pergunte!**
