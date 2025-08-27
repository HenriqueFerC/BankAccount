package br.com.bankaccount.BankAccount.Dto.TransacaoDto;

import br.com.bankaccount.BankAccount.model.TipoTransacao;

import java.math.BigDecimal;

public record CadastrarTransacaoDto(
        TipoTransacao tipoTransacao,
        BigDecimal valor
) {
}
