package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.dto.EnderecoDto.AtualizarEnderecoDto;
import br.com.bankaccount.BankAccount.dto.EnderecoDto.DetalhesEnderecoDto;
import br.com.bankaccount.BankAccount.Repository.EnderecoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("endereco")
@Tag(name = "endereco", description = "API do modelo Endereço, métodos Buscar, Atualizar, Excluir")
public class EnderecoController {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @GetMapping("id")
    @Operation(summary = "Buscar Endereço", description = "Busca o Endereço com base no ID selecionado na URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Endereço buscado com sucesso!",
            content = @Content(schema = @Schema(implementation = DetalhesEnderecoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Não autorizado ou token invalido."),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado!")
    })
    public ResponseEntity<DetalhesEnderecoDto> buscarPorId(@PathVariable("id") Long id){
        try {
            var endereco = enderecoRepository.getReferenceById(id);
            return ResponseEntity.ok(new DetalhesEnderecoDto(endereco));
        } catch (EmptyResultDataAccessException e){
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
    public ResponseEntity<DetalhesEnderecoDto> atualizarEndereco(@PathVariable("id") Long id, @RequestBody @Valid AtualizarEnderecoDto enderecoDto){
        try {
            var endereco = enderecoRepository.getReferenceById(id);
            endereco.atualizarEndereco(enderecoDto);
            return ResponseEntity.ok(new DetalhesEnderecoDto(endereco));
        } catch (EmptyResultDataAccessException e){
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
    public ResponseEntity<Void> excluirEndereco(@PathVariable("id") Long id){
        try {
            var endereco = enderecoRepository.getReferenceById(id);
            var usuario = endereco.getUser();
            usuario.setEndereco(null);
            enderecoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }
}
