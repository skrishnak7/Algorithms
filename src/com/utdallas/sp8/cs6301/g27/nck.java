package cs6301.g27;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class nck {


    static int k;
    static int[] A;
    static int n;
    static int[] chosen;

    //List to store the values of various combinations.
    static List<String> result = new ArrayList<>();

    void combination(int i, int c) {
        if(c == 0) {
            result.add(Arrays.toString(chosen));
        }
        else {

            chosen[k-c]= A[i];
            combination(i+1,c-1);
            if(n-i > c)
                combination(i+1,c);
        }


    }
    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);

        System.out.print("Enter the size of the array (n): ");
        n = in.nextInt();
        System.out.print("Enter the value of the k: ");
        k = in.nextInt();
        System.out.print("VERBOSE(0/1) ");
        int VERBOSE = in.nextInt();
        System.out.print("Enter the elements in array: ");
        A = new int[n];

        for( int i = 0; i < n; i++ )
        {
            A[i] = in.nextInt();
        }

        chosen = new int[k];
        nck c = new nck();
        c.combination(0, k);


        if(VERBOSE == 0)
            System.out.println(result.size());
        else {
            for(String item : result) {
                System.out.println(item);
            }
        }
        in.close();
    }

}
/*
Enter the size of the array( n ): 5
Enter the value of the k: 3
VERBOSE(0/1) 0
Enter the elements in array: 1 2 3 4 5
10
 */
