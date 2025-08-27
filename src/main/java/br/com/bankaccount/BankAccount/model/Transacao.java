package br.com.bankaccount.BankAccount.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

}
