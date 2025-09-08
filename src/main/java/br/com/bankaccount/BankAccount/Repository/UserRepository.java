package br.com.bankaccount.BankAccount.Repository;

import br.com.bankaccount.BankAccount.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByNome(String nome);
}
