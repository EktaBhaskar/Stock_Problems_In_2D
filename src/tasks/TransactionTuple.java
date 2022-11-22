package tasks;

/*
 * class for the use-case of returning a tuple of profit and list of all transactions
 */
public class TransactionTuple {
    int profit;
    TransactionL transaction_list = new TransactionL();

    public TransactionTuple(int profit, TransactionL transaction_list){
        this.profit = profit;
        this.transaction_list = transaction_list;
    }

    public int getProfit(){
        return this.profit;
    }

    public void setProfit(int profit){
        this.profit = profit;
    }

    public TransactionL getTransactionList(){
        return this.transaction_list;
    }

    public void setTransactionList(TransactionL transaction_list){
        this.transaction_list = transaction_list;
    }

    
}
