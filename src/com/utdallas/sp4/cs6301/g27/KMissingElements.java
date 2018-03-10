package cs6301.g27;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by vidya on 9/21/17.
 */

public class KMissingElements {

    ArrayList<Integer> missing = new ArrayList<Integer>();

    public void findMissing(int[] A, int p, int r) {

        int q = (p + r) / 2;

        if (A[r] - A[p] == r - p || q == r) {
            return;
        } else if (p + 1 == r) {
            for (int i = A[p] + 1; i < A[r]; i++) {
                missing.add(i);
            }
        } else {
            findMissing(A, p, q);
            findMissing(A, q, r);

        }

    }

    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);
        System.out.println("Enter size of the array");
        int inputSize = in.nextInt();
        int[] arr = new int[inputSize];
        System.out.println("Enter elements in the array");
        for( int i = 0; i < arr.length; i++ )
        {
           arr[ i ] = in.nextInt();
        }

        ArrayList<Integer> missingNum = new ArrayList<Integer>();

        KMissingElements knum = new KMissingElements();

        knum.findMissing(arr, 0, arr.length - 1);
        missingNum = knum.missing;

        System.out.println(missingNum);

    }

}

