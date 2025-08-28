package br.com.bankaccount.BankAccount.Repository;

import br.com.bankaccount.BankAccount.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
