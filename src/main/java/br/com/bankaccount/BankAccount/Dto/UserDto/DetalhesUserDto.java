package br.com.bankaccount.BankAccount.Dto.UserDto;

import br.com.bankaccount.BankAccount.model.User;
import br.com.bankaccount.BankAccount.model.UserType;

public record DetalhesUserDto(String nome, UserType userType, Long cpfCnpj) {
    public DetalhesUserDto(User user){
        this(user.getNome(), user.getUserType(), user.getCpfCnpj());
    }
}
