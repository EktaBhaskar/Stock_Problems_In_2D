package tasks;

//class to hold all utility methods
public class Util {
    
    /*
     * Fills a 3-D matrix with a desired values
     */
    public static void default_fill_3D(int[][][] input, int val){
        int m = input.length;
        int k = input[0].length;
        int n = input[0][0].length;

        for(int i = 0 ; i < m ; i++){
            for(int j = 0; j < k ; j++){
                for(int l = 0 ; l < n ; l++){
                    input[i][j][l] = val;
                }
            }
        }
    }

    /*
     * Fills a 2-D matrix with a desired values
     */
    public static void default_fill_2D(int[][] input, int val){
        int m = input.length;
        int k = input[0].length;

        for(int i = 0 ; i < m ; i++){
            for(int j = 0; j < k ; j++){
                input[i][j] = val;
            }
        }
    }

     /*
     * Fills a 2-D matrix of transactions with a desired values
     */
    public static void default_fill_txn_2D(TransactionL[][] input, TransactionL val){
        int m = input.length;
        int k = input[0].length;

        for(int i = 0 ; i < m ; i++){
            for(int j = 0; j < k ; j++){
                input[i][j] = val;
            }
        }
    }

    /*
     * Fills a 3-D matrix of transactions with a desired values
     */
    public static void default_fill_txn_3D(TransactionL[][][] input, TransactionL val){
        int m = input.length;
        int k = input[0].length;
        int l = input[0][0].length;

        for(int i = 0 ; i < m ; i++){
            for(int j = 0; j < k ; j++){
                for(int p = 0 ; p < l ;p++){
                    input[i][j][p] = val;
                }
            }
        }
    }

    /*
     * Method to compare 2 transactions based on profit made so far
     * @return Transaction with max_proft
     */
    public static Transaction compare(Transaction a, Transaction b){
        if(a.profit > b.profit){
            return a;
        }else{
            return b;
        }
    }

    
}
