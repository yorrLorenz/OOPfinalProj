package Classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import Helpers.ErrorHandler;

public class BankAccount {

	private JSONArray expenses = null, deposits = null;
	private File file = new File("src/Data/bankaccounts.json");
	
	public BankAccount(String user, String pass) {
		getLists(user, pass);
	}
	
	
	private void getLists(String username, String password) {
			 
			JSONArray arr = Data.getData(file, null);
          
           if(arr.length() > 0) {
        	   for(int i = 0; i < arr.length(); i++) {
        		   JSONObject obj =  arr.getJSONObject(i);
        		   String name = obj.getString("username");
        		   String pass = obj.getString("password");
        		   if(name.contains(username) && pass.contains(password)) {
        			   expenses = new JSONArray(obj.getJSONArray("expenses"));
        			   deposits = new JSONArray(obj.getJSONArray("deposits"));
        		   }
        	   }
           }
	}




	public JSONArray getExpenses() {
		return expenses;
	}




	public void setExpenses(JSONArray expenses) {
		this.expenses = expenses;
	}




	public JSONArray getDeposits() {
		return deposits;
	}




	public void setDeposits(JSONArray deposits) {
		this.deposits = deposits;
	}
}
