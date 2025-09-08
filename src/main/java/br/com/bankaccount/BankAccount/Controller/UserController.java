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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
            content = @Content(schema = @Schema(implementation = DetalhesContaDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Formato do Json incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<DetalhesUserDto> cadastrar(@RequestBody CadastrarUserDto userDto, UriComponentsBuilder uriBuilder){
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
    public ResponseEntity<DetalhesUserDto> buscarPorId(@PathVariable("id") Long id){
        try {
            var usuario = userRepository.getReferenceById(id);
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
    @Operation(summary = "Excluir Usuário", description = "Exclui o Usuário e todos seus relacionados (Cascade) com base no ID da URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso!"),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado. ID incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
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
    @Operation(summary = "Cadastrar Conta", description = "Cadastra a Conta com base no ID do usuário, já relacionando-os.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso",
            content = @Content(schema = @Schema(implementation = DetalhesContaDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou formato Json incorreto"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
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
    @Operation(summary = "Cadastrar Endereço", description = "Cadastra o Endereço com base no ID do Usuário, já relacionando-os.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Endereço cadastrado com sucesso!",
            content = @Content(schema = @Schema(implementation = DetalhesEnderecoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou formato Json incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
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
    @Operation(summary = "Cadastrar Telefone", description = "Cadastra o Telefone com base no ID do Usuário, já relacionando-os" +
            "(Um usuário pode ter vários telefones).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Telefone cadastrado com sucesso",
            content = @Content(schema = @Schema(implementation = DetalhesTelefoneDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou formato Json incorreto"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
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
