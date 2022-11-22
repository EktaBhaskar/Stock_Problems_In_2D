// import tasks.*;
import tasks.*;
//import tasks.Stocks_1;


import java.io.IOException;

public class Driver {
    public static void main(String[] args) throws IOException {
        if(args[0].equals("1")){
            System.out.println("Implementation of Task1 to solve problem using brute force in 0(m * n^2) time");
            Stocks_1 task1 = new Stocks_1();
            task1.runner_task1();
        } else if(args[0].equals("2")){
            System.out.println("Implementation of Task2 to solve problem using greedy algorithm in 0(m * n) time");
            Stocks_1 task2 = new Stocks_1();
            task2.runner_task2();
        }
        else if(args[0].equals("3a")){
            System.out.println("Implementation of Task3a to solve problem using Memoization top-down dp algorithm in 0(m * n) time");
            Stocks_1 task3A = new Stocks_1();
            task3A.runner_task3a();
        } else if (args[0].equals("3b")){
            System.out.println("Implementation of Task3b to solve problem using iterative BottomUp implementation dynamic programming algorithm in 0(m * n) time");
            Stocks_1 task3B = new Stocks_1();
            task3B.runner_task3b();
        } else if(args[0].equals("4")){
            System.out.println("Implementation of Task4 to solve problem using brute force algorithm in 0(m * n^2k) time");
            Stock_2 task4 = new Stock_2();
            task4.runner_task4();
        } else if(args[0].equals("5")){

            System.out.println("Implementation of Task5 to solve problem using dynamic programming algorithm in 0(m * n^2 * k) time");
            Stock_2 task5 = new Stock_2();
            task5.runner_task5();

        } else if(args[0].equals("6a")){
            System.out.println("Implementation of Task6a to solve problem  using top-down Memoization dp algorithm in 0(m * n * k) time");
            Stock_2 task6a = new Stock_2();
            task6a.runner_task6a();
        }else if(args[0].equals("6b")){
            System.out.println("Implementation of Task6b to solve problem using bottom up iterative dynamic programming algorithm in 0(m * n * k) time");
            Stock_2 task6b = new Stock_2();
            task6b.runner_task6b();
        }else if(args[0].equals("7")){
            System.out.println("Implementation of Task7 to solve problem using brute force algorithm in 0(m * 2^n) time");
            Stock_3 task7 = new Stock_3();
            task7.runner_task7();
        }
         else if(args[0].equals("8")){
             System.out.println("Implementation of Task8 to solve problem using dynammic programming algorithm in 0(m * n^2) time");
             Stock_3 task8 = new Stock_3();
             task8.runner_task8();
         }
         else if(args[0].equals("9a")){
             System.out.println("Implementation of Task9a to solve problem using top-down memoization dp algorithm in 0(m * n) time");
             Stock_3 task9 = new Stock_3();
             task9.runner_task9a();
         }
         else if(args[0].equals("9b")){
             System.out.println("Implementation of Task9b to solve problem using bottom-up iterative dp algorithm in 0(m * n) time");
             Stock_3 task9 = new Stock_3();
             task9.runner_task9b();
         }
    }
}

