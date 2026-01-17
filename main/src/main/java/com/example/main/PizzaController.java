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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PizzaController {

    // ====== UI ======
    @FXML private Text cenaKoncowaPizza;
    @FXML private Text jedzenieTextCena;
    @FXML private Text zamowienieMinimumKwotaText;

    // ilości (S/M/L) dla każdej pizzy
    @FXML private Text iloscMarS, iloscMarM, iloscMarL;
    @FXML private Text iloscPepS, iloscPepM, iloscPepL;
    @FXML private Text iloscCapS, iloscCapM, iloscCapL;
    @FXML private Text iloscHawS, iloscHawM, iloscHawL;

    // przyciski usuń (minus)
    @FXML private Button usunMarS, usunMarM, usunMarL;
    @FXML private Button usunPepS, usunPepM, usunPepL;
    @FXML private Button usunCapS, usunCapM, usunCapL;
    @FXML private Button usunHawS, usunHawM, usunHawL;

    @FXML private Button powrotPrzycisk;

    // dodatki
    @FXML private CheckBox sosCzosnkowy;
    @FXML private CheckBox sosBBQ;
    @FXML private CheckBox sosPomidorowy;

    // ====== LOGIKA (stany) ======
    private int marS=0, marM=0, marL=0;
    private int pepS=0, pepM=0, pepL=0;
    private int capS=0, capM=0, capL=0;
    private int hawS=0, hawM=0, hawL=0;

    // ====== CENY ======
    private static final int MAR_S = 42, MAR_M = 52, MAR_L = 62;
    private static final int PEP_S = 44, PEP_M = 54, PEP_L = 64;
    private static final int CAP_S = 45, CAP_M = 55, CAP_L = 65;
    private static final int HAW_S = 43, HAW_M = 53, HAW_L = 63;

    private static final int SOS_CZOSNKOWY = 3;
    private static final int SOS_BBQ = 4;
    private static final int SOS_POMIDOROWY = 2;

    private static final int DOSTAWA = 10;
    private static final int MIN_ZAMOWIENIA = 30;

    private int sumaJedzenia = 0;     // jedzenie + sosy
    private int razemDoZaplaty = 0;   // sumaJedzenia + dostawa

    @FXML
    private void initialize() {
        odswiezWidok();
    }

    // ====== PLUSY ======
    @FXML void plusMarS(ActionEvent event) { marS++; odswiezWidok(); }
    @FXML void plusMarM(ActionEvent event) { marM++; odswiezWidok(); }
    @FXML void plusMarL(ActionEvent event) { marL++; odswiezWidok(); }

    @FXML void plusPepS(ActionEvent event) { pepS++; odswiezWidok(); }
    @FXML void plusPepM(ActionEvent event) { pepM++; odswiezWidok(); }
    @FXML void plusPepL(ActionEvent event) { pepL++; odswiezWidok(); }

    @FXML void plusCapS(ActionEvent event) { capS++; odswiezWidok(); }
    @FXML void plusCapM(ActionEvent event) { capM++; odswiezWidok(); }
    @FXML void plusCapL(ActionEvent event) { capL++; odswiezWidok(); }

    @FXML void plusHawS(ActionEvent event) { hawS++; odswiezWidok(); }
    @FXML void plusHawM(ActionEvent event) { hawM++; odswiezWidok(); }
    @FXML void plusHawL(ActionEvent event) { hawL++; odswiezWidok(); }

    // ====== MINUSY ======
    @FXML void usunMarS(ActionEvent event) { if (marS>0) marS--; odswiezWidok(); }
    @FXML void usunMarM(ActionEvent event) { if (marM>0) marM--; odswiezWidok(); }
    @FXML void usunMarL(ActionEvent event) { if (marL>0) marL--; odswiezWidok(); }

    @FXML void usunPepS(ActionEvent event) { if (pepS>0) pepS--; odswiezWidok(); }
    @FXML void usunPepM(ActionEvent event) { if (pepM>0) pepM--; odswiezWidok(); }
    @FXML void usunPepL(ActionEvent event) { if (pepL>0) pepL--; odswiezWidok(); }

    @FXML void usunCapS(ActionEvent event) { if (capS>0) capS--; odswiezWidok(); }
    @FXML void usunCapM(ActionEvent event) { if (capM>0) capM--; odswiezWidok(); }
    @FXML void usunCapL(ActionEvent event) { if (capL>0) capL--; odswiezWidok(); }

    @FXML void usunHawS(ActionEvent event) { if (hawS>0) hawS--; odswiezWidok(); }
    @FXML void usunHawM(ActionEvent event) { if (hawM>0) hawM--; odswiezWidok(); }
    @FXML void usunHawL(ActionEvent event) { if (hawL>0) hawL--; odswiezWidok(); }

    // ====== DODATKI ======
    @FXML void dodatkiZmiana(ActionEvent event) { odswiezWidok(); }

    // ====== WIDOK ======
    private void odswiezWidok() {
        iloscMarS.setText(String.valueOf(marS)); iloscMarM.setText(String.valueOf(marM)); iloscMarL.setText(String.valueOf(marL));
        iloscPepS.setText(String.valueOf(pepS)); iloscPepM.setText(String.valueOf(pepM)); iloscPepL.setText(String.valueOf(pepL));
        iloscCapS.setText(String.valueOf(capS)); iloscCapM.setText(String.valueOf(capM)); iloscCapL.setText(String.valueOf(capL));
        iloscHawS.setText(String.valueOf(hawS)); iloscHawM.setText(String.valueOf(hawM)); iloscHawL.setText(String.valueOf(hawL));

        usunMarS.setVisible(marS>0); usunMarM.setVisible(marM>0); usunMarL.setVisible(marL>0);
        usunPepS.setVisible(pepS>0); usunPepM.setVisible(pepM>0); usunPepL.setVisible(pepL>0);
        usunCapS.setVisible(capS>0); usunCapM.setVisible(capM>0); usunCapL.setVisible(capL>0);
        usunHawS.setVisible(hawS>0); usunHawM.setVisible(hawM>0); usunHawL.setVisible(hawL>0);

        int sumaPizz =
                marS*MAR_S + marM*MAR_M + marL*MAR_L +
                pepS*PEP_S + pepM*PEP_M + pepL*PEP_L +
                capS*CAP_S + capM*CAP_M + capL*CAP_L +
                hawS*HAW_S + hawM*HAW_M + hawL*HAW_L;

        boolean cosWKoszyku = sumaPizz>0;

        sosCzosnkowy.setDisable(!cosWKoszyku);
        sosBBQ.setDisable(!cosWKoszyku);
        sosPomidorowy.setDisable(!cosWKoszyku);
        if(!cosWKoszyku){ sosCzosnkowy.setSelected(false); sosBBQ.setSelected(false); sosPomidorowy.setSelected(false); }

        int dodatki = 0;
        if(sosCzosnkowy.isSelected()) dodatki+=SOS_CZOSNKOWY;
        if(sosBBQ.isSelected()) dodatki+=SOS_BBQ;
        if(sosPomidorowy.isSelected()) dodatki+=SOS_POMIDOROWY;

        int dostawa = cosWKoszyku ? DOSTAWA : 0;

        sumaJedzenia = sumaPizz + dodatki;
        razemDoZaplaty = sumaJedzenia + dostawa;

        jedzenieTextCena.setText(sumaJedzenia + " zł");
        cenaKoncowaPizza.setText(razemDoZaplaty + " zł");
    }

    // ====== RACHUNEK ======
    private String zbudujRachunek(LocalDateTime data){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("========== RACHUNEK - PIZZA MINI ==========\n");
        sb.append("Data zamówienia: ").append(data.format(fmt)).append("\n");
        sb.append("------------------------------------------\n");

        appendPozycja(sb,"Margherita","S",marS,MAR_S);
        appendPozycja(sb,"Margherita","M",marM,MAR_M);
        appendPozycja(sb,"Margherita","L",marL,MAR_L);

        appendPozycja(sb,"Pepperoni","S",pepS,PEP_S);
        appendPozycja(sb,"Pepperoni","M",pepM,PEP_M);
        appendPozycja(sb,"Pepperoni","L",pepL,PEP_L);

        appendPozycja(sb,"Capricciosa","S",capS,CAP_S);
        appendPozycja(sb,"Capricciosa","M",capM,CAP_M);
        appendPozycja(sb,"Capricciosa","L",capL,CAP_L);

        appendPozycja(sb,"Hawajska","S",hawS,HAW_S);
        appendPozycja(sb,"Hawajska","M",hawM,HAW_M);
        appendPozycja(sb,"Hawajska","L",hawL,HAW_L);

        sb.append("------------------------------------------\n");
        if(sosCzosnkowy.isSelected()) sb.append("Sos czosnkowy: +").append(SOS_CZOSNKOWY).append(" zł\n");
        if(sosBBQ.isSelected()) sb.append("Sos BBQ:       +").append(SOS_BBQ).append(" zł\n");
        if(sosPomidorowy.isSelected()) sb.append("Sos pomidorowy:+").append(SOS_POMIDOROWY).append(" zł\n");

        sb.append("------------------------------------------\n");
        sb.append("Jedzenie: ").append(sumaJedzenia).append(" zł\n");
        sb.append("Dostawa:  ").append((razemDoZaplaty>0 ? DOSTAWA:0)).append(" zł\n");
        sb.append("RAZEM:    ").append(razemDoZaplaty).append(" zł\n");
        sb.append("==========================================\n");

        return sb.toString();
    }

    private void appendPozycja(StringBuilder sb, String nazwa, String rozmiar, int ilosc, int cenaJedn){
        if(ilosc<=0) return;
        int wartosc = ilosc*cenaJedn;
        sb.append(nazwa).append(" ").append(rozmiar)
          .append(" x").append(ilosc)
          .append(" (").append(cenaJedn).append(" zł) = ")
          .append(wartosc).append(" zł\n");
    }

    // ====== ZAMÓW ======
    @FXML void zamowienieJedzenia(ActionEvent event){
        if(razemDoZaplaty<MIN_ZAMOWIENIA){
            pokazKomunikatMin("Minimalna wartość zamówienia: " + MIN_ZAMOWIENIA + " zł");
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
        marS=marM=marL=0;
        pepS=pepM=pepL=0;
        capS=capM=capL=0;
        hawS=hawM=hawL=0;

        sosCzosnkowy.setSelected(false);
        sosBBQ.setSelected(false);
        sosPomidorowy.setSelected(false);

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