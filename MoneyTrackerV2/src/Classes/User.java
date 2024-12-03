package Classes;

import org.json.JSONArray;

public class User {
    private String username;
    private double balance;
    private JSONArray transactions;
    private JSONArray expenses;

    public User(String username, double balance, JSONArray transactions, JSONArray expenses) {
        this.username = username;
        this.balance = balance;
        this.transactions = transactions;
        this.expenses = expenses;
    }

    public String getUsername() {
        return username;
    }

    public double getBalance() {
        return balance;
    }

    public JSONArray getTransactions() {
        return transactions;
    }

    public JSONArray getExpenses() {
        return expenses;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setTransactions(JSONArray transactions) {
        this.transactions = transactions;
    }

    public void setExpenses(JSONArray expenses) {
        this.expenses = expenses;
    }
}
