package com.example.main;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MakaronController {

    @FXML private Text iloscSpaghetti, iloscCarbonara, iloscPenne, iloscPennePesto;
    @FXML private Text cenaKoncowaMakaron, jedzenieTextCena, zamowienieMinimumKwotaText;

    @FXML private CheckBox checkBoxSer, checkBoxOliwa;

    @FXML private Button usunSpaghetti, usunCarbonara, usunPenne, usunPennePesto;
    @FXML private Button powrotPrzycisk;

    private int spaghetti = 0, carbonara = 0, penne = 0, pennePesto = 0;

    private static final int CENA_SPAGHETTI = 32;
    private static final int CENA_CARBONARA = 34;
    private static final int CENA_PENNE = 30;
    private static final int CENA_PENNE_PESTO = 33;

    private static final int SER = 4;
    private static final int OLIWA = 3;
    private static final int DOSTAWA = 10;
    
    private static final int MIN_ZAMOWIENIA = 30;

    private int sumaJedzenia = 0;
    private int razemDoZaplaty = 0;

    @FXML
    private void initialize() {
        odswiezWidok();
    }


    @FXML private void plusSpaghetti(ActionEvent e) { spaghetti++; odswiezWidok(); }
    @FXML private void plusCarbonara(ActionEvent e) { carbonara++; odswiezWidok(); }
    @FXML private void plusPenne(ActionEvent e) { penne++; odswiezWidok(); }
    @FXML private void plusPennePesto(ActionEvent e) { pennePesto++; odswiezWidok(); }


    @FXML private void usunSpaghetti(ActionEvent e) { if (spaghetti > 0) spaghetti--; odswiezWidok(); }
    @FXML private void usunCarbonara(ActionEvent e) { if (carbonara > 0) carbonara--; odswiezWidok(); }
    @FXML private void usunPenne(ActionEvent e) { if (penne > 0) penne--; odswiezWidok(); }
    @FXML private void usunPennePesto(ActionEvent e) { if (pennePesto > 0) pennePesto--; odswiezWidok(); }


    @FXML private void dodatkiZmiana(ActionEvent e) { odswiezWidok(); }

    @FXML
    public void powrotDoWyboruRestauracji(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/main/RestauracjeController.fxml")
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

    private String zbudujRachunek(LocalDateTime data) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        int suma = spaghetti * CENA_SPAGHETTI
                + carbonara * CENA_CARBONARA
                + penne * CENA_PENNE
                + pennePesto * CENA_PENNE_PESTO;

        int dodatki = 0;
        StringBuilder dodatkiLista = new StringBuilder();

        if (checkBoxSer.isSelected()) { dodatki += SER; dodatkiLista.append("Ser +").append(SER).append(" zł\n"); }
        if (checkBoxOliwa.isSelected()) { dodatki += OLIWA; dodatkiLista.append("Oliwa +").append(OLIWA).append(" zł\n"); }

        int jedzenie = suma + dodatki;
        int dostawa = (jedzenie > 0) ? DOSTAWA : 0;
        int razem = jedzenie + dostawa;

        StringBuilder sb = new StringBuilder();
        sb.append("========== RACHUNEK - MAKARON MINI ==========\n");
        sb.append("Data zamówienia: ").append(data.format(fmt)).append("\n");
        sb.append("------------------------------------------\n");
        sb.append(String.format("%-15s %5s %8s %10s\n", "Produkt", "Ilość", "Cena", "Wartość"));
        sb.append("------------------------------------------\n");

        if (spaghetti > 0) sb.append(String.format("%-15s %5d %8d %10d\n", "Spaghetti", spaghetti, CENA_SPAGHETTI, spaghetti * CENA_SPAGHETTI));
        if (carbonara > 0) sb.append(String.format("%-15s %5d %8d %10d\n", "Carbonara", carbonara, CENA_CARBONARA, carbonara * CENA_CARBONARA));
        if (penne > 0) sb.append(String.format("%-15s %5d %8d %10d\n", "Penne", penne, CENA_PENNE, penne * CENA_PENNE));
        if (pennePesto > 0) sb.append(String.format("%-15s %5d %8d %10d\n", "Penne Pesto", pennePesto, CENA_PENNE_PESTO, pennePesto * CENA_PENNE_PESTO));

        sb.append("------------------------------------------\n");
        if (dodatki > 0) {
            sb.append("Dodatki:\n").append(dodatkiLista);
            sb.append("------------------------------------------\n");
        }

        sb.append(String.format("Jedzenie: %d zł\n", jedzenie));
        sb.append(String.format("Dostawa:  %d zł\n", dostawa));
        sb.append(String.format("RAZEM:    %d zł\n", razem));
        sb.append("==========================================\n");

        return sb.toString();
    }

    @FXML
    private void zamowienieJedzenia(ActionEvent event) {
        if (razemDoZaplaty < MIN_ZAMOWIENIA) {
            zamowienieMinimumKwotaText.setText("Minimalna wartość zamówienia: 30 zł");
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> zamowienieMinimumKwotaText.setText(""));
            pause.play();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Zamówienie");
        alert.setHeaderText("Zamówiono jedzenie ✅");
        alert.setContentText("Zapiszę rachunek do pliku.");
        alert.showAndWait();

        LocalDateTime data = LocalDateTime.now();
        String rachunek = zbudujRachunek(data);

        FileChooser fc = new FileChooser();
        fc.setTitle("Zapisz rachunek");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik tekstowy (*.txt)", "*.txt"));
        fc.setInitialFileName("rachunek_" + data.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt");

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fc.showSaveDialog(stage);
        
        if (file == null) return;

        try {
            Files.writeString(file.toPath(), rachunek, StandardCharsets.UTF_8);
            Alert done = new Alert(Alert.AlertType.INFORMATION);
            done.setTitle("Rachunek");
            done.setHeaderText("Rachunek zapisany ✅");
            done.setContentText("Zapisano plik:\n" + file.getAbsolutePath());
            done.showAndWait();
            wyczyscKoszyk();
        } catch (Exception e) {
            e.printStackTrace();
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Błąd");
            err.setHeaderText("Nie udało się zapisać rachunku");
            err.setContentText(e.getMessage());
            err.showAndWait();
        }
    }


    private void wyczyscKoszyk() {
        spaghetti = 0; 
        carbonara = 0; 
        penne = 0; 
        pennePesto = 0;
        checkBoxSer.setSelected(false);
        checkBoxOliwa.setSelected(false);
        zamowienieMinimumKwotaText.setText("");
        odswiezWidok();
    }
    
    private void odswiezWidok() {
        iloscSpaghetti.setText(String.valueOf(spaghetti));
        iloscCarbonara.setText(String.valueOf(carbonara));
        iloscPenne.setText(String.valueOf(penne));
        iloscPennePesto.setText(String.valueOf(pennePesto));

        usunSpaghetti.setVisible(spaghetti > 0);
        usunCarbonara.setVisible(carbonara > 0);
        usunPenne.setVisible(penne > 0);
        usunPennePesto.setVisible(pennePesto > 0);

        int suma = spaghetti * CENA_SPAGHETTI
                + carbonara * CENA_CARBONARA
                + penne * CENA_PENNE
                + pennePesto * CENA_PENNE_PESTO;

        boolean cosWKoszyku = suma > 0;

        checkBoxSer.setDisable(!cosWKoszyku);
        checkBoxOliwa.setDisable(!cosWKoszyku);
        if (!cosWKoszyku) {
            checkBoxSer.setSelected(false);
            checkBoxOliwa.setSelected(false);
        }

        int dodatki = 0;
        if (checkBoxSer.isSelected()) dodatki += SER;
        if (checkBoxOliwa.isSelected()) dodatki += OLIWA;

        int dostawa = cosWKoszyku ? DOSTAWA : 0;
        
        sumaJedzenia = suma + dodatki;
        razemDoZaplaty = sumaJedzenia + dostawa;

        jedzenieTextCena.setText(sumaJedzenia + " zł");
        cenaKoncowaMakaron.setText(razemDoZaplaty + " zł");
    }
}