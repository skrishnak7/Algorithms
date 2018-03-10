package cs6301.g27;

/**
 * Group number: G27
 * Members:
 *      Gayathri Balakumar
 *      Susindaran Elangovan
 *      Vidya Gopalan
 *      Saikrishna Kanukuntla
 * */

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class ExactlyOnce {

    static<T extends Comparable<? super T>> ArrayList<T> exactlyOnce(T[] A) {

        Map<T,Integer> hm = new TreeMap<T,Integer>();
        ArrayList<T> list = new ArrayList<T>();

        for(int i = 0; i < A.length ; i++ ) {
            if(!hm.containsKey(A[i])) {
                hm.put(A[i], 1);
            }else {
                int count = hm.get(A[i]);
                hm.put(A[i], count + 1);
            }
        }

        for(int i = 0; i < A.length ;i++) {
            if(hm.get(A[i]) == 1)
                list.add(A[i]);
        }
        return list;
    }


    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);
        System.out.println("Enter size of the array");
        int inputSize = in.nextInt();
        Integer[] arr = new Integer[inputSize];
        System.out.println("Enter elements in the array");
        for( int i = 0; i < arr.length; i++ )
        {
            arr[i] = in.nextInt();
        }
        System.out.println( "Output: " + ExactlyOnce.exactlyOnce(arr));
        in.close();
    }
}
/**
 *
 Sample Input:

 Enter size of the array
 6
 Enter elements in the array
 6 3 4 5 3 5
 Output: [6, 4]

 */
