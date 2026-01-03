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

    
     @FXML
    private Button Zakupy;
    @FXML
    private Button Restauracje;
    
     @FXML
    public void initialize() {
        Zakupy.getStyleClass().add("pick-button");
        Restauracje.getStyleClass().add("pick-button");
    }
  
public void wrocDoPoprzedniegoOkna(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/main/GlovoMiniApp.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        Stage stage = (Stage) ((javafx.scene.control.MenuItem) event.getSource())
                .getParentPopup().getOwnerWindow();

        stage.setScene(scene);

    } catch (IOException e) {
        e.printStackTrace();
    }
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
            System.err.println("BŁĄD przy kliknięciu ZACZNIJ");
            e.printStackTrace();
        }
}






    
}

