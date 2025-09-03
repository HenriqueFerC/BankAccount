package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.Dto.EnderecoDto.AtualizarEnderecoDto;
import br.com.bankaccount.BankAccount.Dto.EnderecoDto.DetalhesEnderecoDto;
import br.com.bankaccount.BankAccount.Repository.EnderecoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("endereco")
public class EnderecoController {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @GetMapping("id")
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
    public ResponseEntity<DetalhesEnderecoDto> atualizarEndereco(@PathVariable("id") Long id, @RequestBody AtualizarEnderecoDto enderecoDto){
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
    public ResponseEntity<Void> excluirEndereco(@PathVariable("id") Long id){
        try {
            enderecoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }
}
