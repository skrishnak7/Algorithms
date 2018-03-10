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
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;


public class HowMany {

    /**
     * @param A Array in which we have find out pairs which sum to target
     * @param X The Target sum
     * @return The number of pairs which sum to Target X.
     */
    static int howMany(int[] A, int X) {

        TreeMap<Integer,ArrayList<Integer>> map = new TreeMap<>();
        int count = 0;

		/*
		 * putting all the keys which their indexes in the array.
		 * <K,V> for [3 4 4 5 3 4 4 5]  would be
		 * {<3,[0,4]> , <4,[1,2,6,]> , <5 ,[3,7]>}
		 */
        for(int i = 0; i < A.length ; i++) {
            if( !map.containsKey(A[i])) {
                map.put(A[i], new ArrayList<Integer>());
                map.get(A[i]).add(i);
            }
            else
                map.get(A[i]).add(i);
        }

        //Creating a set to iterate over the key set.
        Set<Integer> c = map.keySet();
        Iterator<Integer> it = c.iterator();

        while(it.hasNext()) {
            int t = it.next();
            if(map.containsKey(X - t)) {
                ArrayList<Integer> result1 = map.get(X-t);
                ArrayList<Integer> result2 = map.get(t);
                int r1 = result1.size();
                int r2 = result2.size();

                if(result1 == result2 && r1>1) {
                    count += r1*(r1 -1)/2; // case where the key is same but have more than one index like 4 + 4 == 8 case. Here i calculate nC2.
                }
                else if( r1 != 1 || r2 != 1) {
                    count += r1*r2;			// count of number of pairs of distinct keys which sum to target.
                }
            }
            it.remove();
        }
        return count;
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
        System.out.println("Enter the Target sum X:");
        int X = in.nextInt();
        System.out.println( "Output: " + howMany(arr,X));
        in.close();

    }

}
/*
Enter size of the array
8
Enter elements in the array
3 4 4 5 3 4 4 5
Enter the Target sum X:
8
Output: 10
 */
