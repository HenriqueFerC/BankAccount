package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.Dto.UserDto.CadastrarUserDto;
import br.com.bankaccount.BankAccount.Dto.UserDto.DetalhesUserDto;
import br.com.bankaccount.BankAccount.Repository.UserRepository;
import br.com.bankaccount.BankAccount.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("cadastrar")
    @Transactional
    public ResponseEntity<DetalhesUserDto> cadastrar(@RequestBody CadastrarUserDto userDto, UriComponentsBuilder uriBuilder){
        var usuario = new User(userDto);
        userRepository.save(usuario);
        var uri = uriBuilder.path("user/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalhesUserDto(usuario));
    }

}
