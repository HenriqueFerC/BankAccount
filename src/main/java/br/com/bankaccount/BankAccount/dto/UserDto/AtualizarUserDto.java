package br.com.bankaccount.BankAccount.dto.UserDto;

import br.com.bankaccount.BankAccount.model.UserType;

public record AtualizarUserDto(
        String nome,
        String password,
        UserType userType,
        Long cpfCnpj
) {
}
