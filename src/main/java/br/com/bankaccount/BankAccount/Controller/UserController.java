package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.Dto.ContaDto.CadastrarContaDto;
import br.com.bankaccount.BankAccount.Dto.ContaDto.DetalhesContaDto;
import br.com.bankaccount.BankAccount.Dto.EnderecoDto.CadastrarEnderecoDto;
import br.com.bankaccount.BankAccount.Dto.EnderecoDto.DetalhesEnderecoDto;
import br.com.bankaccount.BankAccount.Dto.TelefoneDto.CadastrarTelefoneDto;
import br.com.bankaccount.BankAccount.Dto.TelefoneDto.DetalhesTelefoneDto;
import br.com.bankaccount.BankAccount.Dto.UserDto.AtualizarUserDto;
import br.com.bankaccount.BankAccount.Dto.UserDto.CadastrarUserDto;
import br.com.bankaccount.BankAccount.Dto.UserDto.DetalhesUserDto;
import br.com.bankaccount.BankAccount.Repository.ContaRepository;
import br.com.bankaccount.BankAccount.Repository.EnderecoRepository;
import br.com.bankaccount.BankAccount.Repository.TelefoneRepository;
import br.com.bankaccount.BankAccount.Repository.UserRepository;
import br.com.bankaccount.BankAccount.model.Conta;
import br.com.bankaccount.BankAccount.model.Endereco;
import br.com.bankaccount.BankAccount.model.Telefone;
import br.com.bankaccount.BankAccount.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("user")
@Tag(name = "user", description = "API do modelo Usuário, métodos de Buscar, Cadastrar, Atualizar, Listar, Excluir." +
        " Métodos para Cadastrar telefone, conta e endereço já relacionado à classe user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

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

    //Método para criação de Conta Corrente abaixo

    @PostMapping("cadastrarConta/{id}")
    @Transactional
    public ResponseEntity<DetalhesContaDto> cadastrarConta(@PathVariable("id") Long id, @RequestBody CadastrarContaDto contaDto, UriComponentsBuilder uriBuilder){
        try {
            var usuario = userRepository.getReferenceById(id);
            var conta = new Conta(contaDto, usuario);
            usuario.setConta(conta);
            contaRepository.save(conta);
            var uri = uriBuilder.path("conta/{id}").buildAndExpand(conta.getId()).toUri();
            return ResponseEntity.created(uri).body(new DetalhesContaDto(conta));
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }

    //Método para criação de Endereco abaixo

    @PostMapping("cadastrarEndereco/{id}")
    @Transactional
    public ResponseEntity<DetalhesEnderecoDto> cadastrarEndereco(@PathVariable("id") Long id, @RequestBody CadastrarEnderecoDto enderecoDto, UriComponentsBuilder uriBuilder){
        try {
            var usuario = userRepository.getReferenceById(id);
            var endereco = new Endereco(enderecoDto, usuario);
            usuario.setEndereco(endereco);
            enderecoRepository.save(endereco);
            var uri = uriBuilder.path("endereco/{id}").buildAndExpand(endereco.getId()).toUri();
            return ResponseEntity.created(uri).body(new DetalhesEnderecoDto(endereco));
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }


    //Método para criação de Telefone abaixo

    @PostMapping("cadastrarTelefone/{id}")
    @Transactional
    public ResponseEntity<DetalhesTelefoneDto> cadastrarTelefone(@PathVariable("id") Long id, @RequestBody CadastrarTelefoneDto telefoneDto, UriComponentsBuilder uriBuilder){
        try {
            var usuario = userRepository.getReferenceById(id);
            var telefone = new Telefone(telefoneDto);
            usuario.adicionarTelefone(telefone);
            telefoneRepository.save(telefone);
            var uri = uriBuilder.path("telefone/{id}").buildAndExpand(telefone.getId()).toUri();
            return ResponseEntity.created(uri).body(new DetalhesTelefoneDto(telefone));
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }

}
