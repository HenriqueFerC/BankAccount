package br.com.bankaccount.BankAccount.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoTransacao {
    SAQUE("Saque"), DEPÓSITO("Depósito"), TRANSFERÊNCIA("Transferência"), PIX("Pix"), CARTÃO("Cartão");

    private String label;

    TipoTransacao(String label){
        this.label = label;
    }

    @JsonValue
    private String getLabel(){
        return label;
    }
}
