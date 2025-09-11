package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.Repository.UserRepository;
import br.com.bankaccount.BankAccount.dto.ContaDto.AtualizarContaDto;
import br.com.bankaccount.BankAccount.dto.ContaDto.CadastrarContaDto;
import br.com.bankaccount.BankAccount.dto.ContaDto.DetalhesContaDto;
import br.com.bankaccount.BankAccount.dto.TransacaoDto.CadastrarTransacaoDto;
import br.com.bankaccount.BankAccount.dto.TransacaoDto.DetalhesTransacaoDto;
import br.com.bankaccount.BankAccount.Repository.ContaRepository;
import br.com.bankaccount.BankAccount.Repository.TransacaoRepository;
import br.com.bankaccount.BankAccount.model.Conta;
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

import java.math.BigDecimal;

@RestController
@RequestMapping("conta")
@Tag(name = "conta", description = "API do modelo Conta Bancária, métodos Atualizar, Buscar." +
        " Método para realizar transação com relacionamento Muitos para Muitos")
public class ContaController {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("cadastrarConta")
    @Transactional
    @Operation(summary = "Cadastrar Conta", description = "Cadastra a Conta com base no ID do usuário, já relacionando-os.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso",
                    content = @Content(schema = @Schema(implementation = DetalhesContaDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou formato Json incorreto"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> cadastrarConta(@RequestBody @Valid CadastrarContaDto contaDto, UriComponentsBuilder uriBuilder, Authentication authentication) {
        try {
            var user = (User) authentication.getPrincipal();
            var conta = new Conta(contaDto, user);
            user.setConta(conta);
            contaRepository.save(conta);
            var uri = uriBuilder.path("conta/{id}").buildAndExpand(conta.getId()).toUri();
            return ResponseEntity.created(uri).body(new DetalhesContaDto(conta));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Método de transação de Conta
    @PostMapping("realizarTransacao/{idDestinatario}")
    @Transactional
    @Operation(summary = "Operação de Transação x Conta", description = "Realiza uma transação bancária para outra conta com ID designado," +
            " junto com o json, realiza o cadastro conta x transação.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transação e Conta cadastrados com sucesso!",
                    content = @Content(schema = @Schema(implementation = DetalhesTransacaoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "ID's das contas não encontrados ou formato json incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> transacao(@PathVariable("idDestinatario") Long id, @RequestBody @Valid CadastrarTransacaoDto transacaoDto, UriComponentsBuilder uriBuilder, Authentication authentication) {
        try {
            var user = (User) authentication.getPrincipal();
            var conta1 = contaRepository.getReferenceById(user.getConta().getId());
            var conta2 = contaRepository.getReferenceById(id);

            if (!user.getId().equals(conta1.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode fazer uma transação sem ser sua a conta!");
            }

            if (conta1.getSaldo().compareTo(transacaoDto.valor()) < 0) {
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
        } catch (EntityNotFoundException e) {
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
    public ResponseEntity<?> atualizarConta(@PathVariable("id") Long id, @RequestBody @Valid AtualizarContaDto contaDto, Authentication authentication) {
        try {
            var conta = contaRepository.getReferenceById(id);

            var user = (User) authentication.getPrincipal();

            if (!user.getId().equals(conta.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode atualizar uma conta que não é sua!");
            }

            conta.atualizarConta(contaDto);
            contaRepository.save(conta);
            return ResponseEntity.ok(new DetalhesContaDto(conta));
        } catch (EmptyResultDataAccessException e) {
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
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id, Authentication authentication) {
        try {
            var conta = contaRepository.getReferenceById(id);

            var user = (User) authentication.getPrincipal();

            if (!user.getId().equals(conta.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode buscar uma conta que não é sua!");
            }

            return ResponseEntity.ok(new DetalhesContaDto(conta));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("valor-depositado")
    @Transactional
    @Operation(summary = "Depositar Dinheiro", description = "Deposita o dinheiro em sua conta bancária, somando no seu saldo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Valor Depositado com sucesso!",
                    content = @Content(schema = @Schema(implementation = DetalhesContaDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Token inválido ou sem permissão de acesso!"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> depositar(@RequestParam("valor-depositado") BigDecimal valor, Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        var conta = user.getConta();
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Valor precisa ser positivo!");
        }
        conta.depositar(conta, valor);
        contaRepository.save(conta);
        return ResponseEntity.ok(new DetalhesContaDto(conta));
    }

    @PostMapping("valor-sacado")
    @Transactional
    @Operation(summary = "Sacar Dinheiro", description = "Saca o dinheiro com base no seu valor bancário, subtraindo-o de sua conta.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Valor Sacado com sucesso!",
                    content = @Content(schema = @Schema(implementation = DetalhesContaDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Token inválido ou sem permissão de acesso."),
            @ApiResponse(responseCode = "406", description = "Valor inserido maior do que restante em conta."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<?> sacar(@RequestParam("valor-sacado") BigDecimal valor, Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        var conta = user.getConta();
        if (valor.compareTo(conta.getSaldo()) > 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Valor maior do que salado bancário.");
        } else if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Valor precisa ser positivo!");
        }
        conta.sacar(conta, valor);
        contaRepository.save(conta);
        return ResponseEntity.ok(new DetalhesContaDto(conta));
    }
}
