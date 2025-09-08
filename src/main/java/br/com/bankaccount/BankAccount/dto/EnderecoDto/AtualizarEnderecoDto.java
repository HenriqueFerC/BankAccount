package br.com.bankaccount.BankAccount.dto.EnderecoDto;

import br.com.bankaccount.BankAccount.model.Estados;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AtualizarEnderecoDto(
        @Schema(description = "O Estado escolhido precisa estar na lista de opções. Exemplo: São Paulo (SP)")
        Estados estado,
        @Size(min = 3, max = 30, message = "Nome da cidade precisa ter entre 3 a 30 caractéres!")
        @NotBlank(message = "Nome da cidade não pode estar em branco!")
        @Schema(description = "Nome da cidade precisa ter no mínimo 3 caractéres e no máximo 30. Exemplo: Barueri")
        String cidade,
        @Size(min = 3, max = 30, message = "Nome do bairro precisa ter entre 3 a 30 caractéres!")
        @NotBlank(message = "Nome do bairro não pode estar em branco!")
        @Schema(description = "Nome do bairro precisa ter no mínico 3 caractéres e no máximo 30. Exemplo: Parque dos Camargos")
        String bairro,
        @Size(min = 8, max = 9, message = "CEP precisa ter todos os caractéres! (*****-***)")
        @NotBlank(message = "CEP não pode estar em branco!")
        @Schema(description = "CEP precisa ter a formatação padrão. Exemplo: 12345-678")
        String cep,
        @Size(min = 3, max = 30, message = "Nome do logradouro precisa ter entre 3 a 30 caractéres!")
        @NotBlank(message = "Nome do logradouro não pode estar em branco!")
        @Schema(description = "Nome do logradouro precisa ter no mínimo 3 e no máximo 30. Exemplo: Vila Mariana")
        String logradouro,
        @Min(value = 0, message = "Numero da residência precisa ser positivo!")
        @NotNull(message = "Número da residência não pode ser nulo!")
        @Schema(description = "Número de residência precisa ser possitivo e não nulo. Exemplo: 1234")
        short numero
) {
}
