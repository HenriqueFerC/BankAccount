package br.com.bankaccount.BankAccount.dto.ContaDto;

import br.com.bankaccount.BankAccount.dto.UserDto.DetalhesUserDto;
import br.com.bankaccount.BankAccount.model.Conta;
import br.com.bankaccount.BankAccount.model.TipoConta;

public record DetalhesContaDto(int numero, TipoConta tipoConta, DetalhesUserDto userDto) {
    public DetalhesContaDto(Conta conta) {
        this(conta.getNumero(), conta.getTipoConta(), new DetalhesUserDto(conta.getUser()));
    }
}
