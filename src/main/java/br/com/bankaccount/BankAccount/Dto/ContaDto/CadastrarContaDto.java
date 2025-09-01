package br.com.bankaccount.BankAccount.Dto.ContaDto;


import br.com.bankaccount.BankAccount.model.Conta;
import br.com.bankaccount.BankAccount.model.TipoConta;

import java.math.BigDecimal;

public record CadastrarContaDto(
        int numero,
        BigDecimal saldo,
        TipoConta tipoConta
    ) {
}
