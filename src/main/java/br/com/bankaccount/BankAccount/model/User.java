package br.com.bankaccount.BankAccount.model;

import br.com.bankaccount.BankAccount.Dto.UserDto.AtualizarUserDto;
import br.com.bankaccount.BankAccount.Dto.UserDto.CadastrarUserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "TB_USER")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "ID_USER")
    private Long id;

    @Column(name = "NOME", length = 80, nullable = false)
    private String nome;

    @Column(name = "PASSWORD", length = 15, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_TYPE", nullable = false)
    private UserType userType;

    @Column(name = "CPF_CNPJ", length = 14, nullable = false, unique = true)
    private Long cpfCnpj;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Endereco endereco;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Telefone> telefones = new ArrayList<>();;

    @OneToOne(mappedBy = "user", cascade = CascadeType.DETACH)
    private Conta conta;

    public void adicionarTelefone(Telefone telefone){
        telefones.add(telefone);
    }

    public void removerTelefone(Telefone telefone){
        telefones.remove(telefone);
    }

    public User(CadastrarUserDto userDto){
        nome = userDto.nome();
        password = userDto.password();
        userType = userDto.userType();
        cpfCnpj = userDto.cpfCnpj();
    }

    public void atualizarUser(AtualizarUserDto userDto){
        nome = userDto.nome();
        password = userDto.password();
        userType = userDto.userType();
        cpfCnpj = userDto.cpfCnpj();
    }
}
