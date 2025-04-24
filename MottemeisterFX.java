package oopsprojekt.demo;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class MottemeisterFX extends Application {
    private int nupuArv; // Mitu nuppu
    private boolean korduvad; // Kas värvide kordumine
    private boolean positsioonilineTagasiside; // Tagasiside küsimine
    private TextField nimiField; // Kasutaja nime sisend
    private List<List<Circle>> gameCircles; // Mänguvälja ringide hoidmiseks
    private int currentRow; // Jälgida, millised read on täidetud
    private ToggleGroup korduvadGroup; // Kordumise valik
    private ToggleGroup tagasisideGroup; // Tagasiside valik
    private RadioButton jahButton; // "Jah" nupp
    private RadioButton eiButton; // "Ei" nupp
    private RadioButton posYesButton; // Tagasiside "Jah" nupp
    private RadioButton posNoButton; // Tagasiside "Ei" nupp
    private Tooltip tooltip; // Hoidke töötlus privaatsena
    private HBox feedbackRow; // Tagasiside row
    private Pane gameBoard; // Mänguväli
    private List<String> salakood; // Salakood

            public static void main(String[] args) {
                launch(args);
            }

            @Override
            public void start(Stage primaryStage) {
                primaryStage.setTitle("Mõttemeister");

                // Peamine paigutus
                VBox root = new VBox(10);
                root.setPadding(new Insets(10));
                root.setAlignment(Pos.CENTER); // Keskendab VBox elemendid

                // Kasutaja nime sisend
                nimiField = new TextField();
                nimiField.setPromptText("Sisesta oma nimi");
                Button nameHelpBtn = createHelpButton("Sisesta oma nimi");

                // Nime sisestamise paneel
                HBox nimiPaneel = new HBox(10, new Label("Sisesta oma nimi: "), nimiField, nameHelpBtn);
                nimiPaneel.setAlignment(Pos.CENTER); // Keskendab ka HBox elemendid

                // Küsi, mitu värvi mängu soovite (3-10)
                Label nupuArvLabel = new Label("Mitu värvi soovite mängus (3-10)?");
                TextField nupuArvField = new TextField();

                // Nupuväli
                HBox nupuArvPaneel = new HBox(10, nupuArvLabel, nupuArvField);
                nupuArvPaneel.setAlignment(Pos.CENTER); // Keskendab HBox elemendid

                // Küsi, kas värv võib korduda
                Label korduvadLabel = new Label("Kas värv võib korduda?");
                korduvadGroup = new ToggleGroup(); // Määramine
                jahButton = new RadioButton("Jah"); // Jah nupp
                eiButton = new RadioButton("Ei"); // Ei nupp
                jahButton.setToggleGroup(korduvadGroup);
                eiButton.setToggleGroup(korduvadGroup);
                Tooltip.install(jahButton, new Tooltip("Valides 'Jah', võivad värvid korduda, seega võib ühte värvi olla mitu korda."));

                HBox korduvadPaneel = new HBox(10, korduvadLabel, jahButton, eiButton);
                korduvadPaneel.setAlignment(Pos.CENTER); // Keskendab HBox elemendid

                // Küsi tagasiside vorm
                Label posTagasisideLabel = new Label("Kas tagasiside on positsiooniline?");
                tagasisideGroup = new ToggleGroup(); // Määramine
                posYesButton = new RadioButton("Jah"); // Tagasiside "Jah" nupp
                posNoButton = new RadioButton("Ei"); // Tagasiside "Ei" nupp
                posYesButton.setToggleGroup(tagasisideGroup);
                posNoButton.setToggleGroup(tagasisideGroup);

                Tooltip.install(posYesButton, new Tooltip("Valides 'Jah', antakse tagasiside igas nupu kohta eraldi tema positsioonis."));
                Tooltip.install(posNoButton, new Tooltip("Valides 'Ei', antakse tagasiside tulemuste põhjal."));

                HBox tagasisidePaneel = new HBox(10, posTagasisideLabel, posYesButton, posNoButton);
                tagasisidePaneel.setAlignment(Pos.CENTER); // Keskendab HBox elemendid

                // Alustame mängu nupp
                Button startBtn = new Button("Alusta mängu");
                startBtn.setOnAction(e -> handleStartButton(nupuArvField));

                // Koondame kõik paneelid
                root.getChildren().addAll(nimiPaneel, nupuArvPaneel, korduvadPaneel, tagasisidePaneel, startBtn);

                Scene scene = new Scene(root, 400, 250); // Aknas kõrgus
                primaryStage.setScene(scene);
                primaryStage.show();
            }

            private void handleStartButton(TextField nupuArvField) {
                // Käitleb mängu alustamise loogikat
                if (!nupuArvField.getText().isEmpty()) {
                    try {
                        nupuArv = Integer.parseInt(nupuArvField.getText());
                        if (nupuArv < 3 || nupuArv > 10) {
                            showAlert("Viga", "Palun sisesta kehtiv arv (3-10).");
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        showAlert("Viga", "Palun sisesta kehtiv arv (3-10).");
                        return;
                    }
                }

                // Tuvastame valikud
                if (korduvadGroup.getSelectedToggle() != null) {
                    korduvad = jahButton.isSelected();
                }
                if (tagasisideGroup.getSelectedToggle() != null) {
                    positsioonilineTagasiside = posYesButton.isSelected();
                }

                // Alustame mängu; edastame primaryStage
                alustaMangu();
            }

            private Button createHelpButton(String helpText) {
                Button helpBtn = new Button("?");
                helpBtn.setOnMouseEntered(e -> showTooltip(helpText, helpBtn));
                helpBtn.setOnMouseExited(e -> hideTooltip());
                return helpBtn;
            }

            private void showTooltip(String text, Node sourceNode) {
                Tooltip tooltip = new Tooltip(text);
                tooltip.show(sourceNode.getScene().getWindow(),
                        sourceNode.getLayoutX() + sourceNode.getScene().getX() + 20,
                        sourceNode.getLayoutY() + sourceNode.getScene().getY() - 20);
            }

            private void hideTooltip() {
                if (tooltip != null) {
                    tooltip.hide();
                }
            }

            private void showAlert(String title, String content) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
                alert.setTitle(title);
                alert.showAndWait();
            }

            private void alustaMangu() {
                System.out.println("Mängu algatus: Nimi: " + nimiField.getText() +
                        ", Värvide arv: " + nupuArv +
                        ", Korduvus: " + korduvad +
                        ", Positsiooniline tagasiside: " + positsioonilineTagasiside);

                // Suletakse esialgne andmete sisestamise aken
                //primaryStage.close(); // Sulge algne aknas

                // Loo uus akna suund, et kuvada mänguväljak
                Stage gameStage = new Stage();
                gameStage.setTitle("Mänguväli");

                // Peamine paigutus
                BorderPane root = new BorderPane();

                // Ülemine osa: Salakood (varjatud)
                HBox topPanel = new HBox();
                topPanel.setAlignment(Pos.CENTER);
                Label salakoodLabel = new Label("Salakood: ");
                salakoodLabel.setStyle("-fx-font-weight: bold;");
                // Alguses varjame salakoodi
                for (String color : genereeriSalakood(4, false)) {
                    Label label = new Label("●"); // Kausme ringi
                    label.setTextFill(Color.GRAY);
                    topPanel.getChildren().add(label);
                }

                root.setTop(topPanel);

                // Keskmine osa: Mänguväli
                gameBoard = new VBox(); // Loome keskel mänguvälja

                // Nuppude augud (suured ringid)
                HBox buttonsHoles = new HBox(10);
                gameCircles = new ArrayList<>();

                for (int j = 0; j < nupuArv; j++) {
                    Circle circle = new Circle(20); // Suured ringid
                    circle.setFill(Color.GRAY); // Algne värvus
                    buttonsHoles.getChildren().add(circle);
                    gameCircles.add(new ArrayList<Circle>() {{ add(circle); }}); // Salvestame ringid
                }

                // Tagasiside ringid (väikesed ringid)
                feedbackRow = new HBox(10);
                for (int j = 0; j < nupuArv; j++) {
                    Circle feedbackCircle = new Circle(10); // Väikesed ringid
                    feedbackCircle.setFill(Color.GRAY); // Algne värvus
                    feedbackRow.getChildren().add(feedbackCircle);
                }

                gameBoard.getChildren().addAll(buttonsHoles, feedbackRow);
                root.setCenter(gameBoard); // Lisage mänguväli keskele

                // Alumine osa: Nupud
                HBox colorButtons = new HBox(10);
                // Genereerime salakoodi ja loome värvinupud
                salakood = genereeriSalakood(nupuArv, korduvad);
                for (String color : salakood) {
                    Button colorButton = new Button(color);
                    colorButton.setStyle("-fx-background-color: " + getColorString(color) + ";"); // Värvi määramine
                    colorButton.setOnAction(e -> handleColorSelection(color)); // Käitlemise loogika
                    colorButtons.getChildren().add(colorButton);
                }

                root.setBottom(colorButtons); // Lisame nupud alumisele reale

                Scene gameScene = new Scene(root, 600, 400); // Uus stseen
                gameStage.setScene(gameScene);
                gameStage.show();

                currentRow = 0; // Algse rida näitama
            }

            private void handleColorSelection(String color) {
                if (currentRow < gameCircles.size()) {
                    List<Circle> currentCircles = gameCircles.get(currentRow);
                    for (Circle circle : currentCircles) {
                        if (circle.getFill() == Color.GRAY) { // Kui ring on hall, siis värvime
                            circle.setFill(Color.web(getColorString(color))); // Värvime valitud värviga
                            break; // Värvime ainult esmase avatud ringi
                        }
                    }
                    // Kontrollime, kas rida on täis ja andke tagasiside, kui nii on
                    checkRowCompletion();
                }
            }

            private void checkRowCompletion() {
                // Kontrollige, kas rida on täis, ning andke tagasiside, kui nii on
                List<Circle> currentCircles = gameCircles.get(currentRow);
                boolean isRowFull = true;
                for (Circle circle : currentCircles) {
                    if (circle.getFill() == Color.GRAY) {
                        isRowFull = false;
                        break;
                    }
                }

                if (isRowFull) {
                    // Andke tagasiside: proovige lisada tagasiside meie tagasiside ritta
                    int[] tagasiside = kontrolliKoodi(currentCircles); // Tehke tagasiside kontroll
                    updateFeedbackRow(tagasiside);
                    currentRow++; // Liigume järgmise rea juurde
                }
            }

            private int[] kontrolliKoodi(List<Circle> currentCircles) {
                // Siin peate täitma tagasiside loogika
                // Näiteks: naaske tagasiside [õige, vale, tühi] näitena
                int[] tagasiside = new int[3]; // 0 = õige, 1 = vale, 2 = tühi
                // Logika salakoodiga võrreldes
                // Tagasta tagasiside - teie enda implementeeritav
                return tagasiside;
            }

            // Funktsioon tagasiside värvimiseks
            private void updateFeedbackRow(int[] feedback) {
                for (int i = 0; i < feedback.length; i++) {
                    Circle feedbackCircle = (Circle) feedbackRow.getChildren().get(i);
                    if (feedback[i] > 0) {
                        if (feedback[i] == 1) {
                            feedbackCircle.setFill(Color.WHITE); // "õige" tagasiside
                        } else if (feedback[i] == 2) {
                            feedbackCircle.setFill(Color.BLACK); // "vale" tagasiside
                        } else {
                            feedbackCircle.setFill(Color.GRAY); // "tühi"
                        }
                    }
                }
            }

            private List<String> genereeriSalakood(int nupuArv, boolean korduvad) {
                List<String> kõikVärvid = new ArrayList<>(List.of("valge", "kollane", "oranž", "beež", "punane", "sinine", "hall", "roheline", "must", "lilla"));
                List<String> salakood = new ArrayList<>();

                for (int i = 0; i < nupuArv; i++) {
                    String juhuslikVärv;
                    if (korduvad) {
                        juhuslikVärv = kõikVärvid.get((int) (Math.random() * kõikVärvid.size()));
                    } else {
                        juhuslikVärv = kõikVärvid.remove((int) (Math.random() * kõikVärvid.size())); // Eemaldab ja valib
                    }
                    salakood.add(juhuslikVärv);
                }
                return salakood;
            }

            private String getColorString(String color) {
                switch (color) {
                    case "valge":
                        return "white";
                    case "kollane":
                        return "yellow";
                    case "oranž":
                        return "orange";
                    case "beež":
                        return "##F5F5DC"; // Beež
                    case "punane":
                        return "red";
                    case "sinine":
                        return "blue";
                    case "hall":
                        return "gray";
                    case "roheline":
                        return "green";
                    case "must":
                        return "black";
                    case "lilla":
                        return "purple";
                    default:
                        return "gray"; // Vaikimisi värv
                }
            }
        }

        
