# Projeto A3

Sistema de gestão de projetos multi-perfis com Java 21, Spring Boot, templates Thymeleaf e persistência real de dados.

---

## 🆕 Principais Atualizações

### 1. **Integração com Banco de Dados PostgreSQL**
- **Novo:** Sistema agora usa PostgreSQL para dados persistentes (perfil `prod`).
- **pom.xml:** Adicionada dependência do driver PostgreSQL.
- **Configuração:** Novo arquivo `application-prod.properties` com instruções para uso de variáveis de ambiente.

### 2. **Correções em Templates Thymeleaf**
- **Erros anteriores:** Whitelabel Error Page (500) causados por sintaxe incompatível.
- **Correção:** Todas as expressões ternárias e condicionais Th:classappend/Th:if ajustadas conforme Thymeleaf 3.x.
- **Formatação:** Uso do `#temporals` para datas (compatível com Java 21).

### 3. **Startup e Configuração**
- **Spring SQL init:** Desligado o modo de inicialização de scripts SQL (deixa Hibernate criar as tabelas).
- **Porta e Perfis:** Rodando na porta 8080, fácil alternar entre dev (H2) e prod (PostgreSQL).

---

## 📦 Dependências no pom.xml

```xml
<!-- ...outras dependências... -->
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
<!-- ... -->
```

---

## 📂 Estrutura do Projeto

Arquivos principais e alterados:

```
projeto-A3/
├── pom.xml                                <-- atualizado: PostgreSQL
├── src/
│   └── main/
│       ├── java/meuprojeto/               <-- códigos das entidades, services, repositories, controllers
│       │   └── ...
│       └── resources/
│           ├── application.properties
│           ├── application-prod.properties <-- novo: PostgreSQL
│           └── templates/
│               ├── usuario/list.html       <-- corrigido th:classappend
│               ├── projeto/list.html       <-- corrigido expressão/data
│               ├── equipe/list.html        <-- verificado
│               └── relatorio/dashboard.html<-- corrigido expressão/data
```

---

## 🚀 Como Executar

### Desenvolvimento (H2 em memória)
```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

### Produção (PostgreSQL)
```bash
# Configure variáveis de ambiente com dados do banco:
# (No Windows PowerShell)
$env:SPRING_PROFILES_ACTIVE="prod"
$env:DB_URL="jdbc:postgresql://localhost:5432/nome_do_banco"
$env:DB_USERNAME="seu_usuario"
$env:DB_PASSWORD="sua_senha"

# Rode o projeto
mvn spring-boot:run
```
Ou:
```bash
java -jar target/projeto-A3-0.0.1-SNAPSHOT.jar
```
(Lembrando de configurar o perfil e os dados de conexão!)

---

## 👤 Credenciais de Teste (padrão)

| Login        | Senha    | Perfil         |
|--------------|----------|---------------|
| admin        | admin    | Administrador |
| gerente      | gerente  | Gerente       |
| colaborador  | 123456   | Colaborador   |

---

## 💻 Links Importantes

- `/usuarios`        — Gerenciar usuários (admin)
- `/projetos`        — Gerenciar projetos (gerente/admin)
- `/equipes`         — Equipes
- `/relatorios/dashboard` — Dashboard de relatórios

---

## ❗️Observações

- Templates Thymeleaf possuem sintaxe ajustada para máxima compatibilidade com Spring Boot 3+ e Java 21.
- Para produzir facilmente para PostgreSQL, use o perfil `prod` e ajuste seus ambientes conforme exemplo acima.
- Se for rodar em dev, basta usar o default com H2 (nada a instalar).

---

## 🤝 Contribuição

Colabore, envie issues com dúvidas e sugestões!

---

## 📅 Última atualização

Mai/2026
