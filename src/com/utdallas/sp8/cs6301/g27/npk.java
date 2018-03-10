package cs6301.g27;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class npk {

    static int k;
    static int[] A;
    static int n;
    //List to store the values of various permutations.
    static List<String> result = new ArrayList<>();

    void permute(int c) {
        if (c == 0) {
            result.add(Arrays.toString(A));
        }
        else {
            int d = k - c;
            permute(c-1);
            for(int i = d+1; i< A.length; i++) {

                int temp = A[d]; A[d] = A[i]; A[i]= temp;
                permute(c-1);
                A[i]=A[d]; A[d] = temp;

            }
        }
    }

    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);

        System.out.print("Enter the size of the array( n ): ");
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

        npk p = new npk();
        p.permute(k);


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
