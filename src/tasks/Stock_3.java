package tasks;

import java.util.*;
public class Stock_3 {

    static int[][] price_at_day_8a;

    static int[][] price_at_day_9a;
    static int cool_down_7;

    static int[][][] memo_8a;
    static TransactionL[][][] txn_8a;
    static int cool_down_9a;

    /*
     * Bottom-Up implementation of m*n
     */
    public static TransactionTuple task9b(int[][] price_at_day, int cool_down){
        // no of stocks
        int len_stocks = price_at_day.length;
        // no of days
        int n = price_at_day[0].length;
        // iterater through days
        int[][] cool = new int[len_stocks][n];
        int[][] sell = new int[len_stocks][n];
        int[][] hold = new int[len_stocks][n];

        TransactionL[][] cool_txn =  new TransactionL[len_stocks][n];
        TransactionL[][] sell_txn = new TransactionL[len_stocks][n];
        TransactionL[][] hold_txn = new TransactionL[len_stocks][n];

        Util.default_fill_2D(cool, 0);
        Util.default_fill_2D(sell, 0);
        Util.default_fill_2D(hold, Integer.MIN_VALUE);
        Util.default_fill_txn_2D(hold_txn, new TransactionL());
        Util.default_fill_txn_2D(sell_txn, new TransactionL());
        Util.default_fill_txn_2D(cool_txn, new TransactionL());

        for(int j = 0 ; j < n ; j++){
            int t1 = Integer.MIN_VALUE;
            int m1 = 0;
            int flag_cool_or_sell = -1;

            for(int i = 0 ; i < len_stocks ; i++){
                int cool_val = 0;
                int sell_val = 0;
                if(j == 0){
                    cool_val = 0;
                    sell_val = 0;
                }else{
                    cool_val = cool[i][j-1];
                    sell_val = sell[i][j-1];
                }
//                System.out.println("cool:"+cool_val);
//                System.out.println("sell:"+sell_val);

                
                if(t1 < cool_val){
                    t1 = cool_val;
                    m1 = i;
                    flag_cool_or_sell = 1;
                }
                if(t1 < sell_val){
                    t1 = sell_val;
                    m1 = i;
                    flag_cool_or_sell = 0;
                }

            }

            for(int i = 0 ; i < len_stocks ; i++){
//                System.out.println("t1:"+t1);
                cool[i][j] = t1;
                if(j == 0){
                    cool_txn[i][j] = new TransactionL();
                }else{
                    if(flag_cool_or_sell == 0){
                        cool_txn[i][j] = new TransactionL(new ArrayList<>(sell_txn[m1][j-1].getTransaction_L().getTransactionList_L()));
                    }else if(flag_cool_or_sell == 1){
                        cool_txn[i][j] = new TransactionL(new ArrayList<>(cool_txn[m1][j-1].getTransaction_L().getTransactionList_L()));
    
                    }
                }
            }

            t1 = Integer.MIN_VALUE;
            m1 = 0;

            for(int i = 0 ; i < len_stocks ; i++){
                int hold_val = 0;
                if(j == 0){
                    hold_val = 0;
                }else{
                    hold_val = hold[i][j-1];
                }
                if(t1 < hold_val + price_at_day[i][j]){
                    t1 = hold_val + price_at_day[i][j];
                    m1 = i;
                }
            }

            List<Transaction> update_txn = new ArrayList<>();
//             TransactionL cur = new TransactionL();
//             if(j == 0){
//                 cur = zeroth_trans;
//             }else{
//                 cur = hold_txn[m1][j-1];
//             }

            if(j > 0){
                List<Transaction> x = hold_txn[m1][j-1].getTransactionList_L();
//                for(int i = 0 ; i < x.size(); i++){
//                    System.out.println(x.get(i).getStockNumber() + " " + x.get(i).getBuyDay() + " " + x.get(i).getSellDay());
////                " " + x.get(i).getProfit());
//                }
//                System.out.println("pq");
                for(Transaction txn :  hold_txn[m1][j-1].getTransactionList_L()){
//                    System.out.println("ab");
////                    for(int i = 0 ; i < txn.size(); i++){
//                        System.out.println(txn.getStockNumber() + " " + txn.getBuyDay() + " " + txn.getSellDay());
////                " " + x.get(i).getProfit());
                    if(txn.getProfit() == 0){
                        continue;
                    }
                    if(txn.getSellDay() == -1){ // it is sell day check only??
                        update_txn.add(new Transaction(txn.getStockNumber(), txn.getBuyDay(), j, txn.getProfit() + price_at_day[m1][j]));
                    }else{
                        update_txn.add(txn);
                    }
                }


//                List<Transaction> y = update_txn;
//                for(int i = 0 ; i < y.size(); i++){
//                    System.out.println(y.get(i).getStockNumber() + " " + y.get(i).getBuyDay() + " " + y.get(i).getSellDay());
////                " " + x.get(i).getProfit());
//                }

            }
            
            for(int i = 0 ; i < len_stocks ; i++){
                sell[i][j] = t1;
                TransactionL cool_trans = new TransactionL();
                if(j == 0){
                    cool_trans = new TransactionL();
                }else{
                    cool_trans = new TransactionL(cool_txn[m1][j-1].getTransactionList_L());
                }
                sell_txn[i][j] = new TransactionL(cool_trans.add_transactionL(update_txn).getTransactionList_L());
            }

            for(int i = 0 ; i < len_stocks ; i++){
                TransactionL hold_trans = new TransactionL();
                int last_max = 0;
                int last_cool = 0;
                if(j == 0){
                    last_max = 0;
                    hold_trans = new TransactionL();
                }else{
                    last_max = hold[i][j-1];
                    hold_trans = new TransactionL(new ArrayList<>(hold_txn[i][j-1].getTransactionList_L()));
                }

                if(j-cool_down < 0){
                    last_cool = 0;
                }else{
                    last_cool = cool[i][j-cool_down];
                }


                if(last_max > last_cool - price_at_day[i][j]){
                    hold[i][j] = last_max;
                    hold_txn[i][j] = hold_trans;
                }else{
                    hold[i][j] = last_cool - price_at_day[i][j];
                    hold_txn[i][j] = new TransactionL(new ArrayList<>(Arrays.asList(new Transaction(i, j, -1, -price_at_day[i][j]))));
                }
            }
        }

        

        List<Transaction> x = new ArrayList<>();
//        for(int[] cur : cool){
//            for(int pr: cur){
//                System.out.println(pr);
//            }
//        }
//
//        for(int[] cur : sell){
//            for(int pr: cur){
//                System.out.println(pr);
//            }
//        }
        if(sell[len_stocks-1][n-1] > cool[len_stocks-1][n-1]){
            x = sell_txn[len_stocks-1][n-1].getTransactionList_L();
            Collections.sort(x, (Transaction a, Transaction b) -> (b.profit - a.profit));
//
//            for(int i = 0 ; i < x.size(); i++){
//                System.out.println(x.get(i).getStockNumber() + " " + x.get(i).getBuyDay() + " " + x.get(i).getSellDay());
////                " " + x.get(i).getProfit());
//            }

            // System.out.println("profit:-"+sell[len_stocks-1][n-1]);

            return new TransactionTuple(sell[len_stocks-1][n-1], new TransactionL(get_non_overlaps(x, sell[len_stocks-1][n-1])));
        }

        x = cool_txn[len_stocks-1][n-1].getTransactionList_L();
        Collections.sort(x, (Transaction a, Transaction b) -> (b.profit - a.profit));
//        for(int i = 0 ; i < x.size(); i++){
//            System.out.println(x.get(i).getStockNumber() + " " + x.get(i).getBuyDay() + " " + x.get(i).getSellDay());
////                " " + x.get(i).getProfit());
//        }
        // System.out.println("profit:-"+cool[len_stocks-1][n-1]);
        return new TransactionTuple(cool[len_stocks-1][n-1],  new TransactionL(get_non_overlaps(x, cool[len_stocks-1][n-1])));
    }

    public static Boolean overlaps(Transaction i, List<Transaction> good){
        for(Transaction g : good){
            if(!(i.getSellDay() <= g.getBuyDay() || i.getBuyDay() >= g.getSellDay())){
                return true;
            }
        }
        return false;
    }

    public static List<Transaction> get_non_overlaps(List<Transaction> l, int total){
        List<Transaction> good = new ArrayList<>();
        for(Transaction cur : l){
            if(overlaps(cur, good)){
                continue;
            }

            good.add(cur);
            total = total - cur.getProfit();
            if(total <= 0){
                break;
            }
        }
        return good;
    }
    

    public static TransactionTuple task7(int[][] price_at_day, int cool_down){
        price_at_day_8a = price_at_day;
        cool_down_7 = cool_down;
        TransactionTuple res_tuple = task7_helper(0, false, 0);
        return res_tuple;

    }
    
    public static TransactionTuple task7_helper(int i, Boolean sell, int m){
        // no of stocks
        int len_stocks = price_at_day_8a.length;
        // no of days
        int len_days = price_at_day_8a[0].length;

        if(i >= len_days){
            return new TransactionTuple(0, new TransactionL()); 
        }

        if(i == len_days-1){
            if(sell){
                return new TransactionTuple(price_at_day_8a[m][i], new TransactionL(new ArrayList<>(Arrays.asList(new Transaction(m, -1, i, price_at_day_8a[m][i])))));
            }else{
                return new TransactionTuple(0, new TransactionL());
            }
        }

        if(sell){
            TransactionTuple tuple1 = task7_helper(i+1, true, m);
            TransactionTuple tuple2 = task7_helper(i+cool_down_7+1, false, m);

            int val1 = tuple1.getProfit();
            int val2 = tuple2.getProfit();

            TransactionL x1 = tuple1.getTransactionList();
            TransactionL x2 = tuple2.getTransactionList();

            if(val1 > val2 + price_at_day_8a[m][i]){
                return new TransactionTuple(val1, x1.add(new Transaction(m, -1, i, price_at_day_8a[m][i])));
            }
            return new TransactionTuple(val2 + price_at_day_8a[m][i], x2.add(new Transaction(m, -1, i, price_at_day_8a[m][i])));
        }else{
            // If buy_state is true

            // parameter that will help us pick transactions with maximum profit
            int total_max = 0;
            // Initialising a list that will hold our final transactions
            List<Transaction> x3 = new ArrayList<>();
            for(int m1 = 0; m1 < len_stocks ; m1++){
                // If buy_state is true, we will be checking for all stocks and see buying which stock at a particular day gives us maximum profit
                
                // Again we have 2 possible choices:-
                // case:-1.Don't buy on current day, so buy_state for next day will be true and no transactions are made, hence k remains unchanged
                TransactionTuple tuple1 = task7_helper(i+1, false, m1);
                // case:-2.Buy on cur day, then sell_state of next day will be true. A transaction is complete when a stock has been sold. Hence k remains unchanged.
                TransactionTuple tuple2 = task7_helper(i+1, true, m1);

                int val1 = tuple1.getProfit();
                int val2 = tuple2.getProfit();

                    
                TransactionL x1 = tuple1.getTransactionList();
                TransactionL x2 = tuple2.getTransactionList();

                // for case2:- we are spending money to buy stock on cur day, hence total_profit_so_far = total_profit_so_far - cur_price
                val2 = val2 - price_at_day_8a[m1][i];

                // to store all stocks who has buy day
                List<Transaction> xf = new ArrayList<>();
                // to store all those stocks who doesn't have a buy day till we get result from case:-2
                List<Transaction> xf2 = new ArrayList<>();

                for(Transaction t : x2.getTransactionList_L()){
                    if(t.getBuyDay() == -1){
                        // making the buy day as i for all stocks not having a buy day
                        xf2.add(new Transaction(t.getStockNumber(), i, t.getSellDay(), t.getProfit() - price_at_day_8a[m1][i]));
                    }else{
                        xf.add(t);
                    }
                }

                // Sorting the list of all stocks in descending order of profit whose buy day we set as i.
                Collections.sort(xf2, ((Transaction a, Transaction b) -> (b.profit - a.profit)));

                // to store maximum value of profit gained from case:-1 and case:-2 above
                int temp = Math.max(val1, val2);

                // we are appending to final list of transactions only if we have found a bigger profit value
                if(total_max < temp){
                    // if profit gained from case:-1 is equal to new max, we will add the list gained from case:-1 to final
                    if(val1 == temp){
                        x3 = x1.getTransactionList_L();
                    }else{
                        // when val2 = temp :- means profit gained from case:-2 is new max, we are adding topmost element with max_profit from  the list whose buy_day we set to i to the list of transactions so far
                        xf.addAll(xf2.subList(0, 1));
                        x3 = new ArrayList<>(xf);
                    }
                    // updating max_profit so far
                    total_max = temp;
                }
                temp = Math.max(val1, val2);
                if(total_max < temp){
                    total_max = temp;
                }

        }

            return new TransactionTuple(total_max, new TransactionL(x3));
        }
    }

    public static TransactionTuple task9a(int[][] price_at_day, int cool_down){
        price_at_day_9a = price_at_day;
        cool_down_9a = cool_down;
        // no of stocks
        int len_stocks = price_at_day_9a.length;
        // no of days
        int len_days = price_at_day_9a[0].length;

        memo_8a = new int[len_stocks][len_days][2];
        txn_8a = new TransactionL[len_stocks][len_days][2];
        Util.default_fill_3D(memo_8a, -1);
        Util.default_fill_txn_3D(txn_8a, new TransactionL());
        TransactionTuple res_tuple = task9a_helper(0, false, 0);
        return res_tuple;

    }

    public static TransactionTuple task9a_helper(int i, Boolean sell, int m){
        // no of stocks
        int len_stocks = price_at_day_9a.length;
        // no of days
        int len_days = price_at_day_9a[0].length;

        if(i >= len_days){
            return new TransactionTuple(0, new TransactionL());
        }

        if(sell){
            if(memo_8a[m][i][1] != -1){
                return new TransactionTuple(memo_8a[m][i][1], txn_8a[m][i][1]);
            }
        }else{
            if(memo_8a[m][i][0] != -1){
                return new TransactionTuple(memo_8a[m][i][0], txn_8a[m][i][0]);
            }

        }

        if(i == len_days-1){
            if(sell){
                memo_8a[m][i][1] = price_at_day_9a[m][i];
                txn_8a[m][i][1] = new TransactionL(new ArrayList<>(Arrays.asList(new Transaction(m, -1, i, price_at_day_9a[m][i]))));
                return new TransactionTuple(memo_8a[m][i][1], txn_8a[m][i][1]);
            }else{
                return new TransactionTuple(0, new TransactionL());
            }
        }

        if(sell){
            TransactionTuple tuple1 = task9a_helper(i+1, true, m);
            TransactionTuple tuple2 = task9a_helper(i+cool_down_9a+1, false, m);

            int val1 = tuple1.getProfit();
            int val2 = tuple2.getProfit();

            TransactionL x1 = tuple1.getTransactionList();
            TransactionL x2 = tuple2.getTransactionList();

            if(val1 > val2 + price_at_day_9a[m][i]){
                memo_8a[m][i][1] = val1;
                txn_8a[m][i][1] = x1.add(new Transaction(m, -1, i, price_at_day_9a[m][i]));
                return new TransactionTuple(val1, txn_8a[m][i][1]);
            }
            memo_8a[m][i][1] = val2 + price_at_day_9a[m][i];
            txn_8a[m][i][1] = x2.add(new Transaction(m, -1, i, price_at_day_9a[m][i]));
            return new TransactionTuple(memo_8a[m][i][1], txn_8a[m][i][1]);
        }else{
            // If buy_state is true

            // parameter that will help us pick transactions with maximum profit
            int total_max = 0;
            // Initialising a list that will hold our final transactions
            List<Transaction> x3 = new ArrayList<>();
            for(int m1 = 0; m1 < len_stocks ; m1++){
                // If buy_state is true, we will be checking for all stocks and see buying which stock at a particular day gives us maximum profit

                // Again we have 2 possible choices:-
                // case:-1.Don't buy on current day, so buy_state for next day will be true and no transactions are made, hence k remains unchanged
                TransactionTuple tuple1 = task9a_helper(i+1, false, m1);
                // case:-2.Buy on cur day, then sell_state of next day will be true. A transaction is complete when a stock has been sold. Hence k remains unchanged.
                TransactionTuple tuple2 = task9a_helper(i+1, true, m1);

                int val1 = tuple1.getProfit();
                int val2 = tuple2.getProfit();


                TransactionL x1 = tuple1.getTransactionList();
                TransactionL x2 = tuple2.getTransactionList();

                // for case2:- we are spending money to buy stock on cur day, hence total_profit_so_far = total_profit_so_far - cur_price
                val2 = val2 - price_at_day_9a[m1][i];

                // to store all stocks who has buy day
                List<Transaction> xf = new ArrayList<>();
                // to store all those stocks who doesn't have a buy day till we get result from case:-2
                List<Transaction> xf2 = new ArrayList<>();

                for(Transaction t : x2.getTransactionList_L()){
                    if(t.getBuyDay() == -1){
                        // making the buy day as i for all stocks not having a buy day
                        xf2.add(new Transaction(t.getStockNumber(), i, t.getSellDay(), t.getProfit() - price_at_day_9a[m1][i]));
                    }else{
                        xf.add(t);
                    }
                }

                // Sorting the list of all stocks in descending order of profit whose buy day we set as i.
                Collections.sort(xf2, ((Transaction a, Transaction b) -> (b.profit - a.profit)));

                // to store maximum value of profit gained from case:-1 and case:-2 above
                int temp = Math.max(val1, val2);

                // we are appending to final list of transactions only if we have found a bigger profit value
                if(total_max < temp){
                    // if profit gained from case:-1 is equal to new max, we will add the list gained from case:-1 to final
                    if(val1 == temp){
                        x3 = x1.getTransactionList_L();
                    }else{
                        // when val2 = temp :- means profit gained from case:-2 is new max, we are adding topmost element with max_profit from  the list whose buy_day we set to i to the list of transactions so far
                        xf.addAll(xf2.subList(0, 1));
                        x3 = new ArrayList<>(xf);
                    }
                    // updating max_profit so far
                    total_max = temp;
                }
                temp = Math.max(val1, val2);
                if(total_max < temp){
                    total_max = temp;
                }

            }

            memo_8a[m][i][0] = total_max;
            txn_8a[m][i][0] = new TransactionL(x3);
            return new TransactionTuple(total_max, txn_8a[m][i][0]);
        }
    }


    public static TransactionTuple task8(int[][] price_at_day, int cool_down){
//        Stocks
        int m=price_at_day.length;
//        days
        int n=price_at_day[0].length;

        int[][] profit = new int[m][n];

        for(int j = 1;j<n;j++){
            //corresponding buy
            int max_profit=Integer.MIN_VALUE;
            //finding the best buy for the specific sell
            for(int x=0;x<j;x++){

                for(int m1=0;m1<m;m1++){
                    // storing the transaction which maximize the profit
                    max_profit = Math.max(max_profit, price_at_day[m1][j]-price_at_day[m1][x]+((x-cool_down)<0?0:profit[m][x-cool_down]));

                }
                for(int m1=0;m1<m;m1++){
                    profit[m1][j] = Math.max(max_profit,profit[m1][j-1]);

                }
            }
        }

        // System.out.println(dp[m1-1][n-1]);
        return backTrack(price_at_day,profit,cool_down);
    }

    private static TransactionTuple  backTrack(int[][] price_at_day, int[][] profit, int cool_down) {
        int m=price_at_day.length;
        int n=price_at_day[0].length;

        ArrayList<Transaction> res_txn = new ArrayList<>();

        for(int j = n-1;j>0;j--){
            //corresponding buy
            int max_profit=Integer.MIN_VALUE;

            // storing the transaction which maximize the profit
            Transaction temp = new Transaction(-1,-1,-1,-1);
            res_txn.add(temp);
            //finding the best buy for the specific sell
            for(int x=j-1;x>=0;x--){

                //here checking for each stock
                for(int m1 = m-1;m1>=0;m1--){

                    if((price_at_day[m][j]-price_at_day[m][x]+((j-cool_down)<0?0:profit[m][x-cool_down]))>max_profit){

                        int cost = (price_at_day[m][j]-price_at_day[m][x]+((x-cool_down)<0?0:profit[m][x-cool_down]));
                        max_profit=cost;
                        //adding this transaction in the list for backtracking
                        temp = new Transaction(m1, x, j, max_profit);
//                        temp.add(0,m);temp.add(1,x);temp.add(2,j);
                    }
                }
            }
            res_txn.add(temp);

        }
//
//        ArrayList<Transaction> res_transactions = new ArrayList<>();
//        //making a pair for storing the transactions
//        Combo p = new Combo();
//        ArrayList<Combo>temp =  new ArrayList<Combo>(n);
//        for(int i=0;i<n;i++)temp.add( new Combo(-1, -1));
//
//        for(Transaction i:res_txn){
//            temp.add(i.getSellDay(), new Combo(i.getStockNumber(),i.getBuyDay()));
//
//        }
//
//        int last = res_txn.get(0).getBuyDay() - cool_down;
//
//        Transaction res_temp= new Transaction(res_txn.get(0).getStockNumber(), res_txn.get(0).getBuyDay(), );
//        //putting the first transaction from the last
//        res_temp.add(res_txn.get(0).getStockNumber());res_temp.add(res_txn.get(0).get(1));res_temp.add(res_txn.get(0).get(2));
//
//        res.add(res_temp);
//
//        while(last>=0&&temp.get(last).getValue()!=-1){
//
//            ArrayList<Integer>res_temp1= new ArrayList<Integer>();
//            res_temp1.add(temp.get(last).getKey());
//            res_temp1.add(temp.get(last).getValue());
//            res_temp1.add(last);
//            res.add(res_temp1);
//            //we can pick the next transaction only after cooldown
//            last= temp.get(last).getValue() - c;
//
//            if(last<=0)break;
//        }
//
//        //printing the transaction
//        for(int i=(res.size())- 1;i>=0;i--){
//
//            System.out.println((res.get(i).get(0)+1)+" "+ (res.get(i).get(1)+1)+" "+ (res.get(i).get(2)+1));
//
//        }
        return new TransactionTuple(0, new TransactionL());
    }

    /*
     * Runner for task7
     */
    public  void runner_task7(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter cool_down period, c");
        String input1 = scanner.nextLine(); 
        int c = Integer.parseInt(input1);

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
        TransactionTuple transaction = task7(price,c);
        List<Transaction> transaction_list = transaction.getTransactionList().getTransactionList_L();
        System.out.println("We're using 0-base indexing here");
        System.out.println("Result of task" + 7);
            
        Collections.sort(transaction_list, (Transaction a, Transaction b) -> (a.buy_day - b.buy_day));

        if(transaction_list.size() == 0){
            System.out.println("Maximum profit gained so far is 0. Hence, no need to do any transactions");
        }else{
            System.out.println("Transactions are:-");
            
            for(int i = 0 ; i < transaction_list.size(); i++){
                System.out.println(transaction_list.get(i).getStockNumber() + " " + transaction_list.get(i).getBuyDay() + " " + transaction_list.get(i).getSellDay());
                
            }
        }

    }

    /*
     * Runner for task8
     */
    public  void runner_task8(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter cool_down period, c");
        String input1 = scanner.nextLine();
        int c = Integer.parseInt(input1);

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
        TransactionTuple transaction = task8(price,c);
        List<Transaction> transaction_list = transaction.getTransactionList().getTransactionList_L();
        System.out.println("We're using 0-base indexing here");
        System.out.println("Result of task" + 8);

        Collections.sort(transaction_list, (Transaction a, Transaction b) -> (a.buy_day - b.buy_day));

        if(transaction_list.size() == 0){
            System.out.println("Maximum profit gained so far is 0. Hence, no need to do any transactions");
        }else{
            System.out.println("Transactions are:-");

            for(int i = 0 ; i < transaction_list.size(); i++){
                System.out.println(transaction_list.get(i).getStockNumber() + " " + transaction_list.get(i).getBuyDay() + " " + transaction_list.get(i).getSellDay());

            }
        }

    }

    /*
     * Runner for task9a
     */
    public  void runner_task9a(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter cool_down period, c");
        String input1 = scanner.nextLine();
        int c = Integer.parseInt(input1);

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
        TransactionTuple transaction = task9a(price,c);
        List<Transaction> transaction_list = transaction.getTransactionList().getTransactionList_L();
        System.out.println("We're using 0-base indexing here");
        System.out.println("Result of task 9a");

        Collections.sort(transaction_list, (Transaction a, Transaction b) -> (a.buy_day - b.buy_day));

        if(transaction_list.size() == 0){
            System.out.println("Maximum profit gained so far is 0. Hence, no need to do any transactions");
        }else{
            System.out.println("Transactions are:-");

            for(int i = 0 ; i < transaction_list.size(); i++){
                System.out.println(transaction_list.get(i).getStockNumber() + " " + transaction_list.get(i).getBuyDay() + " " + transaction_list.get(i).getSellDay());

            }
        }

    }

    /*
     * Runner for task9b
     */
    public  void runner_task9b(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter cool_down period, c");
        String input1 = scanner.nextLine();
        int c = Integer.parseInt(input1);

        System.out.println("Enter:- 1.number of stocks 2. number of days");
        String input2 = scanner.nextLine();
        String[] numbers = input2.split(" ");
        int m = Integer.parseInt(numbers[0]);
        int n = Integer.parseInt(numbers[1]);
        int[][] price = new int[m][n];

//        System.out.println("Enter each row:-");
        System.out.println("Enter each row:-");
        for (int i = 0; i < m; i++) {
            String line = scanner.nextLine();
            String[] row = line.split(" ");
            for (int j = 0; j < n; j++) {
                price[i][j] = Integer.parseInt(row[j]);
            }
        }
        scanner.close();
        TransactionTuple transaction = task9b(price,c);
        List<Transaction> transaction_list = transaction.getTransactionList().getTransactionList_L();
        System.out.println("We're using 0-base indexing here");
        System.out.println("Result of task 9b");

        Collections.sort(transaction_list, (Transaction a, Transaction b) -> (a.buy_day - b.buy_day));

        if(transaction_list.size() == 0){
            System.out.println("Maximum profit gained so far is 0. Hence, no need to do any transactions");
        }else{
            System.out.println("Transactions are:-");

            for(int i = 0 ; i < transaction_list.size(); i++){
                System.out.println(transaction_list.get(i).getStockNumber() + " " + transaction_list.get(i).getBuyDay() + " " + transaction_list.get(i).getSellDay());

            }
        }

    }
}
