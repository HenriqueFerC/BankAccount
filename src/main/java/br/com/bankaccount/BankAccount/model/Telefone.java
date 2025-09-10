package br.com.bankaccount.BankAccount.model;

import br.com.bankaccount.BankAccount.dto.TelefoneDto.AtualizarTelefoneDto;
import br.com.bankaccount.BankAccount.dto.TelefoneDto.CadastrarTelefoneDto;
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
@Table(name = "TB_TELEFONE")
public class Telefone {

    @Id
    @GeneratedValue
    @Column(name = "ID_TELEFONE")
    private Long id;

    @Column(name = "DDD", length = 2, nullable = false)
    private short ddd;

    @Column(name = "NUMERO", length = 9, nullable = false)
    private int numero;

    @ManyToOne
    @JoinColumn(name = "ID_USER")
    private User user;

    public Telefone(CadastrarTelefoneDto telefoneDto) {
        ddd = telefoneDto.ddd();
        numero = telefoneDto.numero();
    }

    public void atualizarTelefone(AtualizarTelefoneDto telefoneDto) {
        ddd = telefoneDto.ddd();
        numero = telefoneDto.numero();
    }
}
