package br.com.bankaccount.BankAccount.Repository;

import br.com.bankaccount.BankAccount.model.Transacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    Page<Transacao> findByDataTransacaoBetween(LocalDateTime data1, LocalDateTime data2, Pageable pageable);
}
