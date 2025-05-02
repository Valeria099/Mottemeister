package oopsprojekt.projekt_mottemeister;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class MottemeisterFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mõttemeister – Mängu seadistused");

        // Paigutus
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(15);
        grid.setVgap(15);

        // Veerulaiused
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(150);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(200);
        col2.setHgrow(Priority.SOMETIMES);
        //col2.setHgrow(Priority.ALWAYS);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPrefWidth(50);

        grid.getColumnConstraints().addAll(col1, col2, col3);

        // 1. Nimi
        Label nimiLabel = new Label("Mängija nimi:");
        TextField nimiInput = new TextField();
        nimiInput.setMaxWidth(150);
        Button nimiHelp = looAbiNupp("Sisesta oma eesnimi või hüüdnimi.");

        grid.add(nimiLabel, 0, 0);
        grid.add(nimiInput, 1, 0);
        grid.add(nimiHelp, 2, 0);

        // 2. Värvide arv
        Label varvideArvLabel = new Label("Värvide arv:");
        Spinner<Integer> varvideSpinner = new Spinner<>(3, 10, 4);
        Button varvideHelp = looAbiNupp("Vali, mitme erineva värviga mäng toimub (3 kuni 10).");

        grid.add(varvideArvLabel, 0, 1);
        grid.add(varvideSpinner, 1, 1);
        grid.add(varvideHelp, 2, 1);

        // 3. Kordused
        Label kordusLabel = new Label("Kas värvid võivad korduda?");
        RadioButton kordusJah = new RadioButton("Jah");
        RadioButton kordusEi = new RadioButton("Ei");
        ToggleGroup kordusGroup = new ToggleGroup();
        kordusJah.setToggleGroup(kordusGroup);
        kordusEi.setToggleGroup(kordusGroup);
        kordusJah.setSelected(true);
        HBox kordusValikud = new HBox(10, kordusJah, kordusEi);
        kordusValikud.setAlignment(Pos.CENTER_LEFT);
        Button kordusHelp = looAbiNupp("Kas sama värv võib esineda koodis mitu korda?");

        grid.add(kordusLabel, 0, 2);
        grid.add(kordusValikud, 1, 2);
        grid.add(kordusHelp, 2, 2);

        // 4. Tagasiside tüüp
        Label tagasisideLabel = new Label("Tagasiside tüüp:");
        ComboBox<String> tagasisideValik = new ComboBox<>();
        tagasisideValik.setMaxWidth(200);
        tagasisideValik.getItems().addAll("Klassikaline", "Järjekorras");
        tagasisideValik.setValue("Klassikaline");
        Button tagasisideHelp = looAbiNupp("Vali 'Klassikaline', kui soovid näha tagasisidet täpselt iga nupu kohta; 'Järjekorras' annab üldisema järjestuse alusel.");

        grid.add(tagasisideLabel, 0, 3);
        grid.add(tagasisideValik, 1, 3);
        grid.add(tagasisideHelp, 2, 3);

        Region vahe = new Region();
        vahe.setMinHeight(20);
        grid.add(vahe, 0, 4);

        // 5. Nupud
        Button kinnitaBtn = new Button("Kinnita valikud");
        Button katkestaBtn = new Button("Katkesta");
        HBox nupudBox = new HBox(10, kinnitaBtn, katkestaBtn);
        nupudBox.setAlignment(Pos.CENTER_RIGHT);

        grid.add(nupudBox, 1, 5, 2, 1); // katab 2 veergu, jätab 1. veeru (Label) tühjaks

        // Nupuvajutused
        kinnitaBtn.setOnAction(e -> {
            String nimi = nimiInput.getText().trim();
            int varvideArv = varvideSpinner.getValue();
            boolean kordusedLubatud = kordusJah.isSelected();
            String tagasisideTüüp = tagasisideValik.getValue();

            if (nimi.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Palun sisesta oma nimi.");
                alert.showAndWait();
                return;
            }

            Logija.logi("Mängib mängija: " + nimi);

            String kordusInfo = kordusedLubatud ? "kordustega" : "ilma kordusteta";
            Logija.logi("Mängija " + nimi + " mängib " + varvideArv + " värviga, " + kordusInfo + ", millele tagasiside antakse " + tagasisideTüüp + ".");// Vali juhuslikult värvid

            List<Color> koikVarvid = new ArrayList<>(List.of(
                    Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                    Color.ORANGE, Color.PURPLE, Color.PINK, Color.CYAN, Color.WHITE, Color.AQUAMARINE, Color.MAGENTA,Color.BLACK
            ));
            Collections.shuffle(koikVarvid);
            List<Color> valitudVarvid = koikVarvid.subList(0, varvideArv);

            Logija.logi("Värvid valitakse salakoodi jaoks järgmisest hulgast: "+valitudVarvid);

            // Genereeri värvide nimed (kasutame Color.toString() või muu loogika)
            List<String> varviNimed = new ArrayList<>();
            for (Color v : valitudVarvid) {
                varviNimed.add(v.toString());
            }

            // Loo seadistusobjekt
            ManguSeadistus seadistus = new ManguSeadistus(nimi, varvideArv, kordusedLubatud, tagasisideTüüp, valitudVarvid);

            // Genereeri salakood mängu jaoks
            Salakood salakood = genereeriSalakood(varvideArv, varviNimed, kordusedLubatud);

            // Ava mängulaua vaade
            Stage manguStage = new Stage();
            MangulaudVaade mangulaud = new MangulaudVaade(seadistus, salakood);
            mangulaud.kuvaMangulaud(manguStage);

            // Sulge seadistuse aken
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        });



        katkestaBtn.setOnAction(e -> primaryStage.close());

        Scene scene = new Scene(grid, 500, 300);
        grid.setStyle("-fx-background-color: #ffffe0;");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Abi tooltip nupuga – töötab hiire allhoidmisel
    private Button looAbiNupp(String tekst) {
        Button abiNupp = new Button("?");
        Tooltip tooltip = new Tooltip(tekst);
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(300);

        abiNupp.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            Tooltip.install(abiNupp, tooltip);
            tooltip.show(abiNupp, e.getScreenX(), e.getScreenY() + 10);
        });

        abiNupp.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> tooltip.hide());
        abiNupp.addEventHandler(MouseEvent.MOUSE_EXITED, e -> tooltip.hide());

        return abiNupp;
    }

    private Salakood genereeriSalakood(int nupuArv, List<String> valikuVärvid, boolean korduvad) {
        List<String> salakood = new ArrayList<>();
        List<String> värvivalikKopeeri = new ArrayList<>(valikuVärvid);

        for (int i = 0; i < nupuArv; i++) {
            String juhuslikVärv;
            if (korduvad) {
                juhuslikVärv = värvivalikKopeeri.get((int) (Math.random() * värvivalikKopeeri.size()));
            } else {
                juhuslikVärv = värvivalikKopeeri.remove((int) (Math.random() * värvivalikKopeeri.size()));
            }
            salakood.add(juhuslikVärv);
        }

        return new Salakood(salakood);
    }

    public static void käivitaUuesti() {
        Stage uusStage = new Stage();
        try {
            new MottemeisterFX().start(uusStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
