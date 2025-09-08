package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.dto.TelefoneDto.AtualizarTelefoneDto;
import br.com.bankaccount.BankAccount.dto.TelefoneDto.DetalhesTelefoneDto;
import br.com.bankaccount.BankAccount.Repository.TelefoneRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("telefone")
@Tag(name = "telefone", description = "API do modelo Telefone, métodos Atualizar e Excluir")
public class TelefoneController {

    @Autowired
    private TelefoneRepository telefoneRepository;

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
    public ResponseEntity<DetalhesTelefoneDto> atualizarTelefone(@PathVariable("id") Long id, AtualizarTelefoneDto telefoneDto){
        try {
            var telefone = telefoneRepository.getReferenceById(id);
            telefone.atualizarTelefone(telefoneDto);
            telefoneRepository.save(telefone);
            return ResponseEntity.ok(new DetalhesTelefoneDto(telefone));
        } catch (EmptyResultDataAccessException e){
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
    public ResponseEntity<Void> excluirTelefone(@PathVariable("id") Long id){
        try {
            telefoneRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }


}
