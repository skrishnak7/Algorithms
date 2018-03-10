package cs6301.g27;

import java.util.Arrays;
import java.util.Scanner;

public class Heap {

    void generate(Integer[] a, int n) {
        int[] c = new int[n];

        for(int i = 0; i < n; i++)
            c[i] = 0;

        System.out.println(Arrays.toString(a));

        int i = 0;

        while ( i < n){

            if( c[i] < i) {

                if(i%2 == 0) {
                    swap(a,0,i);
                }else {
                    swap(a,c[i],i);
                }

                System.out.println(Arrays.toString(a));

                c[i]++;
                i=0;
            }
            else {

                c[i] = 0;
                i++;
            }
        }
    }

    private void swap(Integer[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);
        System.out.println("Enter size of the array");
        int inputSize = in.nextInt();
        Integer[] A = new Integer[inputSize];
        System.out.println("Enter elements in the array");
        for( int i = 0; i < A.length; i++ )
        {
            A[i] = in.nextInt();
        }
        System.out.println( "Output: ");
        in.close();

        Heap h = new Heap();
        h.generate(A, A.length);

    }

}
/*
Enter size of the array
4
Enter elements in the array
1 2 3 4
Output:
[1, 2, 3, 4]
[2, 1, 3, 4]
[3, 1, 2, 4]
[1, 3, 2, 4]
[2, 3, 1, 4]
[3, 2, 1, 4]
[4, 2, 1, 3]
[2, 4, 1, 3]
[1, 4, 2, 3]
[4, 1, 2, 3]
[2, 1, 4, 3]
[1, 2, 4, 3]
[1, 3, 4, 2]
[3, 1, 4, 2]
[4, 1, 3, 2]
[1, 4, 3, 2]
[3, 4, 1, 2]
[4, 3, 1, 2]
[4, 3, 2, 1]
[3, 4, 2, 1]
[2, 4, 3, 1]
[4, 2, 3, 1]
[3, 2, 4, 1]
[2, 3, 4, 1]

 */
