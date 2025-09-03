package br.com.bankaccount.BankAccount.Dto.ContaDto;

import br.com.bankaccount.BankAccount.model.TipoConta;
import io.swagger.v3.oas.annotations.media.Schema;

public record AtualizarContaDto(
        @Schema(description = "Tipo de Conta precisa ser uma das opções possíveis. Exemplo: Conta Corrente (CC)")
        TipoConta tipoConta
) {
}
