package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.dto.ContaDto.AtualizarContaDto;
import br.com.bankaccount.BankAccount.dto.ContaDto.DetalhesContaDto;
import br.com.bankaccount.BankAccount.dto.TransacaoDto.CadastrarTransacaoDto;
import br.com.bankaccount.BankAccount.dto.TransacaoDto.DetalhesTransacaoDto;
import br.com.bankaccount.BankAccount.Repository.ContaRepository;
import br.com.bankaccount.BankAccount.Repository.TransacaoRepository;
import br.com.bankaccount.BankAccount.model.Transacao;
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
@RequestMapping("conta")
@Tag(name = "conta", description = "API do modelo Conta Bancária, métodos Atualizar, Buscar." +
        " Método para realizar transação com relacionamento Muitos para Muitos")
public class ContaController {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    //Método de transação de Conta
    @PostMapping("realizarTransacao/{idRemetente}/{idDestinatario}")
    @Transactional
    @Operation(summary = "Operação de Transação x Conta", description = "Usando o ID da conta pagante e ID da conta recebedora," +
            " junto com o json, realiza o cadastro conta x transação.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transação e Conta cadastrados com sucesso!",
            content = @Content(schema = @Schema(implementation = DetalhesTransacaoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "ID's das contas não encontrados ou formato json incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> transacao(@PathVariable("idRemetente") Long id, @PathVariable("idDestinatario") Long id2, @RequestBody @Valid CadastrarTransacaoDto transacaoDto, UriComponentsBuilder uriBuilder, Authentication authentication){
        try {
            var conta1 = contaRepository.getReferenceById(id);
            var conta2 = contaRepository.getReferenceById(id2);

            var user = (User) authentication.getPrincipal();
            if(!user.getId().equals(conta1.getUser().getId())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode fazer uma transação sem ser sua a conta!");
            }

            if(conta1.getSaldo().compareTo(transacaoDto.valor()) < 0){
                return ResponseEntity.badRequest().build();
            }

            conta1.transacao(conta1, conta2, transacaoDto.valor());
            var transacao = new Transacao(transacaoDto);
            transacao.adicionarConta(conta1);
            transacao.adicionarConta(conta2);
            conta1.adicionarTransacao(transacao);
            conta2.adicionarTransacao(transacao);
            transacaoRepository.save(transacao);
            var uri = uriBuilder.path("transacao/{id}").buildAndExpand(transacao.getId()).toUri();
            return ResponseEntity.created(uri).body(new DetalhesTransacaoDto(transacao));
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("atualizarConta/{id}")
    @Transactional
    @Operation(summary = "Atualizar Conta", description = "Atualiza a Conta com base no ID da URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conta atualizada com sucesso!",
            content = @Content(schema = @Schema(implementation = DetalhesContaDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada ou formato json incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> atualizarConta(@PathVariable("id") Long id, @RequestBody @Valid AtualizarContaDto contaDto, Authentication authentication){
        try {
            var conta = contaRepository.getReferenceById(id);

            var user = (User) authentication.getPrincipal();

            if(!user.getId().equals(conta.getUser().getId())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode atualizar uma conta que não é sua!");
            }

            conta.atualizarConta(contaDto);
            contaRepository.save(conta);
            return ResponseEntity.ok(new DetalhesContaDto(conta));
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    @Operation(summary = "Buscar Conta", description = "Busca a conta com base no ID da URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta buscada com sucesso!",
            content = @Content(schema = @Schema(implementation = DetalhesContaDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Conta não foi encontrada, ID incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id, Authentication authentication){
        try {
            var conta = contaRepository.getReferenceById(id);

            var user = (User) authentication.getPrincipal();

            if(!user.getId().equals(conta.getUser().getId())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode buscar uma conta que não é sua!");
            }

            return ResponseEntity.ok(new DetalhesContaDto(conta));
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }

}
