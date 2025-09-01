package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.Dto.UserDto.AtualizarUserDto;
import br.com.bankaccount.BankAccount.Dto.UserDto.CadastrarUserDto;
import br.com.bankaccount.BankAccount.Dto.UserDto.DetalhesUserDto;
import br.com.bankaccount.BankAccount.Repository.UserRepository;
import br.com.bankaccount.BankAccount.model.User;
import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

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

    @GetMapping("{id}")
    public ResponseEntity<DetalhesUserDto> buscarPorId(@PathVariable("id") Long id){
        try {
            var usuario = userRepository.getReferenceById(id);
            return ResponseEntity.ok(new DetalhesUserDto(usuario));
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("listar")
    public ResponseEntity<List<DetalhesUserDto>> listarUsuarios(Pageable pageable){
        var lista = userRepository.findAll(pageable).stream().map(DetalhesUserDto::new).toList();
        if(lista.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PutMapping("atualizar/{id}")
    @Transactional
    public ResponseEntity<DetalhesUserDto> atualizar(@PathVariable("id") Long id, @RequestBody AtualizarUserDto userDto){
        try {
            var usuario = userRepository.getReferenceById(id);
            usuario.atualizarUser(userDto);
            userRepository.save(usuario);
            return ResponseEntity.ok(new DetalhesUserDto(usuario));
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("excluir/{id}")
    @Transactional
    public ResponseEntity<Void> excluirUsuario(@PathVariable("id") Long id){
        try {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }
}
