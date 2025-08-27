package br.com.bankaccount.BankAccount.model;

public enum UserType {
    PF("Pessoa Física"), PJ("Pessoa Jurídica");

    private String label;

    UserType(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
