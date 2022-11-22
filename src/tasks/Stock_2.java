package tasks;

// Java implementation of the approach
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Stock_2 {

    // declared these fields globally to reduce number of function parameters
    static int[][][] dp_buy;
    static int[][][] dp_sell;
    static TransactionL[][][] dp_buy_txn;
    static TransactionL[][][] dp_sell_txn;
    static int[][] price_at_day_6a;
    static int[][] price_at_day_4;

    /*
     * Brute force recursion for finding atmost k transactions(buy and sell) which gives maximum profit
     * with m stocks and n days
     * t.c:- O(m*n^2k)
     */
    public static List<Transaction> task4(int[][] price_at_day, int k) {
        // inititalising price matrix globally to reduce number of function parameters
        price_at_day_4 = price_at_day;

        // tuple which consists of max_profit and list of transactions which gives max_profit
        TransactionTuple res_tuple = task4_helper(0, false, k, 0);

        System.out.println("Max_Profit is:-" + res_tuple.getProfit());

        // Handling the case when max_profit is zero, in this case transaction_list will be empty/null
        if (res_tuple.getTransactionList() == null || res_tuple.getTransactionList().getTransactionList_L().isEmpty()) {
            System.out.println("No Transactions are found to print, profit must be 0 and hence no transaction list is empty");
        }

        // returning only the list of transactions giving max_profit
        return res_tuple.getTransactionList().getTransactionList_L();
    }

    /*
     * Helper method for recursion which will return a tuple of maximum profit and sequence of transactions for the maximum profit
     */
    public static TransactionTuple task4_helper(int i, boolean sell, int k, int m) {
        // no of stocks
        int len_stocks = price_at_day_4.length;
        // no of days
        int n = price_at_day_4[0].length;

        // if no of transactions left is 0, we cannot make any more transactions, hence returing 0 profit for that case
        if (k == 0) {
            return new TransactionTuple(0, new TransactionL());
        }
        // handling when we have reached the boundary
        if (i == n) {
            return new TransactionTuple(0, new TransactionL());
        }

        // if we're at last day, buying at this day does not make sense as it would only reduce our max_profit, so only if sell_state is true, we sell it otherwise skip the transaction
        if (i == n - 1) {
            if (sell) {
                return new TransactionTuple(price_at_day_4[m][i], new TransactionL(new LinkedList<>(Arrays.asList(new Transaction(m, -1, i, price_at_day_4[m][i])))));
            } else {
                return new TransactionTuple(0, new TransactionL());
            }
        }

        // if sell_state is true, there are 2 choices that can happen:- 
        if (sell) {
            // 1. not sell on current day that means we still hold a stock, so the sell_state for next day will be true. And since we're not doing anything on current day, number of transactions will remain same
            TransactionTuple tuple1 = task4_helper(i + 1, true, k, m);
            // 2. by selling on current day, we're finishing 1 transaction, hence k = k-1. And then sell_state for next day will be false as we have to buy before selling again
            TransactionTuple tuple2 = task4_helper(i, false, k - 1, m);

            int val1 = tuple1.getProfit();
            int val2 = tuple2.getProfit();

            TransactionL x1 = tuple1.getTransactionList();
            TransactionL x2 = tuple2.getTransactionList();

            // we're comparing the profit made by 2 choices above and returning whichever choice gives us a better profit, we're adding that transaction to our list
            if (val1 > val2 + price_at_day_4[m][i]) {
                return new TransactionTuple(val1, x1.add(new Transaction(m, -1, i, price_at_day_4[m][i])));
            }
            return new TransactionTuple(val2 + price_at_day_4[m][i], x2.add(new Transaction(m, -1, i, price_at_day_4[m][i])));
        } else {
            // If buy_state is true

            // parameter that will help us pick transactions with maximum profit
            int total_max = 0;
            // Initialising a list that will hold our final transactions
            List<Transaction> x3 = new ArrayList<>();
            for (int m1 = 0; m1 < len_stocks; m1++) {
                // If buy_state is true, we will be checking for all stocks and see buying which stock at a particular day gives us maximum profit

                // Again we have 2 possible choices:-
                // case:-1.Don't buy on current day, so buy_state for next day will be true and no transactions are made, hence k remains unchanged
                TransactionTuple tuple1 = task4_helper(i + 1, false, k, m1);
                // case:-2.Buy on cur day, then sell_state of next day will be true. A transaction is complete when a stock has been sold. Hence k remains unchanged.
                TransactionTuple tuple2 = task4_helper(i + 1, true, k, m1);

                int val1 = tuple1.getProfit();
                int val2 = tuple2.getProfit();


                TransactionL x1 = tuple1.getTransactionList();
                TransactionL x2 = tuple2.getTransactionList();

                // for case2:- we are spending money to buy stock on cur day, hence total_profit_so_far = total_profit_so_far - cur_price
                val2 = val2 - price_at_day_4[m1][i];

                // to store all stocks who has buy day
                List<Transaction> xf = new ArrayList<>();
                // to store all those stocks who doesn't have a buy day till we get result from case:-2
                List<Transaction> xf2 = new ArrayList<>();

                for (Transaction t : x2.getTransactionList_L()) {
                    if (t.getBuyDay() == -1) {
                        // making the buy day as i for all stocks not having a buy day
                        xf2.add(new Transaction(t.getStockNumber(), i, t.getSellDay(), t.getProfit() - price_at_day_4[m1][i]));
                    } else {
                        xf.add(t);
                    }
                }

                // Sorting the list of all stocks in descending order of profit whose buy day we set as i.
                Collections.sort(xf2, ((Transaction a, Transaction b) -> (b.profit - a.profit)));

                // to store maximum value of profit gained from case:-1 and case:-2 above
                int temp = Math.max(val1, val2);

                // we are appending to final list of transactions only if we have found a bigger profit value
                if (total_max < temp) {
                    // if profit gained from case:-1 is equal to new max, we will add the list gained from case:-1 to final
                    if (val1 == temp) {
                        x3 = x1.getTransactionList_L();
                    } else {
                        // when val2 = temp :- means profit gained from case:-2 is new max, we are adding topmost element with max_profit from  the list whose buy_day we set to i to the list of transactions so far
                        xf.addAll(xf2.subList(0, 1));
                        x3 = new ArrayList<>(xf);
                    }
                    // updating max_profit so far
                    total_max = temp;
                }

            }
            return new TransactionTuple(total_max, new TransactionL(x3));
        }
    }

    /*
     * dynamic programming solution to solve problem in
     * t.c.:- O(m*n^2*k)
     */
    public static List<Transaction> task5(int[][] price_at_day, int k) {
        // stocks
        int m = price_at_day.length;
        // days
        int n = price_at_day[0].length;
        // profit dp to keep track of maximum profit made by k transactions in n days
        int[][] profit = new int[k + 1][n];

        /*
         * basic idea here is to take each day as selling day and finding the maximum profit made if we choose to sell on that day
         * considering all stocks. In order to find the maximum profit for that selling day, we would have to find a best_buy day
         * for that selling day. we do it by traversing the stock till sell and check which day might be best_buy day for our sell day
         */
        // loop through number of transactions
        for (int i = 1; i < k + 1; i++) {
            // loop through days with j as selling day
            for (int j = 1; j < n; j++) {
                int max1 = 0;
                // for each selling day j, finding a buy day x which gives maximum profit
                for (int x = 0; x < j; x++) {
                    // loop through number of stocks
                    for (int stock = 0; stock < m; stock++) {
                        // if we buy on day x and sell on day j, total profit made will profit made by this transaction + profit made with k-1 transactions till buy day x
                        int temp = (price_at_day[stock][j] - price_at_day[stock][x]) + profit[i - 1][x];
                        if (max1 < temp) {
                            max1 = temp;
                        }

                    }
                }
                // at each point maximum proft so far will be maximum of profit till (j-1)th day and maximum profit by selling on jth day 
                if (j >= 1) {
                    profit[i][j] = Math.max(profit[i][j - 1], max1);
                }

            }
        }
        // profit by k transactions in n days
        System.out.println("Total Profit:- " + profit[k][n - 1]);
        return get_txn_backtrack(price_at_day, profit);
    }

    /*
     * recusrion with Memoization approach to solve problem in
     * t.c :- O(m*n*k)
     */
    public static List<Transaction> task6a(int[][] price_at_day, int k) {
        // stocks
        int m = price_at_day.length;
        // days
        int n = price_at_day[0].length;

        // initialising dp array to store profit after buying stock m at kth transaction on nth day
        dp_buy = new int[m + 1][k + 1][n + 1];
        // initialising dp array to store profit gained by selling stock m at kth transaction on nth day
        dp_sell = new int[m + 1][k + 1][n + 1];

        // utill method to fill 3D dp with -1 
        Util.default_fill_3D(dp_buy, -1);
        Util.default_fill_3D(dp_sell, -1);

        // dp to store buy transactions
        dp_buy_txn = new TransactionL[m + 1][k + 1][n + 1];
        // dp to store buy transactions
        dp_sell_txn = new TransactionL[m + 1][k + 1][n + 1];
        // inititalising price matrix globally to reduce number of function parameters
        price_at_day_6a = price_at_day;

        // tuple which consists of max_profit and list of transactions which gives max_profit
        TransactionTuple res_tuple = task6a_helper(0, false, k, 0);

        System.out.println("Max_Profit is:-" + res_tuple.getProfit());

        if (res_tuple.getTransactionList() == null || res_tuple.getTransactionList().getTransactionList_L().isEmpty()) {
            System.out.println("No Transactions are found to print, profit must be 0 and hence no transaction list is empty");
        }

        return res_tuple.getTransactionList().getTransactionList_L();
    }

    /*
     * helper method for 6a to actually do the recusrion and filling
     * up our memoization table
     */
    public static TransactionTuple task6a_helper(int i, boolean sell, int k, int m) {
        //  stocks
        int len_stocks = price_at_day_6a.length;
        // no of days
        int n = price_at_day_6a[0].length;
        if (sell) {
            // return from dp if a particular sell_state has been found before
            if (dp_sell[m][k][i] != -1) {
                return new TransactionTuple(dp_sell[m][k][i], dp_sell_txn[m][k][i]);
            }
        } else {
            //  return from dp if a particular buy_state has been found before
            if (dp_buy[m][k][i] != -1) {
                return new TransactionTuple(dp_buy[m][k][i], dp_buy_txn[m][k][i]);
            }
        }

        // if no of transactions left is 0, we cannot make any more transactions, hence returing 0 profit for that case
        if (k == 0) {
            return new TransactionTuple(0, new TransactionL());
        }
        // handling when we have reached the boundary
        if (i == n) {
            return new TransactionTuple(0, new TransactionL());
        }

        // if we're at last day, buying at this day does not make sense as it would only reduce our max_profit, so only if sell_state is true, we sell it otherwise skip the transaction
        if (i == n - 1) {
            if (sell) {
                return new TransactionTuple(price_at_day_6a[m][i], new TransactionL(new ArrayList<>(Arrays.asList(new Transaction(m, -1, i, price_at_day_6a[m][i])))));
            } else {
                return new TransactionTuple(0, new TransactionL());
            }
        }

        // if sell_state is true, there are 2 choices that can happen:- 
        if (sell) {
            // case:-1. not sell on current day that means we still hold a stock, so the sell_state for next day will be true. And since we're not doing anything on current day, number of transactions will remain same
            TransactionTuple tuple1 = task6a_helper(i + 1, true, k, m);
            // 2. by selling on current day, we're finishing 1 transaction, hence k = k-1. And then sell_state for next day will be false as we have to buy before selling again
            TransactionTuple tuple2 = task6a_helper(i, false, k - 1, m);
            int val1 = tuple1.getProfit();
            int val2 = tuple2.getProfit();


            TransactionL x1 = tuple1.getTransactionList();
            TransactionL x2 = tuple2.getTransactionList();

            // storing the profit gained by not selling at ith but selling at (i+1)th in sell_dp at (i+1)th day
            dp_sell[m][k][i + 1] = val1;
            // storing the corresponding transaction list
            dp_sell_txn[m][k][i + 1] = new TransactionL(x1.getTransactionList_L());

            // storing the profit gained by selling at ith in buy_dp as the state for next k-1 transaction will be buy
            dp_buy[m][k - 1][i] = val2;
            // storing the corresponding transaction list
            dp_buy_txn[m][k - 1][i] = new TransactionL(x2.getTransactionList_L());

            // we're comparing the profit made by 2 choices above and returning whichever choice gives us a better profit, we're adding that transaction to our list
            if (val1 > val2 + price_at_day_6a[m][i]) {
                return new TransactionTuple(val1, x1.add(new Transaction(m, -1, i, price_at_day_6a[m][i])));
            }
            return new TransactionTuple(val2 + price_at_day_6a[m][i], x2.add(new Transaction(m, -1, i, price_at_day_6a[m][i])));
        } else {
            // If buy_state is true

            // parameter that will help us pick transactions with maximum profit
            int total_max = 0;
            // Initialising a list that will hold our final transactions
            List<Transaction> x3 = new ArrayList<>();
            for (int m1 = 0; m1 < len_stocks; m1++) {
                // If buy_state is true, we will be checking for all stocks and see buying which stock at a particular day gives us maximum profit

                // Again we have 2 possible choices:-
                // case:-1.Don't buy on current day, so buy_state for next day will be true and no transactions are made, hence k remains unchanged
                TransactionTuple tuple1 = task6a_helper(i + 1, false, k, m1);
                // case:-2.Buy on cur day, then sell_state of next day will be true. A transaction is complete when a stock has been sold. Hence k remains unchanged.
                TransactionTuple tuple2 = task6a_helper(i + 1, true, k, m1);
                int val1 = tuple1.getProfit();
                int val2 = tuple2.getProfit();


                TransactionL x1 = tuple1.getTransactionList();
                TransactionL x2 = tuple2.getTransactionList();

                // filling respective values in dp
                dp_sell[m1][k][i + 1] = val2;
                dp_sell_txn[m1][k][i + 1] = new TransactionL(x2.getTransactionList_L());
                ;

                dp_buy[m1][k][i + 1] = val1;
                dp_buy_txn[m1][k][i + 1] = new TransactionL(x1.getTransactionList_L());
                ;

                // for case2:- we are spending money to buy stock on cur day, hence total_profit_so_far = total_profit_so_far - cur_price
                val2 = val2 - price_at_day_6a[m1][i];

                // to store all stocks who has buy day
                List<Transaction> xf = new ArrayList<>();
                // to store all those stocks who doesn't have a buy day till we get result from case:-2
                List<Transaction> xf2 = new ArrayList<>();

                for (Transaction t : x2.getTransactionList_L()) {
                    if (t.getBuyDay() == -1) {
                        // making the buy day as i for all stocks not having a buy day
                        xf2.add(new Transaction(t.getStockNumber(), i, t.getSellDay(), t.getProfit() - price_at_day_6a[m1][i]));
                    } else {
                        xf.add(t);
                    }
                }

                // Sorting the list of all stocks in descending order of profit whose buy day we set as i.
                Collections.sort(xf2, ((Transaction a, Transaction b) -> (b.profit - a.profit)));

                int temp = Math.max(val1, val2);

                // we are appending to final list of transactions only if we have found a bigger profit value
                if (total_max < temp) {
                    // if profit gained from case:-1 is equal to new max, we will add the list gained from case:-1 to final
                    if (val1 == temp) {
                        x3 = x1.getTransactionList_L();
                    } else {
                        // when val2 = temp :- means profit gained from case:-2 is new max, we are adding topmost element with max_profit from  the list whose buy_day we set to i to the list of transactions so far
                        xf.addAll(xf2.subList(0, 1));
                        x3 = new ArrayList<>(xf);
                    }
                    total_max = temp;
                }

            }
            return new TransactionTuple(total_max, new TransactionL(x3));
        }
    }

    /*
     * Dynammic Programming Solution to solve problem in
     * t.c :- O(m*n*k)
     *
     * basic idea here is same as task5, but now instead of iterating through days to find best_buy day, we are keeping track
     * of max_diff_so_far in an array which will help us find maximum profit made if sell on cur day in constant time operation
     */
    public static List<Transaction> task6b(int[][] price_at_day, int k) {
        // stocks
        int m = price_at_day.length;
        // days
        int n = price_at_day[0].length;
        // profit dp to keep track of maximum profit made by k transactions in n days
        int[][] profit = new int[k + 1][n];

        // loop through number of transactions
        for (int i = 1; i < k + 1; i++) {
            // initialising prev array which will help us keep track of best buying_day for a particular selling day by maintaining max_diff_so_far
            int[] prev = new int[m];
            Arrays.fill(prev, Integer.MIN_VALUE);

            // looping through number of day
            for (int j = 1; j < n; j++) {

                int prev2 = Integer.MIN_VALUE;
                // looping through number of stocks
                for (int y = 0; y < m; y++) {
                    // if buying a stock y on (j-1)th day  is more beneficial than update value of prev[y] and mark buy_day for y as j-1
                    if (prev[y] < profit[i - 1][j - 1] - price_at_day[y][j - 1]) {
                        prev[y] = profit[i - 1][j - 1] - price_at_day[y][j - 1];
                    }
                    if (prev2 < prev[y] + price_at_day[y][j]) {
                        prev2 = prev[y] + price_at_day[y][j];
                    }
                }
                // profit of ith transaction will be maximum of profit on (j-1)th day and profit of jth day
                profit[i][j] = Math.max(profit[i][j - 1], prev2);
            }
        }
        // for(int[] y:profit){
        //     for(int u:y)System.out.print(u+" ");
        //     System.out.println();
        // }
        System.out.println("Max_Profit is:-" + profit[k][n - 1]);
        List<Transaction> txn_list = get_txn_backtrack(price_at_day, profit);
        return txn_list;
    }

    /*helper method for task5 and task6b
     * to help us backtrack through our rofit matrix to find
     * atmost transactions that gives us best result
     */
    public static List<Transaction> get_txn_backtrack(int[][] price_at_day, int[][] profit) {
        // transactions
        int m = profit.length - 1;
        // days
        int n = profit[0].length - 1;
        // Inititlaising a list of transactions to store final transactions that leads to maximum profit
        List<Transaction> txn_res = new ArrayList<>();
        /*starting from right corner of profit matrix as our maximum profit will be stored at profit[k][n-1]*/
        while (m > 0 && n > 0) {

            // keep decrementing through days untill profit on current day is equal to profit made last day
            while (n > 0 && profit[m][n] == profit[m][n - 1]) {
                n--;
            }
            // once we found out a different profit was made on last day than current, then we'll mark that as sell index as a transaction must have occured at that index
            int sell = n;
            // getting profit made till that day
            int profit_till_cur_sell = profit[m][n];
            // moving to last day and last stock to find non-overlaping buy day for our current sell day
            n -= 1;
            m -= 1;

            if (n >= 0 && m >= 0) {
                while (n >= 0) {
                    int best_profit_so_far = profit_till_cur_sell - profit[m][n];
                    int is_buy_best = 0;

                    // looping through to find correct buy index for our sell index that resulted to maximum profit
                    for (int y = 0; y < price_at_day.length; y++) {
                        if (price_at_day[y][sell] - price_at_day[y][n] == best_profit_so_far) {
                            // found a buy index which will give best profit
                            Transaction txn = new Transaction(y, n, sell, best_profit_so_far);

                            txn_res.add(txn);
                            is_buy_best = 1;
                            break;
                        }
                    }
                    // if we have already found our best_buy_index, we would want to move on to next transaction
                    if (is_buy_best == 1) {
                        break;
                    }
                    n--;
                }
            }
        }
        // reversing to find transactions which are in increasing order of buy_index
        Collections.reverse(txn_res);
        return txn_res;
        // Collections.sort(transaction_list, (Transaction a, Transaction b) -> (a.buy_day - b.buy_day));
    }

    /*
     * Runner for task4
     */
    public void runner_task4() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of transactions, k");
        String input1 = scanner.nextLine();
        int k = Integer.parseInt(input1);

        System.out.println("Enter:- 1.number of stocks 2. number of days");
        String input2 = scanner.nextLine();
        String[] numbers = input2.split(" ");
        int m = Integer.parseInt(numbers[0]);
        int n = Integer.parseInt(numbers[1]);
        int[][] price = new int[m][n];

        System.out.println("Enter each row:-");
        for (int i = 0; i < m; i++) {
            String line = scanner.nextLine();
            String[] row = line.split(" ");
            for (int j = 0; j < n; j++) {
                price[i][j] = Integer.parseInt(row[j]);
            }
        }
        scanner.close();
        List<Transaction> transaction_list = task4(price, k);
        System.out.println("We're using 0-base indexing here");
        System.out.println("Result of task" + 4);

        Collections.sort(transaction_list, (Transaction a, Transaction b) -> (a.buy_day - b.buy_day));

        if (transaction_list.size() == 0) {
            System.out.println("Maximum profit gained so far is 0. Hence, no need to do any transactions");
        } else {
            System.out.println("Transactions are:-");

            for (int i = 0; i < transaction_list.size(); i++) {
                System.out.println(transaction_list.get(i).getStockNumber() + " " + transaction_list.get(i).getBuyDay() + " " + transaction_list.get(i).getSellDay());

            }
        }

    }

    /*
     * Runner for task5
     */
    public void runner_task5() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of transactions, k");
        String input1 = scanner.nextLine();
        int k = Integer.parseInt(input1);

        System.out.println("Enter:- 1.number of stocks 2. number of days");
        String input2 = scanner.nextLine();
        String[] numbers = input2.split(" ");
        int m = Integer.parseInt(numbers[0]);
        int n = Integer.parseInt(numbers[1]);
        int[][] price = new int[m][n];

        System.out.println("Enter each row:-");
        for (int i = 0; i < m; i++) {
            String line = scanner.nextLine();
            String[] row = line.split(" ");
            for (int j = 0; j < n; j++) {
                price[i][j] = Integer.parseInt(row[j]);
            }
        }
        scanner.close();
        List<Transaction> transaction_list = task5(price, k);
        System.out.println("We're using 0-base indexing here");
        System.out.println("Result of task" + 5);

        Collections.sort(transaction_list, (Transaction a, Transaction b) -> (a.buy_day - b.buy_day));

        if (transaction_list.size() == 0) {
            System.out.println("Maximum profit gained so far is 0. Hence, no need to do any transactions");
        } else {
            System.out.println("Transactions are:-");

            for (int i = 0; i < transaction_list.size(); i++) {
                System.out.println(transaction_list.get(i).getStockNumber() + " " + transaction_list.get(i).getBuyDay() + " " + transaction_list.get(i).getSellDay());

            }
        }

    }

    /*
     * Runner for task6a
     */
    public void runner_task6a() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of transactions, k");
        String input1 = scanner.nextLine();
        int k = Integer.parseInt(input1);

        System.out.println("Enter:- 1.number of stocks 2. number of days");
        String input2 = scanner.nextLine();
        String[] numbers = input2.split(" ");
        int m = Integer.parseInt(numbers[0]);
        int n = Integer.parseInt(numbers[1]);
        int[][] price = new int[m][n];

        System.out.println("Enter each row:-");
        for (int i = 0; i < m; i++) {
            String line = scanner.nextLine();
            String[] row = line.split(" ");
            for (int j = 0; j < n; j++) {
                price[i][j] = Integer.parseInt(row[j]);
            }
        }
        scanner.close();
        List<Transaction> transaction_list = task6a(price, k);
        System.out.println("We're using 0-base indexing here");
        System.out.println("Result of task 6a");

        Collections.sort(transaction_list, (Transaction a, Transaction b) -> (a.buy_day - b.buy_day));

        if (transaction_list.size() == 0) {
            System.out.println("Maximum profit gained so far is 0. Hence, no need to do any transactions");
        } else {
            System.out.println("Transactions are:-");

            for (int i = 0; i < transaction_list.size(); i++) {
                System.out.println(transaction_list.get(i).getStockNumber() + " " + transaction_list.get(i).getBuyDay() + " " + transaction_list.get(i).getSellDay());

            }
        }

    }


    /*
     * Runner for task6b
     */
    public void runner_task6b() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of transactions, k");
        String input1 = scanner.nextLine();
        int k = Integer.parseInt(input1);

        System.out.println("Enter:- 1.number of stocks 2. number of days");
        String input2 = scanner.nextLine();
        String[] numbers = input2.split(" ");
        int m = Integer.parseInt(numbers[0]);
        int n = Integer.parseInt(numbers[1]);
        int[][] price = new int[m][n];

        System.out.println("Enter each row:-");
        for (int i = 0; i < m; i++) {
            String line = scanner.nextLine();
            String[] row = line.split(" ");
            for (int j = 0; j < n; j++) {
                price[i][j] = Integer.parseInt(row[j]);
            }
        }
        scanner.close();
        List<Transaction> transaction_list = task6b(price, k);
        System.out.println("We're using 0-base indexing here");
        System.out.println("Result of task 6b");

        Collections.sort(transaction_list, (Transaction a, Transaction b) -> (a.buy_day - b.buy_day));

        if (transaction_list.size() == 0) {
            System.out.println("Maximum profit gained so far is 0. Hence, no need to do any transactions");
        } else {
            System.out.println("Transactions are:-");

            for (int i = 0; i < transaction_list.size(); i++) {
                System.out.println(transaction_list.get(i).getStockNumber() + " " + transaction_list.get(i).getBuyDay() + " " + transaction_list.get(i).getSellDay());

            }
        }

    }
}