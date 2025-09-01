package br.com.bankaccount.BankAccount.model;

import br.com.bankaccount.BankAccount.Dto.EnderecoDto.CadastrarEnderecoDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "TB_ENDERECO")
public class Endereco {

    @Id
    @GeneratedValue
    @Column(name = "ID_ENDERECO")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false)
    private Estados estado;

    @Column(name = "CIDADE", length = 30, nullable = false)
    private String cidade;

    @Column(name = "BAIRRO", length = 30, nullable = false)
    private String bairro;

    @Column(name = "CEP", length = 9, nullable = false)
    private String cep;

    @Column(name = "LOGRADOURO", length = 30, nullable = false)
    private String logradouro;

    @Column(name = "NUMERO", length = 5, nullable = false)
    private short numero;

    @OneToOne
    @JoinColumn(name = "ID_USER")
    private User user;

    public Endereco(CadastrarEnderecoDto enderecoDto){
        estado = enderecoDto.estado();
        cidade = enderecoDto.cidade();
        bairro = enderecoDto.bairro();
        cep = enderecoDto.cep();
        logradouro = enderecoDto.logradouro();
        numero = enderecoDto.numero();
    }
}
