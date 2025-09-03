package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.Dto.TransacaoDto.DetalhesTransacaoDto;
import br.com.bankaccount.BankAccount.Repository.TransacaoRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("transacao")
@Tag(name = "transacao", description = "API do modelo transação, métodos Buscar Todos e Buscar por Data")
public class TransacaoController {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @GetMapping("data-inicial/data-final")
    public ResponseEntity<Page<DetalhesTransacaoDto>> buscarPorData(@RequestParam("data-inicial") LocalDateTime dataInicial, @RequestParam("data-final") LocalDateTime dataFinal, Pageable pageable){
        var transacoes = transacaoRepository.findByDataTransacaoBetween(dataInicial, dataFinal, pageable).map(DetalhesTransacaoDto::new);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("listar")
    public ResponseEntity<List<DetalhesTransacaoDto>> listarTransacoes(Pageable pageable){
        var lista = transacaoRepository.findAll(pageable).stream().map(DetalhesTransacaoDto::new).toList();
        if(lista.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lista);
    }
}
