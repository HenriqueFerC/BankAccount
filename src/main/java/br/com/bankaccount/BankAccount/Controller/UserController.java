package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.dto.ContaDto.CadastrarContaDto;
import br.com.bankaccount.BankAccount.dto.ContaDto.DetalhesContaDto;
import br.com.bankaccount.BankAccount.dto.EnderecoDto.CadastrarEnderecoDto;
import br.com.bankaccount.BankAccount.dto.EnderecoDto.DetalhesEnderecoDto;
import br.com.bankaccount.BankAccount.dto.TelefoneDto.CadastrarTelefoneDto;
import br.com.bankaccount.BankAccount.dto.TelefoneDto.DetalhesTelefoneDto;
import br.com.bankaccount.BankAccount.dto.UserDto.AtualizarUserDto;
import br.com.bankaccount.BankAccount.dto.UserDto.CadastrarUserDto;
import br.com.bankaccount.BankAccount.dto.UserDto.DetalhesUserDto;
import br.com.bankaccount.BankAccount.Repository.ContaRepository;
import br.com.bankaccount.BankAccount.Repository.EnderecoRepository;
import br.com.bankaccount.BankAccount.Repository.TelefoneRepository;
import br.com.bankaccount.BankAccount.Repository.UserRepository;
import br.com.bankaccount.BankAccount.model.Conta;
import br.com.bankaccount.BankAccount.model.Endereco;
import br.com.bankaccount.BankAccount.model.Telefone;
import br.com.bankaccount.BankAccount.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("cadastrar")
    @Transactional
    @Operation(summary = "Cadastrar Usuário", description = "Cadastra o Usuário com base no JSON enviado para a URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso!",
            content = @Content(schema = @Schema(implementation = DetalhesUserDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Formato do Json incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<DetalhesUserDto> cadastrar(@RequestBody @Valid CadastrarUserDto userDto, UriComponentsBuilder uriBuilder){
        var usuario = new User(userDto.nome(), passwordEncoder.encode(userDto.password()), userDto.userType(), userDto.cpfCnpj());
        userRepository.save(usuario);
        var uri = uriBuilder.path("user/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalhesUserDto(usuario));
    }



    @GetMapping("{id}")
    @Operation(summary = "Buscar Usuário por ID", description = "Busca o usuário com base no ID da URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário buscado com sucesso!",
            content = @Content(schema = @Schema(implementation = DetalhesUserDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado. ID incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id, Authentication authentication){
        try {
            var usuario = (User) authentication.getPrincipal();
            if(!usuario.getId().equals(id)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode acessar outro usuário!");
            }
            return ResponseEntity.ok(new DetalhesUserDto(usuario));
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("listar")
    @Operation(summary = "Listar Usuários", description = "Lista os usuários do banco de dados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de Usuários buscada com sucesso!",
            content = @Content(schema = @Schema(implementation = DetalhesUserDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário foi encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<List<DetalhesUserDto>> listarUsuarios(Pageable pageable){
        var lista = userRepository.findAll(pageable).stream().map(DetalhesUserDto::new).toList();
        if(lista.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PutMapping("atualizar/{id}")
    @Transactional
    @Operation(summary = "Atualizar Usuário", description = "Atualiza o Usuário com base no ID da URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso!",
            content = @Content(schema = @Schema(implementation = DetalhesUserDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou formato Json incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> atualizar(@PathVariable("id") Long id, @RequestBody @Valid AtualizarUserDto userDto, Authentication authentication){
        try {
            var usuario = (User) authentication.getPrincipal();
            if(!usuario.getId().equals(id)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode atualizar outro usuário!");
            }
            usuario.atualizarUser(userDto.nome(), passwordEncoder.encode(userDto.password()), userDto.userType(), userDto.cpfCnpj());
            userRepository.save(usuario);
            return ResponseEntity.ok(new DetalhesUserDto(usuario));
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("excluir/{id}")
    @Transactional
    @Operation(summary = "Excluir Usuário", description = "Exclui o Usuário e todos seus relacionados (Cascade) com base no ID da URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso!"),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado. ID incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> excluirUsuario(@PathVariable("id") Long id, Authentication authentication){
        try {
            var usuario = (User) authentication.getPrincipal();
            if(!usuario.getId().equals(id)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode excluir outro usuário!");
            }
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    //Método para criação de Conta Corrente abaixo

    @PostMapping("cadastrarConta/{id}")
    @Transactional
    @Operation(summary = "Cadastrar Conta", description = "Cadastra a Conta com base no ID do usuário, já relacionando-os.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso",
            content = @Content(schema = @Schema(implementation = DetalhesContaDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou formato Json incorreto"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> cadastrarConta(@PathVariable("id") Long id, @RequestBody @Valid CadastrarContaDto contaDto, UriComponentsBuilder uriBuilder, Authentication authentication){
        try {
            var user = (User) authentication.getPrincipal();
            if(!user.getId().equals(id)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode cadastrar uma conta para outro usuário!");
            }
            var conta = new Conta(contaDto, user);
            user.setConta(conta);
            contaRepository.save(conta);
            var uri = uriBuilder.path("conta/{id}").buildAndExpand(conta.getId()).toUri();
            return ResponseEntity.created(uri).body(new DetalhesContaDto(conta));
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    //Método para criação de Endereco abaixo

    @PostMapping("cadastrarEndereco/{id}")
    @Transactional
    @Operation(summary = "Cadastrar Endereço", description = "Cadastra o Endereço com base no ID do Usuário, já relacionando-os.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Endereço cadastrado com sucesso!",
            content = @Content(schema = @Schema(implementation = DetalhesEnderecoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou formato Json incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> cadastrarEndereco(@PathVariable("id") Long id, @RequestBody @Valid CadastrarEnderecoDto enderecoDto, UriComponentsBuilder uriBuilder, Authentication authentication){
        try {
            var user = (User) authentication.getPrincipal();
            if(!user.getId().equals(id)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode cadastrar um endereço para outro usuário!");
            }
            var endereco = new Endereco(enderecoDto, user);
            user.setEndereco(endereco);
            enderecoRepository.save(endereco);
            var uri = uriBuilder.path("endereco/{id}").buildAndExpand(endereco.getId()).toUri();
            return ResponseEntity.created(uri).body(new DetalhesEnderecoDto(endereco));
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }


    //Método para criação de Telefone abaixo

    @PostMapping("cadastrarTelefone/{id}")
    @Transactional
    @Operation(summary = "Cadastrar Telefone", description = "Cadastra o Telefone com base no ID do Usuário, já relacionando-os" +
            "(Um usuário pode ter vários telefones).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Telefone cadastrado com sucesso",
            content = @Content(schema = @Schema(implementation = DetalhesTelefoneDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou formato Json incorreto"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> cadastrarTelefone(@PathVariable("id") Long id, @RequestBody @Valid CadastrarTelefoneDto telefoneDto, UriComponentsBuilder uriBuilder, Authentication authentication){
        try {
            var user = (User) authentication.getPrincipal();
            if(!user.getId().equals(id)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode cadastrar o telefone de outro usuário!");
            }
            var telefone = new Telefone(telefoneDto);
            telefone.setUser(user);
            telefoneRepository.save(telefone);
            user.adicionarTelefone(telefone);
            var uri = uriBuilder.path("telefone/{id}").buildAndExpand(telefone.getId()).toUri();
            return ResponseEntity.created(uri).body(new DetalhesTelefoneDto(telefone));
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

}
