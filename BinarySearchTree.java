import java.util.ArrayList;
import java.util.List;
 
/* Do not modify BinaryTree class */
abstract class BinaryTree {
    
    protected class Node implements TreePrinter.PrintableNode {
        protected int key;
        protected Node parent;
        protected Node left;
        protected Node right;
        public Node(int k, Node p) {
            key = k;
            parent = p;
            left = null;
            right = null;
        }
        public Node getLeft() {return left;}
        public Node getRight() {return right;}
        public String toString() {return ""+key;}
    }
 
    protected Node root;
    BinaryTree() {
        root = null;
    }
}
 
class BinarySearchTree extends BinaryTree {
    /* Modify the following methods */
    /* Part (a) methods */
 
    public void delete(int k) {
        // delete node containing k
        // (can assume there is a node containing k)
      root = deleteNode(root, k);
    }
    private Node deleteNode(Node node, int k){
        if (node == null) {
            return null;
        }
    
        if (k < node.key) {
            node.left = deleteNode(node.left, k);
        } else if (k > node.key) {
            node.right = deleteNode(node.right, k);
        } else {
            // case 1: node has no child
            if (node.left == null && node.right == null) {
                node = null;
            }
            // case 2: node has one child
            else if (node.left == null) {
                node = node.right;
            } else if (node.right == null) {
                node = node.left;
            }
            // case 3: node has two children
            else {
                Node predecessor = predecessorNode(node);
                node.key = predecessor.key;
                node.left = deleteNode(node.left, predecessor.key);
            }
        }
        
        return node;  
    }
        
 
    public int successorKey(int k) {
        // return successor of node containing k
        // (can assume there is a node containing k and k is not the max)
        Node currentNode = root;
        Node successor = null;
        
        while (currentNode != null) {
            if (currentNode.key > k) {
                successor = currentNode;
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
        }
        return successor.key;
    }
 
    public int minKey() {
        // return minimum stored key
        // (can assume tree is nonempty)
        Node currentNode = root;
        while (currentNode.left != null) {
            currentNode = currentNode.left;
        }
        return currentNode.key;
    }
 
    /* Part (b) methods */
 
    public int medianKey() {
        // Count the total number of nodes in the binary search tree
        int totalNodes = countNodes(root);
            
        // Create an array to store all the keys in the binary search tree
        int[] keys = new int[totalNodes];
            
        // Traverse the binary search tree in-order and store all the keys in the array
        int index = 0;
        traverseInOrder(root, keys, index);
            
        // Find the median key from the array
        int medianIndex = totalNodes / 2;
        return findMedian(keys, 0, keys.length - 1, medianIndex);
    }
    
    
    private int countNodes(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.left) + countNodes(node.right);
    }
    
    private int traverseInOrder(Node node, int[] keys, int index) {
        // traverses the binary tree in order. used for median methods
        if (node != null) {
            index = traverseInOrder(node.left, keys, index);
            keys[index] = node.key;
            index++;
            index = traverseInOrder(node.right, keys, index);
        }
        return index;
    }
    
    
    private int findMedian(int[] keys, int left, int right, int medianIndex) {
// quicksort algorithm that finds the median
        int pivotIndex = partition(keys, left, right);
        if (pivotIndex == medianIndex) {
            return keys[pivotIndex];
        } else if (pivotIndex < medianIndex) {
            return findMedian(keys, pivotIndex + 1, right, medianIndex);
        } else {
            return findMedian(keys, left, pivotIndex - 1, medianIndex);
        }
    }
    
    private int partition(int[] keys, int left, int right) {
        // used for the findmedian method to sort the tree. part of quicksort
        int pivot = keys[right];
        int i = left;
        for (int j = left; j < right; j++) {
            if (keys[j] <= pivot) {
                swap(keys, i, j);
                i++;
            }
        }
        swap(keys, i, right);
        return i;
    }
    
    
    private void swap(int[] keys, int i, int j) {
        // changes the elements in an array
        int temp = keys[i];
        keys[i] = keys[j];
        keys[j] = temp;
    }
    
 
    /* Part (c) methods */
 
    public int nodesInLevel(int h) {
        // return the number of nodes in level h
        // (can assume h >= 0, but nothing else. The answer might be 0) 
        return nodesInLevel(root, h, 0);
    }
    
    private int nodesInLevel(Node node, int targetLevel, int currentLevel) {
        if (node == null) {
            return 0;
        }
        if (currentLevel == targetLevel) {
            return 1;
        }
        return nodesInLevel(node.left, targetLevel, currentLevel + 1) +
               nodesInLevel(node.right, targetLevel, currentLevel + 1);
    }
    
 
    /* Do not modify below this comment. The rest of the methods are already implemented */
 
    public boolean contains(int needle) {
        Node ret = getDescendant(root, needle);
        if (ret == null) {return false;}
        return true;
    }
    private Node getDescendant(Node n, int needle) {
        if (n == null) {return null;}
        if (needle == n.key) {return n;}
        else if (needle < n.key) {return getDescendant(n.left, needle);}
        else {return getDescendant(n.right, needle);}
    }
 
    public void add(int key) {
        if (root == null) {
            root = new Node(key, null);
        }
        else {
            addDescendant(key, root);
        }
    }
    private void addDescendant(int key, Node n) {
        // assumes n is not null
        if (key < n.key) {
            if (n.left == null) {
                n.left = new Node(key, n);
            }
            else {
                addDescendant(key, n.left);
            }
        }
        else {
            if (n.right == null) {
                n.right = new Node(key, n);
            }
            else {
                addDescendant(key, n.right);
            }
        }
    }
 
    public int maxKey() {
        // assumes root is not null
        return maxDescendant(root).key;
    }
 
    private Node maxDescendant(Node n) {
        // assumes n is not null
        Node current = n;
        while (current.right != null) {
            current = current.right;
        }
        return current;
    }
 
    public int predecessorKey(int key) {
        // assumes key is not the minimum
        Node n = getDescendant(root, key);
        return predecessorNode(n).key;
    }
 
    private Node predecessorNode(Node x) {
        if (x.left != null) {return maxDescendant(x.left);}
        Node ancestor = x.parent;
        while (x != ancestor.right) {
            x = ancestor;
            ancestor = x.parent;
        }
        return ancestor;
    }
    
 
    public void output() {
        TreePrinter.print(root);
    }
 
    public static void main(String[] args) {
        java.util.Scanner myScanner = new java.util.Scanner(System.in);
        BinarySearchTree myTree = new BinarySearchTree();
        boolean done = false;
        while (!done) {
            String operation = myScanner.next();
            if (operation.equals("add")) {
                myTree.add(myScanner.nextInt());
            }
            else if (operation.equals("contains")) {
                System.out.println(myTree.contains(myScanner.nextInt()));
            }
            else if (operation.equals("maxKey")) {
                System.out.println(myTree.maxKey()); 
            }
            else if (operation.equals("output")) {
                myTree.output(); 
            }
            else if (operation.equals("predecessorKey")) {
                System.out.println(myTree.predecessorKey(myScanner.nextInt()));
            }
            else if (operation.equals("successorKey")) {
                System.out.println(myTree.successorKey(myScanner.nextInt()));
            }
            else if (operation.equals("minKey")) {
                System.out.println(myTree.minKey());
            }
            else if (operation.equals("medianKey")) {
                System.out.println(myTree.medianKey());
            }
            else if (operation.equals("delete")) {
                myTree.delete(myScanner.nextInt());
            }
            else if (operation.equals("nodesInLevel")) {
                System.out.println(myTree.nodesInLevel(myScanner.nextInt()));
            }
            else if (operation.equals("quit")) {
                done = true;
            }
        }
        myScanner.close();
    }
}
 
 
/**
 * Binary tree printer (do not modify)
 * 
 * @author MightyPork
 * @see <a href="https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram-in-java">source</a>
 */
class TreePrinter
{
    /** Node that can be printed */
    public interface PrintableNode {
        /** Get left child */
        PrintableNode getLeft();
 
        /** Get right child */
        PrintableNode getRight();
    }
 
    /**
     * Print a tree
     */
    public static void print(PrintableNode root) {
        List<List<String>> lines = new ArrayList<List<String>>();
 
        List<PrintableNode> level = new ArrayList<PrintableNode>();
        List<PrintableNode> next = new ArrayList<PrintableNode>();
 
        level.add(root);
        int nn = 1;
 
        int widest = 0;
 
        while (nn != 0) {
            List<String> line = new ArrayList<String>();
 
            nn = 0;
 
            for (PrintableNode n : level) {
                if (n == null) {
                    line.add(null);
 
                    next.add(null);
                    next.add(null);
                } else {
                    String aa = n.toString();
                    line.add(aa);
                    if (aa.length() > widest) widest = aa.length();
 
                    next.add(n.getLeft());
                    next.add(n.getRight());
 
                    if (n.getLeft() != null) nn++;
                    if (n.getRight() != null) nn++;
                }
            }
 
            if (widest % 2 == 1) widest++;
 
            lines.add(line);
 
            List<PrintableNode> tmp = level;
            level = next;
            next = tmp;
            next.clear();
        }
 
        int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
        for (int i = 0; i < lines.size(); i++) {
            List<String> line = lines.get(i);
            int hpw = (int) Math.floor(perpiece / 2f) - 1;
 
            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {
 
                    // split node
                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '┴' : '┘';
                        } else {
                            if (j < line.size() && line.get(j) != null) c = '└';
                        }
                    }
                    System.out.print(c);
 
                    // lines and spaces
                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) {
                            System.out.print(" ");
                        }
                    } else {
 
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? " " : "─");
                        }
                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }
                System.out.println();
            }
 
            // print line of numbers
            for (int j = 0; j < line.size(); j++) {
 
                String f = line.get(j);
                if (f == null) f = "";
                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);
 
                // a number
                for (int k = 0; k < gap1; k++) {
                    System.out.print(" ");
                }
                System.out.print(f);
                for (int k = 0; k < gap2; k++) {
                    System.out.print(" ");
                }
            }
            System.out.println();
 
            perpiece /= 2;
        }
    }
}