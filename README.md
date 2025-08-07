# 🔐 Teste Técnico: Sistema de Notificações por E-mail com Fallback (Back-end)


Este é o back-end de uma aplicação de recuperação de senha com envio de e-mail, desenvolvido com **Java + Spring Boot**. A API permite registro, autenticação, envio de e-mail para redefinição de senha, persistência de logs e consulta de logs de envio.

---

## 🧪 Funcionalidades

- Registro de usuários com criptografia de senha.
- Login com autenticação JWT.
- Envio de e-mail para redefinição de senha utilizando protocolo SMTP.
- Geração e expiração de tokens para redefinição.
- Salvamento e visualização de logs de e-mail (status, erro, data).
- Consulta protegida dos logs via token JWT.

---

## 🧱 Tecnologias utilizadas

- Java 17
- Maven
- Spring Boot 3.5.4
  - Dependencies:
    - Lombok
    - Spring Boot DevTools
    - Spring Security (JWT)
    - Spring Data JPA
    - Spring Web
    - Java Mail Sender
    - H2 Database
- Mailtrap integrado com comunicação SMTP

---

## 📁 Estrutura de pacotes

```bash
src/main/java/com.example...
├── auth/security       # Segurança, tokens, autenticação e EmailService
├── controllers/        # Controladores REST
├── domain/
│   ├── log/            # Entidade EmailLog
│   └── user/           # Entidade User
├── dto/                # Data Transfer Objects
├── repositories/       # Interfaces JPA (UserRepository, EmailLogRepository)
````

---

## 📦 Instalação e execução

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/recover-password-backend.git
cd recover-password-backend
```
---
### 2. Integração com provedor SMTP

Provedor escolhido para esse projeto foi Mailtrap, O Mailtrap é uma ferramenta usada principalmente em ambientes de desenvolvimento para testar o envio de e-mails sem que eles cheguem ao destinatário real. 

---

### O que o Mailtrap faz?

* **Intercepta e-mails enviados pela sua aplicação**;
* **Simula uma caixa de entrada**, onde você pode visualizar os e-mails enviados;
* Permite testar:

    * o conteúdo do e-mail,
    * o layout (HTML/texto),
    * os headers (assunto, remetente, etc.),
    * anexos,
    * sem risco de enviar e-mails de verdade.

---

### Por que usar Mailtrap?

Durante o desenvolvimento, **você não quer enviar e-mails reais** para usuários ou clientes. Mailtrap resolve isso:

- Testa se sua lógica de envio funciona
- Verifica se o e-mail está sendo montado corretamente 
- Evita spam real acidental

---

### Como usar o Mailtrap no Spring Boot?

1. **Crie uma conta** em [mailtrap.io](https://mailtrap.io/)
2. Vá até a opção na barra lateral esquerda **Sandboxes**
3. Selecione a sandbox, na área de "My Project"
4. Escolha a opção de **Integration** e **SMTP**
5. Copie as configurações SMTP fornecidas, como:

```properties
spring.mail.host=smtp.mailtrap.io
spring.mail.port=465
spring.mail.username=SEU_USER_MAILTRAP
spring.mail.password=SUA_SENHA_MAILTRAP
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

4. Substitua no seu `application.properties`.

---

### Exemplo de uso real:

Se sua API envia um e-mail de redefinição de senha, o Mailtrap captura esse envio e te mostra como o e-mail ficou — **sem de fato enviá-lo ao usuário**.

---
### 3. Configure o application.properties

⚠️ Crie um arquivo `application.properties` baseado no `application.properties.example`.

```
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=seuemail@gmail.com
spring.mail.password=suasenha
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

jwt.secret=seuSegredoJWT
```

---

### 4. Instalar dependências

Rode o comando, na raiz do projeto.

````bash
mvn clean install
````

---

## ▶️ Executar o projeto

Pela sua IDE (O IntelliJ foi utilizada durante o processo de desenvolvimento).

```bash
src/main/java/com.example...
├── DtTecnicoBackApplication       #Execute a classe
````

---

## 🔐 Rotas disponíveis

| Método | Endpoint         | Autenticação | Descrição                               |
| ------ | ---------------- | ------------ | --------------------------------------- |
| POST   | `/auth/login`    | ❌            | Login do usuário                        |
| POST   | `/auth/register` | ❌            | Registro de novo usuário                |
| POST   | `/auth/recover`  | ❌            | Envio de e-mail com link de redefinição |
| GET    | `/auth/log`      | ✅ JWT        | Consulta de logs de envio               |

---

## Rotas da API

### `/auth/register`

**Método:** `POST`
**Descrição:** Registra um novo usuário.
**Corpo esperado (JSON):**

```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "password": "123456"
}
```

**Resposta (200):**

```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "token": "jwt-token-gerado"
}
```

---

### 🔐 `/auth/login`

**Método:** `POST`
**Descrição:** Realiza login de usuário.
**Corpo esperado (JSON):**

```json
{
  "email": "joao@email.com",
  "password": "123456"
}
```

**Resposta (200):**

```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "token": "jwt-token-gerado"
}
```

---

### 🔐 `/auth/recover`

**Método:** `POST`
**Descrição:** Inicia processo de recuperação de senha.
**Corpo esperado (JSON):**

```json
{
  "email": "joao@email.com"
}
```

**Resposta (200):**

```
"E-mail enviado com instruções de recuperação."
```

**Resposta (404):**

```
"Usuário não encontrado"
```

---

### 📜 `/auth/log`

**Método:** `GET`
**Descrição:** Retorna lista de tentativas de envio de e-mail.

**Cabeçalhos:**

```
Authorization: Bearer <jwt-token>
```

**Resposta (200):**

```json
[
  {
    "recipient": "joao@email.com",
    "status": "SUCESS",
    "error": null,
    "dateSend": "2025-08-07T15:42:12.512"
  },
  {
    "recipient": "erro@test.com",
    "status": "ERROR",
    "error": "Erro simulado no envio de e-mail: e-mail:erro@test.com não é valido.",
    "dateSend": "2025-08-07T15:45:03.141"
  }
]
```

## 🔒 Segurança

* **JWT Token** é capturado ao logar e enviado via `Authorization: Bearer <token>`.
* Rotas como `/auth/log` são protegidas e só acessíveis com token válido.
* Tokens de redefinição de senha possuem **expiração de 30 minutos**.

---

## 📊 Modelo de Log

Cada envio de e-mail (com sucesso ou falha) é registrado com:

* 📧 Destinatário (`recipient`)
* ✅ Status (`status`)
* ❌ Erro (`error`)
* ⏱️ Data de envio (`dateSend`)

---

## 🧪 Teste com Front-End

> [Link para o repositório do Front-End](https://github.com/RonaldoFidelis/DT-FRONT)

Certifique-se de que o front está apontando para o back via `.env`:

```
VITE_API_URL=http://localhost:8080/auth
```

---

### 1. **Como você lida com falhas no envio de e-mail? Qual é a lógica de retry?**

As falhas no envio de e-mails são tratadas capturando exceções como `sendEmail`. Quando ocorre uma falha, o sistema:

* Registra a tentativa com status `ERROR` no banco de dados.
* Armazena a descrição do erro.
* Permite o reenvio manual posteriormente via painel administrativo.

**retry**:

* Manual (através de botão de reenvio).
* Foi configurado no front-end, um rety simples, que tenta executar a requisição 1 vez após o intervalo de 5seg. hook `useFetch` método `useRecoverPassword`.

---

### 2. **O que acontece se o provedor SMTP ficar indisponível por um tempo?**

Se o servidor SMTP estiver fora do ar:

* Nenhum e-mail é enviado.
* O erro é capturado e registrado no log.
* O status da tentativa fica marcado como `ERROR`.

---

### 3. **Como o reenvio manual é feito sem duplicar efeitos colaterais?**

Cada envio é tratado de forma **idempotente**:

* O e-mail é reenviado com os mesmos dados.
* Nenhuma nova ação ou alteração de estado é provocada além do envio.
* O log da nova tentativa é separado, sem sobrescrever o anterior.

Desse modo, não possui duplicações.

---

### 4. **Como o sistema protege credenciais e evita abuso de disparos?**

#### Proteção de Credenciais:

* As credenciais SMTP estão em variáveis de ambiente (`application.properties`).
#### Prevenção de Abusos:

* As rotas de envio são protegidas por autenticação JWT.
* Já na API a classe `SecurityFilter` é responsavel por trata as requisições uma de cada vez, uma vez que ela extende `OncePerRequestFilter`.
* Utilização de ambientes seguros de testes, como o Mailtrap.

---

### 5. **Quais dos diferenciais foram implementados e por quê?**

* Rate limiting: Não cheguei a limitar o número de requisições, mas a classe `SecurityFilter` lida como possiveis abusos.
* Idempotência e prevenção de duplicidade: Como nenhuma nova ação é realizada, além do reenvio das requisições que deram erradas, o logs antigos não são sobrescrito.
---

## 👨‍💻 Autor

* Nome: **Ronaldo Fidelis**
* [LinkedIn](https://www.linkedin.com/in/ronaldo-fidelis-9922941a9/)


