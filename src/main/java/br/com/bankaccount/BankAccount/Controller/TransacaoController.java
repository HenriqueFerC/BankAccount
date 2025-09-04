package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.Dto.TransacaoDto.DetalhesTransacaoDto;
import br.com.bankaccount.BankAccount.Repository.TransacaoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("transacao")
@Tag(name = "transacao", description = "API do modelo transação, métodos Buscar Todos e Buscar por Data")
public class TransacaoController {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @GetMapping("data-inicial/data-final")
    @Operation(summary = "Buscar Transação por Data", description = "Busca as transações com base no intervalo das datas da URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transações buscadas com sucesso!",
            content = @Content(schema = @Schema(implementation = DetalhesTransacaoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Transações não encontradas!"),
            @ApiResponse(responseCode = "500", description = "Erro de Servidor.")
    })
    public ResponseEntity<Page<DetalhesTransacaoDto>> buscarPorData(@RequestParam("data-inicial") LocalDateTime dataInicial, @RequestParam("data-final") LocalDateTime dataFinal, Pageable pageable){
        var transacoes = transacaoRepository.findByDataTransacaoBetween(dataInicial, dataFinal, pageable).map(DetalhesTransacaoDto::new);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("listar")
    @Operation(summary = "Buscar Transações", description = "Busca todas as transações do banco de dados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de Transações buscadas com sucesso!",
            content = @Content(schema = @Schema(implementation = DetalhesTransacaoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Nenhuma transação foi encontrada."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<List<DetalhesTransacaoDto>> listarTransacoes(Pageable pageable){
        var lista = transacaoRepository.findAll(pageable).stream().map(DetalhesTransacaoDto::new).toList();
        if(lista.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("id")
    @Operation(summary = "Buscar Transação por ID", description = "Busca uma transação com base no ID da URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transação buscada com sucesso!",
            content = @Content(schema = @Schema(implementation = DetalhesTransacaoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada. ID incorreto."),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    })
    public ResponseEntity<DetalhesTransacaoDto> buscarPorId(@PathVariable("id") Long id){
        try {
            var transacao = transacaoRepository.getReferenceById(id);
            return ResponseEntity.ok(new DetalhesTransacaoDto(transacao));
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }
}
