# 🚀 Todo List API — Spring Boot + MySQL + Docker

CRUD simples para praticar Java + Spring Boot + JPA + MySQL rodando no Docker.

README.md gerado por IA para documentação prática do aprendizado.

Projeto 100% backend, direto ao ponto. Apenas API REST

---

## 🧱 Stack

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL 8.4 (Docker)
- Maven

---

## 📦 Entidade

Task {
Long id
String description
boolean finished
}

Tabela: tasks

---

# 🐳 Subindo o MySQL no Docker

Arquivo docker-compose.yml:

services:
mysql:
image: mysql:8.4
container_name: todo_mysql
environment:
MYSQL_DATABASE: todo_db
MYSQL_USER: todo_user
MYSQL_PASSWORD: todo_pass
MYSQL_ROOT_PASSWORD: root_pass
TZ: America/Sao_Paulo
ports:
- "3306:3306"
volumes:
- mysql_data:/var/lib/mysql
volumes:
mysql_data:

Subir banco:

docker compose up -d

---

# ⚙️ Configuração do application.yml

server:
port: 8080

spring:
datasource:
url: jdbc:mysql://localhost:3306/todo_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
username: todo_user
password: todo_pass

jpa:
hibernate:
ddl-auto: update
show-sql: true

---

# ▶️ Rodando a aplicação

./mvnw spring-boot:run

ou

mvn spring-boot:run

Servidor sobe em:

http://localhost:8080

---

# 📌 Endpoints

Base URL:

/tasks

---

## ✅ Criar Task

curl -X POST http://localhost:8080/tasks \
-H "Content-Type: application/json" \
-d '{"description": "Create endpoint GET /issues/:id", "finished": false}'

---

## 📄 Listar todas

curl -X GET http://localhost:8080/tasks -v | python3 -m json.tool

---

## 🔎 Buscar por ID

curl -X GET http://localhost:8080/tasks/1 -v | python3 -m json.tool

---

## 🔄 Atualizar (PATCH)

curl -X PATCH http://localhost:8080/tasks/1 \
-H "Content-Type: application/json" \
-d '{"description": "Endpoint PATCH /tasks/:id", "finished": true}' \
-v | python3 -m json.tool

---

## ❌ Deletar

curl -X DELETE http://localhost:8080/tasks/2 \
-v | python3 -m json.tool

---

# 🧠 Regras atuais

- Lista ordenada por description
- update substitui description e finished
- delete retorna lista atualizada
- listById usa .get() (vai lançar erro se não existir — proposital para estudo)

---

# 🗂 Estrutura

entity/
service/
controller/
repository/

Arquitetura:

Controller → Service → Repository → MySQL

---

# 🎯 Objetivo do Projeto

Praticar:

- JPA
- Tipos primitivos do Java
- Spring e Springboot
- Injeção de dependência
- Docker com banco relacional
- CRUD REST

---