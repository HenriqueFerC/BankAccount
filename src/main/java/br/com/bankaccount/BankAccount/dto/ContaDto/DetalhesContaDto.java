package br.com.bankaccount.BankAccount.dto.ContaDto;

import br.com.bankaccount.BankAccount.dto.UserDto.DetalhesUserDto;
import br.com.bankaccount.BankAccount.model.Conta;
import br.com.bankaccount.BankAccount.model.TipoConta;

import java.math.BigDecimal;

public record DetalhesContaDto(int numero, TipoConta tipoConta, BigDecimal saldo, DetalhesUserDto userDto) {
    public DetalhesContaDto(Conta conta) {
        this(conta.getNumero(), conta.getTipoConta(), conta.getSaldo(), new DetalhesUserDto(conta.getUser()));
    }
}
