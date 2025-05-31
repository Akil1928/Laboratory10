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

    // Constantes para el diseño mejorado
    private static final double NODE_RADIUS = 15; // Reducido de 20 a 15
    private static final double VERTICAL_SPACING = 60; // Espaciado vertical entre niveles
    private static final double MIN_HORIZONTAL_SPACING = 35; // Espaciado mínimo horizontal
    private static final int FONT_SIZE = 10; // Tamaño de fuente reducido

    // Clase interna para representar un nodo del árbol
    private static class TreeNode {
        int value;
        TreeNode left, right;
        int level; // Agregar nivel para mejor cálculo de posiciones

        TreeNode(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
            this.level = 0;
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
        rbBST.setSelected(true);

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
        if (animationTimeline != null) {
            animationTimeline.stop();
        }

        if (rbBST.isSelected()) {
            currentTree = new BST();
            lblTourTitle.setText("Graphic Binary Search Tree - Transversal Tour");
        } else {
            currentTree = new AVL();
            lblTourTitle.setText("Graphic AVL Tree - Transversal Tour");
        }

        // Reducir número de valores para mejor visualización
        int numValues = rbBST.isSelected() ? 12 : 15; // Reducido
        Set<Integer> uniqueValues = new HashSet<>();
        Random random = new Random();

        // Usar rango más pequeño para valores más manejables
        while (uniqueValues.size() < numValues) {
            uniqueValues.add(random.nextInt(50)); // 0 a 49
        }

        currentData = new ArrayList<>(uniqueValues);

        if (!rbBST.isSelected()) {
            Collections.shuffle(currentData);
        }

        System.out.println("Generated values: " + currentData);

        try {
            for (Integer value : currentData) {
                currentTree.add(value);
            }

            drawTree();
            showTraversal("PreOrder");

        } catch (Exception e) {
            System.err.println("Error generating tree: " + e.getMessage());
            e.printStackTrace();
            lblSubTitle.setText("Error generating tree: " + e.getMessage());
        }
    }

    private void switchTreeType() {
        if (currentData != null && !currentData.isEmpty()) {
            if (rbBST.isSelected()) {
                currentTree = new BST();
                lblTourTitle.setText("Graphic Binary Search Tree - Transversal Tour");
            } else {
                currentTree = new AVL();
                lblTourTitle.setText("Graphic AVL Tree - Transversal Tour");
            }

            try {
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
                TreeNode root = buildTreeRepresentation();
                if (root != null) {
                    // Calcular dimensiones del panel
                    double paneWidth = Math.max(treePane.getWidth(), 750);
                    double paneHeight = Math.max(treePane.getHeight(), 450);

                    // Calcular el ancho total del árbol
                    int treeWidth = calculateTreeWidth(root);

                    // Ajustar el espaciado inicial basado en el ancho del árbol
                    double initialSpacing = Math.min(paneWidth / (treeWidth + 1), paneWidth * 0.25);

                    // Establecer niveles para cada nodo
                    setNodeLevels(root, 0);

                    // Dibujar desde la raíz con mejor espaciado
                    drawNodeImproved(root, paneWidth / 2, 40, initialSpacing);
                }
            }
        } catch (Exception e) {
            System.err.println("Error drawing tree: " + e.getMessage());
            e.printStackTrace();
            lblSubTitle.setText("Error: " + e.getMessage());
        }
    }

    // Método para calcular el ancho del árbol (número de hojas en el nivel más profundo)
    private int calculateTreeWidth(TreeNode node) {
        if (node == null) return 0;
        if (node.left == null && node.right == null) return 1;
        return calculateTreeWidth(node.left) + calculateTreeWidth(node.right);
    }

    // Establecer niveles para cada nodo
    private void setNodeLevels(TreeNode node, int level) {
        if (node == null) return;
        node.level = level;
        setNodeLevels(node.left, level + 1);
        setNodeLevels(node.right, level + 1);
    }

    private TreeNode buildTreeRepresentation() {
        try {
            String preOrderStr = currentTree.preOrder().trim();
            if (preOrderStr.isEmpty()) return null;

            String[] preOrderValues = preOrderStr.split("\\s+");

            if (rbBST.isSelected()) {
                List<Integer> preOrder = new ArrayList<>();
                for (String val : preOrderValues) {
                    preOrder.add(Integer.parseInt(val));
                }
                return buildBSTFromPreOrder(preOrder, 0, preOrder.size() - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            } else {
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
            return null;
        }
    }

    private TreeNode buildBSTFromPreOrder(List<Integer> preOrder, int start, int end, int min, int max) {
        if (start > end || start >= preOrder.size()) {
            return null;
        }

        int rootValue = preOrder.get(start);

        if (rootValue < min || rootValue > max) {
            return null;
        }

        TreeNode root = new TreeNode(rootValue);

        if (start == end) {
            return root;
        }

        int i = start + 1;
        while (i <= end && preOrder.get(i) < rootValue) {
            i++;
        }

        root.left = buildBSTFromPreOrder(preOrder, start + 1, i - 1, min, rootValue - 1);
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

        int rootValue = preOrder.get(preStart);
        TreeNode root = new TreeNode(rootValue);

        int rootIndex = -1;
        for (int i = inStart; i < inEnd; i++) {
            if (inOrder.get(i) == rootValue) {
                rootIndex = i;
                break;
            }
        }

        if (rootIndex == -1) {
            return root;
        }

        int leftSize = rootIndex - inStart;

        root.left = buildTreeHelper(preOrder, inOrder, preStart + 1, inStart, rootIndex);
        root.right = buildTreeHelper(preOrder, inOrder, preStart + 1 + leftSize, rootIndex + 1, inEnd);

        return root;
    }

    // Método mejorado para dibujar nodos con mejor espaciado
    private void drawNodeImproved(TreeNode node, double x, double y, double spacing) {
        if (node == null) return;

        // Crear círculo más pequeño
        Circle circle = new Circle(x, y, NODE_RADIUS);
        circle.setFill(Color.LIGHTBLUE);
        circle.setStroke(Color.DARKBLUE);
        circle.setStrokeWidth(1.5);

        // Crear texto más pequeño
        Text text = new Text(String.valueOf(node.value));
        text.setFont(Font.font("Arial", FontWeight.BOLD, FONT_SIZE));
        text.setFill(Color.BLACK);

        // Centrar el texto
        text.setX(x - text.getBoundsInLocal().getWidth() / 2);
        text.setY(y + 3);

        treePane.getChildren().addAll(circle, text);
        nodePositions.put(node.value, new NodePosition(x, y, circle, text));

        // Calcular posiciones de hijos con espaciado adaptativo
        double nextY = y + VERTICAL_SPACING;
        double nextSpacing = Math.max(spacing * 0.6, MIN_HORIZONTAL_SPACING);

        if (node.left != null) {
            double leftX = Math.max(x - spacing, NODE_RADIUS + 5);

            // Línea de conexión más fina
            Line line = new Line(x - NODE_RADIUS * 0.7, y + NODE_RADIUS * 0.7,
                    leftX + NODE_RADIUS * 0.7, nextY - NODE_RADIUS * 0.7);
            line.setStroke(Color.DARKGREEN);
            line.setStrokeWidth(1.5);
            treePane.getChildren().add(0, line);

            drawNodeImproved(node.left, leftX, nextY, nextSpacing);
        }

        if (node.right != null) {
            double paneWidth = Math.max(treePane.getWidth(), 750);
            double rightX = Math.min(x + spacing, paneWidth - NODE_RADIUS - 5);

            Line line = new Line(x + NODE_RADIUS * 0.7, y + NODE_RADIUS * 0.7,
                    rightX - NODE_RADIUS * 0.7, nextY - NODE_RADIUS * 0.7);
            line.setStroke(Color.DARKGREEN);
            line.setStrokeWidth(1.5);
            treePane.getChildren().add(0, line);

            drawNodeImproved(node.right, rightX, nextY, nextSpacing);
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

            if (!result.trim().isEmpty()) {
                animateTraversal(result.trim().split("\\s+"));
            }

        } catch (TreeException e) {
            lblSubTitle.setText("Error: " + e.getMessage());
        }
    }

    private void animateTraversal(String[] values) {
        if (animationTimeline != null) {
            animationTimeline.stop();
        }

        resetNodeColors();
        animationTimeline = new Timeline();

        for (int i = 0; i < values.length; i++) {
            final int index = i;
            final Integer value = Integer.parseInt(values[i]);

            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(i * 0.8),
                    e -> highlightNode(value, index + 1)
            );

            animationTimeline.getKeyFrames().add(keyFrame);
        }

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
            nodePos.circle.setFill(Color.YELLOW);
            nodePos.circle.setStroke(Color.RED);
            nodePos.circle.setStrokeWidth(2.5);

            // Texto de paso más pequeño y mejor posicionado
            Text stepText = new Text(nodePos.x + NODE_RADIUS + 5, nodePos.y - NODE_RADIUS - 5, String.valueOf(step));
            stepText.setFont(Font.font("Arial", FontWeight.BOLD, 9));
            stepText.setFill(Color.RED);
            stepText.setId("step-" + step);
            treePane.getChildren().add(stepText);
        }
    }

    private void resetNodeColors() {
        for (NodePosition nodePos : nodePositions.values()) {
            nodePos.circle.setFill(Color.LIGHTBLUE);
            nodePos.circle.setStroke(Color.DARKBLUE);
            nodePos.circle.setStrokeWidth(1.5);
        }

        treePane.getChildren().removeIf(node ->
                node instanceof Text &&
                        node.getId() != null &&
                        node.getId().startsWith("step-")
        );
    }
}