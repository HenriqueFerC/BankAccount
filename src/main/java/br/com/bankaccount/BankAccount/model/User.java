package br.com.bankaccount.BankAccount.model;

import br.com.bankaccount.BankAccount.dto.UserDto.AtualizarUserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "TB_USER")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "ID_USER")
    private Long id;

    @Column(name = "NOME", length = 80, nullable = false)
    private String nome;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_TYPE", nullable = false)
    private UserType userType;

    @Column(name = "CPF_CNPJ", length = 18, nullable = false, unique = true)
    private String cpfCnpj;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Endereco endereco;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Telefone> telefones = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.DETACH)
    private Conta conta;

    public void adicionarTelefone(Telefone telefone){
        telefones.add(telefone);
    }

    public void removerTelefone(Telefone telefone){
        telefones.remove(telefone);
    }

    public User(String nome, String password, UserType userType, String cpfCnpj){
        this.nome = nome;
        this.password = password;
        this.userType = userType;
        this.cpfCnpj = cpfCnpj;
    }

    public void atualizarUser(String nome, String password, UserType userType, String cpfCnpj){
        this.nome = nome;
        this.password = password;
        this.userType = userType;
        this.cpfCnpj = cpfCnpj;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority(userType.name()));
    }

    @Override
    public String getUsername(){
        return nome;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }
}
