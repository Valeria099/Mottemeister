// MänguJuhtimine klass
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

public class ManguJuhtimine {
    private Mängija mängija;
    private Salakood salakood;
    private boolean korduvad;
    private boolean positsioonilineTagasiside;
    private List<String> valikuVärvid;

    public ManguJuhtimine(Mängija mängija, boolean korduvad, boolean positsioonilineTagasiside, int nupuArv) {
        this.mängija = mängija;
        this.korduvad = korduvad;
        this.positsioonilineTagasiside = positsioonilineTagasiside;
        this.valikuVärvid = genereeriVärvideValik(nupuArv);
        System.out.println("Valitavad värvid: " + valikuVärvid);
        this.salakood = genereeriSalakood(nupuArv);
    }

    public List<String> genereeriVärvideValik(int nupuArv) {
        List<String> kõikVärvid = new ArrayList<>(Arrays.asList("valge", "kollane", "oranž", "beež", "punane", "sinine", "hall", "roheline", "must", "lilla"));
        Collections.shuffle(kõikVärvid); // Juhuslikult segame värvid
        return kõikVärvid.subList(0, Math.min(nupuArv, kõikVärvid.size())); // Võtke sobiv arv nuppe: NupuArv-värvideArv
    }

    // salakoodi genereerimine kasutades juhusliku arvu genereerimist
    public Salakood genereeriSalakood(int nupuArv) {
        List<String> salakood = new ArrayList<>();

        for (int i = 0; i < nupuArv; i++) {
            String juhuslikVärv;
            if (korduvad) {
                juhuslikVärv = valikuVärvid.get((int)(Math.random() * valikuVärvid.size()));
            } else {
                juhuslikVärv = valikuVärvid.remove((int)(Math.random() * valikuVärvid.size())); // Eemaldab ja tagastab
            }
            salakood.add(juhuslikVärv);
        }
        return new Salakood(salakood);
    }

    public String kontrolliKoodi(List<String> pakutudKood) {
        List<String> salakoodList = salakood.getSalakood();

        // Kutsume Tagasiside klassi kontrolliKoodi meetodit, andes kolm argumenti
        String tagasisideKood = Tagasiside.kontrolliKoodi(pakutudKood, salakoodList, positsioonilineTagasiside);

        // Prindime tagasiside
        System.out.println("Tagasiside: " + tagasisideKood);

        return tagasisideKood; // Tagasta tagasiside
    }
}
