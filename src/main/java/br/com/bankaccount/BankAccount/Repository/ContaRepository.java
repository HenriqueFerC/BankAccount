package br.com.bankaccount.BankAccount.Repository;

import br.com.bankaccount.BankAccount.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Long, Conta> {
}
