package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AVLTest {

    @Test
    void test() {
        AVL avl = new AVL();
        for (int i = 0; i < 30; i++) {
            avl.add(util.Utility.random(50)+1);
        }
        System.out.println(avl); //toString
    }
}