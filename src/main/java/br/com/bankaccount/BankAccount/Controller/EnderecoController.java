package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.Repository.UserRepository;
import br.com.bankaccount.BankAccount.dto.EnderecoDto.AtualizarEnderecoDto;
import br.com.bankaccount.BankAccount.dto.EnderecoDto.CadastrarEnderecoDto;
import br.com.bankaccount.BankAccount.dto.EnderecoDto.DetalhesEnderecoDto;
import br.com.bankaccount.BankAccount.Repository.EnderecoRepository;
import br.com.bankaccount.BankAccount.model.Endereco;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("endereco")
@Tag(name = "endereco", description = "API do modelo Endereço, métodos Cadastrar, Buscar, Atualizar, Excluir")
public class EnderecoController {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("cadastrarEndereco")
    @Transactional
    @Operation(summary = "Cadastrar Endereço", description = "Cadastra o Endereço, já relacionando-o com o Usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Endereço cadastrado com sucesso!",
                    content = @Content(schema = @Schema(implementation = DetalhesEnderecoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou formato Json incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> cadastrarEndereco(@RequestBody @Valid CadastrarEnderecoDto enderecoDto, UriComponentsBuilder uriBuilder, Authentication authentication) {
        try {
            var user = (User) authentication.getPrincipal();
            var endereco = new Endereco(enderecoDto, user);
            user.setEndereco(endereco);
            enderecoRepository.save(endereco);
            var uri = uriBuilder.path("endereco/{id}").buildAndExpand(endereco.getId()).toUri();
            return ResponseEntity.created(uri).body(new DetalhesEnderecoDto(endereco));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    @Operation(summary = "Buscar Endereço", description = "Busca o Endereço com base no ID selecionado na URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Endereço buscado com sucesso!",
                    content = @Content(schema = @Schema(implementation = DetalhesEnderecoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado!")
    })
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id, Authentication authentication) {
        try {
            var endereco = enderecoRepository.getReferenceById(id);

            var user = (User) authentication.getPrincipal();

            if (!user.getId().equals(endereco.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode buscar um endereço de outro usuário!");
            }

            return ResponseEntity.ok(new DetalhesEnderecoDto(endereco));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("atualizarEndereco/{id}")
    @Transactional
    @Operation(summary = "Atualizar Endereço", description = "Atualiza o Endereço com base no ID da URL e JSON enviado na URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Endereço atualizar com sucesso!",
                    content = @Content(schema = @Schema(implementation = DetalhesEnderecoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado para atualizar ou formato incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    })
    public ResponseEntity<?> atualizarEndereco(@PathVariable("id") Long id, @RequestBody @Valid AtualizarEnderecoDto enderecoDto, Authentication authentication) {
        try {
            var user = (User) authentication.getPrincipal();

            var endereco = enderecoRepository.getReferenceById(id);

            if (!user.getId().equals(endereco.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode atualizar um endereço que não é seu!");
            }

            endereco.atualizarEndereco(enderecoDto);
            return ResponseEntity.ok(new DetalhesEnderecoDto(endereco));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("excluirEndereco/{id}")
    @Transactional
    @Operation(summary = "Excluir Endereço", description = "Exclui o Endereço com base no ID da URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Endereço excluído com sucesso!"),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "ID do Endereço não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor!")
    })
    public ResponseEntity<?> excluirEndereco(@PathVariable("id") Long id, Authentication authentication) {
        try {
            var endereco = enderecoRepository.getReferenceById(id);

            var user = (User) authentication.getPrincipal();

            if (!user.getId().equals(endereco.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode excluir um endereço que não é seu!");
            }
            user.setEndereco(null);
            userRepository.save(user);
            enderecoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
