package oopsprojekt.projekt_mottemeister;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ManguJuhtimine {

    private Mängija mängija;
    private Salakood salakood;
    private boolean korduvad;
    private boolean positsioonilineTagasiside;
    private List<String> valikuVärvid;
    private int katseteArv;
    private ManguSeadistus seadistus;

    public ManguJuhtimine(ManguSeadistus seadistus) {
        this.seadistus = seadistus;
        this.mängija = new Mängija(seadistus.nimi);
        this.korduvad = seadistus.kordusedLubatud;
        this.positsioonilineTagasiside = seadistus.tagasisideTüüp.equalsIgnoreCase("klassikaline");

        this.valikuVärvid = genereeriVärvideValik(seadistus.varvideArv);
        System.out.println("Valitavad värvid: " + valikuVärvid);

        this.salakood = genereeriSalakood(seadistus.varvideArv);
        this.katseteArv = 0;
    }

    public int getVarvideArv() {
        return seadistus.getVarvideArv();
    }

    public Mängija getMangija() {
        return mängija;
    }

    public List<String> getValikuVärvid() {
        return valikuVärvid;
    }

    public List<String> getSalakood() {
        return salakood.getSalakood();
    }

    public boolean isPositsioonilineTagasiside() {
        return positsioonilineTagasiside;
    }

    public List<String> genereeriVärvideValik(int nupuArv) {
        List<String> kõikVärvid = new ArrayList<>(Arrays.asList(
                "valge", "kollane", "oranž", "beež", "punane",
                "sinine", "hall", "roheline", "must", "lilla"
        ));
        Collections.shuffle(kõikVärvid);
        return kõikVärvid.subList(0, Math.min(nupuArv, kõikVärvid.size()));
    }

    public Salakood genereeriSalakood(int nupuArv) {
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

    public String kontrolliKoodi(List<String> pakutudKood) {
        katseteArv++;
        List<String> salakoodList = salakood.getSalakood();

        return Tagasiside.kontrolliKoodi(pakutudKood, salakoodList, positsioonilineTagasiside);
    }

    public int getKatseteArv() {
        return katseteArv;
    }
}
