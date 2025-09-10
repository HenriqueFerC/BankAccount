package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.Repository.UserRepository;
import br.com.bankaccount.BankAccount.dto.TelefoneDto.AtualizarTelefoneDto;
import br.com.bankaccount.BankAccount.dto.TelefoneDto.CadastrarTelefoneDto;
import br.com.bankaccount.BankAccount.dto.TelefoneDto.DetalhesTelefoneDto;
import br.com.bankaccount.BankAccount.Repository.TelefoneRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("telefone")
@Tag(name = "telefone", description = "API do modelo Telefone, métodos Cadastrar, Atualizar e Excluir")
public class TelefoneController {

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("cadastrarTelefone")
    @Transactional
    @Operation(summary = "Cadastrar Telefone", description = "Cadastra o Telefone, já relacionando-o com o Usuário" +
            "(Um usuário pode ter vários telefones).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Telefone cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = DetalhesTelefoneDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou formato Json incorreto"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> cadastrarTelefone(@RequestBody @Valid CadastrarTelefoneDto telefoneDto, UriComponentsBuilder uriBuilder, Authentication authentication) {
        try {
            var user = (User) authentication.getPrincipal();
            var telefone = new Telefone(telefoneDto);
            telefone.setUser(user);
            user.adicionarTelefone(telefone);
            telefoneRepository.save(telefone);
            var uri = uriBuilder.path("telefone/{id}").buildAndExpand(telefone.getId()).toUri();
            return ResponseEntity.created(uri).body(new DetalhesTelefoneDto(telefone));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("atualizarTelefone/{id}")
    @Transactional
    @Operation(summary = "Atualizar Telefone", description = "Atualiza o Telefone com base no ID da URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Telefone atualizado com sucesso!",
                    content = @Content(schema = @Schema(implementation = DetalhesTelefoneDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Telefone não encontrado ou formato Json incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> atualizarTelefone(@PathVariable("id") Long id, @RequestBody @Valid AtualizarTelefoneDto telefoneDto, Authentication authentication) {
        try {
            var user = (User) authentication.getPrincipal();
            var telefone = telefoneRepository.getReferenceById(id);

            if (!user.getId().equals(telefone.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode atualizar o telefone de outro usuário!");
            }

            telefone.atualizarTelefone(telefoneDto);
            telefoneRepository.save(telefone);
            return ResponseEntity.ok(new DetalhesTelefoneDto(telefone));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("excluirTelefone/{id}")
    @Transactional
    @Operation(summary = "Excluir Telefone", description = "Exclui o telefone com base no ID da URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Telefone excluído com sucesso."),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Telefone não encontrado. ID incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> excluirTelefone(@PathVariable("id") Long id, Authentication authentication) {
        try {
            var user = (User) authentication.getPrincipal();
            var telefone = telefoneRepository.getReferenceById(id);

            if (!user.getId().equals(telefone.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode excluir o telefone de outra pessoa!");
            }
            user.removerTelefone(telefone);
            userRepository.save(user);
            telefoneRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
