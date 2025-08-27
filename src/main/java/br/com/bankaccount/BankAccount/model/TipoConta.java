package br.com.bankaccount.BankAccount.model;

public enum TipoConta {
    CC("Conta Corrente"),
    CP("Conta Poupança"),
    CS("Conta Salário");

    private String label;

    TipoConta(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
