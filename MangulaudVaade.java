package oopsprojekt.projekt_mottemeister;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Tooltip;

import java.util.ArrayList;
import java.util.List;

public class MangulaudVaade {

    private final String mangijaNimi;
    private final int varvideArv;
    private final boolean kordusedLubatud;
    private final String tagasisideTyyp;
    private final List<Color> saadavalVarvid;
    private VBox riduKonteiner;
    private int aktiivneRidaIndeks = 0;
    private Circle[][] suuredRingid;
    private List<Color> hetkeValikud;
    private Button kinnitaNupp;
    private Salakood salakood;
    private boolean positsioonilineTagasiside;
    private ScrollPane scrollPane;

    // Ühtne vasak vaheline ruum (kasutame igal pool)
    private final Region vasakVahe = looVasakVahe();

    public MangulaudVaade(String mangijaNimi, int varvideArv, boolean kordusedLubatud,
                          String tagasisideTyyp, List<Color> saadavalVarvid, Salakood salakood) {
        this.mangijaNimi = mangijaNimi;
        this.varvideArv = varvideArv;
        this.kordusedLubatud = kordusedLubatud;
        this.tagasisideTyyp = tagasisideTyyp;
        this.saadavalVarvid = saadavalVarvid;
        this.salakood=salakood;
        hetkeValikud = new java.util.ArrayList<>();
    }

    public MangulaudVaade(ManguSeadistus seadistus, Salakood salakood) {
        this.mangijaNimi = seadistus.getNimi();
        this.varvideArv = seadistus.getVarvideArv();
        this.kordusedLubatud = seadistus.isKordusedLubatud();
        this.tagasisideTyyp = seadistus.getTagasisideTüüp();
        this.saadavalVarvid = seadistus.getVarvid();
        this.salakood = salakood;
        this.positsioonilineTagasiside = seadistus.tagasisideTüüp.equalsIgnoreCase("klassikaline");
        hetkeValikud = new java.util.ArrayList<>();
    }

    private Region looVasakVahe() {
        Region r = new Region();
        int baasLaius = 150;
        int maxNupud = 3;

        // iga lisanduva nupu kohta vähenda laiust
        int uusLaius = baasLaius - (Math.max(0, varvideArv - maxNupud) * 20);

        // Ära lase väiksemaks kui 50 (visuaalse ühtsuse pärast)
        uusLaius = Math.max(50, uusLaius);

        r.setPrefWidth(uusLaius);
        return r;
    }

    private void looTuhjadManguread(int ridadeArv, int varvideArv) {
        suuredRingid = new Circle[ridadeArv][varvideArv];

        for (int i = 0; i < ridadeArv; i++) {
            HBox rida = new HBox(10);
            rida.setAlignment(Pos.CENTER_LEFT);

            // Vasak joondus
            rida.getChildren().add(looVasakVahe());

            for (int j = 0; j < varvideArv; j++) {
                Circle suurRing = looSuurRing(Color.LIGHTGRAY);
                suurRing.setStroke(Color.DARKGRAY);
                suuredRingid[i][j] = suurRing;
                rida.getChildren().add(suurRing);
            }

            Region keskmineVahe = new Region();
            keskmineVahe.setPrefWidth(20);
            rida.getChildren().add(keskmineVahe);

            for (int j = 0; j < varvideArv; j++) {
                Circle vaikseRing = new Circle(8, Color.TRANSPARENT);
                vaikseRing.setStroke(Color.GRAY);
                rida.getChildren().add(vaikseRing);
            }

            riduKonteiner.getChildren().add(0, rida);
        }
    }

    public void kuvaLoppAnimatsioon(boolean voit) {
        // Näita salakoodi
        HBox salakoodiBox = new HBox(10);
        salakoodiBox.setAlignment(Pos.CENTER);
        List<Color> salakoodVarvid = teisendaStringidVarvideks(salakood.getSalakood());

        for (Color värv : salakoodVarvid) {
            Circle ring = new Circle(15, värv);
            ring.setStroke(Color.BLACK);
            salakoodiBox.getChildren().add(ring);
        }

        // Eristame sõnumi ja stiili vastavalt võidule
        Label silt = new Label();
        if (voit) {
            Logija.logi("Mängija "+mangijaNimi+" lahendas salakoodi.");
            silt.setText("Tubli, " + mangijaNimi + "! Lahendasid mõistatuse!");
            silt.setStyle("-fx-font-size: 18px; -fx-text-fill: darkgreen;");
        } else {
            Logija.logi("Mängijal "+mangijaNimi+" jäi salakood lahendamata.");
            silt.setText("Kahjuks seekord, " + mangijaNimi + ", jäi salakood lahendamata!");
            silt.setStyle("-fx-font-size: 18px; -fx-text-fill: darkred;");
        }

        VBox vBox = new VBox(20, silt, salakoodiBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));

        // Animatsioon – võib eristuda näiteks kiiruse või efektide poolest
        FadeTransition ft = new FadeTransition(Duration.seconds(2), vBox);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        // Lisame teise animatsiooni ainult võidu korral
        if (voit) {
            ScaleTransition scale = new ScaleTransition(Duration.seconds(1), silt);
            scale.setFromX(0.8);
            scale.setToX(1.2);
            scale.setCycleCount(4);
            scale.setAutoReverse(true);
            scale.play();
        }

        // Nupud
        Button uuesti = new Button("Mängi uuesti");
        Button lopeta = new Button("Välju");

        uuesti.setOnAction(e -> {
            MottemeisterFX.käivitaUuesti();
            ((Stage) vBox.getScene().getWindow()).close();
        });

        lopeta.setOnAction(e -> {
            // Näita teadet logifaili kohta enne väljumist
            Alert logiTeade = new Alert(Alert.AlertType.INFORMATION);
            logiTeade.setTitle("Logi salvestatud");
            logiTeade.setHeaderText("Mängu info on logitud.");
            logiTeade.setContentText("Logifail asub: \n" + Logija.saaLogiFail().getAbsolutePath());
            logiTeade.showAndWait();

            Platform.exit();
        });

        HBox nupud = new HBox(10, uuesti, lopeta);
        nupud.setAlignment(Pos.CENTER);

        VBox koguVaade = new VBox(20, vBox, nupud);
        koguVaade.setAlignment(Pos.CENTER);
        koguVaade.setPadding(new Insets(30));

        // Taustavärvi eristamine (näiteks võidu korral hele roheline, kaotuse korral helehall)
        koguVaade.setStyle(voit ? "-fx-background-color: #e8fce8;" : "-fx-background-color: #f0f0f0;");

        Scene loppStseen = new Scene(koguVaade, 600, 400);
        Stage loppStage = new Stage();
        loppStage.setTitle("Mäng lõppenud");
        loppStage.setScene(loppStseen);
        loppStage.show();
    }


    public void kuvaMangulaud(Stage stage) {
        BorderPane root = new BorderPane();

        // Top – peidetud salakood
        VBox topBox = looPeidetudSalakood();
        root.setTop(topBox);


        // Center – mänguridade konteiner
        scrollPane = new ScrollPane(riduKonteiner);
        riduKonteiner = new VBox(10);
        riduKonteiner.setPadding(new Insets(10));
        riduKonteiner.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane(riduKonteiner);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        root.setCenter(scrollPane);

        // Bottom – värvivalik ja nupud
        HBox tagasisideJuhend = looTagasisideJuhend();
        HBox varviValikuRida = looVarviValikuRida();

        VBox alumineOsa = new VBox(10, tagasisideJuhend, varviValikuRida);
        alumineOsa.setAlignment(Pos.CENTER);
        alumineOsa.setPadding(new Insets(10));

        root.setBottom(alumineOsa);

        // Loo mänguread
        int esialgneRidadeArv = 2 * varvideArv;
        looTuhjadManguread(esialgneRidadeArv, varvideArv);

        // Scene
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Mõttemeister – Mängulaud");
        stage.show();
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }

    private HBox looVarviValikuRida() {
        HBox varviRida = new HBox(10);
        varviRida.setAlignment(Pos.CENTER_LEFT);
        varviRida.setPadding(new Insets(10));

        // Lisa vasak joondus
        varviRida.getChildren().add(looVasakVahe());

        for (Color varv : saadavalVarvid) {
            Circle varviRing = new Circle(15, varv);
            varviRing.setStroke(Color.DARKGRAY);

            StackPane klikitavRing = new StackPane(varviRing);
            klikitavRing.setCursor(Cursor.HAND);

            // Nupule sarnane stiil
            klikitavRing.setStyle(
                    "-fx-background-color: lightgrey; " +
                            "-fx-background-radius: 50%; " +
                            "-fx-padding: 5px; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 2);"
            );

            klikitavRing.setOnMouseClicked(e -> {
                if (hetkeValikud.size() < varvideArv) {
                    suuredRingid[aktiivneRidaIndeks][hetkeValikud.size()].setFill(varv);
                    hetkeValikud.add(varv);
                    Logija.logi("Valiti värv: " + varv);

                    if (hetkeValikud.size() == varvideArv) {
                        kinnitaNupp.setDisable(false);
                    }
                }
            });

            varviRida.getChildren().add(klikitavRing);
        }

        // Info nupp koos tooltipiga
        Button infoNupp = new Button("?");
        infoNupp.setFocusTraversable(false);
        infoNupp.setStyle(
                "-fx-font-weight: bold; " +
                        "-fx-background-radius: 50%; " +
                        "-fx-min-width: 30px; " +
                        "-fx-min-height: 30px; " +
                        "-fx-max-width: 30px; " +
                        "-fx-max-height: 30px;"
        );

        Tooltip infoTooltip = new Tooltip(
                "Nupu lauale asetamiseks vajuta vastava värvi nuppu.\n" +
                        "Kui tahad nuppu tagasi võtta, vajuta nuppu \"Tagasi\".\n" +
                        "Kui kogu värvidrida täidetud ja oled kindel oma arvamuses, vajuta nuppu \"Kinnita\".\n" +
                        "Selle peale antakse sulle tagasiside väikestes ringides ning kui salakood ei ole veel arvatud, liigutakse järgmisele mängureale."
        );
        infoTooltip.setShowDelay(Duration.ZERO);  // Siin määrad, et delay puudub

        infoNupp.setTooltip(infoTooltip);


        // Tagasi nupp
        Button tagasiNupp = new Button("Tagasi");
        tagasiNupp.setOnAction(e -> {
            int viimane = hetkeValikud.size() - 1;
            if (viimane >= 0) {
                suuredRingid[aktiivneRidaIndeks][viimane].setFill(Color.LIGHTGRAY);
                hetkeValikud.remove(viimane);
                Logija.logi("Eemaldati " + viimane);
            }
            if (hetkeValikud.size() < varvideArv) {
                kinnitaNupp.setDisable(true);
            }
        });

        // Katkesta nupp
        Button katkestaNupp = new Button("Katkesta");
        katkestaNupp.setOnAction(e -> {
            Platform.exit();
        });

        // Kinnita nupp
        kinnitaNupp = new Button("Kinnita");
        kinnitaNupp.setDisable(true);
        kinnitaNupp.setOnAction(e -> kinnitaPakkumine());

        varviRida.getChildren().addAll(kinnitaNupp, tagasiNupp, katkestaNupp, infoNupp);

        return varviRida;
    }



    private HBox looTagasisideJuhend() {
        HBox juhendBox = new HBox(15);
        juhendBox.setAlignment(Pos.CENTER_LEFT); // Võid muuta CENTER, kui soovid
        juhendBox.setPadding(new Insets(8));

        Label pealkiri = new Label("Tagasiside värvide tähendused:");
        pealkiri.setStyle("-fx-font-weight: bold;");

        juhendBox.getChildren().addAll(
                pealkiri,
                looVärviSelgitus(Color.LIGHTGREEN, "Õige värv ja koht"),
                looVärviSelgitus(Color.LIGHTSALMON, "Õige värv, vale koht"),
                looVärviSelgitus(Color.GRAY, "Värv ei esine salakoodis")
        );
        return juhendBox;
    }


    private HBox looVärviSelgitus(Color värv, String selgitus) {
        Circle ring = new Circle(10, värv);
        Label silt = new Label(selgitus);
        HBox box = new HBox(5, ring, silt);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }


    private void kinnitaPakkumine() {
        //System.out.println("Pakkumine kinnitati: " + hetkeValikud);

        int visuaalneIndeks = suuredRingid.length - 1 - aktiivneRidaIndeks;

        List<String> salakoodList = salakood.getSalakood(); // List<String>
        Logija.logi("Moodustati salakood "+salakoodList);
        List<Color> salakoodVarvid = teisendaStringidVarvideks(salakoodList); // List<Color>
        List<String> salakoodStr = teisendaVarvidStringideks(salakoodVarvid); // List<String>

        List<String> pakkumineStr = teisendaVarvidStringideks(hetkeValikud); // hetkeValikud on juba List<Color>

        String tagasiside = Tagasiside.kontrolliKoodi(pakkumineStr, salakoodStr, positsioonilineTagasiside);
        Logija.logi("Genereeritud tagasiside: " + tagasiside);
        boolean mangLabi = tagasiside.chars().allMatch(c -> c == 'õ');
        if (mangLabi) {
            boolean voit=true;
            kuvaLoppAnimatsioon(voit);
            return;
        }

        // Kuvame tagasiside
        HBox rida = (HBox) riduKonteiner.getChildren().get(visuaalneIndeks);
        for (int i = 0; i < tagasiside.length(); i++) {
            Circle tagasisideRing = (Circle) rida.getChildren().get(varvideArv + 2 + i);
            char märk = tagasiside.charAt(i);
            if (märk == 'õ') {
                tagasisideRing.setFill(Color.LIGHTGREEN); // Õige värv ja koht
            } else if (märk == 'v') {
                tagasisideRing.setFill(Color.LIGHTSALMON); // Õige värv, vale koht
            } else {
                tagasisideRing.setFill(Color.GRAY); // Vale
            }
        }

        // Järgmine käik
        hetkeValikud.clear();
        kinnitaNupp.setDisable(true);
        aktiivneRidaIndeks++;

        if (aktiivneRidaIndeks >= suuredRingid.length) {
            System.out.println("Mäng läbi – rohkem ridu pole.");
            boolean kaotus=false;
            kuvaLoppAnimatsioon(kaotus);
            kinnitaNupp.setDisable(true);
        }
        // Kerime mängulaua ülespoole, et uus aktiivne rida oleks nähtav
        Platform.runLater(() -> scrollPane.setVvalue(0.0));
    }


    private String kontrolliKoodi(List<String> pakutudKood) {
        List<String> salakoodList = salakood.getSalakood();
        System.out.println("Salakoodi värvid:"+salakoodList);
        return Tagasiside.kontrolliKoodi(pakutudKood, salakoodList, positsioonilineTagasiside);
    }

    private String värvStringiks(Color värv) {
        if (värv.equals(Color.RED)) return "r";
        if (värv.equals(Color.BLUE)) return "b";
        if (värv.equals(Color.GREEN)) return "g";
        if (värv.equals(Color.YELLOW)) return "y";
        if (värv.equals(Color.ORANGE)) return "o";
        if (värv.equals(Color.PURPLE)) return "p";
        if (värv.equals(Color.PINK)) return "i";
        if (värv.equals(Color.CYAN)) return "c";
        if (värv.equals(Color.MAGENTA)) return "m";
        if (värv.equals(Color.WHITE)) return "w";
        if (värv.equals(Color.BLACK)) return "k";
        if (värv.equals(Color.AQUAMARINE)) return "a";
        return värv.toString(); // varuvariant, kui uus värv lisandub
    }

    private Color stringVärviks(String s) {
        switch (s) {
            case "r": return Color.RED;
            case "b": return Color.BLUE;
            case "g": return Color.GREEN;
            case "y": return Color.YELLOW;
            case "o": return Color.ORANGE;
            case "p": return Color.PURPLE;
            case "i": return Color.PINK;
            case "c": return Color.CYAN;
            case "m": return Color.MAGENTA;
            case "w": return Color.WHITE;
            case "k": return Color.BLACK;
            case "a": return Color.AQUAMARINE;
            default:
                // Proovime teisendada heks stringi Color objektiks
                try {
                    return Color.web(s); // näiteks "0xff0000ff"
                } catch (Exception e) {
                    System.out.println("Tundmatu värv: " + s);
                    return Color.GRAY;
                }
        }
    }


    private List<String> teisendaVarvidStringideks(List<Color> varvid) {
        List<String> tulem = new ArrayList<>();
        for (Color v : varvid) {
            tulem.add(värvStringiks(v));
        }
        return tulem;
    }

    private List<Color> teisendaStringidVarvideks(List<String> varviSümbolid) {
        List<Color> tulem = new ArrayList<>();
        for (String sümbol : varviSümbolid) {
            tulem.add(stringVärviks(sümbol));
        }
        return tulem;
    }

    private VBox looPeidetudSalakood() {
        Label selgitus = new Label("Peidetud salakood (ära piilu!)");
        selgitus.setStyle("-fx-font-style: italic; -fx-text-fill: gray;");
        selgitus.setPadding(new Insets(10));
        selgitus.setAlignment(Pos.CENTER_LEFT);

        HBox ringideBox = new HBox(10);
        ringideBox.setPadding(new Insets(5));
        ringideBox.setAlignment(Pos.CENTER_LEFT);
        ringideBox.getChildren().add(looVasakVahe());

        for (int i = 0; i < varvideArv; i++) {
            Circle varvRing = looSuurRing(Color.GRAY);
            ringideBox.getChildren().add(varvRing);
        }

        VBox koguBox = new VBox(3, selgitus, ringideBox);
        koguBox.setAlignment(Pos.CENTER_LEFT);
        koguBox.setPadding(new Insets(10));

        return koguBox;
    }


    private Circle looSuurRing(Color täitevärv) {
        Circle ring = new Circle(15); // Ühtne suurus kõikjal
        ring.setFill(täitevärv);      // Vastavalt vajadusele: tume, hele, värviline
        ring.setStroke(Color.DARKGRAY); // Ühtne ääris
        return ring;
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
