package com.example.main;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SushiController {

    @FXML private Button powrotPrzycisk;

    @FXML private CheckBox checkBoxImbir;
    @FXML private CheckBox checkBoxSosSojowy;
    @FXML private CheckBox checkBoxWasabi;

    @FXML private Text iloscCalifornia, iloscFutomaki, iloscNigiri, iloscPhiladelphia, iloscTemaki;
    @FXML private Button usunCalifornia, usunFutomaki, usunNigiri, usunPhiladelphia, usunTemaki;

    @FXML private Text cenaKoncowaSushi, jedzenieTextCena, zamowienieMinimumKwotaText;

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
    private void initialize() { odswiezWidok(); }

    // ====== PLUSY ======
    @FXML void plusFutomaki(ActionEvent e){ futomaki++; odswiezWidok(); }
    @FXML void plusNigiri(ActionEvent e){ nigiri++; odswiezWidok(); }
    @FXML void plusCalifornia(ActionEvent e){ california++; odswiezWidok(); }
    @FXML void plusPhiladelphia(ActionEvent e){ philadelphia++; odswiezWidok(); }
    @FXML void plusTemaki(ActionEvent e){ temaki++; odswiezWidok(); }

    // ====== MINUSY ======
    @FXML void usunFutomaki(){ if(futomaki>0) futomaki--; odswiezWidok(); }
    @FXML void usunNigiri(){ if(nigiri>0) nigiri--; odswiezWidok(); }
    @FXML void usunCalifornia(){ if(california>0) california--; odswiezWidok(); }
    @FXML void usunPhiladelphia(){ if(philadelphia>0) philadelphia--; odswiezWidok(); }
    @FXML void usunTemaki(){ if(temaki>0) temaki--; odswiezWidok(); }

    // ====== DODATKI ======
    @FXML void dodatkiZmiana(ActionEvent e){ odswiezWidok(); }

    // ====== WIDOK ======
    private void odswiezWidok(){
        iloscFutomaki.setText(String.valueOf(futomaki));
        iloscNigiri.setText(String.valueOf(nigiri));
        iloscCalifornia.setText(String.valueOf(california));
        iloscPhiladelphia.setText(String.valueOf(philadelphia));
        iloscTemaki.setText(String.valueOf(temaki));

        usunFutomaki.setVisible(futomaki>0);
        usunNigiri.setVisible(nigiri>0);
        usunCalifornia.setVisible(california>0);
        usunPhiladelphia.setVisible(philadelphia>0);
        usunTemaki.setVisible(temaki>0);

        int suma = futomaki*CENA_FUTOMAKI + nigiri*CENA_NIGIRI + california*CENA_CALIFORNIA +
                philadelphia*CENA_PHILADELPHIA + temaki*CENA_TEMAKI;

        boolean cosWKoszyku = suma>0;

        checkBoxImbir.setDisable(!cosWKoszyku);
        checkBoxSosSojowy.setDisable(!cosWKoszyku);
        checkBoxWasabi.setDisable(!cosWKoszyku);
        if(!cosWKoszyku){ checkBoxImbir.setSelected(false); checkBoxSosSojowy.setSelected(false); checkBoxWasabi.setSelected(false); }

        int dodatki = 0;
        if(checkBoxImbir.isSelected()) dodatki+=DODATEK;
        if(checkBoxSosSojowy.isSelected()) dodatki+=DODATEK;
        if(checkBoxWasabi.isSelected()) dodatki+=DODATEK;

        int dostawa = cosWKoszyku ? DOSTAWA : 0;

        sumaJedzenia = suma + dodatki;
        razemDoZaplaty = sumaJedzenia + dostawa;

        jedzenieTextCena.setText(sumaJedzenia + " zł");
        cenaKoncowaSushi.setText(razemDoZaplaty + " zł");
    }

    // ====== RACHUNEK ======
    private String zbudujRachunek(LocalDateTime data){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("========== RACHUNEK - SUSHI MINI ==========\n");
        sb.append("Data zamówienia: ").append(data.format(fmt)).append("\n");
        sb.append("------------------------------------------\n");

        appendPozycja(sb,"Futomaki",futomaki,CENA_FUTOMAKI);
        appendPozycja(sb,"Nigiri",nigiri,CENA_NIGIRI);
        appendPozycja(sb,"California",california,CENA_CALIFORNIA);
        appendPozycja(sb,"Philadelphia",philadelphia,CENA_PHILADELPHIA);
        appendPozycja(sb,"Temaki",temaki,CENA_TEMAKI);

        sb.append("------------------------------------------\n");

        if(checkBoxImbir.isSelected()) sb.append("Imbir: +").append(DODATEK).append(" zł\n");
        if(checkBoxSosSojowy.isSelected()) sb.append("Sos sojowy: +").append(DODATEK).append(" zł\n");
        if(checkBoxWasabi.isSelected()) sb.append("Wasabi: +").append(DODATEK).append(" zł\n");

        sb.append("------------------------------------------\n");
        sb.append("Jedzenie: ").append(sumaJedzenia).append(" zł\n");
        sb.append("Dostawa:  ").append((razemDoZaplaty>0?DOSTAWA:0)).append(" zł\n");
        sb.append("RAZEM:    ").append(razemDoZaplaty).append(" zł\n");
        sb.append("==========================================\n");

        return sb.toString();
    }

    private void appendPozycja(StringBuilder sb, String nazwa, int ilosc, int cenaJedn){
        if(ilosc<=0) return;
        sb.append(nazwa).append(" x").append(ilosc).append(" (").append(cenaJedn).append(" zł) = ")
          .append(ilosc*cenaJedn).append(" zł\n");
    }

    // ====== ZAMÓW ======
    @FXML void zamowienieJedzenia(ActionEvent event){
        if(razemDoZaplaty<MIN_ZAMOWIENIA){
            pokazKomunikatMin("Minimalna wartość zamówienia: "+MIN_ZAMOWIENIA+" zł");
            return;
        }

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/main/Checkout.fxml"));
            Parent root = loader.load();

            CheckoutController checkoutController = loader.getController();
            checkoutController.ustawZawartoscKoszyka(zbudujRachunek(LocalDateTime.now()));

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            wyczyscKoszyk();

        }catch(IOException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Nie udało się przejść do Checkout");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void pokazKomunikatMin(String tekst){
        zamowienieMinimumKwotaText.setText(tekst);
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e->zamowienieMinimumKwotaText.setText(""));
        pause.play();
    }

    private void wyczyscKoszyk(){
        futomaki=nigiri=california=philadelphia=temaki=0;
        checkBoxImbir.setSelected(false);
        checkBoxSosSojowy.setSelected(false);
        checkBoxWasabi.setSelected(false);
        zamowienieMinimumKwotaText.setText("");
        odswiezWidok();
    }

    // ====== POWRÓT ======
    @FXML void powrotDoWyboruRestauracji(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/main/RestauracjeController.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch(IOException e){ e.printStackTrace(); }
    }
}