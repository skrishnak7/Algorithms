package cs6301.g27;

/**
 * Group number: G27
 * Members:
 *      Gayathri Balakumar
 *      Susindaran Elangovan
 *      Vidya Gopalan
 *      Saikrishna Kanukuntla
 * */

import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class LongestStreak {
    /**
     * @param A : an array with duplicates
     * @return the length of a longest streak of consecutive integers that occur in A
     */
    public static int longestStreak(int[] A) {


        int count = 1,maxCount = 1;

        TreeSet<Integer> set = new TreeSet<Integer>();

        for(int i = 0; i < A.length ; i++)
            set.add(A[i]);

        Iterator<Integer> it = set.iterator();
        int prev = it.next(), curr = 0;

        while(it.hasNext()){
            curr = it.next();
            if(curr - prev == 1) {
                count++;
                maxCount = (maxCount < count) ? count : maxCount;
            }
            else {
                count = 1;
            }
            prev = curr;
        }
        return maxCount;
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
        System.out.println( "Output: " + longestStreak(arr));
        in.close();
    }

}
/*
Enter size of the array
16
Enter elements in the array
1 7 9 4 1 7 4 8 7 1 10 6 6 9 11 12
Output: 7
 */

