package cs6301.g27;

/**
 * Group number: G27
 * Members:
 *      Gayathri Balakumar
 *      Susindaran Elangovan
 *      Vidya Gopalan
 *      Saikrishna Kanukuntla
 * */
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class BST<T extends Comparable<? super T>> implements Iterable<T> {
    protected static class Entry<T> {
        protected T element;
        protected Entry<T> left, right;

        Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
            this.left = left;
            this.right = right;
        }

        protected boolean isLeaf() {
            return ((left == null) && (right == null));
        }
    }

    protected Entry<T> root;
    int size;

    public BST() {
        root = null;
        size = 0;
    }


    /** Is x contained in tree?
     */
    public boolean contains(T x) {
        Entry<T> t = find(x);
        return t!=null && t.element == x;
    }

    /** Is there an element that is equal to x in the tree?
     *  Element in tree that is equal to x is returned, null otherwise.
     */
    public T get(T x) {
        Entry<T> t = find(x);
        return t!=null? t.element : null ;
    }

    /** Add x to tree.
     *  If tree contains a node with same key, replace element by x.
     *  Returns true if x is a new element added to tree.
     */


    protected Entry<T> getEntry(T x){
        return new Entry<>(x, null, null);
    }

    // private Deque<Entry<T>> entriesStack =  new ArrayDeque<>();

    public boolean add(T x) {
        Entry<T> newNode = this.addEntry(x);
        return newNode!=null;
    }

    public Entry<T> addEntry(T x) {

        Entry<T> newEntry = this.getEntry(x);
        if (this.root == null) {
            this.root = newEntry;
            size=1;
            return root;
        }

        Entry<T> entry = find(x);
        if(x.compareTo(entry.element) == 0) {
            entry.element = x; //replace existing element;
            return null;
        }
        else if (x.compareTo(entry.element) < 0)
            entry.left = newEntry;
        else entry.right = newEntry;


        size++;
        stack.push(entry);
        return newEntry;
    }




    protected Deque<Entry<T>> stack; //stack used to store the path from root.
    private Entry<T> find(T x) {
        stack = new ArrayDeque<>();
        return find(root,x);
    }


    private Entry<T> find(Entry<T> entry, T x) {
        if( entry== null || entry.element == x)
            return entry;

        while(true) {
            if(x.compareTo(entry.element)< 0) {
                if(entry.left == null)
                    break;
                else {
                    stack.push(entry);
                    entry = entry.left;
                }
            }else if ( x == entry.element)
                break;
            else {
                if(entry.right == null) break;
                else {
                    stack.push(entry);
                    entry = entry.right;
                }
            }
        }
        return entry;
    }


    /** Remove x from tree.
     *  Return x if found, otherwise return null
     */
    public T remove(T x) {
        if(root == null) return null;
        Entry<T> t = find(x);
        if(t.element != x)
            return null;

        T result = t.element;

        if(t.left == null || t.right == null)
            bypass(t);
        else {
            stack.push(t);
            Entry<T> minRight = find(t.right,t.element);
            t.element = minRight.element;
            bypass(minRight);
        }
        size--;
        return result;
    }

    private void bypass(Entry<T> t) {
        Entry<T> pt = stack.peek();
        Entry<T> c = (t.left == null) ? t.right :t.left;

        if( pt == null)
            root = c;
        else if (pt.left == t)
            pt.left = c;
        else
            pt.right = c;
    }


    /** TO DO: Iterate elements in sorted order of keys
     */
    public Iterator<T> iterator() {

        Deque<Entry<T>> stack = new ArrayDeque<>();
        Entry<T> t = root;
        while(t != null) {
            stack.push(t);
            t = t.left;
        }

        Iterator<T> it = new Iterator<T>() {
            @Override
            public boolean hasNext() { return !stack.isEmpty();}

            @Override
            public T next() {
                Entry<T> temp = stack.pop();
                Entry<T> current = temp.right;
                while(current != null) {stack.push(current); current = current.left;}
                return temp.element;
            }

        };

        return it;
    }

    public static void main(String[] args) {
        BST<Integer> t = new BST<>();
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            int x = in.nextInt();
            if(x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                //t.checkStack();
                t.printTree();
            } else if(x < 0) {
                System.out.print("Remove " + x + " : ");
                t.remove(-x);
                t.printTree();
            } else {
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for(int i=0; i<t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
                return;
            }
        }
    }

    private List<T> result ;


    void listTree(Entry<T> node) {
        if(node != null) {
            listTree(node.left);
            result.add(node.element);
            listTree(node.right);
        }
    }

    public Comparable[] toArray() {
        result = new ArrayList<T>();
        listTree(root);
        Comparable[] arr = result.toArray(new Comparable[] {});
        return arr;
    }

    public void printTree() {
        System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }


    // In Order traversal of tree
    void printTree(Entry<T> node) {
        if(node != null) {
            printTree(node.left);
            System.out.print( " " +node.element );
            printTree(node.right);
        }
    }



}
/*
Sample input:
1 3 5 7 9 2 4 6 8 10 -3 -6 -3 0
Output:
Add 1 : [1] 1
Add 3 : [2] 1 3
Add 5 : [3] 1 3 5
Add 7 : [4] 1 3 5 7
Add 9 : [5] 1 3 5 7 9
Add 2 : [6] 1 2 3 5 7 9
Add 4 : [7] 1 2 3 4 5 7 9
Add 6 : [8] 1 2 3 4 5 6 7 9
Add 8 : [9] 1 2 3 4 5 6 7 8 9
Add 10 : [10] 1 2 3 4 5 6 7 8 9 10
Remove -3 : [9] 1 2 4 5 6 7 8 9 10
Remove -6 : [8] 1 2 4 5 7 8 9 10
Remove -3 : [8] 1 2 4 5 7 8 9 10
Final: 1 2 4 5 7 8 9 10
*/