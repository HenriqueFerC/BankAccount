package br.com.bankaccount.BankAccount.dto.TransacaoDto;

import br.com.bankaccount.BankAccount.model.TipoTransacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CadastrarTransacaoDto(
        @NotNull(message = "Tipo de Transação precisa ser definida!")
        @Schema(description = "Tipo de Transação precisa estar na lista de opções. Exemplo: PIX (Pix)")
        TipoTransacao tipoTransacao,
        @Min(value = 0, message = "Valor precisa ser positivo!")
        @NotNull(message = "Valor não pode ser nulo!")
        @Schema(description = "Valor precisa ser positivo e não nulo para realizar a transação. Exemplo: 200")
        BigDecimal valor
) {
}
