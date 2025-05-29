
package domain;

import org.junit.jupiter.api.Test;
import util.Utility;

import static org.junit.jupiter.api.Assertions.*;

class AVLTest {

    @Test
    void testAVL() {
        // a. Crear e instanciar un objeto tipo AVL e insertar 30 números aleatorios entre 20 y 200
        AVL avl = new AVL();
        System.out.println("Árbol AVL con 30 números aleatorios entre 20 y 200:");
        int[] numbersToStore = new int[30];

        for (int i = 0; i < 30; i++) {
            int randomNumber = Utility.random(181) + 20; // Genera números entre 20 y 200
            avl.add(randomNumber);
            numbersToStore[i] = randomNumber;
        }

        try {
            // b. Mostrar el contenido del árbol por consola
            System.out.println(avl.toString());

            // c. Probar los métodos: size(), min(), max()
            System.out.println("\nTamaño del árbol AVL: " + avl.size());
            System.out.println("Valor mínimo del árbol AVL: " + avl.min());
            System.out.println("Valor máximo del árbol AVL: " + avl.max());

            // d. Indicar si el árbol está balanceado
            System.out.println("\n¿El árbol AVL está balanceado? " + avl.isBalanced());

            // e. Eliminar 5 elementos del árbol
            System.out.println("\nEliminando 5 elementos del árbol AVL:");
            for (int i = 0; i < 5; i++) {
                boolean removed = avl.remove(numbersToStore[i]);
                System.out.println("Eliminando " + numbersToStore[i] + ": " + (removed ? "Éxito" : "Fallido"));
            }

            // f. Mostrar el contenido del árbol después de eliminar
            System.out.println("\nContenido del árbol AVL después de eliminar 5 elementos:");
            System.out.println(avl.toString());
            System.out.println("Tamaño del árbol AVL: " + avl.size());

            // g. Volver a comprobar si el árbol está balanceado
            System.out.println("\n¿El árbol AVL sigue balanceado después de eliminar? " + avl.isBalanced());

            // h. Re-equilibrar el árbol si no está balanceado
            if (!avl.isBalanced()) {
                System.out.println("\nRe-equilibrando el árbol AVL...");
                avl.rebalance();
                System.out.println("Árbol re-equilibrado.");
            }

            // i. Mostrar el contenido del árbol después de re-equilibrar
            System.out.println("\nContenido del árbol AVL después de re-equilibrar:");
            System.out.println(avl.toString());

            // j. Comprobar nuevamente si el árbol está balanceado
            System.out.println("\n¿El árbol AVL está balanceado después de re-equilibrar? " + avl.isBalanced());

            // k. Probar los nuevos algoritmos
            System.out.println("\n--- Probando nuevos algoritmos ---");

            // Probar el método father
            System.out.println("\nPadres de varios elementos:");
            for (int i = 5; i < 10; i++) {
                Object father = avl.father(numbersToStore[i]);
                System.out.println("Padre de " + numbersToStore[i] + ": " +
                        (father != null ? father : "Sin padre (es la raíz o no existe)"));
            }

            // Probar el método brother
            System.out.println("\nHermanos de varios elementos:");
            for (int i = 5; i < 10; i++) {
                Object brother = avl.brother(numbersToStore[i]);
                System.out.println("Hermano de " + numbersToStore[i] + ": " +
                        (brother != null ? brother : "Sin hermano"));
            }

            // Probar el método children
            System.out.println("\nHijos de varios elementos:");
            for (int i = 5; i < 10; i++) {
                String children = avl.children(numbersToStore[i]);
                System.out.println("Hijos de " + numbersToStore[i] + ": " +
                        (children.isEmpty() ? "Sin hijos" : children));
            }

        } catch (TreeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}