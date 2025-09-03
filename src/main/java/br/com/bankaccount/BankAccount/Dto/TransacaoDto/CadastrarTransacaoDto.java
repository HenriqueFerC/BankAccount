package br.com.bankaccount.BankAccount.Dto.TransacaoDto;

import br.com.bankaccount.BankAccount.model.TipoTransacao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CadastrarTransacaoDto(
        TipoTransacao tipoTransacao,
        @Min(value = 0, message = "Valor precisa ser positivo!")
        @NotNull(message = "Valor não pode ser nulo!")
        BigDecimal valor
) {
}
