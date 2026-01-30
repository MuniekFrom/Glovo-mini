package com.example.main;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class KebabController {

    @FXML private Text jedzenieTextCena, cenaKoncowaPizza, zamowienieMinimumKwotaText;
    @FXML private CheckBox sosLagodny, sosMieszany, sosOstry;

    @FXML private ComboBox<String> rozmiarRollo, rozmiarPita, rozmiarBox, rozmiarWrap;
    @FXML private ComboBox<String> miesoRollo, miesoPita, miesoBox, miesoWrap;

    @FXML private ListView<CartItem> cartList;

    private final ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

    private static final int CENA_ROLLO = 32;
    private static final int CENA_PITA  = 34;
    private static final int CENA_BOX   = 35;
    private static final int CENA_WRAP  = 33;

    private static final int DOPLATA_SREDNI = 8;
    private static final int DOPLATA_DUZY   = 16;

    private static final int SOS_LAGODNY = 4;
    private static final int SOS_MIESZANY = 5;
    private static final int SOS_OSTRY = 6;

    private static final int DOSTAWA = 10;
    private static final int MIN_ZAMOWIENIA = 30;

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

        cartList.setItems(cartItems);

      
        cartList.setOnMouseClicked(ev -> {
            if (ev.getClickCount() == 2) {
                CartItem selected = cartList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    cartItems.remove(selected);
                    odswiezWidokKoszyka();
                }
            }
        });

        odswiezWidokKoszyka();
    }

    private void initRozmiar(ComboBox<String> cb) {
        cb.getItems().setAll("Mały", "Średni", "Duży");
        cb.setValue("Średni");
    }

    private void initMieso(ComboBox<String> cb) {
        cb.getItems().setAll("Baranina", "Kurczak", "Mieszane");
        cb.setValue("Baranina");
    }

    private int doplataRozmiar(String size) {
        if (size == null) return 0;
        return switch (size) {
            case "Mały" -> 0;
            case "Średni" -> DOPLATA_SREDNI;
            case "Duży" -> DOPLATA_DUZY;
            default -> 0;
        };
    }

    private int doplataMieso(String meat) {
        if (meat == null) return 0;
        return switch (meat) {
            case "Mieszane" -> 3;
            default -> 0;
        };
    }

    @FXML
    private void addRollo(ActionEvent e) {
        addItem(CartItem.Type.ROLLO, rozmiarRollo.getValue(), miesoRollo.getValue(), CENA_ROLLO);
    }

    @FXML
    private void addPita(ActionEvent e) {
        addItem(CartItem.Type.PITA, rozmiarPita.getValue(), miesoPita.getValue(), CENA_PITA);
    }

    @FXML
    private void addBox(ActionEvent e) {
        addItem(CartItem.Type.BOX, rozmiarBox.getValue(), miesoBox.getValue(), CENA_BOX);
    }

    @FXML
    private void addWrap(ActionEvent e) {
        addItem(CartItem.Type.WRAP, rozmiarWrap.getValue(), miesoWrap.getValue(), CENA_WRAP);
    }

    private void addItem(CartItem.Type type, String size, String meat, int basePrice) {
        int price = basePrice + doplataRozmiar(size) + doplataMieso(meat);
        cartItems.add(new CartItem(type, size, meat, price));
        odswiezWidokKoszyka();
    }

    @FXML
    private void removeSelected(ActionEvent e) {
        CartItem selected = cartList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cartItems.remove(selected);
            odswiezWidokKoszyka();
        }
    }

    @FXML
    private void dodatkiZmiana(ActionEvent e) {
        odswiezWidokKoszyka();
    }

    private void odswiezWidokKoszyka() {
        int sumaJedzenia = cartItems.stream().mapToInt(CartItem::getPrice).sum();
        boolean cosWKoszyku = !cartItems.isEmpty();

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

        int jedzeniePlusDodatki = sumaJedzenia + dodatki;
        int razem = jedzeniePlusDodatki + dostawa;

        jedzenieTextCena.setText(jedzeniePlusDodatki + " zł");
        cenaKoncowaPizza.setText(razem + " zł");
    }

    private int getRazemDoZaplaty() {
        int sumaJedzenia = cartItems.stream().mapToInt(CartItem::getPrice).sum();

        int dodatki = 0;
        if (!cartItems.isEmpty()) {
            if (sosLagodny.isSelected()) dodatki += SOS_LAGODNY;
            if (sosMieszany.isSelected()) dodatki += SOS_MIESZANY;
            if (sosOstry.isSelected()) dodatki += SOS_OSTRY;
        }

        int dostawa = cartItems.isEmpty() ? 0 : DOSTAWA;
        return sumaJedzenia + dodatki + dostawa;
    }

    private String zbudujRachunek(LocalDateTime data) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        sb.append("========== RACHUNEK - KEBAB MINI ==========\n");
        sb.append("Data zamówienia: ").append(data.format(fmt)).append("\n");
        sb.append("------------------------------------------\n");

        for (CartItem item : cartItems) {
            sb.append(item.getType())
              .append(" (").append(item.getSize()).append(", ").append(item.getMeat()).append(")")
              .append(" - ").append(item.getPrice()).append(" zł\n");
        }
        
        if (!cartItems.isEmpty() && (sosLagodny.isSelected() || sosMieszany.isSelected() || sosOstry.isSelected())) {
        sb.append("------------------------------------------\n");
        sb.append("Dodatki:\n");

        if (sosLagodny.isSelected())  sb.append("Sos Łagodny - ").append(SOS_LAGODNY).append(" zł\n");
        if (sosMieszany.isSelected()) sb.append("Sos Mieszany - ").append(SOS_MIESZANY).append(" zł\n");
        if (sosOstry.isSelected())    sb.append("Sos Ostry - ").append(SOS_OSTRY).append(" zł\n");
    }

        sb.append("------------------------------------------\n");

        int sumaJedzenia = cartItems.stream().mapToInt(CartItem::getPrice).sum();

        int dodatki = 0;
        if (sosLagodny.isSelected()) dodatki += SOS_LAGODNY;
        if (sosMieszany.isSelected()) dodatki += SOS_MIESZANY;
        if (sosOstry.isSelected()) dodatki += SOS_OSTRY;

        int jedzeniePlusDodatki = sumaJedzenia + dodatki;
        int dostawa = cartItems.isEmpty() ? 0 : DOSTAWA;
        int razem = jedzeniePlusDodatki + dostawa;

        sb.append("Jedzenie: ").append(jedzeniePlusDodatki).append(" zł\n");
        sb.append("Dostawa:  ").append(dostawa).append(" zł\n");
        sb.append("RAZEM:    ").append(razem).append(" zł\n");
        sb.append("==========================================\n");

        return sb.toString();
    }

    @FXML
    private void zamowienieJedzenia(ActionEvent event) {
        int razem = getRazemDoZaplaty();

        if (razem < MIN_ZAMOWIENIA) {
            zamowienieMinimumKwotaText.setText("Minimalna wartość zamówienia: 30 zł");
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> zamowienieMinimumKwotaText.setText(""));
            pause.play();
            return;
        }

        if (cartItems.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Koszyk jest pusty.");
            a.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/main/Checkout.fxml"));
            Parent root = loader.load();

            CheckoutController checkoutController = loader.getController();
            checkoutController.ustawZawartoscKoszyka(zbudujRachunek(LocalDateTime.now()));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            cartItems.clear();
            sosLagodny.setSelected(false);
            sosMieszany.setSelected(false);
            sosOstry.setSelected(false);
            odswiezWidokKoszyka();

        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nie udało się przejść do Checkout:\n" + ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void powrotDoWyboruRestauracji(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/main/RestauracjeController.fxml"));
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
}