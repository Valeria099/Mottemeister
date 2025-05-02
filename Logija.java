package oopsprojekt.projekt_mottemeister;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logija {

    private static final File logiFail;

    static {
        // Määrame logifaili tee kasutaja kodukataloogi alla kausta "Mõttemeister"
        String kasutajaKodu = System.getProperty("user.home");
        File kaust = new File(kasutajaKodu, "Mõttemeister");
        if (!kaust.exists()) {
            kaust.mkdirs(); // loo kaust, kui see ei eksisteeri
        }
        logiFail = new File(kaust, "logi.txt");
    }

    public static void logi(String sisu) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logiFail, true))) {
            String ajatempel = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            bw.write("[" + ajatempel + "] " + sisu);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Logimise viga: " + e.getMessage());
        }
    }

    public static File saaLogiFail() {
        return logiFail;
    }
}
