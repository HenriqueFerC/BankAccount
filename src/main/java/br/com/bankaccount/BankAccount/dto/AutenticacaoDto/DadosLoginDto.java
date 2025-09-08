package br.com.bankaccount.BankAccount.dto.AutenticacaoDto;

import jakarta.validation.constraints.NotBlank;

public record DadosLoginDto(
        @NotBlank(message = "Nome não pode estar em branco!")
        String nome,
        @NotBlank(message = "Senha não pode estar em branco!")
        String password) {
}
