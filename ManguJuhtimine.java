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
        return kõikVärvid.subList(0, Math.min(nupuArv, kõikVärvid.size())); // Võtke kõige rohkem nupuArv-värvi
    }

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

    public Tagasiside kontrolliKoodi(List<String> pakutudKood) {
        int õige = 0;
        int vale = 0;
        List<String> salakoodList = salakood.getSalakood();
        StringBuilder tagasiside = new StringBuilder("o".repeat(pakutudKood.size())); // Alguses oletame, et kõik on "o"

        // 1. Positsiooniline kontroll
        for (int i = 0; i < pakutudKood.size(); i++) {
            if (pakutudKood.get(i).equals(salakoodList.get(i))) {
                tagasiside.setCharAt(i, 'õ'); // Õige värv ja õige positsioon
                õige++;
            }
        }

        // 2. Kontrollib vale positsiooniga värvide
        boolean[] checkedSalakood = new boolean[salakoodList.size()];
        for (int i = 0; i < pakutudKood.size(); i++) {
            if (tagasiside.charAt(i) != 'õ') { // Ainult need, mis ei olnud m
                for (int j = 0; j < salakoodList.size(); j++) {
                    // Otsime vale positsiooniga värvi, kui see on salakoodis
                    if (!checkedSalakood[j] && pakutudKood.get(i).equals(salakoodList.get(j))) {
                        tagasiside.setCharAt(i, 'v'); // Vale värv, vale positsioon
                        vale++;
                        checkedSalakood[j] = true; // Märkige salakood kontrollitud
                        break; // Mine järgmise pakutud nupu juurde
                    }
                }
            }
        }

        // 3. Positsioonilisest tagasisidest tulemuseks väljund
        if (!positsioonilineTagasiside) {
            StringBuilder järjestatudTagasiside = new StringBuilder();
            // Loo muutuja, et lugeda, sageli koguseid
            for (int i = 0; i < tagasiside.length(); i++) {
                char ch = tagasiside.charAt(i);
                if (ch == 'õ') järjestatudTagasiside.append(ch);
            }
            for (int i = 0; i < tagasiside.length(); i++) {
                char ch = tagasiside.charAt(i);
                if (ch == 'v') järjestatudTagasiside.append(ch);
            }
            for (int i = 0; i < tagasiside.length(); i++) {
                char ch = tagasiside.charAt(i);
                if (ch == 'o') järjestatudTagasiside.append(ch);
            }
            tagasiside = järjestatudTagasiside; // Vaheta tagasiside ümber
        }

        // Prindime tagasiside
        String tagasisideString = tagasiside.toString();
        System.out.println("Tagasiside: " + tagasisideString); // Näita tagasiside

        // Tagasta Tagasiside objekt
        return new Tagasiside(õige, vale, pakutudKood.size() - õige - vale); // Tagasta tagasiside
    }
}