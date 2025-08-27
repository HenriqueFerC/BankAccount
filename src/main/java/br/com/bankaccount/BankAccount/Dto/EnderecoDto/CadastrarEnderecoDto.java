package br.com.bankaccount.BankAccount.Dto.EnderecoDto;

import br.com.bankaccount.BankAccount.model.Estados;

public record CadastrarEnderecoDto(
        Estados estado,
        String cidade,
        String bairro,
        String cep,
        String logradouro,
        short numero
) {
}
