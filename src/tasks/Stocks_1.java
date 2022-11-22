// Java implementation of the approach
package tasks;
import java.util.*;

public class Stocks_1{

    /* BruteForce Algorithm
     * @return maximum profit transaction
     */

    public static Transaction task1(int price_at_day[][]){
        int m = price_at_day.length;
        int n = price_at_day[0].length;
        // Initialise the max_profit
        int max_profit = 0;
        int fin_stock = -1;
        int fin_buy = -1;
        int fin_sell = -1;

        // looping through m-stocks whose price keep changing on nth day
        for(int i = 0 ; i < m ; i++){
            //Each stock has different prices on different days, we're checking pairs of days which will give us maximum profit
            for(int j = 0 ; j < n ; j++){
                //stock cannot be sold before it's been bought, so always keping track of next day 
                for(int k = j+1; k < n ; k++){
                    //profit will be thee only if price on day it is sold is greater than bought price
                    if(price_at_day[i][k] > price_at_day[i][j]){
                        int cur_diff = price_at_day[i][k] - price_at_day[i][j];
                        //keep changing the value of max_profit if we see a greater profit
                        if(cur_diff > max_profit){
                            max_profit = cur_diff;
                            fin_stock = i;
                            fin_buy = j;
                            fin_sell = k;

                        }
                    }
                }
            }
        }
        // System.out.println("Max_Profit:"+ max_profit);
        // System.out.println("Price Bought:-" + price_at_day[fin_stock][fin_buy]);
        // System.out.println("Price Sold:-" + price_at_day[fin_stock][fin_sell]);
        return  new Transaction(fin_stock, fin_buy, fin_sell, max_profit);

    }

    /*
     * 
     * As Per Greedy:-
     * for each price we need to find the profit we could get by selling at that price. In order to do that we have to keep track of the lowest price that we have seen so far. If we find a lower price at any step, we'll update it.
     * We also want to keep track of the biggest profit which we have seen from all the previous prices. That way we have something to compare the current price's profit margin to. If it's bigger than the biggest profit, then we have a new biggest profit to use as a reference.
     * This way we're constantly keeping track of 'the best answer so far', so when we finish our iteration we can just return that answer.
     */

    public static Transaction task2(int[][] price_at_day){
        int m = price_at_day.length;
        int n = price_at_day[0].length;

        //initialising the minimum value and answer to very minimum
        int max_profit = Integer.MIN_VALUE;
        int mini = Integer.MAX_VALUE ;
        //to store smallest price found for stock i
        int[] fin_buy = new int[m];
        //to store largest price found for stock i
        int[] fin_sell = new int[m];
        //to store buying and selling of which stock will give us maximum profit
        int fin_stock = -1;
        

        for(int i = 0 ; i < m ; i++){
            mini = Integer.MAX_VALUE;
            int cur_profit = Integer.MIN_VALUE;
            for(int j = 0 ; j < n ; j++){
                //keeping track of minimum price so far
                if(price_at_day[i][j] < mini){
                    mini = price_at_day[i][j];
                    fin_buy[i] = j;
                //keeping track of maximum profit so far
                }else if(price_at_day[i][j] -  mini > cur_profit){
                    cur_profit = price_at_day[i][j] - mini;
                    if(cur_profit > max_profit){
                        fin_stock = i;
                        max_profit = cur_profit;
                    }
                    fin_sell[i] = j;
                }
            }
        }
        max_profit = max_profit < 0 ? 0 : max_profit;
        // System.out.println("Max_Profit:-" + max_profit);
        // System.out.println("Price Bought:-" + price_at_day[fin_stock][fin_buy[fin_stock]]);
        // System.out.println("Price Sold:-" + price_at_day[fin_stock][fin_sell[fin_stock]]);
        
        return new Transaction(fin_stock, fin_buy[fin_stock], fin_sell[fin_stock], max_profit);
    }

    
    /*
     * Top -Down DP:- Recusrion with Memoization
     * Base logic is same :- for each stock we're finding maximum profit transaction and comparing maximum values of all stocks to get final max_profit transaction
     */
    public static Transaction task_3a(int[][] price_at_day){
        int m = price_at_day.length;
        int n = price_at_day[0].length;
        // int max_profit = 0;
        Transaction max_transaction = new Transaction(-1, -1, -1, -1);
        Transaction[][] stock_wise_transaction = new Transaction[m][n];
        for(int stock = 0 ; stock < m ; stock++){
            max_transaction = Util.compare(max_transaction, task_3a_helper1(price_at_day, stock_wise_transaction, stock));
        }
        return new Transaction(max_transaction.getStockNumber(), max_transaction.getBuyDay(), max_transaction.getSellDay(), max_transaction.getProfit());
    }

    /*
     * Helper method to find maximum profit transaction for each stock which is passed to main method to compare it again
     */
    public static Transaction task_3a_helper1(int[][] price_at_day, Transaction[][] stock_wise_transaction, int stock){
        
        task_3a_helper2(price_at_day, stock_wise_transaction, stock, price_at_day[0].length - 1);

        Transaction max_for_stock = stock_wise_transaction[stock][0];

        // calculating maximum profit transaction from each stock
        for(int day = 1 ; day < price_at_day[0].length ; day++){
            max_for_stock = Util.compare(max_for_stock, stock_wise_transaction[stock][day]);
        }
        return max_for_stock;
    }

    /*
     * Helper method to fill memo table for each stock
     */
    public static Transaction task_3a_helper2(int[][] price_at_day, Transaction[][] stock_wise_transaction, int stock, int day){
        // if day == 0, profit will be 0, and we can say that buy and sell can be made on day 0
        if(day == 0){
            stock_wise_transaction[stock][day] = new Transaction(stock, day, day, 0);
            return stock_wise_transaction[stock][day];
        }

        // if we have already encountered that case before, return it's stored value
        if(stock_wise_transaction[stock][day] != null){
            return stock_wise_transaction[stock][day];
        }

        // To initialise current day transaction
        Transaction cur_day_transaction = new Transaction(stock, day, day, 0);
        // calculating last day transaction by calling recursion for (day-1)
        Transaction last_day_transaction = task_3a_helper2(price_at_day, stock_wise_transaction, stock, day-1);
        // cur_day profit will be profit till day-1 + if we buy on day-1 and sell on day
        cur_day_transaction.profit = last_day_transaction.profit + (price_at_day[stock][day] - price_at_day[stock][day-1]);
        // buy-day fro cur_transaction will get changed to last_day buy day
        cur_day_transaction.buy_day = stock_wise_transaction[stock][day-1].buy_day;

        // If we do not make any profit by selling on cur_day, we will store profit on that day as 0 and hence buy_day and sell-day will be equal
        stock_wise_transaction[stock][day] = Util.compare(cur_day_transaction, new Transaction(stock, day, day, 0));
        // returning profit that can be made by selling/skipping on cur_day for stock 
        return stock_wise_transaction[stock][day];

    }

    /**
     * Using bottom-Up Dynammic Programming
     * The maximum profit is actually the maximum consecutively increasing difference.
     * The maximum consecutively increasing difference can end at any element in nums.
     * The maximum consecutively increasing difference ending at position i + 1 is
     * either includes maximum consecutively increasing difference ending at position i or it doesn't.
     * @return maximum profit
     */

    public static Transaction task_3b(int[][] price_at_day){
        int m = price_at_day.length;
        int n = price_at_day[0].length;
        int max_profit = Integer.MIN_VALUE;

         // maxIncDiffSum[i][j] max sum of increasing difference of stock[i] ending at prices[j].
         int[][] maxIncDiff = new int[m][n];
         //to store smallest price found for stock i
         int[] fin_buy = new int[m];
         //to store largest price found for stock i
         int[] fin_sell = new int[m];
         // to store buying and selling of which stock will give us maximum profit
         int fin_stock = -1;
         for(int i = 0 ; i < m ; i++){
            for(int j = 1 ; j < n ; j++){
                //calculating increasing maximum sum, if it becomes negative, make maximum sum so far as 0
                if(maxIncDiff[i][j-1] + price_at_day[i][j] - price_at_day[i][j-1] >= 0){
                    maxIncDiff[i][j] = maxIncDiff[i][j-1] + price_at_day[i][j] - price_at_day[i][j-1];
                    fin_sell[i] = j;
                }else{
                    maxIncDiff[i][j] = 0;
                    fin_buy[i] = j;
                }
                if(maxIncDiff[i][j] > max_profit){
                    max_profit = maxIncDiff[i][j];
                    fin_stock = i;
                }
            }
         }
    
        return new Transaction(fin_stock, fin_buy[fin_stock], fin_sell[fin_stock], max_profit);
    }

    public  void runner_task1(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter:- 1.number of stocks 2. number of days");
        String input = scanner.nextLine(); 
        String[] numbers = input.split(" ");
        int m = Integer.parseInt(numbers[0]);
        int n = Integer.parseInt(numbers[1]);
        int[][] price = new int[m][n];

        System.out.println("Enter each row:-");
        for(int i = 0 ; i < m ; i++){
            String line = scanner.nextLine(); 
            String[] row = line.split(" ");
            for(int j = 0 ; j < n ; j++){
                price[i][j] = Integer.parseInt(row[j]);
            }
        }
        scanner.close();
        Transaction ans1 = task1(price);
        System.out.println("We're using 0-base indexing here");
        System.out.println("Result of task" + 1);
            
        // int i;
        Transaction transaction= ans1;
            // System.out.println("Profit:" + profit);

        if(transaction == null){
            System.out.println("Maximum profit gained so far is 0. Hence, no need to do any transactions");
        }else{
            System.out.println("Transaction is:-");
            System.out.println(transaction.getStockNumber() + " " + transaction.getBuyDay() + " " + transaction.getSellDay());
        }

    }

    public  void runner_task2(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter:- 1.number of stocks 2. number of days");
        String input = scanner.nextLine(); 
        String[] numbers = input.split(" ");
        int m = Integer.parseInt(numbers[0]);
        int n = Integer.parseInt(numbers[1]);
        int[][] price = new int[m][n];

        System.out.println("Enter each row:-");
        for(int i = 0 ; i < m ; i++){
            String line = scanner.nextLine(); 
            String[] row = line.split(" ");
            for(int j = 0 ; j < n ; j++){
                price[i][j] = Integer.parseInt(row[j]);
            }
        }
        scanner.close();
        Transaction ans1 = task2(price);
        System.out.println("We're using 0-base indexing here");
        System.out.println("Result of task" + 2);
            
        // int i;
        Transaction transaction= ans1;
            // System.out.println("Profit:" + profit);

        if(transaction == null){
            System.out.println("Maximum profit gained so far is 0. Hence, no need to do any transactions");
        }else{
            System.out.println("Transaction is:-");
            System.out.println(transaction.getStockNumber() + " " + transaction.getBuyDay() + " " + transaction.getSellDay());
        }

    }

    public  void runner_task3a(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter:- 1.number of stocks 2. number of days");
        String input = scanner.nextLine(); 
        String[] numbers = input.split(" ");
        int m = Integer.parseInt(numbers[0]);
        int n = Integer.parseInt(numbers[1]);
        int[][] price = new int[m][n];

        System.out.println("Enter each row:-");
        for(int i = 0 ; i < m ; i++){
            String line = scanner.nextLine(); 
            String[] row = line.split(" ");
            for(int j = 0 ; j < n ; j++){
                price[i][j] = Integer.parseInt(row[j]);
            }
        }
        scanner.close();
        Transaction ans1 = task_3a(price);
        System.out.println("We're using 0-base indexing here");
        System.out.println("Result of task 3a");
            
        // int i;
        Transaction transaction= ans1;
            // System.out.println("Profit:" + profit);

        if(transaction == null){
            System.out.println("Maximum profit gained so far is 0. Hence, no need to do any transactions");
        }else{
            System.out.println("Transaction is:-");
            System.out.println(transaction.getStockNumber() + " " + transaction.getBuyDay() + " " + transaction.getSellDay());
        }

    }

    public  void runner_task3b(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter:- 1.number of stocks 2. number of days");
        String input = scanner.nextLine(); 
        String[] numbers = input.split(" ");
        int m = Integer.parseInt(numbers[0]);
        int n = Integer.parseInt(numbers[1]);
        int[][] price = new int[m][n];

        System.out.println("Enter each row:-");
        for(int i = 0 ; i < m ; i++){
            String line = scanner.nextLine(); 
            String[] row = line.split(" ");
            for(int j = 0 ; j < n ; j++){
                price[i][j] = Integer.parseInt(row[j]);
            }
        }
        scanner.close();
        Transaction ans1 = task_3b(price);
        System.out.println("We're using 0-base indexing here");
        System.out.println("Result of task 3b");
            
        Transaction transaction= ans1;

        if(transaction == null){
            System.out.println("Maximum profit gained so far is 0. Hence, no need to do any transactions");
        }else{
            System.out.println("Transaction is:-");
            System.out.println(transaction.getStockNumber() + " " + transaction.getBuyDay() + " " + transaction.getSellDay());
        }

    }

	// Driver code
	public static void main(String[] args)
	{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter:- 1.number of stocks 2. number of days");
        String input = scanner.nextLine(); 
        String[] numbers = input.split(" ");
        int m = Integer.parseInt(numbers[0]);
        int n = Integer.parseInt(numbers[1]);
        int[][] price = new int[m][n];

        System.out.println("Enter each row:-");
        for(int i = 0 ; i < m ; i++){
            String line = scanner.nextLine(); 
            String[] row = line.split(" ");
            for(int j = 0 ; j < n ; j++){
                price[i][j] = Integer.parseInt(row[j]);
            }
        }
        scanner.close();

        Transaction ans1 = task1(price);
        Transaction ans2 = task2(price); 
        Transaction ans3 = task_3a(price);
        Transaction ans4 = task_3b(price);
        Transaction[] ans= {ans1, ans2, ans3, ans4};

        System.out.println("We're using 0-base indexing here");
        for(int j = 0 ; j <4 ; j++){
            System.out.println("Result of tasks :- " + (j+1));
            
            // int i;
            Transaction transaction= ans[j];
            // System.out.println("Profit:" + profit);

            if(transaction == null){
                System.out.println("Maximum profit gained so far is 0. Hence, no need to do any transactions");
            }else{
                System.out.println("Transaction is:-");
                System.out.println(transaction.getStockNumber() + " " + transaction.getBuyDay() + " " + transaction.getSellDay());
                // + " Profit:" + transaction.getProfit());
                    
            }
        }

	}
}

