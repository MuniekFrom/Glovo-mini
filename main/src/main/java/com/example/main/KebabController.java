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
import javafx.scene.control.ComboBox;

public class KebabController {

    @FXML private Text iloscRollo, iloscPita, iloscBox, iloscWrap;
    @FXML private Text cenaKoncowaPizza, jedzenieTextCena, zamowienieMinimumKwotaText;

    @FXML private CheckBox sosLagodny, sosMieszany, sosOstry;

    @FXML private Button usunRollo, usunPita, usunBox, usunWrap;
    @FXML private Button powrotPrzycisk;

    @FXML private ComboBox<String> rozmiarRollo, rozmiarPita, rozmiarBox, rozmiarWrap;
    @FXML private ComboBox<String> miesoRollo, miesoPita, miesoBox, miesoWrap;

    private int rollo = 0, pita = 0, box = 0, wrap = 0;

    private static final int CENA_ROLLO = 32;
    private static final int CENA_PITA = 34;
    private static final int CENA_BOX = 35;
    private static final int CENA_WRAP = 33;

    private static final int DOPLATA_SREDNI = 8;
    private static final int DOPLATA_DUZY = 16;

    private static final int SOS_LAGODNY = 4;
    private static final int SOS_MIESZANY = 5;
    private static final int SOS_OSTRY = 6;

    private static final int DOSTAWA = 10;
    private static final int MIN_ZAMOWIENIA = 30;

    private int sumaJedzenia = 0;
    private int razemDoZaplaty = 0;

    @FXML
    private void initialize() {
        initRozmiar(rozmiarRollo);
        initRozmiar(rozmiarPita);
        initRozmiar(rozmiarBox);
        initRozmiar(rozmiarWrap);

        initMieso(miesoRollo);
        initMieso(miesoPita);
        initMieso(miesoBox);
        initMieso(miesoWrap);

        odswiezWidok();
    }

    private void initRozmiar(ComboBox<String> cb) {
        cb.getItems().addAll("Mały", "Średni", "Duży");
        cb.setValue("Średni");
        cb.setOnAction(e -> odswiezWidok());
    }

    private void initMieso(ComboBox<String> cb) {
        cb.getItems().addAll("Baranina", "Kurczak", "Mieszane");
        cb.setValue("Baranina");
        cb.setOnAction(e -> odswiezWidok());
    }

    private int doplata(ComboBox<String> cb) {
        if (cb.getValue() == null) return 0;
        return switch (cb.getValue()) {
            case "Mały" -> 0;
            case "Średni" -> DOPLATA_SREDNI;
            case "Duży" -> DOPLATA_DUZY;
            default -> 0;
        };
    }

    private int doplataMieso(ComboBox<String> cb, int cenaPodstawowa) {
        if (cb.getValue() == null) return 0;
        return switch (cb.getValue()) {
            case "Baranina" -> 0;
            case "Kurczak" -> 0;
            case "Mieszane" -> 3;
            default -> 0;
        };
    }

    // ➕
    @FXML private void plusRollo(ActionEvent e){ rollo++; odswiezWidok(); }
    @FXML private void plusPita(ActionEvent e){ pita++; odswiezWidok(); }
    @FXML private void plusBox(ActionEvent e){ box++; odswiezWidok(); }
    @FXML private void plusWrap(ActionEvent e){ wrap++; odswiezWidok(); }

    // ➖
    @FXML private void usunRollo(ActionEvent e){ if(rollo > 0) rollo--; odswiezWidok(); }
    @FXML private void usunPita(ActionEvent e){ if(pita > 0) pita--; odswiezWidok(); }
    @FXML private void usunBox(ActionEvent e){ if(box > 0) box--; odswiezWidok(); }
    @FXML private void usunWrap(ActionEvent e){ if(wrap > 0) wrap--; odswiezWidok(); }

    @FXML
    private void dodatkiZmiana(ActionEvent e) {
        odswiezWidok();
    }

    private void odswiezWidok() {
        iloscRollo.setText(String.valueOf(rollo));
        iloscPita.setText(String.valueOf(pita));
        iloscBox.setText(String.valueOf(box));
        iloscWrap.setText(String.valueOf(wrap));

        usunRollo.setVisible(rollo > 0);
        usunPita.setVisible(pita > 0);
        usunBox.setVisible(box > 0);
        usunWrap.setVisible(wrap > 0);

        int suma =
            rollo * (CENA_ROLLO + doplata(rozmiarRollo) + doplataMieso(miesoRollo, CENA_ROLLO)) +
            pita * (CENA_PITA + doplata(rozmiarPita) + doplataMieso(miesoPita, CENA_PITA)) +
            box * (CENA_BOX + doplata(rozmiarBox) + doplataMieso(miesoBox, CENA_BOX)) +
            wrap * (CENA_WRAP + doplata(rozmiarWrap) + doplataMieso(miesoWrap, CENA_WRAP));

        boolean cosWKoszyku = suma > 0;

        // Sosy aktywne tylko jeśli coś w koszyku
        sosLagodny.setDisable(!cosWKoszyku);
        sosMieszany.setDisable(!cosWKoszyku);
        sosOstry.setDisable(!cosWKoszyku);

        if (!cosWKoszyku) {
            sosLagodny.setSelected(false);
            sosMieszany.setSelected(false);
            sosOstry.setSelected(false);
        }

        int dodatki = 0;
        if (sosLagodny.isSelected()) dodatki += SOS_LAGODNY;
        if (sosMieszany.isSelected()) dodatki += SOS_MIESZANY;
        if (sosOstry.isSelected()) dodatki += SOS_OSTRY;

        int dostawa = cosWKoszyku ? DOSTAWA : 0;

        sumaJedzenia = suma + dodatki;
        razemDoZaplaty = sumaJedzenia + dostawa;

        jedzenieTextCena.setText(sumaJedzenia + " zł");
        cenaKoncowaPizza.setText(razemDoZaplaty + " zł");
    }

    private String zbudujRachunek(LocalDateTime data) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        sb.append("========== RACHUNEK - KEBAB MINI ==========\n");
        sb.append("Data zamówienia: ").append(data.format(fmt)).append("\n");
        sb.append("------------------------------------------\n");

        if (rollo > 0)
            sb.append("Rollo x").append(rollo)
              .append(" (").append(rozmiarRollo.getValue()).append(", ").append(miesoRollo.getValue()).append(")\n");

        if (pita > 0)
            sb.append("Pita x").append(pita)
              .append(" (").append(rozmiarPita.getValue()).append(", ").append(miesoPita.getValue()).append(")\n");

        if (box > 0)
            sb.append("Box x").append(box)
              .append(" (").append(rozmiarBox.getValue()).append(", ").append(miesoBox.getValue()).append(")\n");

        if (wrap > 0)
            sb.append("Wrap x").append(wrap)
              .append(" (").append(rozmiarWrap.getValue()).append(", ").append(miesoWrap.getValue()).append(")\n");

        sb.append("------------------------------------------\n");
        sb.append("Jedzenie: ").append(sumaJedzenia).append(" zł\n");
        sb.append("Dostawa:  ").append(DOSTAWA).append(" zł\n");
        sb.append("RAZEM:    ").append(razemDoZaplaty).append(" zł\n");
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

    try {
        // Tworzymy loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/main/Checkout.fxml"));
        Parent root = loader.load();

        // Pobieramy kontroler Checkout
        CheckoutController checkoutController = loader.getController();

        // Tworzymy rachunek i ustawiamy w Checkout
        checkoutController.ustawZawartoscKoszyka(zbudujRachunek(LocalDateTime.now()));

        // Ustawiamy scenę
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        // Opcjonalnie czyścimy koszyk
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
        rollo = pita = box = wrap = 0;
        sosLagodny.setSelected(false);
        sosMieszany.setSelected(false);
        sosOstry.setSelected(false);
        zamowienieMinimumKwotaText.setText("");
        odswiezWidok();
    }

    @FXML
    public void powrotDoWyboruRestauracji(ActionEvent event) throws IOException {
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
}