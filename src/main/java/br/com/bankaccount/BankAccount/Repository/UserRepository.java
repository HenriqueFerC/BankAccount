package br.com.bankaccount.BankAccount.Repository;

import br.com.bankaccount.BankAccount.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, User> {
}
