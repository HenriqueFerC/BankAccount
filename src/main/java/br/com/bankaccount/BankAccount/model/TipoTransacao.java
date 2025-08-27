package br.com.bankaccount.BankAccount.model;

public enum TipoTransacao {
    SAQUE("Saque"), DEPÓSITO("Depósito"), TRANSFERÊNCIA("Transferência"), PIX("Pix"), CARTÃO("Cartão");

    private String label;

    TipoTransacao(String label){
        this.label = label;
    }

    private String getLabel(){
        return label;
    }
}
