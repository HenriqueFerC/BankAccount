package br.com.bankaccount.BankAccount.dto.AutenticacaoDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record DadosLoginDto(
        @Schema(description = "Nome de Usuário não pode estar em branco. Exemplo: JoaoDasNeves")
        @NotBlank(message = "Nome não pode estar em branco!")
        String nome,
        @NotBlank(message = "Senha não pode estar em branco!")
        String password) {
}
