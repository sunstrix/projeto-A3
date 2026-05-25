---

## 🚀 Como Executar

### Pré-requisitos

- [Java 21 JDK](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/downloads)

### Desenvolvimento (H2 em Memória)

```bash
git clone https://github.com/sunstrix/projeto-A3.git
cd projeto-A3
mvn clean install
mvn spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8080**

### Produção (JAR Empacotado)

```bash
mvn clean package -DskipTests
java -jar target/projeto-A3-0.0.1-SNAPSHOT.jar
```

---

## 🔐 Acesso ao Sistema

### Credenciais de Teste

| Perfil | Login | Senha | Acesso |
|--------|-------|-------|--------|
| Administrador | `admin` | `admin` | Gerenciar usuários, ver todos os projetos |
| Gerente | `gerente` | `gerente` | Criar/editar projetos, montar equipes |
| Colaborador | `colaborador` | `123456` | Visualizar projetos, acessar relatórios |

### Endereços Úteis

| Página | URL | Acesso |
|--------|-----|--------|
| 🏠 Home | `http://localhost:8080/` | Público |
| 🔑 Login | `http://localhost:8080/login` | Público |
| 👥 Usuários | `http://localhost:8080/usuarios` | Admin |
| 📊 Projetos | `http://localhost:8080/projetos` | Gerente/Admin |
| 👨‍💼 Equipes | `http://localhost:8080/equipes` | Gerente/Admin |
| 📈 Dashboard | `http://localhost:8080/relatorios/dashboard` | Todos |

---

## 📝 Funcionalidades

- **Gestão de Usuários**: Cadastro com validação de CPF, ativação/desativação e controle de perfis (Administrador, Gerente, Colaborador)
- **Gestão de Projetos**: Cadastro com datas de início e término, status (Planejado, Em Andamento, Concluído, Cancelado) e gerente responsável
- **Gestão de Equipes**: Equipes com múltiplos membros, líder Gerente e vínculo a projetos
- **Relatórios**: Dashboard com estatísticas, desempenho por projeto e acompanhamento de progresso
- **Segurança**: Autenticação com login/senha, controle de acesso por perfil, validação de CPF e interceptor de segurança em todas as rotas

---

## 🗄️ Banco de Dados

### Perfil: Development (Padrão)

Banco H2 em memória, sem arquivo físico, resetado a cada reinicialização.

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:data.sql
```

### Perfil: Production (PostgreSQL)

```bash
DB_URL=jdbc:postgresql://localhost:5432/projeto_a3
DB_USERNAME=postgres
DB_PASSWORD=sua_senha
```

### Tabelas Principais

- `usuarios` — Usuários do sistema
- `projetos` — Projetos
- `equipes` — Equipes
- `equipe_membros` — Membros das equipes
- `equipe_projeto` — Relação equipe-projeto (many-to-many)

---

## 🧪 Testes

```bash
mvn test
```

Resultados disponíveis em `target/surefire-reports/`.

---

## 🐛 Troubleshooting

### Porta 8080 já em uso

```bash
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Ou usar porta diferente
java -Dserver.port=8090 -jar target/projeto-A3-0.0.1-SNAPSHOT.jar
```

### CPF inválido durante cadastro

Use um CPF com dígitos verificadores corretos. CPFs válidos para teste: `11144477735`, `87654321596`, `39053344705`.

### Erro "Cannot find symbol" durante compilação

```bash
mvn clean install -U
```

---

## 📦 Docker (Opcional)

```dockerfile
FROM openjdk:21-slim
COPY target/projeto-A3-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
docker build -t projeto-a3 .
docker run -p 8080:8080 projeto-a3
```

---

## 🔗 Links Úteis

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Jakarta Persistence](https://jakarta.ee/specifications/persistence/)
- [Thymeleaf Template Engine](https://www.thymeleaf.org/)
- [H2 Database](https://www.h2database.com/)

---

## 👥 Autores

**Desenvolvedor**: @sunstrix — Maio 2026

---

## 📄 Licença

Este projeto está sob licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

## 📞 Suporte

Para dúvidas ou problemas, abra uma issue: [Issues - Projeto A3](https://github.com/sunstrix/projeto-A3/issues)

---

> 💡 Para desenvolvimento local, use o perfil padrão com banco H2 em memória. Para produção, configure as variáveis de ambiente e use PostgreSQL.
