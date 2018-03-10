/**
 * Group number: G27
 * Members:
 *      Gayathri Balakumar
 *      Susindaran Elangovan
 *      Vidya Gopalan
 *      Saikrishna Kanukuntla
 *
 * Short Project #4
 */

package cs6301.g27;

import java.util.Arrays;
import java.util.Scanner;

public class RearrangeMinus {
    /**
     * Method of reversing the elements of a subarray.
     * @param arr array to be reversed
     * @param p starting index of array
     * @param r last index of array
     */
    private static void reverse(int[] arr, int p, int r) {
        if ( p < r) {
            int c = arr[p];
            arr[p]= arr[r];
            arr[r] = c;
            reverse(arr, ++p, --r);
        }
    }

    /**
     *
     * @param arr is the Array which we pass on which the merge operation of negatives followed by positive happens
     * @param p is the starting index of left array
     * @param q is the last index of left array
     * @param r is the last index of right array
     * @variable j is initialized as starting index of right array.
     *
     *  The method rearranges the [Left-Negative, Left-Positive , Right- Negative , Right-Positive] to
     *  [ Left-negative , Right-negative , Left-Positive, Right-Positive] which returns [negative, positive] array
     *  preserving the order of occurence.
     *
     */
    static void merge(int arr[], int p, int q, int r) {
        int i = p;
        int j = q+1;

        while( i <= q && arr[i] < 0)
            i++;

        while( j <= r && arr[j] < 0)
            j++;

        // reverses the positive part of left-array
        reverse(arr, i ,q);
        //reverses the negative part of right-array
        reverse(arr, q+1, j-1);
        //reverses the subArray of negative and positive such that it preserves the order of occurence and gives an array
        //of negative and positive
        reverse(arr, i, j-1);
    }



    static void rearrangeMinusPlus(int[] arr, int p, int r) {
        if(p<r) {
            int q = (p + (r-1))/2;
            rearrangeMinusPlus(arr, p, q);
            rearrangeMinusPlus(arr, q+1, r);
            merge(arr, p, q, r);
        }
    }

    public static void main(String args[]) {
        //int arr[] = {-12, 11, -13, 6, -7, 5, -3, -6};

        Scanner in = new Scanner(System.in);
        System.out.println("Enter the size of array:");
        int n = in.nextInt();

        int arr[] = new int[n];
        System.out.println("Enter the elements of array:");
        for ( int i =0; i < n ; i++){
            arr[i]= in.nextInt();
        }

        rearrangeMinusPlus(arr,0, arr.length -1);
        System.out.println(Arrays.toString(arr));
    }

}

/*

Enter the size of array:
7
Enter the elements of array:
2
-3
-1
6
-10
7
9
[-3, -1, -10, 2, 6, 7, 9]

Enter the size of array:
8
Enter the elements of array:
-12
11
-13
6
-7
5
-3
-6
[-12, -13, -7, -3, -6, 11, 6, 5]
 */