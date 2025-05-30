package controller;

import domain.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.*;

public class OperationsController {

    @FXML
    private Button btnNodeHeight;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnContains;
    @FXML
    private Button btnRemove;
    @FXML
    private Button btnTreeHeight;
    @FXML
    private Button btnRandomize;
    @FXML
    private Pane treePane;
    @FXML
    private RadioButton radioBST;
    @FXML
    private RadioButton radioAVL;
    @FXML
    private Label lblStatus;

    private Tree currentTree;
    private ToggleGroup treeTypeGroup;
    private Random random = new Random();

    // Constantes para la visualización mejoradas
    private static final double NODE_RADIUS = 20;
    private static final double MIN_LEVEL_HEIGHT = 60;
    private static final double MIN_HORIZONTAL_SPACING = 45;
    private static final Color NODE_COLOR = Color.CYAN;
    private static final Color LINE_COLOR = Color.BLACK;
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final double MARGIN = 30;

    @FXML
    public void initialize() {
        treeTypeGroup = new ToggleGroup();
        radioBST.setToggleGroup(treeTypeGroup);
        radioAVL.setToggleGroup(treeTypeGroup);
        radioBST.setSelected(true);

        currentTree = new BST();
        updateStatus();
        setupEventHandlers();
        generateRandomValues();
    }

    private void setupEventHandlers() {
        btnRandomize.setOnAction(e -> generateRandomValues());
        btnAdd.setOnAction(e -> addElement());
        btnContains.setOnAction(e -> searchElement());
        btnRemove.setOnAction(e -> removeElement());
        btnNodeHeight.setOnAction(e -> getNodeHeight());
        btnTreeHeight.setOnAction(e -> getTreeHeight());

        treeTypeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> switchTreeType());
    }

    private void generateRandomValues() {
        try {
            currentTree.clear();
            for (int i = 0; i < 30; i++) {
                int value = random.nextInt(51);
                currentTree.add(value);
            }
            updateVisualization();
            updateStatus();
            showInfo("Se generaron 30 valores aleatorios entre 0 y 50");
        } catch (Exception ex) {
            showError("Error al generar valores aleatorios: " + ex.getMessage());
        }
    }

    private void addElement() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Elemento");
        dialog.setHeaderText("Ingrese el valor a agregar:");
        dialog.setContentText("Valor:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int value = Integer.parseInt(result.get());
                currentTree.add(value);
                updateVisualization();
                updateStatus();
                showInfo("Elemento " + value + " agregado correctamente");
            } catch (NumberFormatException ex) {
                showError("Por favor ingrese un número válido");
            } catch (Exception ex) {
                showError("Error al agregar elemento: " + ex.getMessage());
            }
        }
    }

    private void searchElement() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Elemento");
        dialog.setHeaderText("Ingrese el valor a buscar:");
        dialog.setContentText("Valor:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int value = Integer.parseInt(result.get());
                boolean found = currentTree.contains(value);

                if (found) {
                    showInfo("El elemento " + value + " SÍ está en el árbol");
                } else {
                    showInfo("El elemento " + value + " NO está en el árbol");
                }
            } catch (NumberFormatException ex) {
                showError("Por favor ingrese un número válido");
            } catch (TreeException ex) {
                showError("Error: " + ex.getMessage());
            }
        }
    }

    private void removeElement() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Eliminar Elemento");
        dialog.setHeaderText("Ingrese el valor a eliminar:");
        dialog.setContentText("Valor:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int value = Integer.parseInt(result.get());
                boolean removed = currentTree.remove(value);

                if (removed) {
                    updateVisualization();
                    updateStatus();
                    showInfo("Elemento " + value + " eliminado correctamente");
                } else {
                    showInfo("El elemento " + value + " no se encontró en el árbol");
                }
            } catch (NumberFormatException ex) {
                showError("Por favor ingrese un número válido");
            } catch (TreeException ex) {
                showError("Error: " + ex.getMessage());
            }
        }
    }

    private void getNodeHeight() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Altura del Nodo");
        dialog.setHeaderText("Ingrese el valor del nodo:");
        dialog.setContentText("Valor:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int value = Integer.parseInt(result.get());
                int height = currentTree.height(value);
                showInfo("La altura del nodo " + value + " es: " + height);
            } catch (NumberFormatException ex) {
                showError("Por favor ingrese un número válido");
            } catch (TreeException ex) {
                showError("Error: " + ex.getMessage());
            }
        }
    }

    private void getTreeHeight() {
        try {
            int height = currentTree.height();
            showInfo("La altura del árbol es: " + height);
        } catch (TreeException ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private void switchTreeType() {
        try {
            String inOrderData = "";
            if (!currentTree.isEmpty()) {
                inOrderData = currentTree.inOrder();
            }

            if (radioBST.isSelected()) {
                currentTree = new BST();
            } else {
                currentTree = new AVL();
            }

            if (!inOrderData.isEmpty()) {
                String[] values = inOrderData.trim().split("\\s+");
                for (String value : values) {
                    if (!value.isEmpty()) {
                        currentTree.add(Integer.parseInt(value));
                    }
                }
            }

            updateVisualization();
            updateStatus();
        } catch (Exception ex) {
            showError("Error al cambiar tipo de árbol: " + ex.getMessage());
        }
    }

    private void updateStatus() {
        try {
            String treeType = radioBST.isSelected() ? "BST" : "AVL";
            boolean balanced = isTreeBalanced();

            lblStatus.setText(treeType + (balanced ? " is balanced!!!" : " is not balanced!!!"));
            lblStatus.setTextFill(balanced ? Color.GREEN : Color.RED);
        } catch (Exception ex) {
            lblStatus.setText("Error al verificar balance");
            lblStatus.setTextFill(Color.RED);
        }
    }

    // Método mejorado para verificar si el árbol está balanceado
    private boolean isTreeBalanced() {
        try {
            BTreeNode root = getRoot();
            if (root == null) return true;

            // Para BST: verificar si está balanceado (diferencia de alturas <= 1)
            if (currentTree instanceof BST) {
                return isBalanced(root);
            }
            // Para AVL: siempre está balanceado por definición
            else if (currentTree instanceof AVL) {
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // Método recursivo para verificar balance
    private boolean isBalanced(BTreeNode node) {
        if (node == null) return true;

        int leftHeight = getHeight(node.left);
        int rightHeight = getHeight(node.right);

        return Math.abs(leftHeight - rightHeight) <= 1
                && isBalanced(node.left)
                && isBalanced(node.right);
    }

    // Método para obtener altura de un nodo
    private int getHeight(BTreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    private void updateVisualization() {
        treePane.getChildren().clear();

        try {

            String inOrderData = currentTree.inOrder();
            Label treeDataLabel = new Label("In-Order: " + inOrderData);
            treeDataLabel.setLayoutX(10);
            treeDataLabel.setLayoutY(10);
            treeDataLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50;");
            treePane.getChildren().add(treeDataLabel);

            if (!currentTree.isEmpty()) {
                BTreeNode root = getRoot();
                if (root != null) {

                    double availableWidth = treePane.getWidth() - (2 * MARGIN);
                    double availableHeight = treePane.getHeight() - 50; // Espacio para el label


                    int treeHeight = getHeight(root);
                    double levelHeight = Math.max(MIN_LEVEL_HEIGHT, availableHeight / Math.max(treeHeight, 1));


                    int maxNodesAtBottom = (int) Math.pow(2, treeHeight - 1);
                    double initialSpacing = Math.max(MIN_HORIZONTAL_SPACING,
                            availableWidth / Math.max(maxNodesAtBottom * 2, 1));

                    drawTreeImproved(root, availableWidth / 2 + MARGIN, 50, initialSpacing, levelHeight);
                }
            }
        } catch (TreeException ex) {
            showError("Error al visualizar el árbol: " + ex.getMessage());
        }
    }

    private BTreeNode getRoot() {
        try {

            if (currentTree instanceof BST) {
                return ((BST) currentTree).getRoot();
            } else if (currentTree instanceof AVL) {
                return ((AVL) currentTree).getRoot();
            }
        } catch (Exception e) {

            try {
                java.lang.reflect.Field rootField = currentTree.getClass().getDeclaredField("root");
                rootField.setAccessible(true);
                return (BTreeNode) rootField.get(currentTree);
            } catch (Exception ex) {
                System.err.println("No se pudo acceder al root: " + ex.getMessage());
            }
        }
        return null;
    }


    private void drawTreeImproved(BTreeNode node, double x, double y, double horizontalSpacing, double levelHeight) {
        if (node == null) return;


        double childSpacing = Math.max(MIN_HORIZONTAL_SPACING, horizontalSpacing * 0.6);


        if (node.left != null) {
            double leftX = x - horizontalSpacing;
            double leftY = y + levelHeight;


            leftX = Math.max(MARGIN + NODE_RADIUS, leftX);
            leftX = Math.min(treePane.getWidth() - MARGIN - NODE_RADIUS, leftX);

            Line leftLine = new Line(x, y, leftX, leftY);
            leftLine.setStroke(LINE_COLOR);
            leftLine.setStrokeWidth(2);
            treePane.getChildren().add(leftLine);

            drawTreeImproved(node.left, leftX, leftY, childSpacing, levelHeight);
        }

        if (node.right != null) {
            double rightX = x + horizontalSpacing;
            double rightY = y + levelHeight;

            rightX = Math.max(MARGIN + NODE_RADIUS, rightX);
            rightX = Math.min(treePane.getWidth() - MARGIN - NODE_RADIUS, rightX);

            Line rightLine = new Line(x, y, rightX, rightY);
            rightLine.setStroke(LINE_COLOR);
            rightLine.setStrokeWidth(2);
            treePane.getChildren().add(rightLine);

            drawTreeImproved(node.right, rightX, rightY, childSpacing, levelHeight);
        }


        double nodeX = Math.max(MARGIN + NODE_RADIUS, x);
        nodeX = Math.min(treePane.getWidth() - MARGIN - NODE_RADIUS, nodeX);

        Circle circle = new Circle(nodeX, y, NODE_RADIUS);
        circle.setFill(NODE_COLOR);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        treePane.getChildren().add(circle);


        Text text = new Text(nodeX, y + 5, node.data.toString());
        text.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        text.setFill(TEXT_COLOR);

        text.setX(nodeX - text.getBoundsInLocal().getWidth() / 2);
        treePane.getChildren().add(text);
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}