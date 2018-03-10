package cs6301.g27;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    public static class A
    {
        boolean flag;

        @Override
        public String toString() {
            return Boolean.toString(flag);
        }
    }
    public static void main(String[] args) {
        A a = new A();
        List<A> l1 = new ArrayList<>(), l2 = new ArrayList<>();
        l1.add(a);
        l2.add(a);

        System.out.println(Arrays.toString(l1.toArray()));
        System.out.println(Arrays.toString(l2.toArray()));

        l1.get(0).flag = true;

        System.out.println(Arrays.toString(l1.toArray()));
        System.out.println(Arrays.toString(l2.toArray()));
    }
}
