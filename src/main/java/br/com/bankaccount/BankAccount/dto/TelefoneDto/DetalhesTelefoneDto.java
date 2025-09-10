package br.com.bankaccount.BankAccount.dto.TelefoneDto;

import br.com.bankaccount.BankAccount.dto.UserDto.DetalhesUserDto;
import br.com.bankaccount.BankAccount.model.Telefone;

public record DetalhesTelefoneDto(short ddd, int numero, DetalhesUserDto userDto) {
    public DetalhesTelefoneDto(Telefone telefone) {
        this(telefone.getDdd(), telefone.getNumero(), new DetalhesUserDto(telefone.getUser()));
    }
}
