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
@Tag(name = "user", description = "API do modelo Usuário, métodos de Buscar, Cadastrar, Atualizar, Listar, Excluir.")
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
    public ResponseEntity<DetalhesUserDto> cadastrar(@RequestBody @Valid CadastrarUserDto userDto, UriComponentsBuilder uriBuilder) {
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
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id, Authentication authentication) {
        try {
            var usuario = (User) authentication.getPrincipal();
            if (!usuario.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode acessar outro usuário!");
            }
            return ResponseEntity.ok(new DetalhesUserDto(usuario));
        } catch (EmptyResultDataAccessException e) {
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
    public ResponseEntity<List<DetalhesUserDto>> listarUsuarios(Pageable pageable) {
        var lista = userRepository.findAll(pageable).stream().map(DetalhesUserDto::new).toList();
        if (lista.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PutMapping("atualizar")
    @Transactional
    @Operation(summary = "Atualizar Usuário", description = "Atualiza o Usuário com base no ID da URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso!",
                    content = @Content(schema = @Schema(implementation = DetalhesUserDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou formato Json incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> atualizar(@RequestBody @Valid AtualizarUserDto userDto, Authentication authentication) {
        try {
            var usuario = (User) authentication.getPrincipal();
            usuario.atualizarUser(userDto.nome(), passwordEncoder.encode(userDto.password()), userDto.userType(), userDto.cpfCnpj());
            userRepository.save(usuario);
            return ResponseEntity.ok(new DetalhesUserDto(usuario));
        } catch (EntityNotFoundException e) {
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
    public ResponseEntity<?> excluirUsuario(@PathVariable("id") Long id, Authentication authentication) {
        try {
            var usuario = (User) authentication.getPrincipal();
            if (!usuario.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode excluir outro usuário!");
            }
            userRepository.delete(usuario);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
