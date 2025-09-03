package br.com.bankaccount.BankAccount.Dto.EnderecoDto;

import br.com.bankaccount.BankAccount.model.Estados;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AtualizarEnderecoDto(
        Estados estado,
        @Size(min = 3, max = 30, message = "Nome da cidade precisa ter entre 3 a 30 caractéres!")
        @NotBlank(message = "Nome da cidade não pode estar em branco!")
        String cidade,
        @Size(min = 3, max = 30, message = "Nome do bairro precisa ter entre 3 a 30 caractéres!")
        @NotBlank(message = "Nome do bairro não pode estar em branco!")
        String bairro,
        @Size(min = 8, max = 9, message = "CEP precisa ter todos os caractéres! (*****-***)")
        @NotBlank(message = "CEP não pode estar em branco!")
        String cep,
        @Size(min = 3, max = 30, message = "Nome do logradouro precisa ter entre 3 a 30 caractéres!")
        @NotBlank(message = "Nome do logradouro não pode estar em branco!")
        String logradouro,
        @Min(value = 0, message = "Numero da residência precisa ser positivo!")
        @NotNull(message = "Número da residência não pode ser nulo!")
        short numero
) {
}
