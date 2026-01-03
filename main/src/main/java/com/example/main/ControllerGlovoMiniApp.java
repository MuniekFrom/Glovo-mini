package com.example.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerGlovoMiniApp {



    @FXML
    private void wyjdzZOkna(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
        stage.close();
    }

    @FXML
    public void zacznijMetodaDoKolejnegoOkna(ActionEvent event) {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/main/MainController.fxml"));
            Parent root = loader.load();

            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

            System.out.println("ZACZNIJ");

        } catch (IOException e) {
            System.err.println("BŁĄD przy kliknięciu ZACZNIJ");
            e.printStackTrace();
        }
    }
}