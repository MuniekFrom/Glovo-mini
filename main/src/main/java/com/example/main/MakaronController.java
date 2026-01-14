package com.example.main;

import java.io.IOException;
import java.time.LocalDateTime;
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

    // ====== PLUSY ======
    @FXML private void plusSpaghetti(ActionEvent e) { spaghetti++; odswiezWidok(); }
    @FXML private void plusCarbonara(ActionEvent e) { carbonara++; odswiezWidok(); }
    @FXML private void plusPenne(ActionEvent e) { penne++; odswiezWidok(); }
    @FXML private void plusPennePesto(ActionEvent e) { pennePesto++; odswiezWidok(); }

    // ====== MINUSY ======
    @FXML private void usunSpaghetti(ActionEvent e) { if (spaghetti > 0) spaghetti--; odswiezWidok(); }
    @FXML private void usunCarbonara(ActionEvent e) { if (carbonara > 0) carbonara--; odswiezWidok(); }
    @FXML private void usunPenne(ActionEvent e) { if (penne > 0) penne--; odswiezWidok(); }
    @FXML private void usunPennePesto(ActionEvent e) { if (pennePesto > 0) pennePesto--; odswiezWidok(); }

    @FXML private void dodatkiZmiana(ActionEvent e) { odswiezWidok(); }

    // ====== POWRÓT ======
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

    // ====== RACHUNEK ======
    private String zbudujRachunek(LocalDateTime data) {
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
        sb.append("Data zamówienia: ").append(data).append("\n");
        sb.append("------------------------------------------\n");

        if (spaghetti > 0) sb.append("Spaghetti x").append(spaghetti).append("\n");
        if (carbonara > 0) sb.append("Carbonara x").append(carbonara).append("\n");
        if (penne > 0) sb.append("Penne x").append(penne).append("\n");
        if (pennePesto > 0) sb.append("Penne Pesto x").append(pennePesto).append("\n");

        if (dodatki > 0) {
            sb.append("Dodatki:\n").append(dodatkiLista);
        }

        sb.append("------------------------------------------\n");
        sb.append("Jedzenie: ").append(jedzenie).append(" zł\n");
        sb.append("Dostawa: ").append(dostawa).append(" zł\n");
        sb.append("RAZEM: ").append(razem).append(" zł\n");
        sb.append("==========================================\n");

        return sb.toString();
    }

    // ====== ZAMÓW (przejście do Checkout) ======
    @FXML
    private void zamowienieJedzenia(ActionEvent event) {
        if (razemDoZaplaty < MIN_ZAMOWIENIA) {
            zamowienieMinimumKwotaText.setText("Minimalna wartość zamówienia: 30 zł");
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> zamowienieMinimumKwotaText.setText(""));
            pause.play();
            return;
        }

        try {
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

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Nie udało się przejść do Checkout");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
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