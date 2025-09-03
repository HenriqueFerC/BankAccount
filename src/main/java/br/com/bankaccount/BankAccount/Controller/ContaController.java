package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.Dto.ContaDto.AtualizarContaDto;
import br.com.bankaccount.BankAccount.Dto.ContaDto.DetalhesContaDto;
import br.com.bankaccount.BankAccount.Dto.TransacaoDto.CadastrarTransacaoDto;
import br.com.bankaccount.BankAccount.Dto.TransacaoDto.DetalhesTransacaoDto;
import br.com.bankaccount.BankAccount.Repository.ContaRepository;
import br.com.bankaccount.BankAccount.Repository.TransacaoRepository;
import br.com.bankaccount.BankAccount.model.Transacao;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<DetalhesTransacaoDto> transacao(@PathVariable("idRemetente") Long id, @PathVariable("idDestinatario") Long id2, @RequestBody CadastrarTransacaoDto transacaoDto, UriComponentsBuilder uriBuilder){
        try {
            var conta1 = contaRepository.getReferenceById(id);
            var conta2 = contaRepository.getReferenceById(id2);

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
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("atualizarConta/{id}")
    @Transactional
    public ResponseEntity<DetalhesContaDto> atualizarConta(@PathVariable("id") Long id, @RequestBody AtualizarContaDto contaDto){
        try {
            var conta = contaRepository.getReferenceById(id);
            conta.atualizarConta(contaDto);
            contaRepository.save(conta);
            return ResponseEntity.ok(new DetalhesContaDto(conta));
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("id")
    public ResponseEntity<DetalhesContaDto> buscarPorId(@PathVariable("id") Long id){
        try {
            var conta = contaRepository.getReferenceById(id);
            return ResponseEntity.ok(new DetalhesContaDto(conta));
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }

}
