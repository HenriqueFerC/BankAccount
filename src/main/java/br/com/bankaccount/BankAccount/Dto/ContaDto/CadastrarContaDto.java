package br.com.bankaccount.BankAccount.Dto.ContaDto;


import br.com.bankaccount.BankAccount.model.Conta;
import br.com.bankaccount.BankAccount.model.TipoConta;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CadastrarContaDto(
        @Min(value = 0, message = "Número precisa ser positivo!")
        @NotNull(message = "Número da conta não pode ser nulo!")
        int numero,
        @Min(value = 0, message = "Saldo precisa ser um valor positivo!")
        @NotNull(message = "Saldo não pode ser nulo!")
        BigDecimal saldo,
        TipoConta tipoConta
    ) {
}
