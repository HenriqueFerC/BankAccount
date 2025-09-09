package br.com.bankaccount.BankAccount.dto.AutenticacaoDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record DadosLoginDto(
        @Schema(description = "Cpf ou Cnpj de Usuário não pode estar em branco. Exemplo: 54912312323")
        @NotBlank(message = "Cpf ou Cnpj não pode estar em branco!")
        String cpfCnpj,
        @NotBlank(message = "Senha não pode estar em branco!")
        String password) {
}
