package br.com.bankaccount.BankAccount.Dto.EnderecoDto;

import br.com.bankaccount.BankAccount.model.Endereco;
import br.com.bankaccount.BankAccount.model.Estados;

public record DetalhesEnderecoDto(Estados estado, String cidade, String bairro, String cep, String logradouro, short numero) {
    public DetalhesEnderecoDto(Endereco endereco){
        this(endereco.getEstado(), endereco.getCidade(), endereco.getBairro(), endereco.getCep(), endereco.getLogradouro(), endereco.getNumero());
    }
}
