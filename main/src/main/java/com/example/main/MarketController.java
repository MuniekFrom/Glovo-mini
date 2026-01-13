package com.example.main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MarketController {

    @FXML private Text iloscChleb, iloscMleko, iloscJajka, iloscMaslo, iloscSer, iloscJogurt, iloscSok;
    @FXML private Text produktyTextCena, cenaKoncowaSporzywczy, zamowienieMinimumKwotaText;

    @FXML private Button usunChleb, usunMleko, usunJajka, usunMaslo, usunSer, usunJogurt, usunSok;
    @FXML private Button powrotPrzycisk;

    private int chleb=0, mleko=0, jajka=0, maslo=0, ser=0, jogurt=0, sok=0;

    private static final int CENA_CHLEB = 5;
    private static final int CENA_MLEKO = 4;
    private static final int CENA_JAJKA = 8;
    private static final int CENA_MASLO = 10;
    private static final int CENA_SER = 12;
    private static final int CENA_JOGURT = 6;
    private static final int CENA_SOK = 7;

    private static final int DOSTAWA = 10;
    private static final int MIN_ZAMOWIENIA = 20;

    private int sumaProduktow = 0;
    private int razemDoZaplaty = 0;

    @FXML
    private void initialize() {
        odswiezWidok();
    }

    // ====== PLUSY ======
    @FXML private void plusChleb(ActionEvent e) { chleb++; odswiezWidok(); }
    @FXML private void plusMleko(ActionEvent e) { mleko++; odswiezWidok(); }
    @FXML private void plusJajka(ActionEvent e) { jajka++; odswiezWidok(); }
    @FXML private void plusMaslo(ActionEvent e) { maslo++; odswiezWidok(); }
    @FXML private void plusSer(ActionEvent e) { ser++; odswiezWidok(); }
    @FXML private void plusJogurt(ActionEvent e) { jogurt++; odswiezWidok(); }
    @FXML private void plusSok(ActionEvent e) { sok++; odswiezWidok(); }

    // ====== MINUSY ======
    @FXML private void usunChleb(ActionEvent e) { if(chleb>0) chleb--; odswiezWidok(); }
    @FXML private void usunMleko(ActionEvent e) { if(mleko>0) mleko--; odswiezWidok(); }
    @FXML private void usunJajka(ActionEvent e) { if(jajka>0) jajka--; odswiezWidok(); }
    @FXML private void usunMaslo(ActionEvent e) { if(maslo>0) maslo--; odswiezWidok(); }
    @FXML private void usunSer(ActionEvent e) { if(ser>0) ser--; odswiezWidok(); }
    @FXML private void usunJogurt(ActionEvent e) { if(jogurt>0) jogurt--; odswiezWidok(); }
    @FXML private void usunSok(ActionEvent e) { if(sok>0) sok--; odswiezWidok(); }

    // ====== POWRÓT ======
    @FXML
    public void powrotDoWyboruSklepow(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/example/main/Sklepy.fxml")
            );
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                getClass().getResource("/css/style.css").toExternalForm()
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // ====== RACHUNEK ======
    private String zbudujRachunek(LocalDateTime data) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        int suma = chleb*CENA_CHLEB + mleko*CENA_MLEKO + jajka*CENA_JAJKA
                + maslo*CENA_MASLO + ser*CENA_SER + jogurt*CENA_JOGURT + sok*CENA_SOK;

        int dostawa = (suma>0)? DOSTAWA : 0;
        int razem = suma + dostawa;

        StringBuilder sb = new StringBuilder();
        sb.append("====== RACHUNEK - SKLEP SPOŻYWCZY ======\n");
        sb.append("Data zamówienia: ").append(data.format(fmt)).append("\n");
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-15s %5s %8s %10s\n", "Produkt", "Ilość", "Cena", "Wartość"));
        sb.append("----------------------------------------\n");

        if(chleb>0) sb.append(String.format("%-15s %5d %8d %10d\n","Chleb", chleb, CENA_CHLEB, chleb*CENA_CHLEB));
        if(mleko>0) sb.append(String.format("%-15s %5d %8d %10d\n","Mleko", mleko, CENA_MLEKO, mleko*CENA_MLEKO));
        if(jajka>0) sb.append(String.format("%-15s %5d %8d %10d\n","Jajka", jajka, CENA_JAJKA, jajka*CENA_JAJKA));
        if(maslo>0) sb.append(String.format("%-15s %5d %8d %10d\n","Masło", maslo, CENA_MASLO, maslo*CENA_MASLO));
        if(ser>0) sb.append(String.format("%-15s %5d %8d %10d\n","Ser", ser, CENA_SER, ser*CENA_SER));
        if(jogurt>0) sb.append(String.format("%-15s %5d %8d %10d\n","Jogurt", jogurt, CENA_JOGURT, jogurt*CENA_JOGURT));
        if(sok>0) sb.append(String.format("%-15s %5d %8d %10d\n","Sok", sok, CENA_SOK, sok*CENA_SOK));

        sb.append("----------------------------------------\n");
        sb.append(String.format("Produkty: %d zł\n", suma));
        sb.append(String.format("Dostawa: %d zł\n", dostawa));
        sb.append(String.format("RAZEM: %d zł\n", razem));
        sb.append("========================================\n");

        return sb.toString();
    }

    // ====== ZAMÓW ======
    @FXML
    private void zamowienie(ActionEvent event) {
        if(razemDoZaplaty<MIN_ZAMOWIENIA){
            zamowienieMinimumKwotaText.setText("Minimalna wartość zamówienia: "+MIN_ZAMOWIENIA+" zł");
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e->zamowienieMinimumKwotaText.setText(""));
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
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik tekstowy (*.txt)","*.txt"));
        fc.setInitialFileName("rachunek_"+data.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))+".txt");

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        File file = fc.showSaveDialog(stage);

        if(file==null) return;

        try{
            Files.writeString(file.toPath(), rachunek, StandardCharsets.UTF_8);
            Alert done = new Alert(Alert.AlertType.INFORMATION);
            done.setTitle("Rachunek");
            done.setHeaderText("Rachunek zapisany ✅");
            done.setContentText("Zapisano plik:\n"+file.getAbsolutePath());
            done.showAndWait();
            wyczyscKoszyk();
        }catch(Exception e){
            e.printStackTrace();
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Błąd");
            err.setHeaderText("Nie udało się zapisać rachunku");
            err.setContentText(e.getMessage());
            err.showAndWait();
        }
    }

    private void wyczyscKoszyk(){
        chleb=0; mleko=0; jajka=0; maslo=0; ser=0; jogurt=0; sok=0;
        zamowienieMinimumKwotaText.setText("");
        odswiezWidok();
    }

    private void odswiezWidok(){
        iloscChleb.setText(String.valueOf(chleb));
        iloscMleko.setText(String.valueOf(mleko));
        iloscJajka.setText(String.valueOf(jajka));
        iloscMaslo.setText(String.valueOf(maslo));
        iloscSer.setText(String.valueOf(ser));
        iloscJogurt.setText(String.valueOf(jogurt));
        iloscSok.setText(String.valueOf(sok));

        usunChleb.setVisible(chleb>0);
        usunMleko.setVisible(mleko>0);
        usunJajka.setVisible(jajka>0);
        usunMaslo.setVisible(maslo>0);
        usunSer.setVisible(ser>0);
        usunJogurt.setVisible(jogurt>0);
        usunSok.setVisible(sok>0);

        int suma = chleb*CENA_CHLEB + mleko*CENA_MLEKO + jajka*CENA_JAJKA
                + maslo*CENA_MASLO + ser*CENA_SER + jogurt*CENA_JOGURT + sok*CENA_SOK;

        boolean cosWKoszyku = suma>0;
        sumaProduktow = suma;
        razemDoZaplaty = suma + (cosWKoszyku ? DOSTAWA : 0);

        produktyTextCena.setText(sumaProduktow + " zł");
        cenaKoncowaSporzywczy.setText(razemDoZaplaty + " zł");
    }
}