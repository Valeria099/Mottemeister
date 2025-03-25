import java.util.List;

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

    public void prindiTagasiside(boolean positsiooniline) {
        StringBuilder tagasiside = new StringBuilder();

        if (positsiooniline) {
            // Positiivne tagasiside
            for (int i = 0; i < must; i++) {
                tagasiside.append("õ"); // 'm' musta (õige värv, õige positsioon) jaoks
            }
            for (int i = 0; i < valge; i++) {
                tagasiside.append("v"); // 'v' valge (õige värv, vale positsioon) jaoks
            }
        } else {
            // Mittepositsiooniline tagasiside
            for (int i = 0; i < must; i++) {
                tagasiside.append("õ"); // 'm' musta (õige värv, õige positsioon) jaoks
            }
            for (int i = 0; i < valge; i++) {
                tagasiside.append("v"); // 'v' valge (õige värv, vale positsioon) jaoks
            }
            for (int i = 0; i < tühi; i++) {
                tagasiside.append("t"); // 't' tühi (vale värv, vale positsioon) jaoks
            }
        }
        // Tühjad elemendid ei kuva, seega ei vaja "o" käsitlemist

        // Prindi tagasiside kujul
        System.out.println("Tagasiside: " + tagasiside.toString()); // Prindi tagasiside kujul

    }

    // Uus meetod, et kontrollida koodi ja anda tagasiside
    public static String kontrolliKoodi(List<String> pakutudKood, List<String> salakoodList, boolean positsioonilineTagasiside) {
        StringBuilder tagasiside = new StringBuilder(); // Kasutame StringBuilderit tagasiside jaoks
        boolean[] salakoodKontrollitud = new boolean[salakoodList.size()]; // Märgime, milliseid salakoode on kontrollitud

        // 1. Positsiooniline kontroll
        for (int i = 0; i < pakutudKood.size(); i++) {
            if (pakutudKood.get(i).equals(salakoodList.get(i))) {
                tagasiside.append("õ"); // Õige värv ja õige positsioon
                salakoodKontrollitud[i] = true; // Märgi, et see positsioon on kontrollitud
            } else {
                tagasiside.append("t"); // Alguses oletame, et see on vale
            }
        }

        // 2. Kontrollime vale positsiooniga värve
        for (int i = 0; i < pakutudKood.size(); i++) {
            if (tagasiside.charAt(i) == 't') { // Ainult need, mis ei olnud õigel positsioonil
                for (int j = 0; j < salakoodList.size(); j++) {
                    if (!salakoodKontrollitud[j] && pakutudKood.get(i).equals(salakoodList.get(j))) {
                        tagasiside.setCharAt(i, 'v'); // Õige värv, vale positsioon
                        salakoodKontrollitud[j] = true; // Märgime, et see värv on leitud
                        break; // Mine järgmise pakutud nupu juurde
                    }
                }
            }
        }

        // Tagastame stringi kas positsiooniliselt või mittepositsiooniliselt
        if (positsioonilineTagasiside) {
            return tagasiside.toString(); // Tagasta positsiooniline tagasiside
        } else {
            // Loeme kokku 'õ', 'v' ja 't'
            int õArv = 0, vArv = 0, tArv = 0;

            // Loendage 'õ', 'v' ja 't'
            for (int i = 0; i < tagasiside.length(); i++) {
                char ch = tagasiside.charAt(i);
                if (ch == 'õ') {
                    õArv++;
                } else if (ch == 'v') {
                    vArv++;
                } else if (ch == 't') {
                    tArv++;
                }
            }

            // Koostame uue tagasiside stringi
            StringBuilder uusTagasiside = new StringBuilder();
            uusTagasiside.append("õ".repeat(õArv)); // Kõik 'õ'
            uusTagasiside.append("v".repeat(vArv)); // Kõik 'v'
            uusTagasiside.append("t".repeat(tArv)); // Kõik 't'

            return uusTagasiside.toString(); // Tagasta mittepositsiooniline tagasiside
        }
    }
}
