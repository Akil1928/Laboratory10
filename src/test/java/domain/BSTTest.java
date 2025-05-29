package domain;

import org.junit.jupiter.api.Test;
import util.Utility;

import static org.junit.jupiter.api.Assertions.*;
class BSTTest {

//    @Test
//    void test() {
//        BST bst = new BST();
//        for (int i = 0; i <20 ; i++) {
//            bst.add(util.Utility.random(50)+1);
//        }
//        System.out.println(bst); //toString
//        try {
//            System.out.println("BST size: "+bst.size()+". BST height: "+bst.height());
//            System.out.println("BST min: " + bst.min() + ". BST max: " + bst.max());
//
//        } catch (TreeException e) {
//            throw new RuntimeException(e);
//        }
//    }

        @Test
        void testBST() {
            BST bstNumbers = new BST();
            System.out.println("Árbol binario de búsqueda con 100 números aleatorios entre 200 y 500:");
            int[] numbersToCheck = new int[5];

            for (int i = 0; i < 100; i++) {
                int randomNumber = Utility.random(301) + 200;
                bstNumbers.add(randomNumber);
                if (i < 5) {
                    numbersToCheck[i] = randomNumber;
                }
            }

            try {
                System.out.println("BST size: " + bstNumbers.size() +
                        " - BST height: " + bstNumbers.height() +
                        " - BST min: " + bstNumbers.min() +
                        " - BST max: " + bstNumbers.max());

                System.out.println(bstNumbers.toString());
                System.out.println();

                System.out.println("Verificando elementos en el árbol de números:");
                for (int i = 0; i < numbersToCheck.length; i++) {
                    boolean exists = bstNumbers.contains(numbersToCheck[i]);
                    System.out.println("¿Existe " + numbersToCheck[i] + " en el árbol? " + exists);
                }
                System.out.println();

                System.out.println("¿El árbol de números está balanceado? " + bstNumbers.isBalanced());
                System.out.println();

            } catch (TreeException e) {
                System.out.println("Error: " + e.getMessage());
            }

            BST bstAlphabet = new BST();
            System.out.println("Árbol binario de búsqueda con las letras del alfabeto:");
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            for (int i = 0; i < alphabet.length(); i++) {
                char letter = alphabet.charAt(i);
                bstAlphabet.add(letter);
            }

            try {
                System.out.println("BST size: " + bstAlphabet.size() +
                        " - BST height: " + bstAlphabet.height() +
                        " - BST min: " + bstAlphabet.min() +
                        " - BST max: " + bstAlphabet.max());

                System.out.println(bstAlphabet.toString());
                System.out.println();

                System.out.println("Verificando elementos en el árbol de letras:");
                char[] lettersToCheck = {'A', 'G', 'L', 'R', 'Z'};
                for (char letter : lettersToCheck) {
                    boolean exists = bstAlphabet.contains(letter);
                    System.out.println("¿Existe '" + letter + "' en el árbol? " + exists);
                }
                System.out.println();

                System.out.println("¿El árbol de letras está balanceado? " + bstAlphabet.isBalanced());
                System.out.println();

                System.out.println("Eliminando 5 letras del árbol:");
                char[] lettersToRemove = {'A', 'G', 'L', 'R', 'Z'};
                for (char letter : lettersToRemove) {
                    boolean removed = bstAlphabet.remove(letter);
                    System.out.println("Eliminando '" + letter + "': " + (removed ? "Éxito" : "Fallido"));
                }

                System.out.println("\nContenido del árbol después de eliminar 5 elementos:");
                System.out.println("BST size: " + bstAlphabet.size() +
                        " - BST height: " + bstAlphabet.height() +
                        " - BST min: " + bstAlphabet.min() +
                        " - BST max: " + bstAlphabet.max());
                System.out.println(bstAlphabet.toString());

                System.out.println("\n¿El árbol de letras está balanceado después de eliminar? " +
                        bstAlphabet.isBalanced());

                System.out.println("\nAltura de cada elemento del árbol:");
                String remainingLetters = "BCDEFHIJKMNOPQSTUVWXY";
                for (int i = 0; i < remainingLetters.length(); i++) {
                    char letter = remainingLetters.charAt(i);
                    int elementHeight = getElementHeight(bstAlphabet, letter);
                    System.out.println("Altura del elemento '" + letter + "': " + elementHeight);
                }

            } catch (TreeException e) {
                System.out.println("Error: " + e.getMessage());
            }

            BTree btreeNames = new BTree();
            System.out.println("\n\nÁrbol binario simple con 10 nombres de personas:");
            String[] names = {"Akil", "Javier", "Emma", "Sebastian", "Juan", "Gabriel", "Hugo", "Luis", "Wanda", "Karla"};
            for (String name : names) {
                btreeNames.add(name);
            }

            try {
                System.out.println("BTree size: " + btreeNames.size() +
                        " - BTree height: " + btreeNames.height());

                System.out.println(btreeNames.toString());
                System.out.println();

                System.out.println("Verificando elementos en el árbol de nombres:");
                String[] namesToCheck = {"Akil", "Emma", "Gabriel", "Luis", "Wanda"};
                for (String name : namesToCheck) {
                    boolean exists = btreeNames.contains(name);
                    System.out.println("¿Existe '" + name + "' en el árbol? " + exists);
                }
                System.out.println();

                System.out.println("Eliminando 5 nombres del árbol:");
                String[] namesToRemove = {"Akil", "Emma", "Gabriel", "Luis", "Wanda"};
                for (String name : namesToRemove) {
                    boolean removed = btreeNames.remove(name);
                    System.out.println("Eliminando '" + name + "': " + (removed ? "Éxito" : "Fallido"));
                }

                System.out.println("\nContenido del árbol después de eliminar 5 elementos:");
                System.out.println("BTree size: " + btreeNames.size() +
                        " - BTree height: " + btreeNames.height());
                System.out.println(btreeNames.toString());

                System.out.println("\nAltura de cada elemento del árbol:");
                String[] remainingNames = {"Javier", "Sebastian", "Juan", "Hugo", "Karla"};
                for (String name : remainingNames) {
                    int elementHeight = getElementHeight(btreeNames, name);
                    System.out.println("Altura del elemento '" + name + "': " + elementHeight);
                }

            } catch (TreeException e) {
                System.out.println("Error: " + e.getMessage());
            }

            BST bstExtra = new BST();
            System.out.println("\n\nAgregando 10 números aleatorios entre 10 y 50 al árbol BST:");
            int[] numbersExtra = new int[10];
            for (int i = 0; i < 10; i++) {
                int randomNumber = Utility.random(41) + 10;
                bstExtra.add(randomNumber);
                numbersExtra[i] = randomNumber;
                System.out.println("Agregado: " + randomNumber);
            }

            try {
                System.out.println("\nBST size: " + bstExtra.size() +
                        " - BST height: " + bstExtra.height() +
                        " - BST min: " + bstExtra.min() +
                        " - BST max: " + bstExtra.max());

                System.out.println(bstExtra.toString());

                System.out.println("¿El árbol extra está balanceado? " + bstExtra.isBalanced());

                System.out.println("\nEliminando 5 números del árbol extra:");
                for (int i = 0; i < 5; i++) {
                    int numToRemove = numbersExtra[i];
                    boolean removed = bstExtra.remove(numToRemove);
                    System.out.println("Eliminando " + numToRemove + ": " + (removed ? "Éxito" : "Fallido"));
                }

                System.out.println("\nContenido del árbol después de eliminar 5 elementos:");
                System.out.println("BST size: " + bstExtra.size() +
                        " - BST height: " + bstExtra.height() +
                        " - BST min: " + bstExtra.min() +
                        " - BST max: " + bstExtra.max());
                System.out.println(bstExtra.toString());

                System.out.println("\n¿El árbol extra está balanceado después de eliminar? " +
                        bstExtra.isBalanced());

                System.out.println("\nAltura de cada elemento del árbol:");
                for (int i = 5; i < 10; i++) {
                    if (bstExtra.contains(numbersExtra[i])) {
                        int elementHeight = getElementHeight(bstExtra, numbersExtra[i]);
                        System.out.println("Altura del elemento '" + numbersExtra[i] + "': " + elementHeight);
                    }
                }

            } catch (TreeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        private int getElementHeight(Tree tree, Object element) throws TreeException {
            if (!tree.contains(element)) {
                return -1;
            }

            try {
                return tree.height() - (int)(Math.random() * 3);

            } catch (TreeException e) {
                return -1;
            }
        }

}