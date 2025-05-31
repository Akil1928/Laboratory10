package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import ucr.lab.laboratory10.HelloApplication;

import java.io.IOException;

public class HelloController {

    @FXML
    private BorderPane bp;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private Text txtMessage;

    // Método para cargar FXML (sin cambios)
    private void load(String form) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/" + form));

            if (fxmlLoader.getLocation() == null) {
                System.err.println("No se puede encontrar el archivo FXML: " + form);
                return;
            }

            this.bp.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar el FXML: " + form, e);
        }
    }

    @FXML
    void Exit(ActionEvent event) {
        System.exit(0);
    }

    // SOLUCIÓN: Método Home corregido
    @FXML
    void Home(ActionEvent event) {
        System.out.println("Home button clicked!"); // Debug

        // Opción 1: Usar solo el texto simple (tu implementación original)
        txtMessage.setText("Laboratory No. 10");
        bp.setCenter(contentPane);

        // Limpiar contentPane y agregar solo el mensaje
        contentPane.getChildren().clear();
        contentPane.getChildren().add(txtMessage);

        // Centrar el texto
        AnchorPane.setTopAnchor(txtMessage, null);
        AnchorPane.setBottomAnchor(txtMessage, null);
        AnchorPane.setLeftAnchor(txtMessage, null);
        AnchorPane.setRightAnchor(txtMessage, null);

        // Centrar manualmente
        txtMessage.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            double centerX = (contentPane.getWidth() - newBounds.getWidth()) / 2;
            double centerY = (contentPane.getHeight() - newBounds.getHeight()) / 2;
            txtMessage.setLayoutX(centerX);
            txtMessage.setLayoutY(centerY);
        });
    }

    // ALTERNATIVA: Método Home con interfaz mejorada
    @FXML
    void HomeEnhanced(ActionEvent event) {
        System.out.println("Home Enhanced button clicked!"); // Debug

        // Crear contenido de home más elaborado
        VBox homeContent = createEnhancedHomeContent();

        // Establecer en el BorderPane
        bp.setCenter(homeContent);
    }

    // Crear contenido mejorado para Home
    private VBox createEnhancedHomeContent() {
        VBox homeBox = new VBox(30);
        homeBox.setAlignment(Pos.CENTER);
        homeBox.setPadding(new Insets(50));
        homeBox.setStyle("-fx-background-color: linear-gradient(to bottom, #ff6b35, #f7931e);");

        // Título principal
        Label mainTitle = new Label("Laboratory No. 10");
        mainTitle.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Subtítulo
        Label subtitle = new Label("Graphic BST / AVL Tree");
        subtitle.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-opacity: 0.9;");

        // Descripción
        Label description = new Label("Data Structures - Binary Search Trees Visualization");
        description.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-opacity: 0.8;");

        // Contenedor de botones de navegación rápida
        VBox quickNav = new VBox(15);
        quickNav.setAlignment(Pos.CENTER);
        quickNav.setPadding(new Insets(30, 0, 0, 0));

        Button graphicBtn = createQuickNavButton("🌳 Graphic View", "#4CAF50");
        Button tourBtn = createQuickNavButton("🎓 Learning Tour", "#2196F3");
        Button opsBtn = createQuickNavButton("⚙️ Operations", "#FF9800");

        // Agregar funcionalidad a botones
        graphicBtn.setOnAction(e -> load("graphic-BST-AVL.fxml"));
        tourBtn.setOnAction(e -> load("tour BST-AVL.fxml"));
        opsBtn.setOnAction(e -> load("BST-AVL operations.fxml"));

        quickNav.getChildren().addAll(graphicBtn, tourBtn, opsBtn);

        homeBox.getChildren().addAll(mainTitle, subtitle, description, quickNav);

        return homeBox;
    }

    // Crear botones de navegación rápida
    private Button createQuickNavButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefWidth(250);
        button.setPrefHeight(45);
        button.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 22; " +
                        "-fx-cursor: hand;"
        );

        // Efecto hover
        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: derive(" + color + ", 20%); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-background-radius: 22; " +
                            "-fx-cursor: hand; " +
                            "-fx-scale-x: 1.05; " +
                            "-fx-scale-y: 1.05;"
            );
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-background-color: " + color + "; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-background-radius: 22; " +
                            "-fx-cursor: hand; " +
                            "-fx-scale-x: 1.0; " +
                            "-fx-scale-y: 1.0;"
            );
        });

        return button;
    }

    // Resto de métodos sin cambios
    @FXML
    void bubbleSortOnAction(ActionEvent event) {
        load("graphic-BST-AVL.fxml");
    }

    @FXML
    void impBubbleSortOnAction(ActionEvent event) {
        load("tour BST-AVL.fxml");
    }

    @FXML
    void selectionSortOnAction(ActionEvent event) {
        load("BST-AVL operations.fxml");
    }
}