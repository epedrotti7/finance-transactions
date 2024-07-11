package com.example.finance_transactions.dto;

public class Balance {
    private double availableBalance;
    private double waitingFunds;

    public Balance(double availableBalance, double waitingFunds) {
        this.availableBalance = availableBalance;
        this.waitingFunds = waitingFunds;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public double getWaitingFunds() {
        return waitingFunds;
    }

    public void setWaitingFunds(double waitingFunds) {
        this.waitingFunds = waitingFunds;
    }
}