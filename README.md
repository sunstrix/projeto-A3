# 🔐 Central de Projetos Multi-Perfil - Projeto A3

Sistema de gestão de projetos com segurança baseada em hierarquia de perfis. Desenvolvido em **Java 21** com **Spring Boot 3.2.0**.

## 📋 Visão Geral

O sistema oferece uma solução completa para gerenciamento de projetos, equipes e usuários com controle de acesso por perfil. Cada perfil tem permissões específicas:

- **👤 Administrador**: Gerencia todos os usuários do sistema
- **👨‍💼 Gerente**: Cria e gerencia projetos, monta equipes
- **👥 Colaborador**: Visualiza projetos e atualiza status de tarefas

---

## ⚙️ Características Técnicas

### Stack Tecnológico
- ✅ **Java 21** - Linguagem de programação
- ✅ **Spring Boot 3.2.0** - Framework web
- ✅ **Spring Data JPA** - Persistência de dados
- ✅ **Thymeleaf** - Template engine
- ✅ **H2 Database** - Banco de dados (desenvolvimento)
- ✅ **Maven** - Gerenciador de dependências
- ✅ **Jakarta Persistence** - ORM moderno
- ✅ **JUnit 5** - Testes unitários

### Estrutura do Projeto
```
src/main/java/meuprojeto/
├── MeuProjetoApplication.java
├── config/                    # Configurações de segurança
│   └── SecurityConfig.java
├── controller/                # Controladores REST/MVC
│   ├── LoginController.java
│   ├── UsuarioController.java
│   ├── ProjetoController.java
│   ├── EquipeController.java
│   └── RelatorioController.java
├── model/                     # Entidades JPA
│   ├── Usuario.java
│   ├── Projeto.java
│   ├── Equipe.java
│   └── EquipeMembro.java
├── repository/                # Interfaces JPA
│   ├── UsuarioRepository.java
│   ├── ProjetoRepository.java
│   ├── EquipeRepository.java
│   └── EquipeMemberRepository.java
├── service/                   # Lógica de negócio
│   ├── UsuarioService.java
│   ├── ProjetoService.java
│   ├── EquipeService.java
│   ├── RelatorioService.java
│   └── CpfValidator.java
└── exception/                 # Tratamento de exceções
    └── AppException.java
```

---

## 🚀 Como Executar

### Pré-requisitos
- Java 21+ instalado
- Maven 3.8.1+
- Git

### 1️⃣ Clone o Repositório
```bash
git clone https://github.com/sunstrix/projeto-A3.git
cd projeto-A3
```

### 2️⃣ Desenvolvimento (H2 em Memória)
```bash
# Compilar e rodar testes
mvn clean install

# Iniciar a aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: **`http://localhost:8080/`**

### 3️⃣ Produção (Empacotado)
```bash
# Gerar JAR executável
mvn clean package

# Executar o JAR
java -jar target/projeto-A3-0.0.1-SNAPSHOT.jar
```

### 4️⃣ Parar a Aplicação
```bash
# No terminal onde a aplicação está rodando
Ctrl + C
```

---

## 🔐 Acesso ao Sistema

### Credenciais de Teste

| Login | Senha | Perfil | Acesso |
|-------|-------|--------|--------|
| `admin` | `admin` | Administrador | Gerenciar usuários, ver todos os projetos |
| `gerente` | `gerente` | Gerente | Criar/editar projetos, montar equipes |
| `colaborador` | `123456` | Colaborador | Visualizar projetos, acessar relatórios |

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

### ✅ Gestão de Usuários
- Cadastro com validação de CPF
- Campos obrigatórios: Nome, CPF, Email, Cargo, Login, Senha, Perfil
- Perfis: Administrador, Gerente, Colaborador
- Usuários podem ser ativados/desativados

### ✅ Gestão de Projetos
- Cadastro com datas (início/término prevista)
- Status: Planejado, Em Andamento, Concluído, Cancelado
- Cada projeto tem um gerente responsável
- Apenas gerente/admin pode criar projetos
- Apenas gerente responsável ou admin pode editar

### ✅ Gestão de Equipes
- Cadastro de equipes com múltiplos membros
- Uma equipe pode atuar em vários projetos
- Líder da equipe é um Gerente
- Adicionar/remover membros

### ✅ Relatórios
- Dashboard com estatísticas de projetos
- Relatório de desempenho por projeto
- Acompanhamento de progresso

### ✅ Segurança
- Sistema de autenticação com login/senha
- Controle de acesso por perfil
- Validação de CPF (algoritmo de dígito verificador)
- Permissões específicas por perfil
- Interceptor de segurança para todas as rotas

---

## 🗄️ Banco de Dados

### Perfil: Development (Padrão)
- **Banco**: H2 (em memória)
- **Arquivo**: Nenhum (dados em RAM)
- **Resetado**: A cada reinicialização
- **Configuração**: `application-dev.properties`

### Perfil: Production
- **Banco**: PostgreSQL
- **Variáveis de Ambiente**:
  ```bash
  DB_URL=jdbc:postgresql://localhost:5432/projeto_a3
  DB_USERNAME=postgres
  DB_PASSWORD=sua_senha
  ```

### Tabelas Principais
- `usuarios` - Usuários do sistema
- `projetos` - Projetos
- `equipes` - Equipes
- `equipe_membros` - Membros das equipes
- `equipe_projeto` - Relação equipe-projeto (many-to-many)

---

## 🧪 Testes

Executar testes unitários:
```bash
mvn test
```

Visualizar resultados:
```
target/surefire-reports/
```

---

## 📊 Fluxo de Uso

### 1. Administrador
```
Login (admin/admin)
    ↓
Acessar /usuarios
    ↓
Gerenciar usuários (criar, editar, deletar)
    ↓
Visualizar todos os projetos (/projetos)
```

### 2. Gerente
```
Login (gerente/gerente)
    ↓
Acessar /projetos
    ↓
Criar novo projeto
    ↓
Acessar /equipes
    ↓
Montar equipe e vincular a projetos
    ↓
Acompanhar progresso via Dashboard
```

### 3. Colaborador
```
Login (colaborador/123456)
    ↓
Visualizar /projetos
    ↓
Visualizar /relatorios/dashboard
    ↓
Atualizar status de tarefas
```

---

## 🐛 Troubleshooting

### Erro: "Porta 8080 já está em uso"
```bash
# Encontrar processo usando a porta (Windows)
netstat -ano | findstr :8080

# Matar o processo
taskkill /PID <PID> /F

# Ou usar porta diferente
java -Dserver.port=8090 -jar target/projeto-A3-0.0.1-SNAPSHOT.jar
```

### Erro: CPF inválido durante cadastro
- Certifique-se de usar um CPF válido (com dígitos verificadores corretos)
- CPFs válidos para teste:
  - `11144477735` - João Silva
  - `87654321596` - Maria Silva
  - `39053344705` - Pedro Santos

### Erro: "Cannot find symbol" durante compilação
```bash
mvn clean install -U
```

---

## 📦 Build & Deploy

### Gerar WAR (para Tomcat)
```bash
mvn clean package -DskipTests -Pprod
```

### Docker (Opcional)
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

## 📝 Configurações

### application-dev.properties
```properties
spring.application.name=projeto-A3
spring.profiles.active=dev
server.port=8080
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:data.sql
```

---

## 🔗 Links Úteis

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Jakarta Persistence](https://jakarta.ee/specifications/persistence/)
- [Thymeleaf Template Engine](https://www.thymeleaf.org/)
- [H2 Database](https://www.h2database.com/)

---

## 👥 Autores

- **Desenvolvedor**: @sunstrix
- **Data de Criação**: Maio 2026

---

## 📄 Licença

Este projeto está sob licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

## 📞 Suporte

Para dúvidas ou problemas, abra uma issue no GitHub: 
[Issues - Projeto A3](https://github.com/sunstrix/projeto-A3/issues)

---

**Última atualização**: 21 de Maio de 2026 ✅
