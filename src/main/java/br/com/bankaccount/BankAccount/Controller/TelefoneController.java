package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.Dto.TelefoneDto.AtualizarTelefoneDto;
import br.com.bankaccount.BankAccount.Dto.TelefoneDto.DetalhesTelefoneDto;
import br.com.bankaccount.BankAccount.Repository.TelefoneRepository;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Atualizar Telefone", description = "")
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
    public ResponseEntity<Void> excluirTelefone(@PathVariable("id") Long id){
        try {
            telefoneRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }


}
