package br.com.bankaccount.BankAccount.Dto.UserDto;

import br.com.bankaccount.BankAccount.model.UserType;

public record AtualizarUserDto(
        String nome,
        String password,
        UserType userType,
        Long cpfCnpj
) {
}
