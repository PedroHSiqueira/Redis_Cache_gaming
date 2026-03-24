# 🚀 Redis Cache Gaming API

API backend desenvolvida com **Java + Spring Boot**, focada em demonstrar conceitos modernos de backend como **cache, ranking em tempo real e rate limiting** utilizando Redis.

---

## 🧠 Objetivo

Este projeto simula um sistema de usuários com pontuação (gamificação), aplicando estratégias de performance e escalabilidade:

- ⚡ Cache com Redis
- 🏆 Ranking em tempo real (Sorted Set)
- 🛡️ Rate limiting por usuário
- 🗄️ Persistência com PostgreSQL

---

## 🏗️ Arquitetura

```
Client → Controller → Service → Repository → PostgreSQL
                       ↓
                     Redis
```

---

## 🧩 Funcionalidades

### 👤 Usuários
- Criar usuário
- Buscar usuário por ID (com cache)
- Atualizar usuário
- Remover usuário

---

### 🎯 Pontuação
- Incrementar pontos de um usuário
- Sincronização com Redis para ranking

---

### 🏆 Ranking
- Top usuários ordenados por pontuação
- Consulta de posição no ranking

---

### 🛡️ Rate Limiting
- Limite de requisições por usuário
- Implementado com Redis (TTL)

---

## ⚡ Tecnologias

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Cache
- PostgreSQL
- Redis
- Docker / Docker Compose
- Lombok
- Swagger (OpenAPI)

---

## 🧠 Conceitos aplicados

- Cache com `@Cacheable`, `@CachePut`, `@CacheEvict`
- Redis como estrutura de dados (ZSET)
- Rate limiting com contador + TTL
- Separação de responsabilidades (Controller / Service / Repository)
- DTOs e mapeamento
- Logs com SLF4J

---

## 🐳 Como rodar o projeto

### 1️⃣ Subir containers

```bash
docker-compose up -d
```

---

### 🗄️ Serviços disponíveis

| Serviço     | Porta |
|------------|------|
| PostgreSQL | 5433 |
| Redis      | 6379 |

---

### 2️⃣ Configurar aplicação

Exemplo `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/gaming_postgres
    username: postgres
    password: postgres

  redis:
    host: localhost
    port: 6379

  cache:
    type: redis
```

---

### 3️⃣ Rodar aplicação

```bash
./mvnw spring-boot:run
```

---

## 📡 Endpoints principais

### 👤 Usuários

#### Criar usuário
```
POST /users
```

#### Buscar usuário (com cache)
```
GET /users/{id}
```

#### Atualizar usuário
```
PUT /users/{id}
```

#### Deletar usuário
```
DELETE /users/{id}
```

---

### 🎯 Pontuação

#### Adicionar pontos
```
POST /users/{id}/score
```

---

### 🏆 Ranking

#### Top usuários
```
GET /users/ranking/{limit}
```

---

## 🧠 Como funciona o Redis

### 📦 Cache
- Armazena usuários frequentemente acessados
- Evita consultas repetidas ao banco

---

### 🏆 Ranking (ZSET)

Estrutura:

```
Key: ranking
Value: userId
Score: points
```

---

### 🛡️ Rate Limit

Estrutura:

```
rate_limit:user:{id} → contador com TTL
```

---

## 🔥 Diferenciais do projeto

- Uso real de Redis (não apenas cache)
- Implementação de ranking com Sorted Set
- Rate limiting eficiente e simples
- Arquitetura limpa e organizada
- Código preparado para evolução

---

## 📈 Melhorias futuras

- ✅ Testes automatizados (em desenvolvimento)
- 🔄 Paginação no ranking
- 📊 Métricas e monitoramento
- 🔐 Autenticação e autorização
- ⚡ Otimização de queries (evitar N+1)

---

## 👨‍💻 Autor

Desenvolvido por **Pedro Siqueira**

---

## 📌 Observações

Este projeto foi desenvolvido com foco em aprendizado e demonstração de boas práticas de backend, podendo ser facilmente expandido para cenários mais complexos.
