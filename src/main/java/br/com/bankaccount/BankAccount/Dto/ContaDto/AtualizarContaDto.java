package br.com.bankaccount.BankAccount.Dto.ContaDto;

import br.com.bankaccount.BankAccount.model.TipoConta;

public record AtualizarContaDto(
        TipoConta tipoConta
) {
}
