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

public class SushiController {
    
    @FXML
    private Button powrotPrzycisk;
    
      @FXML
    private CheckBox checkBoxImbir;

    @FXML
    private CheckBox checkBoxSosSojowy;

    @FXML
    private CheckBox checkBoxWasabi;
    
     @FXML
    private Text iloscCalifornia;

    @FXML
    private Text iloscFutomaki;

    @FXML
    private Text iloscNigiri;

    @FXML
    private Text iloscPhiladelphia;

    @FXML
    private Text iloscTemaki;

   @FXML
    private Button usunCalifornia;

    @FXML
    private Button usunFutomaki;

    @FXML
    private Button usunNigiri;

    @FXML
    private Button usunPhiladelphia;

    @FXML
    private Button usunTemaki;
    
    @FXML
    private Text cenaKoncowaSushi;
    
    @FXML
    private Text jedzenieTextCena;
    
    @FXML
    private Text zamowienieMinimumKwotaText;
    
   
    
    
    private int futomaki = 0, nigiri = 0, california = 0, philadelphia = 0, temaki = 0;

    private static final int CENA_FUTOMAKI = 28;
    private static final int CENA_NIGIRI = 14;
    private static final int CENA_CALIFORNIA = 32;
    private static final int CENA_PHILADELPHIA = 34;
    private static final int CENA_TEMAKI = 25;

    private static final int DODATEK = 3;
    private static final int DOSTAWA = 10;
    
    private static final int MIN_ZAMOWIENIA = 100;

    private int sumaJedzenia = 0; 
    private int razemDoZaplaty = 0; 

    @FXML
    private void initialize() {
        odswiezWidok();
    }

    @FXML
    private void plusFutomaki(ActionEvent e) {
        futomaki++; 
        odswiezWidok(); 
    }

    @FXML
    private void plusNigiri(ActionEvent e) { 
        nigiri++; 
        odswiezWidok(); 
    }

    @FXML
    private void plusCalifornia(ActionEvent e) {
        california++; 
        odswiezWidok(); 
    }

    @FXML
    private void plusPhiladelphia(ActionEvent e) {
        philadelphia++; 
        odswiezWidok(); 
    }

    @FXML
    private void plusTemaki(ActionEvent e) {
        temaki++; 
        odswiezWidok(); 
    }

    @FXML
    private void dodatkiZmiana(ActionEvent e) { 
        odswiezWidok();
    }
    
    
    
    @FXML
    private void usunFutomaki() {
        if (futomaki > 0) futomaki--;
        odswiezWidok();
    }

    @FXML
    private void usunNigiri() {
        if (nigiri > 0) nigiri--;
        odswiezWidok();
    }

    @FXML
    private void usunCalifornia() {
        if (california > 0) california--;
        odswiezWidok();
    }

    @FXML
    private void usunPhiladelphia() {
        if (philadelphia > 0) philadelphia--;
        odswiezWidok();
    }

    @FXML
    private void usunTemaki() {
        if (temaki > 0) temaki--;
        odswiezWidok();
    }
    
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

        int suma = futomaki * CENA_FUTOMAKI
                + nigiri * CENA_NIGIRI
                + california * CENA_CALIFORNIA
                + philadelphia * CENA_PHILADELPHIA
                + temaki * CENA_TEMAKI;

        int dodatki = 0;
        StringBuilder dodatkiLista = new StringBuilder();

        if (checkBoxImbir.isSelected()) { 
            dodatki += DODATEK; dodatkiLista.append("Imbir +").append(DODATEK).append(" zł\n"); 
        }
        
        if (checkBoxWasabi.isSelected()) {
            dodatki += DODATEK; dodatkiLista.append("Wasabi +").append(DODATEK).append(" zł\n"); 
        }
        if (checkBoxSosSojowy.isSelected()) {
            dodatki += DODATEK; dodatkiLista.append("Sos sojowy +").append(DODATEK).append(" zł\n"); 
        }

        int jedzenie = suma + dodatki;
        int dostawa = (jedzenie > 0) ? DOSTAWA : 0;
        int razem = jedzenie + dostawa;

        StringBuilder sb = new StringBuilder();
        sb.append("========== RACHUNEK - GLOVO MINI ==========\n");
        sb.append("Data zamówienia: ").append(data.format(fmt)).append("\n");
        sb.append("------------------------------------------\n");
        sb.append(String.format("%-15s %5s %8s %10s\n", "Produkt", "Ilość", "Cena", "Wartość"));
        sb.append("------------------------------------------\n");

    
        if (futomaki > 0){
            sb.append(String.format("%-15s %5d %8d %10d\n", "Futomaki", futomaki, CENA_FUTOMAKI, futomaki * CENA_FUTOMAKI));
        }
        if (nigiri > 0){
            sb.append(String.format("%-15s %5d %8d %10d\n", "Nigiri", nigiri, CENA_NIGIRI, nigiri * CENA_NIGIRI));
        }
        if (california > 0){
            sb.append(String.format("%-15s %5d %8d %10d\n", "California", california, CENA_CALIFORNIA, california * CENA_CALIFORNIA));
        }
        if (philadelphia > 0){
            sb.append(String.format("%-15s %5d %8d %10d\n", "Philadelphia", philadelphia, CENA_PHILADELPHIA, philadelphia * CENA_PHILADELPHIA));
        }
        if (temaki > 0){
            sb.append(String.format("%-15s %5d %8d %10d\n", "Temaki", temaki, CENA_TEMAKI, temaki * CENA_TEMAKI));
        }

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
    
    
    
    private void wyczyscKoszyk() {
        futomaki = 0;
        nigiri = 0;
        california = 0;
        philadelphia = 0;
        temaki = 0;

        checkBoxImbir.setSelected(false);
        checkBoxSosSojowy.setSelected(false);
        checkBoxWasabi.setSelected(false);

        zamowienieMinimumKwotaText.setText("");

        odswiezWidok();
    }
    
    @FXML
    private void zamowienieJedzenia(ActionEvent event) {

        if (razemDoZaplaty < MIN_ZAMOWIENIA) {
            zamowienieMinimumKwotaText.setText("Minimalna wartość zamówienia: 100 zł");

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
        
        if (file == null){
            return;
        } 

    
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
    
    

    

    private void odswiezWidok() {
        iloscFutomaki.setText(String.valueOf(futomaki));
        iloscNigiri.setText(String.valueOf(nigiri));
        iloscCalifornia.setText(String.valueOf(california));
        iloscPhiladelphia.setText(String.valueOf(philadelphia));
        iloscTemaki.setText(String.valueOf(temaki));
        
        usunFutomaki.setVisible(futomaki > 0);
        usunNigiri.setVisible(nigiri > 0);
        usunCalifornia.setVisible(california > 0);
        usunPhiladelphia.setVisible(philadelphia > 0);
        usunTemaki.setVisible(temaki > 0);

        
        int suma = futomaki * CENA_FUTOMAKI
                + nigiri * CENA_NIGIRI
                + california * CENA_CALIFORNIA
                + philadelphia * CENA_PHILADELPHIA
                + temaki * CENA_TEMAKI;
        
        

        boolean cosWKoszyku = suma > 0;

        checkBoxImbir.setDisable(!cosWKoszyku);
        checkBoxSosSojowy.setDisable(!cosWKoszyku);
        checkBoxWasabi.setDisable(!cosWKoszyku);

        if (!cosWKoszyku) {
            checkBoxImbir.setSelected(false);
            checkBoxSosSojowy.setSelected(false);
            checkBoxWasabi.setSelected(false);
        }

        int dodatki = 0;
        if (checkBoxImbir.isSelected()) dodatki += DODATEK;
        if (checkBoxSosSojowy.isSelected()) dodatki += DODATEK;
        if (checkBoxWasabi.isSelected()) dodatki += DODATEK;

        int dostawa = cosWKoszyku ? DOSTAWA : 0;

        sumaJedzenia = suma + dodatki;      // <- zapis do pola
        razemDoZaplaty = sumaJedzenia + dostawa;  // <- zapis do pola

        if (jedzenieTextCena != null) {
         jedzenieTextCena.setText(sumaJedzenia + " zł");
        }
        if (cenaKoncowaSushi != null) {
            cenaKoncowaSushi.setText(razemDoZaplaty + " zł");
        }
        

    }
    
    
    
}
