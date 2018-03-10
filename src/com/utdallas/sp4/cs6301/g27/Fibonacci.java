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

import java.math.BigInteger;
import java.util.Scanner;

public class Fibonacci {

    static BigInteger linearFibonacci(int n) {
        BigInteger fib1= BigInteger.ZERO, fib2 = BigInteger.ONE;
        BigInteger fibn = fib1.add(fib2);

        if( fibn.compareTo(fib2) < 0)
            return fib2;
        else {
            for (int i = 2; i <= n; i++) {
                fibn = fib1.add(fib2);
                fib1 = fib2;
                fib2 = fibn;
            }
            return fibn;
        }
    }


    static BigInteger[][] power(BigInteger F[][], BigInteger n) {
        BigInteger two = BigInteger.valueOf(2);
        if (n.equals(BigInteger.ONE))
            return F;
        else {
            BigInteger X[][] = power(F, n.divide(two));
            if( n.mod(two).equals(BigInteger.ZERO)) {
                return multiply(X,X);
            }
            else {
                return multiply(multiply(X,X),F) ;
            }
        }
    }


    private static BigInteger[][] multiply(BigInteger[][] f1, BigInteger[][] f2) {
        int n1 = f1.length, m1 = f1[0].length, m2=f2[0].length ;
        if( m1 != n1)
            throw new RuntimeException("Incompatible Matrix dimensions to multiply");

        BigInteger result[][] = new BigInteger[n1][m2];
        //Initialization of the result array to be returned.
        for(int l=0; l<n1; l++)
            for(int h=0; h <m2 ; h++)
                result[l][h]= BigInteger.ZERO;

        for(int i = 0; i < n1; i++)
            for(int j =0; j < m2; j++)
                for(int k=0; k < m1; k++) {
                    BigInteger temp =f1[i][k].multiply(f2[k][j]);
                    result[i][j]=result[i][j].add(temp);
                }
        return result;
    }


    /**
     * O(logn) for cs6301.g27.Fibonacci
     * It uses the divide and conquer approach power method
     * and returns the (0,0)th index element which corresponds to Fib(n)
     */
    static BigInteger logFibonacci(int n) {
        BigInteger Fib[][] = {{ BigInteger.ONE , BigInteger.ONE}, {BigInteger.ONE , BigInteger.ZERO}} ;
        BigInteger r[][] = power(Fib,BigInteger.valueOf(n-1));
        return r[0][0] ;
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        in.close();

        System.out.println(logFibonacci(n).toString());
        System.out.println(linearFibonacci(n).toString());
    }

}
