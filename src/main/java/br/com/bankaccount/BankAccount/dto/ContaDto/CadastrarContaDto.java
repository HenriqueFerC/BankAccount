package br.com.bankaccount.BankAccount.dto.ContaDto;


import br.com.bankaccount.BankAccount.model.TipoConta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CadastrarContaDto(
        @Min(value = 0, message = "Número precisa ser positivo!")
        @NotNull(message = "Número da conta não pode ser nulo!")
        @Digits(integer = 6, fraction = 0, message = "Número da conta precisa ter 6 caractéres")
        @Schema(description = "Número da conta precisa ter 6 números. Exemplo: 123456")
        int numero,
        @Min(value = 0, message = "Saldo precisa ser um valor positivo!")
        @NotNull(message = "Saldo não pode ser nulo!")
        @Schema(description = "Saldo da conta precisa ser positivo e não nulo. Exemplo: 0")
        BigDecimal saldo,
        @Schema(description = "Tipo de Conta precisa ser uma das opções possíveis. Exemplo: Conta Corrente (CC)")
        @NotNull(message = "Tipo de Conta precisa ser definida!")
        TipoConta tipoConta
) {
}
