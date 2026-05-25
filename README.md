# 🚀 Projeto A3 - Sistema de Gestão

Sistema de gestão desenvolvido com **Spring Boot 3.2.0** e **Java 21**, com interface web em Thymeleaf e banco de dados PostgreSQL.

---

## Tecnologias Utilizadas

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Java | 21 | Linguagem de programação |
| Spring Boot | 3.2.0 | Framework backend |
| Spring Data JPA | - | Persistência de dados |
| Spring Web | - | APIs REST e MVC |
| Thymeleaf | 3.1.2 | Templates HTML dinâmicos |
| PostgreSQL | 16.x | Banco de dados relacional |
| Maven | 3.9.x | Gerenciador de dependências |
| Hibernate | 6.3.1 | ORM para JPA |

---

## Pré-requisitos

Antes de iniciar, certifique-se de ter instalado:

- [Java 21 JDK](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [PostgreSQL 16+](https://www.postgresql.org/download/)
- [Git](https://git-scm.com/downloads)

---

## Configuração do Banco de Dados

### 1. Criar banco e usuário no PostgreSQL

```sql
CREATE DATABASE projeto_a3;
CREATE USER admin WITH PASSWORD 'admin';
GRANT ALL PRIVILEGES ON DATABASE projeto_a3 TO admin;
```

### 2. Configurar credenciais

Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/projeto_a3
spring.datasource.username=admin
spring.datasource.password=admin
spring.datasource.driver-class-name=org.postgresql.Driver
```

---

## Como Executar o Projeto

### Opção 1: Via Maven (Desenvolvimento)

```bash
git clone https://github.com/sunstrix/projeto-A3.git
cd projeto-A3
mvn clean install
mvn spring-boot:run
```

### Opção 2: Executar com perfil de produção

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Opção 3: Executar JAR compilado

```bash
mvn clean package -DskipTests
java -jar target/projeto-A3-0.0.1-SNAPSHOT.jar
```

---

## Acessando a Aplicação

Após iniciar, acesse no navegador: **http://localhost:8080**

### Credenciais de Teste

| Perfil | Login | Senha |
|--------|-------|-------|
| Administrador | `admin` | `admin` |
| Gerente | `gerente` | `gerente` |
| Colaborador | `colaborador` | `123456` |

---

## Estrutura do Projeto
