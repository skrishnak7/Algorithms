package cs6301.g27;

/**
 * Group number: G27
 * Members:
 *      Gayathri Balakumar
 *      Susindaran Elangovan
 *      Vidya Gopalan
 *      Saikrishna Kanukuntla
 * */

import java.util.Scanner;


public class AVLTree<T extends Comparable<? super T>> extends BST<T> {
    static class AVLEntry<T> extends BST.Entry<T> {
        int height;
        AVLEntry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            height = 0;
        }

        public int getHeight() {
            return height;
        }

        protected int updateHeight() {
            if(!this.isLeaf())
            {int leftHeight = 0, rightHeight = 0;
                if (left != null) {
                    AVLEntry<T> leftAVLEntry = (AVLEntry<T>) left;
                    leftHeight = leftAVLEntry.height;
                }

                if (right != null) {
                    AVLEntry<T> rightAVLEntry = (AVLEntry<T>) right;
                    rightHeight = rightAVLEntry.height;
                }

                height = Math.max(leftHeight, rightHeight) + 1;}
            return height;
        }
        protected boolean isBalanced(){
            return this.getBalanceFactor() >= -1 && this.getBalanceFactor() <=1;
        }
        protected int getBalanceFactor() {
            int leftHeight = -1 , rightHeight = -1;
            if (left != null) {
                AVLEntry<T> leftAVLEntry = (AVLEntry<T>) left;
                leftHeight = leftAVLEntry.height;
            }

            if (right != null) {
                AVLEntry<T> rightAVLEntry = (AVLEntry<T> ) right;
                rightHeight = rightAVLEntry.height;
            }
            return rightHeight - leftHeight;
        }

        public String toString() {
            return "Element=" + element + " height=" + height + " left=" + ((left != null) ? left.element : "NULL") + " right=" + ((right != null) ? right.element : "NULL");
        }

    }

    AVLTree() {
        super();
    }


    protected AVLEntry<T> getEntry(T x){
        return new AVLEntry<>(x, null, null);
    }

    @Override
    public boolean add(T x) {
        boolean balanceFlag =false;
        Entry<T> nodeAdded = super.addEntry(x);
        AVLEntry<T> newNode =  (AVLEntry<T>) nodeAdded;
        newNode.updateHeight();
        if(nodeAdded != root){
            //if(!newNode.isBalanced()) balance(newNode);
            AVLEntry<T> balancedNode = null;
            while (!stack.isEmpty() ){
                newNode = (AVLEntry<T>) stack.pop();
                //System.out.println("before" +newNode);
                if(balanceFlag){
                    if(newNode.element.compareTo(balancedNode.element) >0)
                        newNode.left = balancedNode ;
                    else newNode.right = balancedNode ;

                    //System.out.println("after" +newNode);
                }
                newNode.updateHeight();
                //System.out.println("balance factor of" +newNode.element +"is" +newNode.getBalanceFactor());
                if(!newNode.isBalanced())
                {balanceFlag = true;
                    balancedNode = balanceInsert(newNode);
                    //System.out.println("b" +balancedNode);

                }
            }
        }

        return newNode!=null;
    }

    protected AVLEntry<T> leftRotate(AVLEntry<T> p){
        AVLEntry<T> q = (AVLEntry<T>) p.right;
        AVLEntry<T> qChild = q.left!=null ?(AVLEntry<T>) q.left : null;

        q.left = p;
        p.right = qChild;

        AVLEntry<T> qleft = p.left !=null ? (AVLEntry<T> ) q.left : null;
        int qleftHeight = qleft !=null? qleft.getHeight() : -1;
        AVLEntry<T> qright = q.right !=null ? (AVLEntry<T> ) q.right : null;
        int qrightHeight = qright !=null? qright.getHeight() : -1;
        q.height = Math.max( qleftHeight,  qrightHeight) + 1;

        AVLEntry<T> pleft = p.left !=null ? (AVLEntry<T> ) p.left : null;
        int pleftHeight = pleft !=null? pleft.getHeight() : -1;
        AVLEntry<T> pright = p.right !=null ? (AVLEntry<T> ) p.right : null;
        int prightHeight = pright !=null? pright.getHeight() : -1;
        p.height = Math.max( pleftHeight, prightHeight) + 1;

        return q;

    }

    protected AVLEntry<T> rightRotate( AVLEntry<T> q){
        AVLEntry<T> p = (AVLEntry<T>) q.left;
        AVLEntry<T> pChild = p.right!=null ? (AVLEntry<T>) p.right : null;

        p.right = q;
        q.left = pChild;


        AVLEntry<T> qleft = p.left !=null ? (AVLEntry<T> ) q.left : null;
        int qleftHeight = qleft !=null? qleft.getHeight() : -1;
        AVLEntry<T> qright = q.right !=null ? (AVLEntry<T> ) q.right : null;
        int qrightHeight = qright !=null? qright.getHeight() : -1;
        q.height = Math.max( qleftHeight,  qrightHeight) + 1;

        AVLEntry<T> pleft = p.left !=null ? (AVLEntry<T> ) p.left : null;
        int pleftHeight = pleft !=null? pleft.getHeight() : -1;
        AVLEntry<T> pright = p.right !=null ? (AVLEntry<T> ) p.right : null;
        int prightHeight = pright !=null? pright.getHeight() : -1;
        p.height = Math.max( pleftHeight, prightHeight) + 1;

        return p;

    }


    private AVLEntry<T> balanceInsert(AVLEntry<T> node)
    {
        //System.out.println("balance needed");
        int balanceFactor = node.getBalanceFactor();
        if(!node.isBalanced()){
            AVLEntry<T> child = null;
            if (balanceFactor < 0) {
                child = (AVLEntry<T>) node.left;
                balanceFactor = child.getBalanceFactor();
                if (balanceFactor < 0)
                    node= rightRotate(node);
                else
                {
                    node.left = leftRotate(child);
                    node = rightRotate(node);

                }
            } else {
                child = (AVLEntry<T>) node.right;
                balanceFactor = child.getBalanceFactor();
                if (balanceFactor < 0)
                {
                    node.right = rightRotate(child);
                    node = leftRotate(node);

                }
                else
                    node = leftRotate(node);

            }
        }
        return node;
    }

    private AVLEntry<T>  balanceDelete(AVLEntry<T> node){
        //System.out.println("balance needed");
        int balanceFactor = node.getBalanceFactor();
        if(!node.isBalanced()){
            AVLEntry<T> nodeLeft =  node.left !=null ? (AVLEntry<T>) node.left : null;
            AVLEntry<T> nodeRight =  node.right !=null ? (AVLEntry<T>) node.right: null;
            int nodeLeftBalance = nodeLeft!=null? nodeLeft.getBalanceFactor() : 0;
            int nodeRightBalance = nodeRight!=null? nodeRight.getBalanceFactor() : 0;
            if(balanceFactor > 1 && nodeRightBalance>=0  ) node= leftRotate(node);
            else if( balanceFactor > 1 && nodeRightBalance < 0 ){
                node.left = leftRotate(nodeLeft);
                node = rightRotate(node);}
            else if (balanceFactor < -1 && nodeLeftBalance<= 0) node= rightRotate(node);
            else if(balanceFactor < -1 && nodeLeftBalance > 0){
                node.right = rightRotate(nodeRight);
                node = leftRotate(node);
            }



        }
        return node;

    }

    public T remove(T x) {
        boolean balanceFlag = false;
        T result = super.remove(x);
        AVLEntry<T> parentNode = null;
        AVLEntry<T> balancedNode = null;
        while (!stack.isEmpty() ){
            parentNode = (AVLEntry<T>) stack.pop();
            //System.out.println("before" +parentNode);
            if(balanceFlag){
                if(parentNode.element.compareTo(balancedNode.element) >0)
                    parentNode.left = balancedNode ;
                else parentNode.right = balancedNode ;

                //System.out.println("after" +parentNode);
            }
            parentNode.updateHeight();
            //System.out.println("balance factor of" +parentNode.element +"is" +parentNode.getBalanceFactor());
            if(!parentNode.isBalanced())
            {balanceFlag = true;
                balancedNode = balanceDelete(parentNode);
                //System.out.println("b" +balancedNode);

            }
        }
        return result;
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
            System.out.print( node );
            printTree(node.right);
        }
    }

    public static void main(String[] args) {
        AVLTree<Integer> t = new AVLTree<>();
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            int x = in.nextInt();
            if(x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printTree();
            } else if(x < 0) {
                System.out.print("Remove " + x + " : ");
                t.remove(-x);
                t.printTree();}

            else{
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for(int i=0; i<t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
                return;}
        }
        in.close();
    }


}
