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
import javafx.scene.input.MouseEvent;

public class RestauracjeController {
    
    
    
    @FXML
    private Button Kebab;

    @FXML
    private Button Makaron;

    @FXML
    private Button Pizza;

    @FXML
    private Button Sushi;

    public void wrocDoPoprzedniegoOkna(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/main/GlovoMiniApp.fxml")
            );
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/css/style.css").toExternalForm()
            );

            
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
        e.printStackTrace();
        }
    }


   
    public void przejdzDoSushi(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/main/Sushi.fxml"));

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
         e.printStackTrace();
        }
    }
    
    public void przejdzDoMakaron(ActionEvent event) {
      try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/main/Makaron.fxml"));

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
          e.printStackTrace();
        }
    }

    public void przejdzDoPizza(ActionEvent event) {
      try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/main/Pizza.fxml"));

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
          e.printStackTrace();
        }
    }
    
    public void przejdzDoKebab(ActionEvent event) {
      try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/main/Kebab.fxml"));

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
          e.printStackTrace();
        }
    }
    
    @FXML
    private void goToMain(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/main/MainController.fxml")
            );

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/css/style.css").toExternalForm()
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

