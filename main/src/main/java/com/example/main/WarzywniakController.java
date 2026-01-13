package com.example.main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.control.Alert;

public class WarzywniakController {

    // ====== Teksty ilości ======
    @FXML private Text iloscPomidor, iloscOgorek, iloscMarchew, iloscPapryka;
    @FXML private Text iloscSalata, iloscZiemniaki, iloscCebula;

    // ====== Teksty cen ======
    @FXML private Text cenaKoncowaWarzywniak, produktyTextCena, zamowienieMinimumKwotaText;

    // ====== Przyciski ======
    @FXML private Button usunPomidor, usunOgorek, usunMarchew, usunPapryka;
    @FXML private Button plusPomidor, plusOgorek, plusMarchew, plusPapryka;
    @FXML private Button usunSalata, plusSalata;
    @FXML private Button usunZiemniaki, plusZiemniaki;
    @FXML private Button usunCebula, plusCebula;

    @FXML private Button powrotPrzycisk;

    // ====== Ilości produktów ======
    private int pomidor = 0, ogorek = 0, marchew = 0, papryka = 0;
    private int salata = 0, ziemniaki = 0, cebula = 0;

    // ====== Ceny ======
    private static final int CENA_POMIDOR = 5;
    private static final int CENA_OGOREK = 4;
    private static final int CENA_MARCHEW = 3;
    private static final int CENA_PAPRYKA = 6;
    private static final int CENA_SALATA = 4;
    private static final int CENA_ZIEMNIAKI = 2;
    private static final int CENA_CEBULA = 2;

    private static final int DOSTAWA = 10;
    private static final int MIN_ZAMOWIENIA = 30;

    private int sumaProduktow = 0;
    private int razemDoZaplaty = 0;

    @FXML
    private void initialize() {
        odswiezWidok();
    }

    // ====== PLUSY ======
    @FXML private void plusPomidor(ActionEvent e) { pomidor++; odswiezWidok(); }
    @FXML private void plusOgorek(ActionEvent e) { ogorek++; odswiezWidok(); }
    @FXML private void plusMarchew(ActionEvent e) { marchew++; odswiezWidok(); }
    @FXML private void plusPapryka(ActionEvent e) { papryka++; odswiezWidok(); }
    @FXML private void plusSalata(ActionEvent e) { salata++; odswiezWidok(); }
    @FXML private void plusZiemniaki(ActionEvent e) { ziemniaki++; odswiezWidok(); }
    @FXML private void plusCebula(ActionEvent e) { cebula++; odswiezWidok(); }

    // ====== MINUSY ======
    @FXML private void usunPomidor(ActionEvent e) { if(pomidor>0) pomidor--; odswiezWidok(); }
    @FXML private void usunOgorek(ActionEvent e) { if(ogorek>0) ogorek--; odswiezWidok(); }
    @FXML private void usunMarchew(ActionEvent e) { if(marchew>0) marchew--; odswiezWidok(); }
    @FXML private void usunPapryka(ActionEvent e) { if(papryka>0) papryka--; odswiezWidok(); }
    @FXML private void usunSalata(ActionEvent e) { if(salata>0) salata--; odswiezWidok(); }
    @FXML private void usunZiemniaki(ActionEvent e) { if(ziemniaki>0) ziemniaki--; odswiezWidok(); }
    @FXML private void usunCebula(ActionEvent e) { if(cebula>0) cebula--; odswiezWidok(); }

    // ====== POWRÓT ======
    @FXML
    public void powrotDoWyboruSklepow(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/main/Sklepy.fxml"));
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

    // ====== RACHUNEK ======
    private String zbudujRachunek(LocalDateTime data) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int suma = pomidor*CENA_POMIDOR + ogorek*CENA_OGOREK + marchew*CENA_MARCHEW + papryka*CENA_PAPRYKA
                + salata*CENA_SALATA + ziemniaki*CENA_ZIEMNIAKI + cebula*CENA_CEBULA;

        int dostawa = (suma > 0) ? DOSTAWA : 0;
        int razem = suma + dostawa;

        StringBuilder sb = new StringBuilder();
        sb.append("====== RACHUNEK - WARZYWNIAK ======\n");
        sb.append("Data zamówienia: ").append(data.format(fmt)).append("\n");
        sb.append("----------------------------------\n");
        sb.append(String.format("%-20s %5s %8s %10s\n", "Produkt", "Ilość", "Cena", "Wartość"));
        sb.append("----------------------------------\n");

        if(pomidor>0) sb.append(String.format("%-20s %5d %8d %10d\n","Pomidor",pomidor,CENA_POMIDOR,pomidor*CENA_POMIDOR));
        if(ogorek>0) sb.append(String.format("%-20s %5d %8d %10d\n","Ogórek",ogorek,CENA_OGOREK,ogorek*CENA_OGOREK));
        if(marchew>0) sb.append(String.format("%-20s %5d %8d %10d\n","Marchew",marchew,CENA_MARCHEW,marchew*CENA_MARCHEW));
        if(papryka>0) sb.append(String.format("%-20s %5d %8d %10d\n","Papryka",papryka,CENA_PAPRYKA,papryka*CENA_PAPRYKA));
        if(salata>0) sb.append(String.format("%-20s %5d %8d %10d\n","Sałata",salata,CENA_SALATA,salata*CENA_SALATA));
        if(ziemniaki>0) sb.append(String.format("%-20s %5d %8d %10d\n","Ziemniaki",ziemniaki,CENA_ZIEMNIAKI,ziemniaki*CENA_ZIEMNIAKI));
        if(cebula>0) sb.append(String.format("%-20s %5d %8d %10d\n","Cebula",cebula,CENA_CEBULA,cebula*CENA_CEBULA));

        sb.append("----------------------------------\n");
        sb.append(String.format("Produkty: %d zł\n", suma));
        sb.append(String.format("Dostawa:  %d zł\n", dostawa));
        sb.append(String.format("RAZEM:    %d zł\n", razem));
        sb.append("==================================\n");

        return sb.toString();
    }

    // ====== ZAMÓW ======
    @FXML
    private void zamowienie(ActionEvent event) {
        if(razemDoZaplaty < MIN_ZAMOWIENIA) {
            zamowienieMinimumKwotaText.setText("Minimalna wartość zamówienia: 30 zł");
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> zamowienieMinimumKwotaText.setText(""));
            pause.play();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Zamówienie");
        alert.setHeaderText("Zamówiono produkty ✅");
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

        if(file == null) return;

        try {
            Files.writeString(file.toPath(), rachunek, StandardCharsets.UTF_8);
            Alert done = new Alert(Alert.AlertType.INFORMATION);
            done.setTitle("Rachunek");
            done.setHeaderText("Rachunek zapisany ✅");
            done.setContentText("Zapisano plik:\n" + file.getAbsolutePath());
            done.showAndWait();
            wyczyscKoszyk();
        } catch(Exception e) {
            e.printStackTrace();
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Błąd");
            err.setHeaderText("Nie udało się zapisać rachunku");
            err.setContentText(e.getMessage());
            err.showAndWait();
        }
    }

    private void wyczyscKoszyk() {
        pomidor=0; ogorek=0; marchew=0; papryka=0;
        salata=0; ziemniaki=0; cebula=0;
        zamowienieMinimumKwotaText.setText("");
        odswiezWidok();
    }

    private void odswiezWidok() {
        iloscPomidor.setText(String.valueOf(pomidor));
        iloscOgorek.setText(String.valueOf(ogorek));
        iloscMarchew.setText(String.valueOf(marchew));
        iloscPapryka.setText(String.valueOf(papryka));
        iloscSalata.setText(String.valueOf(salata));
        iloscZiemniaki.setText(String.valueOf(ziemniaki));
        iloscCebula.setText(String.valueOf(cebula));

        usunPomidor.setVisible(pomidor>0);
        usunOgorek.setVisible(ogorek>0);
        usunMarchew.setVisible(marchew>0);
        usunPapryka.setVisible(papryka>0);
        usunSalata.setVisible(salata>0);
        usunZiemniaki.setVisible(ziemniaki>0);
        usunCebula.setVisible(cebula>0);

        int suma = pomidor*CENA_POMIDOR + ogorek*CENA_OGOREK + marchew*CENA_MARCHEW + papryka*CENA_PAPRYKA
                + salata*CENA_SALATA + ziemniaki*CENA_ZIEMNIAKI + cebula*CENA_CEBULA;

        boolean cosWKoszyku = suma>0;
        sumaProduktow = suma;
        razemDoZaplaty = suma + (cosWKoszyku ? DOSTAWA : 0);

        produktyTextCena.setText(sumaProduktow + " zł");
        cenaKoncowaWarzywniak.setText(razemDoZaplaty + " zł");
    }
}