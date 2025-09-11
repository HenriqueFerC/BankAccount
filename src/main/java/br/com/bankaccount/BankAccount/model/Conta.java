package br.com.bankaccount.BankAccount.model;

import br.com.bankaccount.BankAccount.dto.ContaDto.AtualizarContaDto;
import br.com.bankaccount.BankAccount.dto.ContaDto.CadastrarContaDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "NUMERO", length = 6, nullable = false, unique = true)
    private int numero;

    @Column(name = "SALDO", nullable = false)
    private BigDecimal saldo;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_CONTA", nullable = false)
    private TipoConta tipoConta;

    @OneToOne
    @JoinColumn(name = "ID_USER")
    private User user;

    @ManyToMany(mappedBy = "contas", cascade = CascadeType.DETACH)
    private List<Transacao> transacoes = new ArrayList<>();

    public void adicionarTransacao(Transacao transacao) {
        transacoes.add(transacao);
    }

    public Conta(CadastrarContaDto contaDto, User user) {
        numero = contaDto.numero();
        saldo = contaDto.saldo();
        tipoConta = contaDto.tipoConta();
        this.user = user;
    }

    public void atualizarConta(AtualizarContaDto contaDto) {
        tipoConta = contaDto.tipoConta();
    }

    public void transacao(Conta conta1, Conta conta2, BigDecimal valor) {
        var valor2 = conta2.getSaldo().add(valor);
        var valor1 = conta1.getSaldo().subtract(valor);
        conta1.setSaldo(valor1);
        conta2.setSaldo(valor2);
    }

    public void depositar(Conta conta,BigDecimal valor){
        BigDecimal saldoConta = conta.getSaldo();
        saldoConta = saldoConta.add(valor);
        this.saldo = saldoConta;
    }

    public void sacar(Conta conta,BigDecimal valor){
        BigDecimal saldoConta = conta.getSaldo();
        saldoConta = saldoConta.subtract(valor);
        this.saldo = saldoConta;
    }
}
