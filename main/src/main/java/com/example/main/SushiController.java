package com.example.main;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class SushiController {
    
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
    
    
     private int futomaki = 0, nigiri = 0, california = 0, philadelphia = 0, temaki = 0;

    private static final int CENA_FUTOMAKI = 28;
    private static final int CENA_NIGIRI = 14;
    private static final int CENA_CALIFORNIA = 32;
    private static final int CENA_PHILADELPHIA = 34;
    private static final int CENA_TEMAKI = 25;

    private static final int DODATEK = 3;
    private static final int DOSTAWA = 10;

    @FXML
    private void initialize() {
        odswiezWidok();
    }

    @FXML
    private void plusFutomaki(ActionEvent e) { futomaki++; odswiezWidok(); }

    @FXML
    private void plusNigiri(ActionEvent e) { nigiri++; odswiezWidok(); }

    @FXML
    private void plusCalifornia(ActionEvent e) { california++; odswiezWidok(); }

    @FXML
    private void plusPhiladelphia(ActionEvent e) { philadelphia++; odswiezWidok(); }

    @FXML
    private void plusTemaki(ActionEvent e) { temaki++; odswiezWidok(); }

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

        int dodatki = 0;
        if (checkBoxImbir.isSelected()) dodatki += DODATEK;
        if (checkBoxSosSojowy.isSelected()) dodatki += DODATEK;
        if (checkBoxWasabi.isSelected()) dodatki += DODATEK;

        int razem = suma + dodatki + DOSTAWA;

        if (cenaKoncowaSushi != null) {
            cenaKoncowaSushi.setText(razem + " zł");
        }
    }
    
}
