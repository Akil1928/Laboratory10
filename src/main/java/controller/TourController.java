package controller;

import domain.AVL;
import domain.BST;
import domain.Tree;
import domain.TreeException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.*;

public class TourController {
    @FXML
    private Label lblSubTitle;
    @FXML
    private Button btnPreOrder;
    @FXML
    private Button btnPostOrder;
    @FXML
    private Button btnInOrder;
    @FXML
    private Label lblTourTitle;
    @FXML
    private Button btnRandomize;
    @FXML
    private Pane treePane;
    @FXML
    private RadioButton rbBST;
    @FXML
    private RadioButton rbAVL;

    private ToggleGroup treeTypeGroup;
    private Tree currentTree;
    private List<Integer> currentData;
    private Map<Integer, NodePosition> nodePositions;
    private Timeline animationTimeline;

    // Clase interna para representar un nodo del árbol
    private static class TreeNode {
        int value;
        TreeNode left, right;

        TreeNode(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    // Clase para almacenar posiciones de nodos en la interfaz
    private static class NodePosition {
        double x, y;
        Circle circle;
        Text text;

        NodePosition(double x, double y, Circle circle, Text text) {
            this.x = x;
            this.y = y;
            this.circle = circle;
            this.text = text;
        }
    }

    @FXML
    public void initialize() {
        // Configurar radio buttons
        treeTypeGroup = new ToggleGroup();
        rbBST.setToggleGroup(treeTypeGroup);
        rbAVL.setToggleGroup(treeTypeGroup);
        rbBST.setSelected(true); // BST seleccionado por defecto

        nodePositions = new HashMap<>();

        // Configurar eventos
        btnRandomize.setOnAction(e -> generateRandomTree());
        btnPreOrder.setOnAction(e -> showTraversal("PreOrder"));
        btnInOrder.setOnAction(e -> showTraversal("InOrder"));
        btnPostOrder.setOnAction(e -> showTraversal("PostOrder"));

        rbBST.setOnAction(e -> switchTreeType());
        rbAVL.setOnAction(e -> switchTreeType());

        // Generar árbol inicial
        generateRandomTree();
    }

    private void generateRandomTree() {
        // Detener animación anterior si existe
        if (animationTimeline != null) {
            animationTimeline.stop();
        }

        // Crear nuevo árbol según el tipo seleccionado
        if (rbBST.isSelected()) {
            currentTree = new BST();
            lblTourTitle.setText("Graphic Binary Search Tree - Transversal Tour");
        } else {
            currentTree = new AVL();
            lblTourTitle.setText("Graphic AVL Tree - Transversal Tour");
        }

        // Generar valores aleatorios únicos entre 0 y 50
        // Para BST usar menos valores para evitar árboles muy desbalanceados
        int numValues = rbBST.isSelected() ? 15 : 20;
        Set<Integer> uniqueValues = new HashSet<>();
        Random random = new Random();

        while (uniqueValues.size() < numValues) {
            uniqueValues.add(random.nextInt(51)); // 0 a 50
        }

        currentData = new ArrayList<>(uniqueValues);

        // Para BST, no ordenar para mantener la estructura natural
        // Para AVL, podemos mezclar un poco
        if (!rbBST.isSelected()) {
            Collections.shuffle(currentData);
        }

        System.out.println("Generated values: " + currentData);

        // Insertar valores en el árbol
        try {
            for (Integer value : currentData) {
                currentTree.add(value);
                System.out.println("Added: " + value);
            }

            System.out.println("Tree created successfully. Empty: " + currentTree.isEmpty());

            // Dibujar el árbol
            drawTree();

            // Mostrar PreOrder por defecto
            showTraversal("PreOrder");

        } catch (Exception e) {
            System.err.println("Error generating tree: " + e.getMessage());
            e.printStackTrace();
            lblSubTitle.setText("Error generating tree: " + e.getMessage());
        }
    }

    private void switchTreeType() {
        if (currentData != null && !currentData.isEmpty()) {
            // Recrear el árbol con los mismos datos pero diferente tipo
            if (rbBST.isSelected()) {
                currentTree = new BST();
                lblTourTitle.setText("Graphic Binary Search Tree - Transversal Tour");
            } else {
                currentTree = new AVL();
                lblTourTitle.setText("Graphic AVL Tree - Transversal Tour");
            }

            try {
                // Insertar los mismos valores
                for (Integer value : currentData) {
                    currentTree.add(value);
                }

                drawTree();
                showTraversal("PreOrder");
            } catch (Exception e) {
                System.err.println("Error switching tree type: " + e.getMessage());
            }
        }
    }

    private void drawTree() {
        treePane.getChildren().clear();
        nodePositions.clear();

        try {
            if (!currentTree.isEmpty()) {
                System.out.println("Drawing tree...");

                // Debug: mostrar los recorridos
                String preOrder = currentTree.preOrder().trim();
                String inOrder = currentTree.inOrder().trim();
                System.out.println("PreOrder: " + preOrder);
                System.out.println("InOrder: " + inOrder);

                // Construir representación interna del árbol para poder dibujarlo
                TreeNode root = buildTreeRepresentation();

                if (root != null) {
                    System.out.println("Tree representation built successfully");

                    double paneWidth = treePane.getPrefWidth();
                    if (paneWidth <= 0) paneWidth = 800; // Valor por defecto

                    double paneHeight = treePane.getPrefHeight();
                    if (paneHeight <= 0) paneHeight = 500; // Valor por defecto

                    // Calcular el ancho inicial para la raíz
                    double initialWidth = paneWidth * 0.3;

                    // Dibujar desde la raíz
                    drawNode(root, paneWidth / 2, 60, initialWidth);
                } else {
                    System.out.println("Failed to build tree representation");
                    lblSubTitle.setText("Error: Cannot build tree representation");
                }
            } else {
                System.out.println("Tree is empty");
            }
        } catch (Exception e) {
            System.err.println("Error drawing tree: " + e.getMessage());
            e.printStackTrace();
            lblSubTitle.setText("Error: " + e.getMessage());
        }
    }

    private TreeNode buildTreeRepresentation() {
        try {
            // Usar el recorrido preorder para reconstruir el árbol
            String preOrderStr = currentTree.preOrder().trim();
            if (preOrderStr.isEmpty()) return null;

            String[] preOrderValues = preOrderStr.split("\\s+");

            // Para BST, podemos usar solo preorder para reconstruir
            if (rbBST.isSelected()) {
                List<Integer> preOrder = new ArrayList<>();
                for (String val : preOrderValues) {
                    preOrder.add(Integer.parseInt(val));
                }
                return buildBSTFromPreOrder(preOrder, 0, preOrder.size() - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            } else {
                // Para AVL usamos preorder e inorder
                String inOrderStr = currentTree.inOrder().trim();
                String[] inOrderValues = inOrderStr.split("\\s+");

                List<Integer> preOrder = new ArrayList<>();
                List<Integer> inOrder = new ArrayList<>();

                for (String val : preOrderValues) {
                    preOrder.add(Integer.parseInt(val));
                }

                for (String val : inOrderValues) {
                    inOrder.add(Integer.parseInt(val));
                }

                return buildTreeFromTraversals(preOrder, inOrder);
            }

        } catch (Exception e) {
            System.err.println("Error building tree representation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private TreeNode buildBSTFromPreOrder(List<Integer> preOrder, int start, int end, int min, int max) {
        if (start > end || start >= preOrder.size()) {
            return null;
        }

        int rootValue = preOrder.get(start);

        // Verificar si el valor está en el rango válido para BST
        if (rootValue < min || rootValue > max) {
            return null;
        }

        TreeNode root = new TreeNode(rootValue);

        if (start == end) {
            return root;
        }

        // Encontrar el primer elemento mayor que root para dividir subárboles
        int i = start + 1;
        while (i <= end && preOrder.get(i) < rootValue) {
            i++;
        }

        // Construir subárbol izquierdo (elementos menores que root)
        root.left = buildBSTFromPreOrder(preOrder, start + 1, i - 1, min, rootValue - 1);

        // Construir subárbol derecho (elementos mayores que root)
        root.right = buildBSTFromPreOrder(preOrder, i, end, rootValue + 1, max);

        return root;
    }

    private TreeNode buildTreeFromTraversals(List<Integer> preOrder, List<Integer> inOrder) {
        if (preOrder.isEmpty() || inOrder.isEmpty()) {
            return null;
        }

        return buildTreeHelper(preOrder, inOrder, 0, 0, inOrder.size());
    }

    private TreeNode buildTreeHelper(List<Integer> preOrder, List<Integer> inOrder,
                                     int preStart, int inStart, int inEnd) {
        if (preStart >= preOrder.size() || inStart >= inEnd) {
            return null;
        }

        // El primer elemento del preorder es la raíz
        int rootValue = preOrder.get(preStart);
        TreeNode root = new TreeNode(rootValue);

        // Encontrar la posición de la raíz en inorder
        int rootIndex = -1;
        for (int i = inStart; i < inEnd; i++) {
            if (inOrder.get(i) == rootValue) {
                rootIndex = i;
                break;
            }
        }

        if (rootIndex == -1) {
            return root; // No se encontró en inorder, solo retornar el nodo
        }

        // Tamaño del subárbol izquierdo
        int leftSize = rootIndex - inStart;

        // Construir recursivamente los subárboles
        root.left = buildTreeHelper(preOrder, inOrder, preStart + 1, inStart, rootIndex);
        root.right = buildTreeHelper(preOrder, inOrder, preStart + 1 + leftSize, rootIndex + 1, inEnd);

        return root;
    }

    private void drawNode(TreeNode node, double x, double y, double xOffset) {
        if (node == null) return;

        // Crear círculo para el nodo
        Circle circle = new Circle(x, y, 20);
        circle.setFill(Color.LIGHTBLUE);
        circle.setStroke(Color.DARKBLUE);
        circle.setStrokeWidth(2);

        // Crear texto para el valor
        Text text = new Text(String.valueOf(node.value));
        text.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        text.setFill(Color.BLACK);

        // Centrar el texto en el círculo
        text.setX(x - text.getBoundsInLocal().getWidth() / 2);
        text.setY(y + 4);

        // Agregar al panel
        treePane.getChildren().addAll(circle, text);

        // Guardar posición
        nodePositions.put(node.value, new NodePosition(x, y, circle, text));

        // Dibujar hijos
        double nextY = y + 80;
        double nextXOffset = xOffset * 0.6;

        if (node.left != null) {
            double leftX = x - xOffset;
            // Dibujar línea de conexión
            Line line = new Line(x - 15, y + 15, leftX + 15, nextY - 15);
            line.setStroke(Color.DARKGREEN);
            line.setStrokeWidth(2);
            treePane.getChildren().add(0, line); // Agregar al fondo

            drawNode(node.left, leftX, nextY, nextXOffset);
        }

        if (node.right != null) {
            double rightX = x + xOffset;
            // Dibujar línea de conexión
            Line line = new Line(x + 15, y + 15, rightX - 15, nextY - 15);
            line.setStroke(Color.DARKGREEN);
            line.setStrokeWidth(2);
            treePane.getChildren().add(0, line); // Agregar al fondo

            drawNode(node.right, rightX, nextY, nextXOffset);
        }
    }

    private void showTraversal(String traversalType) {
        try {
            String result = "";
            String subtitle = "";

            switch (traversalType) {
                case "PreOrder":
                    result = currentTree.preOrder();
                    subtitle = "Pre Order Transversal Tour (N-L-R)";
                    break;
                case "InOrder":
                    result = currentTree.inOrder();
                    subtitle = "In Order Transversal Tour (L-N-R)";
                    break;
                case "PostOrder":
                    result = currentTree.postOrder();
                    subtitle = "Post Order Transversal Tour (L-R-N)";
                    break;
            }

            lblSubTitle.setText(subtitle);

            // Animar el recorrido
            if (!result.trim().isEmpty()) {
                animateTraversal(result.trim().split("\\s+"));
            }

        } catch (TreeException e) {
            lblSubTitle.setText("Error: " + e.getMessage());
        }
    }

    private void animateTraversal(String[] values) {
        // Detener animación anterior
        if (animationTimeline != null) {
            animationTimeline.stop();
        }

        // Resetear colores
        resetNodeColors();

        // Crear nueva animación
        animationTimeline = new Timeline();

        for (int i = 0; i < values.length; i++) {
            final int index = i;
            final Integer value = Integer.parseInt(values[i]);

            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(i * 0.8), // 0.8 segundos entre cada paso
                    e -> highlightNode(value, index + 1)
            );

            animationTimeline.getKeyFrames().add(keyFrame);
        }

        // Agregar frame final para resetear colores después de 3 segundos
        KeyFrame resetFrame = new KeyFrame(
                Duration.seconds(values.length * 0.8 + 3),
                e -> resetNodeColors()
        );
        animationTimeline.getKeyFrames().add(resetFrame);

        animationTimeline.play();
    }

    private void highlightNode(Integer value, int step) {
        NodePosition nodePos = nodePositions.get(value);
        if (nodePos != null) {
            // Cambiar color del nodo actual
            nodePos.circle.setFill(Color.YELLOW);
            nodePos.circle.setStroke(Color.RED);
            nodePos.circle.setStrokeWidth(3);

            // Mostrar número de paso
            Text stepText = new Text(nodePos.x + 25, nodePos.y - 25, String.valueOf(step));
            stepText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            stepText.setFill(Color.RED);
            stepText.setId("step-" + step); // Para poder identificarlo después
            treePane.getChildren().add(stepText);
        }
    }

    private void resetNodeColors() {
        // Resetear colores de nodos
        for (NodePosition nodePos : nodePositions.values()) {
            nodePos.circle.setFill(Color.LIGHTBLUE);
            nodePos.circle.setStroke(Color.DARKBLUE);
            nodePos.circle.setStrokeWidth(2);
        }

        // Remover textos de pasos
        treePane.getChildren().removeIf(node ->
                node instanceof Text &&
                        node.getId() != null &&
                        node.getId().startsWith("step-")
        );
    }
}