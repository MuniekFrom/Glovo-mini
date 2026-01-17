package com.example.main;

import java.io.IOException;
import java.time.LocalDateTime;
import javafx.fxml.FXML;
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

public class AptekaController {

    @FXML private Text iloscAspiryna, iloscParacetamol, iloscIbuprofen, iloscWitaminaC, iloscGripex, iloscPlastry, iloscWodaUtleniona;
    @FXML private Text cenaKoncowaApteka, produktyTextCena, zamowienieMinimumKwotaText;

    @FXML private Button usunAspiryna, usunParacetamol, usunIbuprofen, usunWitaminaC, usunGripex, usunPlastry, usunWodaUtleniona;
    @FXML private Button powrotPrzycisk;

    private int aspiryna = 0, paracetamol = 0, ibuprofen = 0, witaminaC = 0, gripex = 0, plastry = 0, wodaUtleniona = 0;

    private static final int CENA_ASPIRYNA = 12;
    private static final int CENA_PARACETAMOL = 10;
    private static final int CENA_IBUPROFEN = 15;
    private static final int CENA_WITAMINAC = 8;
    private static final int CENA_GRIPEX = 20;
    private static final int CENA_PLASTRY = 5;
    private static final int CENA_WODA_UTLENIONA = 7;

    private static final int DOSTAWA = 10;

    private int sumaProduktow = 0;
    private int razemDoZaplaty = 0;

    @FXML private void initialize() {
        odswiezWidok();
    }

    // ====== PLUSY ======
    @FXML private void plusAspiryna(ActionEvent e) { aspiryna++; odswiezWidok(); }
    @FXML private void plusParacetamol(ActionEvent e) { paracetamol++; odswiezWidok(); }
    @FXML private void plusIbuprofen(ActionEvent e) { ibuprofen++; odswiezWidok(); }
    @FXML private void plusWitaminaC(ActionEvent e) { witaminaC++; odswiezWidok(); }
    @FXML private void plusGripex(ActionEvent e) { gripex++; odswiezWidok(); }
    @FXML private void plusPlastry(ActionEvent e) { plastry++; odswiezWidok(); }
    @FXML private void plusWodaUtleniona(ActionEvent e) { wodaUtleniona++; odswiezWidok(); }

    // ====== MINUSY ======
    @FXML private void usunAspiryna(ActionEvent e) { if(aspiryna>0) aspiryna--; odswiezWidok(); }
    @FXML private void usunParacetamol(ActionEvent e) { if(paracetamol>0) paracetamol--; odswiezWidok(); }
    @FXML private void usunIbuprofen(ActionEvent e) { if(ibuprofen>0) ibuprofen--; odswiezWidok(); }
    @FXML private void usunWitaminaC(ActionEvent e) { if(witaminaC>0) witaminaC--; odswiezWidok(); }
    @FXML private void usunGripex(ActionEvent e) { if(gripex>0) gripex--; odswiezWidok(); }
    @FXML private void usunPlastry(ActionEvent e) { if(plastry>0) plastry--; odswiezWidok(); }
    @FXML private void usunWodaUtleniona(ActionEvent e) { if(wodaUtleniona>0) wodaUtleniona--; odswiezWidok(); }

    // ====== POWRÓT ======
    @FXML public void powrotDoWyboruSklepow(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/main/Sklepy.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    // ====== RACHUNEK ======
    public String zbudujRachunek(LocalDateTime data) {
        int suma = aspiryna*CENA_ASPIRYNA + paracetamol*CENA_PARACETAMOL + ibuprofen*CENA_IBUPROFEN
                 + witaminaC*CENA_WITAMINAC + gripex*CENA_GRIPEX + plastry*CENA_PLASTRY + wodaUtleniona*CENA_WODA_UTLENIONA;
        int dostawa = (suma>0)? DOSTAWA : 0;
        int razem = suma + dostawa;

        StringBuilder sb = new StringBuilder();
        sb.append("===== RACHUNEK APTEKA =====\n");
        sb.append("Data zamówienia: ").append(data).append("\n");
        sb.append("-----------------------------\n");
        if(aspiryna>0) sb.append(String.format("Aspiryna x%d = %d\n", aspiryna, aspiryna*CENA_ASPIRYNA));
        if(paracetamol>0) sb.append(String.format("Paracetamol x%d = %d\n", paracetamol, paracetamol*CENA_PARACETAMOL));
        if(ibuprofen>0) sb.append(String.format("Ibuprofen x%d = %d\n", ibuprofen, ibuprofen*CENA_IBUPROFEN));
        if(witaminaC>0) sb.append(String.format("Witamina C x%d = %d\n", witaminaC, witaminaC*CENA_WITAMINAC));
        if(gripex>0) sb.append(String.format("Gripex x%d = %d\n", gripex, gripex*CENA_GRIPEX));
        if(plastry>0) sb.append(String.format("Plastry x%d = %d\n", plastry, plastry*CENA_PLASTRY));
        if(wodaUtleniona>0) sb.append(String.format("Woda utleniona x%d = %d\n", wodaUtleniona, wodaUtleniona*CENA_WODA_UTLENIONA));
        sb.append("-----------------------------\n");
        sb.append("Produkty: ").append(suma).append(" zł\n");
        sb.append("Dostawa: ").append(dostawa).append(" zł\n");
        sb.append("RAZEM: ").append(razem).append(" zł\n");
        return sb.toString();
    }

    // ====== ZAMÓW (przejście do Koszyka) ======
    @FXML private void zamowienie(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/main/Checkout.fxml"));
            Parent root = loader.load();
            CheckoutController kc = loader.getController();
            kc.ustawZawartoscKoszyka(zbudujRachunek(LocalDateTime.now()));

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            wyczyscKoszyk();

        } catch(IOException e) { e.printStackTrace(); }
    }

    private void wyczyscKoszyk() {
        aspiryna = paracetamol = ibuprofen = witaminaC = gripex = plastry = wodaUtleniona = 0;
        zamowienieMinimumKwotaText.setText("");
        odswiezWidok();
    }

    private void odswiezWidok() {
        iloscAspiryna.setText(String.valueOf(aspiryna));
        iloscParacetamol.setText(String.valueOf(paracetamol));
        iloscIbuprofen.setText(String.valueOf(ibuprofen));
        iloscWitaminaC.setText(String.valueOf(witaminaC));
        iloscGripex.setText(String.valueOf(gripex));
        iloscPlastry.setText(String.valueOf(plastry));
        iloscWodaUtleniona.setText(String.valueOf(wodaUtleniona));

        usunAspiryna.setVisible(aspiryna>0);
        usunParacetamol.setVisible(paracetamol>0);
        usunIbuprofen.setVisible(ibuprofen>0);
        usunWitaminaC.setVisible(witaminaC>0);
        usunGripex.setVisible(gripex>0);
        usunPlastry.setVisible(plastry>0);
        usunWodaUtleniona.setVisible(wodaUtleniona>0);

        int suma = aspiryna*CENA_ASPIRYNA + paracetamol*CENA_PARACETAMOL + ibuprofen*CENA_IBUPROFEN
                + witaminaC*CENA_WITAMINAC + gripex*CENA_GRIPEX + plastry*CENA_PLASTRY + wodaUtleniona*CENA_WODA_UTLENIONA;

        sumaProduktow = suma;
        boolean cosWKoszyku = suma>0;
        razemDoZaplaty = suma + (cosWKoszyku? DOSTAWA:0);

        produktyTextCena.setText(sumaProduktow + " zł");
        cenaKoncowaApteka.setText(razemDoZaplaty + " zł");
    }
}