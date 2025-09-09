package br.com.bankaccount.BankAccount.dto.UserDto;

import br.com.bankaccount.BankAccount.model.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastrarUserDto(
        @NotBlank(message = "Nome não pode estar em branco!")
        @Size(min = 3, max = 80, message = "Nome precisa ter no mínimo 3 e no máximo 80 caractéres!")
        @Schema(description = "Nome não pode estar em branco e ter no mínimo 3 e máximo 80 caractéres. Exemplo: Henrique")
        String nome,
        @NotBlank(message = "Senha não pode estar em branco!")
        @Size(min = 6, max = 15, message = "Senha precisa ter no mínimo 6 e no máximo 15 caractéres!")
        @Schema(description = "Senha não pode estar em branco e ter no mínimo 6 e no máximo 15 caractéres. Exemplo: 123abc")
        String password,
        @NotNull(message = "Tipo de Usuário precisa ser definido!")
        @Schema(description = "Usuário precisa definir qual tipo de pessoa é. Exemplo: Pessoa Física")
        UserType userType,
        @NotBlank(message = "Cpf ou Cnpj não podem estar em branco!")
        @Size(min = 11, max = 18, message = "Cpf ou Cnpj precisam ter entre 11 a 18 caractéres!")
        @Schema(description = "Cpf ou Cnpj não podem estar em branco e ter no mínimo 11 e no máximo 18 caractéres. Exemplo: 12345678912")
        String cpfCnpj
) {
}
