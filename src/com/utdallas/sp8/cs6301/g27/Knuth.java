package cs6301.g27;

import java.util.Arrays;
import java.util.Scanner;

public class Knuth {

    static void KnuthL(int[] A){


        System.out.println(Arrays.toString(A));

        while(true) {

            int j = A.length - 2;

            while(A[j] >= A[j+1]) {
                if( j == 0) System.exit(0);
                j--;
            }

            int l = A.length-1;

            while(A[j] >= A[l]) {
                l--;
            }

            swap(A,j,l);

            int k = j+1;
            l = A.length-1;

            while( k < l) {
                swap(A,k,l);
                k++;
                l--;
            }

            System.out.println(Arrays.toString(A));
        }
    }

    private static void swap( int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;

    }


    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);
        System.out.print("Enter the size of the array: ");

        int n = in.nextInt();
        int[] A = new int[n];

        System.out.println("Enter elements in the array");
        for( int i = 0; i < A.length; i++ )
        {
            A[i] = in.nextInt();
        }
        System.out.println( "Output: ");

        Arrays.sort(A);

        KnuthL(A);
        in.close();
    }

}
/*
 *
Enter the size of the array: 3
Enter elements in the array
1 2 3
Output:
[1, 2, 3]
[1, 3, 2]
[2, 1, 3]
[2, 3, 1]
[3, 1, 2]
[3, 2, 1]

Process finished with exit code 0
 *
 */
