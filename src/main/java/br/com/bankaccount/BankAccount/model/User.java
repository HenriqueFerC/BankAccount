package br.com.bankaccount.BankAccount.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "CPF_CNPJ", length = 14, nullable = false)
    private Long cpfCnpj;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Endereco endereco;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Telefone> telefones;

    @OneToOne(mappedBy = "user", cascade = CascadeType.DETACH)
    private Conta conta;

    public void adicionarTelefone(Telefone telefone){
        telefones.add(telefone);
    }

    public void removerTelefone(Telefone telefone){
        telefones.remove(telefone);
    }
}
