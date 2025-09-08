package br.com.bankaccount.BankAccount.dto.AutenticacaoDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenJwtDto(
        @Schema(description = "Token gerado! Exemplo: 135gadkjgadkg1354")
        String token
) {
}
