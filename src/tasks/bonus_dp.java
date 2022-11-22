//package tasks;
//import java.util.*;
//import util.Pair;
///*
// * @author akashkumar
// * @created on 20/11/22
// */
//
//public class Task8{
//    public static void getMaxProfitForMStocks(int[][] stocksToPrices, int c) {
//        /*
//         * Using tabular approach and filling the DP
//         * dp matrix will store max profit we can make each day for each stock i.e.
//         * dp[i][j] = optimal value for ith stock on jth day.
//         */
//        int m1=stocksToPrices.length;
//        int n=stocksToPrices[0].length;
//
//        int[][] dp = new int[m1][n];
//
//        for(int sell = 1;sell<n;sell++){
//            //corresponding buy
//            int min_p=Integer.MIN_VALUE;
//            //finding the best buy for the specific sell
//            for(int buy=0;buy<sell;buy++){
//
//                for(int m=0;m<m1;m++){
//                    // storing the transaction which maximize the profit
//                    min_p = Math.max(min_p, stocksToPrices[m][sell]-stocksToPrices[m][buy]+((buy-c)<0?0:dp[m][buy-c]));
//
//                }
//                for(int m=0;m<m1;m++){
//                    dp[m][sell] = Math.max(min_p,dp[m][sell-1]);
//
//                }
//
//            }
//        }
//
//        // System.out.println(dp[m1-1][n-1]);
//
//        backTrack(stocksToPrices,dp,c);
//
//    }
//
//    public static void backTrack(int[][] stockData, int[][] dp, int c){
//        /*
//         * Using bottom approach utilizing the DP table , tracing the transaction
//         * dp matrix will store max profit we can make each day for each stock i.e.
//         * dp[i][j] = max profit value for ith stock on jth day.
//         */
//        int m1=stockData.length;
//        int n=stockData[0].length;
//
//        ArrayList<ArrayList<Integer>> txn = new ArrayList<ArrayList<Integer>>();
//
//        for(int sell = n-1;sell>0;sell--){
//            //corresponding buy
//            int min_p=Integer.MIN_VALUE;
//
//            // storing the transaction which maximize the profit
//            ArrayList<Integer> temp = new ArrayList<Integer>(3);
//            temp.add(-1);temp.add(-1);temp.add(-1);
//            //finding the best buy for the specific sell
//            for(int buy=sell-1;buy>=0;buy--){
//
//                //here checking for each stock
//                for(int m=m1-1;m>=0;m--){
//
//                    //stockData[m][buy]+((buy-c)<0?0:dp[m][buy-c] means if we doing transaction at buy day we need to wait for cooldown
//                    if((stockData[m][sell]-stockData[m][buy]+((buy-c)<0?0:dp[m][buy-c]))>min_p){
//
//                        int cost = (stockData[m][sell]-stockData[m][buy]+((buy-c)<0?0:dp[m][buy-c]));
//                        min_p=cost;
//                        //adding this transaction in the list for backtracking
//                        temp.add(0,m);temp.add(1,buy);temp.add(2,sell);
//
//                    }
//
//                }
//
//            }
//            txn.add(temp);
//
//        }
//
//        ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
//        //making a pair for storing the transactions
//        Pair p = new Pair();
//        ArrayList<Pair>temp =  new ArrayList<Pair>(n);
//        for(int i=0;i<n;i++)temp.add( new Pair(-1, -1));
//
//        for(ArrayList<Integer> i:txn){
//            temp.add((Integer) i.get(2), new Pair((Integer) i.get(0), (Integer) i.get(1)));
//
//        }
//
//        int last = txn.get(0).get(1) - c;
//
//        ArrayList<Integer>res_temp= new ArrayList<Integer>();
//        //putting the first transaction from the last
//        res_temp.add(txn.get(0).get(0));res_temp.add(txn.get(0).get(1));res_temp.add(txn.get(0).get(2));
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
//
//    }
//
//    public static void runner() {
//        Scanner scn = new Scanner(System.in);
//        int c = scn.nextInt();
//        int m = scn.nextInt();
//        int n = scn.nextInt();
//
//        int[][] stocksToPrices = new int[m][n];
//
//        for(int i = 0; i < m; i++) {
//            for(int j = 0; j < n; j++) {
//                stocksToPrices[i][j] = scn.nextInt();
//            }
//        }
//
//        getMaxProfitForMStocks(stocksToPrices,c+1);
//
//    }
//
//
//}