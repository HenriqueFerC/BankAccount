package br.com.bankaccount.BankAccount.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType {
    PF("Pessoa Física"), PJ("Pessoa Jurídica");

    private String label;

    UserType(String label){
        this.label = label;
    }

    @JsonValue
    public String getLabel(){
        return label;
    }
}
