package br.com.bankaccount.BankAccount.dto.ContaDto;

import br.com.bankaccount.BankAccount.model.TipoConta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AtualizarContaDto(
        @NotNull(message = "Tipo de Conta precisa ser definida!")
        @Schema(description = "Tipo de Conta precisa ser uma das opções possíveis. Exemplo: Conta Corrente (CC)")
        TipoConta tipoConta
) {
}
