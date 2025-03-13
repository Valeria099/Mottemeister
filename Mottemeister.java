// Peaklass

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Mottemeister {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Mängija nimi
        System.out.print("Sisesta oma nimi: ");
        String nimi = scanner.nextLine();
        Mängija mängija = new Mängija(nimi);

        // Küsi nupud
        System.out.print("Sisesta, mitu kohta nuppudele mängulaual (3-10): ");
        int nupuArv = scanner.nextInt();
        while (nupuArv < 3 || nupuArv > 10) {
            System.out.print("Palun sisesta kehtiv väärtus (3-10): ");
            nupuArv = scanner.nextInt();
        }

        // Küsi, kas värv võib korduda
        System.out.print("Kas nupu värv võib korduda? (jah/ei): ");
        boolean korduvad = scanner.next().equalsIgnoreCase("jah");

        // Küsi tagasiside vorm
        System.out.print("Kas tagasiside antakse positsiooniliselt (õige vihje õigel kohal)? (jah/ei): ");
        boolean positsioonilineTagasiside = scanner.next().equalsIgnoreCase("jah");

        // Mängu juhtimisobjekt
        ManguJuhtimine mänguJuhtimine = new ManguJuhtimine(mängija, korduvad, positsioonilineTagasiside, nupuArv);

        // Alustame mängu
        boolean salakoodArvatud = false;
        while (!salakoodArvatud) {
            System.out.print("Sisesta värvikood värvide esitähtedest (nt 'vklmh'): ");
            String kasutajaSisend = scanner.next();
            List<String> pakutudKood = new ArrayList<>();

            for (char c : kasutajaSisend.toCharArray()) {
                switch (Character.toLowerCase(c)) {
                    case 'v':
                        pakutudKood.add("valge");
                        break;
                    case 'k':
                        pakutudKood.add("kollane");
                        break;
                    case 'o':
                        pakutudKood.add("oranž");
                        break;
                    case 'b':
                        pakutudKood.add("beež");
                        break;
                    case 'p':
                        pakutudKood.add("punane");
                        break;
                    case 's':
                        pakutudKood.add("sinine");
                        break;
                    case 'h':
                        pakutudKood.add("hall");
                        break;
                    case 'r':
                        pakutudKood.add("roheline");
                        break;
                    case 'm':
                        pakutudKood.add("must");
                        break;
                    case 'l':
                        pakutudKood.add("lilla");
                        break;
                    default:
                        System.out.println("Vale värvi algustäht! Proovi uuesti.");
                        pakutudKood.clear();
                        break;
                }
            }

            if (pakutudKood.size() == nupuArv) {
                Tagasiside tagasiside = mänguJuhtimine.kontrolliKoodi(pakutudKood);
                System.out.println("Tagasiside: " + tagasiside);

                if (tagasiside.getMust() == nupuArv) {
                    salakoodArvatud = true; // Mäng lõpetatakse
                    System.out.println("Palju õnne! Sa arvasid salakoodi õigesti.");
                }
            } else {
                System.out.println("Sisesta täpselt " + nupuArv + " värvi.");
            }
        }

        scanner.close();
    }

}

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
        int must = 0;
        int valge = 0;
        List<String> salakoodList = salakood.getSalakood();
        StringBuilder tagasiside = new StringBuilder("o".repeat(pakutudKood.size())); // Alguses oletame, et kõik on "o"

        // 1. Positsiooniline kontroll
        for (int i = 0; i < pakutudKood.size(); i++) {
            if (pakutudKood.get(i).equals(salakoodList.get(i))) {
                tagasiside.setCharAt(i, 'm'); // Õige värv ja õige positsioon
                must++;
            }
        }

        // 2. Kontrollib vale positsiooniga värvide
        boolean[] checkedSalakood = new boolean[salakoodList.size()];
        for (int i = 0; i < pakutudKood.size(); i++) {
            if (tagasiside.charAt(i) != 'm') { // Ainult need, mis ei olnud m
                for (int j = 0; j < salakoodList.size(); j++) {
                    // Otsime vale positsiooniga värvi, kui see on salakoodis
                    if (!checkedSalakood[j] && pakutudKood.get(i).equals(salakoodList.get(j))) {
                        tagasiside.setCharAt(i, 'v'); // Väär värv, vale positsioon
                        valge++;
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
                if (ch == 'm') järjestatudTagasiside.append(ch);
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
        return new Tagasiside(must, valge, pakutudKood.size() - must - valge); // Tagasta tagasiside
    }
}

// Salakood klass
import java.util.List;

public class Salakood {
    private List<String> salakood;

    public Salakood(List<String> salakood) {
        this.salakood = salakood;
    }

    public List<String> getSalakood() {
        return salakood;
    }
}

// Mängija klass
public class Mängija {
    private String nimi;

    public Mängija(String nimi) {
        this.nimi = nimi;
    }

    public String getNimi() {
        return nimi;
    }
}

// Tagasiside klass
public class Tagasiside {
    private int must;    // Õige värv ja õige positsioon
    private int valge;   // Õige värv, aga vale positsioon
    private int tühi;    // Vale värv

    // Konstruktor
    public Tagasiside(int must, int valge, int tühi) {
        this.must = must;
        this.valge = valge;
        this.tühi = tühi;
    }

    // Getter meetodid
    public int getMust() {
        return must;
    }

    public int getValge() {
        return valge;
    }

    public int getTühi() {
        return tühi;
    }

    @Override
    public String toString() {
        return "Must: " + must + ", Valge: " + valge + ", Tühi: " + tühi;
    }
}
