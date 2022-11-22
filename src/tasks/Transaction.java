package tasks;

public class Transaction {
    int stock_number;
    int buy_day;
    int sell_day;
    int profit;

    public Transaction(int stock_number, int buy_day, int sell_day, int profit){
        this.stock_number = stock_number;
        this.buy_day = buy_day;
        this.sell_day = sell_day;
        this.profit = profit;
    }

    public int getStockNumber(){
        return this.stock_number;
    }

    public int getBuyDay(){
        return this.buy_day;
    }
    
    public int getSellDay(){
        return this.sell_day;
    }
    
    public int getProfit(){
        return this.profit;
    }

    public void setStockNumber(int stock_number){
        this.stock_number = stock_number;
    }

    public void setBuyDay(int buy_day){
        this.buy_day = buy_day;
    }
    
    public void setSellDay(int sell_day){
        this.sell_day = sell_day;
    }
    
    public void setProfit(int profit){
        this.profit = profit;
    }
    
    
}
