package br.com.bankaccount.BankAccount.service;

import br.com.bankaccount.BankAccount.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String cpfCnpj) throws UsernameNotFoundException {
        return userRepository.findByCpfCnpj(cpfCnpj);
    }
}
