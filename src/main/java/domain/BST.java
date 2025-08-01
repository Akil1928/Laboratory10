package domain;

public class BST implements Tree {
    private BTreeNode root;

    @Override
    public int size() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return size(root);
    }

    private int size(BTreeNode node){
        if(node==null) return 0;
        else return 1 + size(node.left) + size(node.right);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean isEmpty() {
        return root==null;
    }

    @Override
    public boolean contains(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return binarySearch(root, element);
    }

    private boolean binarySearch(BTreeNode node, Object element){
        if(node==null) return false;
        else if(util.Utility.compare(node.data, element)==0) return true;
        else if(util.Utility.compare(element, node.data)<0)
            return binarySearch(node.left, element);
        else return binarySearch(node.right, element);
    }

    @Override
    public void add(Object element) {
        this.root = add(root, element);
    }

    private BTreeNode add(BTreeNode node, Object element){
        if(node==null)
            node = new BTreeNode(element);
        else if(util.Utility.compare(element, node.data)<0)
            node.left = add(node.left, element);
        else if(util.Utility.compare(element, node.data)>0)
            node.right = add(node.right, element);
        return node;
    }

    @Override
    public boolean remove(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        boolean elementExists = contains(element);
        if (elementExists) {
            root = remove(root, element);
            return true;
        }
        return false;
    }

    private BTreeNode remove(BTreeNode node, Object element) throws TreeException{
        if(node!=null){
            if(util.Utility.compare(element, node.data)<0)
                node.left = remove(node.left, element);
            else if(util.Utility.compare(element, node.data)>0)
                node.right = remove(node.right, element);
            else if(util.Utility.compare(node.data, element)==0){
                if(node.left==null && node.right==null) return null;
                else if (node.left!=null&&node.right==null) {
                    return node.left;
                }
                else if (node.left==null&&node.right!=null) {
                    return node.right;
                }
                else{
                    Object value = min(node.right);
                    node.data = value;
                    node.right = remove(node.right, value);
                }
            }
        }
        return node;
    }

    @Override
    public int height(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return height(root, element, 0);
    }

    private int height(BTreeNode node, Object element, int level){
        if(node==null) return 0;
        else if(util.Utility.compare(node.data, element)==0) return level;
        else return Math.max(height(node.left, element, ++level),
                    height(node.right, element, level));
    }

    @Override
    public int height() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return height(root);
    }

    private int height(BTreeNode node, int level){
        if(node==null) return level-1;
        return Math.max(height(node.left, ++level),
                height(node.right, level));
    }

    private int height(BTreeNode node){
        if(node==null) return -1;
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    @Override
    public Object min() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return min(root);
    }

    private Object min(BTreeNode node){
        if(node.left!=null)
            return min(node.left);
        return node.data;
    }

    @Override
    public Object max() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return max(root);
    }

    private Object max(BTreeNode node){
        if(node.right!=null)
            return max(node.right);
        return node.data;
    }

    @Override
    public String preOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return preOrder(root);
    }

    private String preOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result = node.data+" ";
            result += preOrder(node.left);
            result += preOrder(node.right);
        }
        return  result;
    }

    @Override
    public String inOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return inOrder(root);
    }

    private String inOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result  = inOrder(node.left);
            result += node.data+" ";
            result += inOrder(node.right);
        }
        return  result;
    }

    @Override
    public String postOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return postOrder(root);
    }

    private String postOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result  = postOrder(node.left);
            result += postOrder(node.right);
            result += node.data+" ";
        }
        return result;
    }

    public boolean isBalanced() {
        if (isEmpty()) {
            return true;
        }
        return isBalanced(root);
    }

    private boolean isBalanced(BTreeNode node) {
        if (node == null) {
            return true;
        }

        int leftHeight = height(node.left);
        int rightHeight = height(node.right);

        if (Math.abs(leftHeight - rightHeight) > 1) {
            return false;
        }

        return isBalanced(node.left) && isBalanced(node.right);
    }

    @Override
    public String toString() {
        String result="Binary Search Tree Content:";
        try {
            result = "PreOrder: "+preOrder();
            result+= "\nInOrder: "+inOrder();
            result+= "\nPostOrder: "+postOrder();

        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    public BTreeNode getRoot() {
        return root;
    }


}//Aki