package br.com.bankaccount.BankAccount.model;

import br.com.bankaccount.BankAccount.Dto.TransacaoDto.CadastrarTransacaoDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "TB_TRANSACAO")
@EntityListeners(AuditingEntityListener.class)
public class Transacao {

    @Id
    @GeneratedValue
    @Column(name = "ID_TRANSACAO")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_TRANSACAO", nullable = false)
    private TipoTransacao tipoTransacao;

    @CreatedDate
    @Column(name = "DATA_TRANSACAO", nullable = false)
    private LocalDateTime dataTransacao;

    @Column(name = "VALOR", nullable = false)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "ID_CONTA")
    private Conta conta;

    public Transacao(CadastrarTransacaoDto transacaoDto){
        tipoTransacao = transacaoDto.tipoTransacao();
        valor = transacaoDto.valor();
    }
}
