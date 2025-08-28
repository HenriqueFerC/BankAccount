package br.com.bankaccount.BankAccount.Repository;

import br.com.bankaccount.BankAccount.model.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}
