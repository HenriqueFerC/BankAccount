package br.com.bankaccount.BankAccount.Dto.TelefoneDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastrarTelefoneDto(
        @Min(value = 0, message = "DDD precisa ser positivo!")
        @Size(min = 2, max = 2, message = "DDD precisa ter 2 números!")
        @NotNull(message = "DDD não pode ser nulo!")
        @Schema(description = "DDD precisa ter 2 dígitos conforme o padrão. Exemplo: 11")
        short ddd,
        @Min(value = 0, message = "Número precisa ser positivo!")
        @Size(min = 9, max = 9, message = "Número precisa ter os 9 números!")
        @NotNull(message = "Número não pode ser nulo!")
        @Schema(description = "Número precisa ter os 9 digitos sem o DDD. Exemplo: 987654321")
        int numero
) {
}
