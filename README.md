# Projeto A3


Características aplicadas:
- Java 21
- Spring Boot (Web, Thymeleaf, Data JPA)
- Banco: H2 (dev) / PostgreSQL (prod)
- Flyway para migrações
- Estrutura: controllers, model, repository, service
- Sem Lombok
- GitHub Actions para build/test

Executar em desenvolvimento:

1. mvn clean package
2. java -jar target/projeto-A3-0.0.1-SNAPSHOT.jar

Profiles:
- dev (default) -> H2
- prod -> configurações PostgreSQL via variáveis de ambiente
