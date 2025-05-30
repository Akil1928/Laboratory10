package controller;

import domain.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GraphicController implements Initializable {

    @FXML
    private Button btnTourInfo;
    @FXML
    private Button btnLevels;
    @FXML
    private Button btnRandomize;
    @FXML
    private Pane treePane;
    @FXML
    private RadioButton rbBST;
    @FXML
    private RadioButton rbAVL;
    @FXML
    private ToggleGroup treeTypeGroup;
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblTreeStatus;

    private BST bstTree;
    private AVL avlTree;
    private boolean showLevels = false;
    private boolean showTourInfo = false;
    private final double PANE_WIDTH = 1200.0;
    private final double PANE_HEIGHT = 600.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializar árboles
        bstTree = new BST();
        avlTree = new AVL();

        // Configurar eventos de los botones
        btnRandomize.setOnAction(e -> randomizeTree());
        btnLevels.setOnAction(e -> toggleLevels());
        btnTourInfo.setOnAction(e -> toggleTourInfo());

        // Configurar eventos de radio buttons
        rbBST.setOnAction(e -> selectTreeType());
        rbAVL.setOnAction(e -> selectTreeType());

        // Seleccionar BST por defecto
        rbBST.setSelected(true);

        // Configurar tamaño fijo del panel
        treePane.setPrefSize(PANE_WIDTH, PANE_HEIGHT);

        // Generar árbol inicial
        randomizeTree();
    }

    private void randomizeTree() {
        // Limpiar árboles
        bstTree.clear();
        avlTree.clear();

        // Generar 30 valores aleatorios únicos entre 0 y 50
        List<Integer> values = new ArrayList<>();
        while (values.size() < 30) {
            int value = (int) (Math.random() * 51); // 0 a 50
            if (!values.contains(value)) {
                values.add(value);
                bstTree.add(value);
                avlTree.add(value);
            }
        }

        updateTreeStatus();
        drawTree();
    }

    private void toggleLevels() {
        showLevels = !showLevels;
        drawTree();
    }

    private void toggleTourInfo() {
        showTourInfo = !showTourInfo;
        drawTree();
    }

    private void selectTreeType() {
        updateTreeStatus();
        drawTree();
    }

    private void updateTreeStatus() {
        if (rbBST.isSelected()) {
            boolean balanced = bstTree.isBalanced();
            lblTreeStatus.setText(balanced ? "BST is balanced!!!" : "BST is not balanced!!!");
            lblTreeStatus.setTextFill(balanced ? Color.GREEN : Color.RED);
        } else if (rbAVL.isSelected()) {
            // AVL siempre debe estar balanceado
            lblTreeStatus.setText("AVL is balanced!!!");
            lblTreeStatus.setTextFill(Color.GREEN);
        }
    }

    private void drawTree() {
        treePane.getChildren().clear();

        // Dibujar líneas de nivel si está activado
        if (showLevels) {
            drawLevelLines();
        }

        // Obtener el árbol actual según la selección
        BTreeNode root = getCurrentTreeRoot();

        if (root != null) {
            // Dibujar el árbol con espaciado adecuado
            drawTreeRecursive(root, PANE_WIDTH / 2, 60, PANE_WIDTH / 4);
        }

        // Mostrar información del tour si está activado
        if (showTourInfo) {
            displayTourInfo();
        }
    }

    private BTreeNode getCurrentTreeRoot() {
        try {
            if (rbBST.isSelected()) {
                return getRoot(bstTree);
            } else if (rbAVL.isSelected()) {
                return getRoot(avlTree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private BTreeNode getRoot(Object tree) throws Exception {
        Field rootField = tree.getClass().getDeclaredField("root");
        rootField.setAccessible(true);
        return (BTreeNode) rootField.get(tree);
    }

    private void drawLevelLines() {
        // Dibujar líneas horizontales para los niveles
        for (int i = 1; i <= 8; i++) {
            double y = 40 + (i * 70); // Espaciado entre niveles
            if (y < PANE_HEIGHT - 50) {
                Line line = new Line(0, y, PANE_WIDTH, y);
                line.setStroke(Color.ORANGE);
                line.setStrokeWidth(1);
                treePane.getChildren().add(line);

                // Añadir números de nivel
                Text levelNumber = new Text(10, y - 5, String.valueOf(i - 1));
                levelNumber.setFill(Color.ORANGE);
                levelNumber.setFont(Font.font("System", FontWeight.BOLD, 12));
                treePane.getChildren().add(levelNumber);
            }
        }
    }

    private void drawTreeRecursive(BTreeNode node, double x, double y, double xOffset) {
        if (node == null) return;

        // Obtener hijos usando reflexión
        BTreeNode leftChild = getLeftChild(node);
        BTreeNode rightChild = getRightChild(node);

        // Dibujar conexiones a hijos (líneas)
        if (leftChild != null) {
            double childX = x - xOffset;
            double childY = y + 80;
            Line line = new Line(x, y, childX, childY);
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(2);
            treePane.getChildren().add(line);

            // Dibujar hijo izquierdo recursivamente con menos espaciado
            drawTreeRecursive(leftChild, childX, childY, xOffset / 1.8);
        }

        if (rightChild != null) {
            double childX = x + xOffset;
            double childY = y + 80;
            Line line = new Line(x, y, childX, childY);
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(2);
            treePane.getChildren().add(line);

            // Dibujar hijo derecho recursivamente con menos espaciado
            drawTreeRecursive(rightChild, childX, childY, xOffset / 1.8);
        }

        // Dibujar nodo actual
        Circle nodeCircle = new Circle(x, y, 20);
        nodeCircle.setFill(Color.CYAN);
        nodeCircle.setStroke(Color.DARKBLUE);
        nodeCircle.setStrokeWidth(2);
        treePane.getChildren().add(nodeCircle);

        // Dibujar valor del nodo
        Text value = new Text(String.valueOf(node.data));
        value.setX(x - value.getBoundsInLocal().getWidth() / 2);
        value.setY(y + 5);
        value.setFill(Color.BLACK);
        value.setFont(Font.font("System", FontWeight.BOLD, 12));
        treePane.getChildren().add(value);
    }

    private BTreeNode getLeftChild(BTreeNode node) {
        try {
            Field leftField = node.getClass().getDeclaredField("left");
            leftField.setAccessible(true);
            return (BTreeNode) leftField.get(node);
        } catch (Exception e) {
            return null;
        }
    }

    private BTreeNode getRightChild(BTreeNode node) {
        try {
            Field rightField = node.getClass().getDeclaredField("right");
            rightField.setAccessible(true);
            return (BTreeNode) rightField.get(node);
        } catch (Exception e) {
            return null;
        }
    }

    private void displayTourInfo() {
        try {
            StringBuilder info = new StringBuilder();
            int treeHeight = 0;

            if (rbBST.isSelected() && !bstTree.isEmpty()) {
                treeHeight = bstTree.height();
                info.append("BST Height: ").append(treeHeight).append("\n");
                info.append("PreOrder: ").append(bstTree.preOrder()).append("\n");
                info.append("InOrder: ").append(bstTree.inOrder()).append("\n");
                info.append("PostOrder: ").append(bstTree.postOrder()).append("\n");
                info.append("LevelOrder: ").append(getLevelOrder(bstTree)).append("\n");
            } else if (rbAVL.isSelected() && !avlTree.isEmpty()) {
                treeHeight = avlTree.height();
                info.append("AVL Height: ").append(treeHeight).append("\n");
                info.append("PreOrder: ").append(avlTree.preOrder()).append("\n");
                info.append("InOrder: ").append(avlTree.inOrder()).append("\n");
                info.append("PostOrder: ").append(avlTree.postOrder()).append("\n");
                info.append("LevelOrder: ").append(getLevelOrder(avlTree)).append("\n");
            }

            // Mostrar información en la parte inferior del panel
            Text tourText = new Text(info.toString());
            tourText.setX(20);
            tourText.setY(PANE_HEIGHT - 100);
            tourText.setFill(Color.BLACK);
            tourText.setFont(Font.font("System", FontWeight.NORMAL, 12));

            // Fondo blanco semi-transparente para legibilidad
            javafx.scene.shape.Rectangle background = new javafx.scene.shape.Rectangle(
                    15, PANE_HEIGHT - 120,
                    tourText.getBoundsInLocal().getWidth() + 20,
                    tourText.getBoundsInLocal().getHeight() + 20
            );
            background.setFill(Color.WHITE);
            background.setOpacity(0.8);
            background.setStroke(Color.GRAY);

            treePane.getChildren().addAll(background, tourText);

        } catch (TreeException e) {
            System.out.println("Tree is empty: " + e.getMessage());
        }
    }

    private String getLevelOrder(Tree tree) throws TreeException {
        if (tree.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        List<BTreeNode> currentLevel = new ArrayList<>();
        try {
            currentLevel.add(getRoot(tree));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        while (!currentLevel.isEmpty()) {
            List<BTreeNode> nextLevel = new ArrayList<>();
            for (BTreeNode node : currentLevel) {
                result.append(node.data).append(" ");
                BTreeNode left = getLeftChild(node);
                BTreeNode right = getRightChild(node);
                if (left != null) nextLevel.add(left);
                if (right != null) nextLevel.add(right);
            }
            currentLevel = nextLevel;
        }

        return result.toString().trim();
    }
}