# BankAccount

API REST para gerenciamento de contas bancárias, usuários, endereços, telefones e transações. Desenvolvida com Spring Boot e autenticação via JWT.

Tecnologias utilizadas nesse projeto:
Spring Boot, Spring Security, JWT, Hibernate / JPA, MYSql, Maven, Swagger, Validation, Docker

Funcionalidades:
- Cadastro e autenticação de usuários
- Criação de contas bancárias
- Registro de transações entre contas
- Consulta de saldo e histórico
- Proteção de endpoints com JWT
- Documentação Swagger

## Principais Requisições

### Cadastro de Usuário 

`/user/cadastrar`.

JSON:
{
	"nome": "Rick",
	"password": "123456",
	"userType": "Pessoa Jurídica",
	"cpfCnpj": 12345678912
}
______________________________________
### Login de Usuário

`/login`

JSON:
{
  "nome": "Rick",
  "password": "123456"
}
______________________________________
### Busca de Usuário por ID

`/user/{id}`.

* **Requer Token.**
______________________________________
### Busca de todos os Usuários

/user
Requer Token.
______________________________________
### Atualização de Usuário

`/user/atualizar/{id}`.

* **Requer Token.**

JSON:
{
	"nome": "Rick",
	"password": "123456",
	"userType": "Pessoa Jurídica",
	"cpfCnpj": 12345678912
}
______________________________________
### Deletar Usuário

`/user/excluir/{id}`.

* **Requer Token.**

Observação: Exclusão em cascata, excluindo o usuário, todos os relacionados serão deletados.
______________________________________
### Cadastro de Conta

`/user/cadastrarConta/{idDoUsuário}`.

* **Requer Token.**

JSON:
{
	"numero": 134123,
	"saldo": 10000,
	"tipoConta": "Conta Corrente"
}
______________________________________
### Atualização de Conta

`/conta/atualizarConta/{id}`.
Requer Token.
JSON:
{
	"tipoConta": "Conta Poupança"
}
______________________________________
### Buscar Conta por ID

`/conta/{id}`.

* **Requer Token.**

______________________________________
### Realizar transação entre Contas.

`/conta/realizarTransacao/{idRemetente}/{idDestinatario}`.

* **Requer Token.**

JSON:
{
	"tipoTransacao": "Pix",
	"valor": 3025
}
______________________________________
### Cadastro de Endereço

`user/cadastrarEndereco/{idDoUsuário}`.

* **Requer Token.**

JSON:
{
	"estado": "São Paulo",
	"cidade": "Barueri",
	"bairro": "Parque dos Camargos",
	"cep": "06436-000",
	"logradouro": "Rua Carolina",
	"numero": 188
}
______________________________________
### Atualização de Endereço

`/endereco/atualizarEndereco/{id}`.

* **Requer Token.**

JSON:
{
	"estado": "São Paulo",
	"cidade": "Barueri",
	"bairro": "Parque dos Camargos",
	"cep": "06436-360",
	"logradouro": "Rua Carolina",
	"numero": 188
}
______________________________________
### Deletar Endereço

`/endereco/excluirEndereco/{id}`.

* **Requer Token.**

______________________________________
### Busca de Endereço por ID

`/endereco/{id}`.

* **Requer Token.**

______________________________________
### Cadastro de Telefone

`/user/cadastrarTelefone/{id}`.

* **Requer Token.**

JSON:
{
  "ddd": 11,
  "numero": 987654321
}
______________________________________
### Atualização de Telefone

`/telefone/atualizarTelefone/{id}`.

* **Requer Token.**

JSON:
{
  "ddd": 11,
  "numero": 987654321
}
______________________________________
### Deletar Telefone

`/telefone/excluirTelefone/{id}`.

* **Requer Token.**

______________________________________
### Buscar Transação por intervalo de Data

`/transacao/data-inicial/data-final`.

* **Requer Token.**

______________________________________
### Buscar Transação por ID

`/transacao/{id}`.

* **Requer Token.**

______________________________________
Buscar todas as Transações

`/transacao/listar`.

* **Requer Token.**

