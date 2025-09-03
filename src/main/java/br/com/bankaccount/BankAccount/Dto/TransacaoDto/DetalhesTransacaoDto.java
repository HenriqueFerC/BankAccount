package br.com.bankaccount.BankAccount.Dto.TransacaoDto;

import br.com.bankaccount.BankAccount.Dto.ContaDto.DetalhesContaDto;
import br.com.bankaccount.BankAccount.model.TipoTransacao;
import br.com.bankaccount.BankAccount.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DetalhesTransacaoDto(TipoTransacao tipoTransacao, LocalDateTime dataTransacao, BigDecimal valor) {
    public DetalhesTransacaoDto(Transacao transacao){
        this(transacao.getTipoTransacao(), transacao.getDataTransacao(), transacao.getValor());
    }
}
