package com.example.main;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.Duration;

public class TopBarController {

    @FXML private javafx.scene.control.Label kontoLabel;
    @FXML private javafx.scene.control.Label koszykLabel;

    private Popup kontoPopup;
    private Popup koszykPopup;
    private Popup loginPopup;
    private Popup registerPopup;

    private final UserRepository userRepo = new UserRepository();

    @FXML
    public void initialize() {
        loginPopup = buildLoginPopup();
        registerPopup = buildRegisterPopup();
        kontoPopup = buildKontoPopup();
        koszykPopup = buildKoszykPopup();

        
        updateKontoText();

        
        installHoverPopup(kontoLabel, kontoPopup);
        installHoverPopup(koszykLabel, koszykPopup);
    }

    private void updateKontoText() {
        if (Session.isLoggedIn()) {
            kontoLabel.setText(Session.getLoggedUser());
        } else {
            kontoLabel.setText("KONTO");
        }
    }

    // ---------------- POPUPY ----------------

    private Popup buildKoszykPopup() {
        Label title = new Label("Koszyk");
        title.setStyle("-fx-font-weight: bold;");

        Button goToCart = new Button("Przejdź do koszyka");
        goToCart.setOnAction(e -> {
            System.out.println("Klik: Przejdź do koszyka");
            hideAllPopups();
        });

        VBox box = new VBox(8, title, goToCart);
        stylePopupBox(box);

        Popup popup = new Popup();
        popup.getContent().add(box);
        return popup;
    }

    private Popup buildKontoPopup() {
        Label title = new Label("Konto");
        title.setStyle("-fx-font-weight: bold;");

        Button login = new Button("Zaloguj się");
        Button register = new Button("Rejestracja");
        Button orders = new Button("Moje zamówienia");
        Button logout = new Button("Wyloguj się");

        login.setOnAction(e -> {
            hideAllPopups();
            showPopupUnderNode(kontoLabel, loginPopup);
        });

        register.setOnAction(e -> {
            hideAllPopups();
            showPopupUnderNode(kontoLabel, registerPopup);
        });

        orders.setOnAction(e -> {
            hideAllPopups();
            if (!Session.isLoggedIn()) {
                showPopupUnderNode(kontoLabel, loginPopup);
                return;
            }
            System.out.println("Moje zamówienia użytkownika: " + Session.getLoggedUser());
        });

        logout.setOnAction(e -> {
            Session.logout();
            updateKontoText();
            hideAllPopups();
            refreshKontoMenu();
            System.out.println("Wylogowano");
        });

        VBox box = new VBox(8);
        box.getChildren().add(title);

        if (!Session.isLoggedIn()) {
            box.getChildren().addAll(login, register);
        } else {
            box.getChildren().addAll(orders, logout);
        }

        stylePopupBox(box);

        Popup popup = new Popup();
        popup.getContent().add(box);
        return popup;
    }

    private Popup buildLoginPopup() {
        Label title = new Label("Logowanie");
        title.setStyle("-fx-font-weight: bold;");

        TextField loginField = new TextField();
        loginField.setPromptText("Login");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Hasło");

        Label info = new Label();
        info.setStyle("-fx-text-fill: red;");

        Button btnLogin = new Button("Zaloguj");
        Button btnCancel = new Button("Anuluj");

        btnLogin.setOnAction(e -> {
            String l = loginField.getText().trim();
            String p = passField.getText();

            if (l.isEmpty() || p.isEmpty()) {
                info.setText("Uzupełnij login i hasło");
                return;
            }

            try {
                if (userRepo.loginValid(l, p)) {
                    Session.login(l);
                    updateKontoText();
                    info.setText("");
                    loginPopup.hide();
                    refreshKontoMenu();
                } else {
                    info.setText("Błędny login lub hasło");
                }
            } catch (Exception ex) {
                info.setText("Błąd bazy: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnCancel.setOnAction(e -> loginPopup.hide());

        VBox box = new VBox(8, title, loginField, passField, info, new VBox(6, btnLogin, btnCancel));
        stylePopupBox(box);

        Popup popup = new Popup();
        popup.setAutoHide(true);
        popup.getContent().add(box);
        return popup;
    }

    private Popup buildRegisterPopup() {
        Label title = new Label("Rejestracja");
        title.setStyle("-fx-font-weight: bold;");

        TextField loginField = new TextField();
        loginField.setPromptText("Nowy login");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Hasło");

        PasswordField pass2Field = new PasswordField();
        pass2Field.setPromptText("Powtórz hasło");

        Label info = new Label();
        info.setStyle("-fx-text-fill: red;");

        Button btnCreate = new Button("Utwórz konto");
        Button btnCancel = new Button("Anuluj");

        btnCreate.setOnAction(e -> {
            String l = loginField.getText().trim();
            String p1 = passField.getText();
            String p2 = pass2Field.getText();

            if (l.isEmpty() || p1.isEmpty() || p2.isEmpty()) {
                info.setText("Uzupełnij wszystkie pola");
                return;
            }
            if (!p1.equals(p2)) {
                info.setText("Hasła nie są takie same");
                return;
            }

            try {
                if (userRepo.userExists(l)) {
                    info.setText("Taki login już istnieje");
                    return;
                }

                userRepo.register(l, p1);
                info.setStyle("-fx-text-fill: green;");
                info.setText("Konto utworzone! Możesz się zalogować.");
            } catch (Exception ex) {
                info.setText("Błąd bazy: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnCancel.setOnAction(e -> registerPopup.hide());

        VBox box = new VBox(8, title, loginField, passField, pass2Field, info, new VBox(6, btnCreate, btnCancel));
        stylePopupBox(box);

        Popup popup = new Popup();
        popup.setAutoHide(true);
        popup.getContent().add(box);
        return popup;
    }

    // --------------- NARZEDZIA ----------------

    private void refreshKontoMenu() {
        if (kontoPopup != null) kontoPopup.hide();
        kontoPopup = buildKontoPopup();
        installHoverPopup(kontoLabel, kontoPopup);
    }

    private void stylePopupBox(VBox box) {
        box.setStyle(
                "-fx-background-color: white;" +
                "-fx-padding: 12;" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-border-color: rgba(0,0,0,0.15);" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0.3, 0, 3);"
        );
    }

    private void showPopupUnderNode(Node anchor, Popup popup) {
        if (popup.isShowing()) return;
        Bounds b = anchor.localToScreen(anchor.getBoundsInLocal());
        popup.show(anchor.getScene().getWindow(), b.getMinX(), b.getMaxY() + 6);
    }

    private void installHoverPopup(Node anchor, Popup popup) {
        PauseTransition hideDelay = new PauseTransition(Duration.millis(200));

        anchor.setOnMouseEntered(e -> {
            hideDelay.stop();
            hideAuthPopups();

            if (popup.isShowing()) return;

            Bounds b = anchor.localToScreen(anchor.getBoundsInLocal());
            popup.show(anchor.getScene().getWindow(), b.getMinX(), b.getMaxY() + 6);
        });

        anchor.setOnMouseExited(e -> {
            hideDelay.setOnFinished(ev -> popup.hide());
            hideDelay.playFromStart();
        });

        Node content = popup.getContent().get(0);

        content.setOnMouseEntered(e -> hideDelay.stop());
        content.setOnMouseExited(e -> {
            hideDelay.setOnFinished(ev -> popup.hide());
            hideDelay.playFromStart();
        });
    }

    private void hideAllPopups() {
        if (kontoPopup != null) kontoPopup.hide();
        if (koszykPopup != null) koszykPopup.hide();
        if (loginPopup != null) loginPopup.hide();
        if (registerPopup != null) registerPopup.hide();
    }

    private void hideAuthPopups() {
        if (loginPopup != null) loginPopup.hide();
        if (registerPopup != null) registerPopup.hide();
    }
}
