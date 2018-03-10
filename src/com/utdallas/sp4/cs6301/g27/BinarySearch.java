package cs6301.g27;

import java.util.Scanner;

/**
 * Created by vidya on 9/21/17.
 */
public class BinarySearch {

    public static<T extends Comparable<? super T>> int binarySearch(T[] arr, T x)
    {
        //check if given element is lesser than the first element in the array
        if (arr[0].compareTo(x) > 0) return -1;
        else
        return BinarySearch.recursiveBinarySearch(arr, 0, arr.length-1, x);

    }


    public static<T extends Comparable<? super T>> int recursiveBinarySearch( T[] A, int p, int r, T x )
    {
        //middle element of A[p..r]
        int q = ( p + r ) / 2;
        // Compare middle element of A[p..r] to x to decide which half of the array to search
        if( p <= r )
        {
            if( A[ q ].compareTo(x) < 0 )
            {
                return recursiveBinarySearch( A, q + 1, r, x );
            }
            else if (  A[ q ].compareTo(x) == 0 ) return q;
            else
            {
                return recursiveBinarySearch( A, p, q - 1, x );
            }
        }
        else  return q;
    }
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter size of array");
        int inputSize = in.nextInt();
        Integer arr[] = new Integer[inputSize];
        for(int i=0;i<inputSize;i++)
        {
            arr[i]=in.nextInt();
        }
        System.out.println("Enter element to search");
        int x = in.nextInt();
        int result  = binarySearch(arr,x);
        System.out.println(result);


    }

}
