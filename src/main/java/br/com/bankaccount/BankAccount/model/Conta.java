package br.com.bankaccount.BankAccount.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "TB_CONTA")
public class Conta {

    @Id
    @GeneratedValue
    @Column(name = "ID_CONTA")
    private Long id;

    @Column(name = "NUMERO", length = 6, nullable = false)
    private short numero;

    @Column(name = "SALDO", nullable = false)
    private BigDecimal saldo;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_CONTA", nullable = false)
    private TipoConta tipoConta;
}
