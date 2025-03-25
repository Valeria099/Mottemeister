// Peaklass
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Mottemeister {
    public static void main(String[] args) {
        System.out.println("Mõttemeister – Kuidas mängida?\n" +
                "Arva ära vastase värvikood võimalikult väheste katsetega!\n" +
                "Jätka pakkumist, kuni leiad õige koodi!" +
                "\n");
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
            System.out.print("Sisesta värvikood värvide esitähtedest (nt 'vklmh' on värvivalik valge-kollane-lilla-must-hall): ");
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

                if (tagasiside.getÕige() == nupuArv) {
                    salakoodArvatud = true; // Mäng lõpetatakse
                    System.out.println("Palju õnne " + mängija.getNimi() + "! Sa arvasid salakoodi õigesti.");
                }
            } else {
                System.out.println("Sisesta täpselt " + nupuArv + " värvi.");
            }
        }
        scanner.close();
    }
}
