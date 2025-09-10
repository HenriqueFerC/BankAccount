package br.com.bankaccount.BankAccount.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoConta {
    CC("Conta Corrente"),
    CP("Conta Poupança"),
    CS("Conta Salário");

    private String label;

    TipoConta(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
