package com.example.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainController {

    public void wrocDoPoprzedniegoOkna(ActionEvent event) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/main/GlovoMiniApp.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        }
}

    public void zamowAkcja(ActionEvent event) {
        System.out.println("Kliknięto");
    }
    
    @FXML
    private Button Zakupy;
    @FXML
    private Button Restauracje;

    @FXML
    public void initialize() {
        Zakupy.getStyleClass().add("pick-button");
        Restauracje.getStyleClass().add("pick-button");
    }
    
    @FXML
private void przejdzDoRestauracje(ActionEvent event) {
    try {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/main/RestauracjeController.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false); 
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
}
    
}

