package br.com.bankaccount.BankAccount.model;

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

    @Column(name = "DDD", length = 2,nullable = false)
    private short ddd;

    @Column(name = "NUMERO", length = 9, nullable = false)
    private int telefone;

    @ManyToOne
    @JoinColumn(name = "ID_USER")
    private User user;
}
