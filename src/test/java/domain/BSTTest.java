package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class BSTTest {

    @Test
    void test() {
        BST bst = new BST();
        for (int i = 0; i <20 ; i++) {
            bst.add(util.Utility.random(50)+1);
        }
        System.out.println(bst); //toString
        try {
            System.out.println("BST size: "+bst.size()+". BST height: "+bst.height());
            System.out.println("BST min: " + bst.min() + ". BST max: " + bst.max());

        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
    }
}