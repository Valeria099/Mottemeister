package oopsprojekt.projekt_mottemeister;

import javafx.scene.paint.Color;
import java.util.List;

public class ManguSeadistus {
    public String nimi;
    public int varvideArv;
    public boolean kordusedLubatud;
    public String tagasisideTüüp;
    private List<Color> varvid; // ← see peab olema lisatud, kui kasutad getVarvid()

    public ManguSeadistus(String nimi, int varvideArv, boolean kordusedLubatud,
                          String tagasisideTüüp, List<Color> varvid) {
        this.nimi = nimi;
        this.varvideArv = varvideArv;
        this.kordusedLubatud = kordusedLubatud;
        this.tagasisideTüüp = tagasisideTüüp;
        this.varvid = varvid;
    }

    public int getVarvideArv() {
        return varvideArv;
    }

    public String getTagasisideTüüp() {
        return tagasisideTüüp;
    }

    public String getNimi() {
        return nimi;
    }

    public boolean isKordusedLubatud() {
        return kordusedLubatud;
    }

    public List<Color> getVarvid() {
        return varvid;
    }
}
