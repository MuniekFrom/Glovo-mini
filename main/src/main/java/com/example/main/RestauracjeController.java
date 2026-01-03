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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/main/MainController.fxml"));
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
   
   public void przejdzDoSushi(ActionEvent event){
         try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/main/Sushi.fxml"));
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

