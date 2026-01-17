package com.example.main;

import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SklepyController {

    @FXML private ComboBox<String> kategoriaCombo;

    @FXML private ImageView sklepObrazek;
    @FXML private Text sklepOpis;
    @FXML private Button wejdzButton;

    
    private String targetFxml = null;

    @FXML
    private void initialize() {
        kategoriaCombo.getItems().addAll("Apteka", "Market", "Warzywniak");
        kategoriaCombo.setPromptText("Wybierz opcję");

     
        ukryjKarte();

        
        kategoriaCombo.setOnAction(e -> pokazWybranySklep());
    }

    private void pokazWybranySklep() {
        String wybor = kategoriaCombo.getValue();
        if (wybor == null) {
            ukryjKarte();
            return;
        }

        switch (wybor) {
            case "Apteka" -> {
                ustawKarte(
                    "/photos/apteka.png",
                    "Ból głowy? Katar? Zły dzień?\nMy mamy tabletki,\nktóre udają, że wszystko będzie dobrze.",
                    "/com/example/main/Apteka.fxml"
                );
            }
            case "Market" -> {
                ustawKarte(
                    "/photos/market.png",
                    "Skończyło się jedzenie „na już”?\nMarket ratuje sytuację.\nMy dowieziemy, Ty nie panikuj.",
                    "/com/example/main/Market.fxml"
                );
            }
            case "Warzywniak" -> {
                ustawKarte(
                    "/photos/warzywniak.png",
                    "Zdrowo, kolorowo i bez wymówek.\nWarzywa i owoce wpadają szybciej\nniż postanowienia od nowego tygodnia.",
                    "/com/example/main/Warzywniak.fxml"
                );
            }
            default -> ukryjKarte();
        }
    }

    private void ustawKarte(String obrazekPath, String opis, String fxmlPath) {
        
        URL imgUrl = getClass().getResource(obrazekPath);
        if (imgUrl != null) {
            sklepObrazek.setImage(new Image(imgUrl.toExternalForm()));
        } else {
            sklepObrazek.setImage(null); 
        }

       
        sklepOpis.setText(opis);

        
        targetFxml = fxmlPath;

       
        sklepObrazek.setVisible(true);
        sklepObrazek.setManaged(true);

        sklepOpis.setVisible(true);
        sklepOpis.setManaged(true);

        wejdzButton.setVisible(true);
        wejdzButton.setManaged(true);
    }

    private void ukryjKarte() {
        targetFxml = null;

        sklepObrazek.setVisible(false);
        sklepObrazek.setManaged(false);

        sklepOpis.setVisible(false);
        sklepOpis.setManaged(false);

        wejdzButton.setVisible(false);
        wejdzButton.setManaged(false);
    }

    @FXML
    private void wejdzDoSklepu(ActionEvent event) {
        if (targetFxml == null) return;

        try {
            Parent root = FXMLLoader.load(getClass().getResource(targetFxml));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
