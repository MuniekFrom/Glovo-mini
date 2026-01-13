package com.example.main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class KoszykController {

    @FXML private TextArea koszykRachunekText;
    @FXML private TextField krajTextField, miastoTextField;
    @FXML private TextField ulicaTextField, numerTextField, kodTextField;
    @FXML private ComboBox<String> platnoscComboBox;
    @FXML private Button powrotPrzycisk, zapiszPlikPrzycisk;

    private String zawartoscKoszyka = "";

    // Ustawienie zawartości koszyka z innego kontrolera
    public void ustawZawartoscKoszyka(String rachunek) {
        this.zawartoscKoszyka = rachunek;
        koszykRachunekText.setText(rachunek);
    }

    @FXML
    private void initialize() {
        platnoscComboBox.getItems().addAll("Gotówka", "Karta", "Blik");
        platnoscComboBox.getSelectionModel().selectFirst();

        // Filtracja kodu pocztowego - tylko cyfry
        kodTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d*")) {
                kodTextField.setText(newText.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    public void powrotDoSklepow(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/main/Sklepy.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nie udało się otworzyć sklepu.");
            alert.showAndWait();
        }
    }

    @FXML
    public void zapiszKoszykDoPliku() {
        // Walidacja pól adresu
        if (krajTextField.getText().isEmpty() || miastoTextField.getText().isEmpty()
                || ulicaTextField.getText().isEmpty() || numerTextField.getText().isEmpty()
                || kodTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Błąd");
            alert.setHeaderText("Niepełny adres");
            alert.setContentText("Proszę uzupełnić wszystkie pola adresu.");
            alert.showAndWait();
            return;
        }

        if (zawartoscKoszyka.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Błąd");
            alert.setHeaderText("Koszyk pusty");
            alert.setContentText("Nie ma nic do zapisania.");
            alert.showAndWait();
            return;
        }

        String pelnyRachunek = new StringBuilder()
                .append(zawartoscKoszyka).append("\n")
                .append("Adres dostawy:\n")
                .append("Kraj: ").append(krajTextField.getText()).append("\n")
                .append("Miasto: ").append(miastoTextField.getText()).append("\n")
                .append("Ulica: ").append(ulicaTextField.getText()).append("\n")
                .append("Nr domu: ").append(numerTextField.getText()).append("\n")
                .append("Kod pocztowy: ").append(kodTextField.getText()).append("\n")
                .append("Płatność: ").append(platnoscComboBox.getValue())
                .toString();

        FileChooser fc = new FileChooser();
        fc.setTitle("Zapisz koszyk");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik tekstowy (*.txt)", "*.txt"));
        fc.setInitialFileName("koszyk_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt");

        Stage stage = (Stage) powrotPrzycisk.getScene().getWindow();
        File file = fc.showSaveDialog(stage);
        if (file == null) return;

        try {
            Files.writeString(file.toPath(), pelnyRachunek, StandardCharsets.UTF_8);
            Alert done = new Alert(Alert.AlertType.INFORMATION);
            done.setTitle("Koszyk");
            done.setHeaderText("Koszyk zapisany ✅");
            done.setContentText("Zapisano plik:\n" + file.getAbsolutePath());
            done.showAndWait();

            // Opcjonalnie: wyczyść pola po zapisie
            koszykRachunekText.clear();
            krajTextField.clear();
            miastoTextField.clear();
            ulicaTextField.clear();
            numerTextField.clear();
            kodTextField.clear();
            platnoscComboBox.getSelectionModel().selectFirst();
            zawartoscKoszyka = "";

        } catch (Exception e) {
            e.printStackTrace();
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Błąd");
            err.setHeaderText("Nie udało się zapisać koszyka");
            err.setContentText(e.getMessage());
            err.showAndWait();
        }
    }
}