package tasks;

import java.util.*;

/*class to have all operations on transaction list
   Need for this class arises from defining {static TransactionL[][][] dp_buy_txn, static TransactionL[][][] dp_sell_txn} for memoization
*/
public class TransactionL {
    List<Transaction> transaction_list = new ArrayList<>();

    public TransactionL(){
        this.transaction_list = new ArrayList<>();
    }

    public TransactionL(List<Transaction> transaction_list){
        this.transaction_list = transaction_list;
    }

    public List<Transaction> getTransactionList_L(){
        return this.transaction_list;
    }

    public TransactionL getTransaction_L(){
        return new TransactionL(transaction_list);
    }

    public void setTransactionList(List<Transaction> transaction_list){
        this.transaction_list = transaction_list;
    }

    public TransactionL add(Transaction transaction){
        this.transaction_list.add(transaction);
        return new TransactionL(this.transaction_list);
    }

    public TransactionL add_transactionL(List<Transaction> transactions){
        this.transaction_list.addAll(transactions);
        return new TransactionL(new ArrayList<>(this.transaction_list));
    }
    
}
