**Mäng Mõttemeister**

**Autorid:**
Victoria Grau, Anne Pirn

**Mängu kirjeldus:**

Mõttemeistri mängu eesmärk on võimalikult väheste kordade arvuga ära arvata värvikombinatsioon, mille vastasmängija (selle projekti korral arvuti) on välja mõelnud. Mängulauale seatakse nn "Salakood" värvilistest nuppudest 3-10-kohalise koodina nii, et vastasmängijad ei näe.

Mängus on nuppude paigutuse osas erineva raskusastmega versioone:
- on teada nuppude värvid, mille paigutuse järjekord tuleb ära arvata;
- on teada nuppude värvid (sama palju kui nuppe laual), kuid värv võib korduda - seega mõnda värvi valitud värvide seas ei ole ja mõnda on mitu korda;
- on teada suurem hulk värve, mille seast valitakse täpselt nii palju erinevaid värvi nuppe, kui salakoodis kohti, seega värv ei kordu;
- on teada suurem hulk värve, mille seast valitakse täpselt nii palju erinevaid värvi nuppe, värv võib korduda.

Kui väljamõtleja on rivi paika saanud, siis teine mängija pakub nuppude värvi ja järjekorra. Tagasiside andmisel on kolm olekut:
- nn täiesti vale, ehk seda värvi nuppu ei ole reas;
- nn vale, mis tähendab, et värv on küll õige, aga vale koha peal;
- nn õige, mis tähendab, et nupu värv on õige ja ka õige koha peal.
Mängijale tagasiside andmiseks on kaks versiooni:
- tagasiside antakse iga nupu kohta eraldi;
- tagasiside antakse järjekorras x täiesti vale, y vale ja z õige ehk tead, mis tüüpi on info nuppude kohta, kuid ei tea, millise nupu kohta info käib.

Mäng saab läbi, kui nn "Salakood" on ära arvatud.

**Mängu käik:**

- Mängijale antakse lühidale teada mängu eesmärk, misjärel küsitakse mängijalt nime.
- Seejärel küsitakse mängijalt, mitme nupuga salakoodi ta soovib arvata.
- Seejärel küsitakse, kas nupu värv võib korduda soovides vastuseks kas "Jah" või "Ei".
- Seejärel küsitakse, kas tagasiside antakse iga nupu kohta eraldi (positsiooniliselt) või mitte, millele oodatakse vastuseks "Jah" või "Ei".
Mäng algab nende parameetritega, programm koostab salakoodi ja palub arvata ära salakoodi andes värvivaliku ja paludes sisestada kood värvi esitähtedest (et oleksi lihtsam sisestada ja vältida trükivigu).
Seepeale annab mäng tagasiside arvamusele vastavalt valitud tagasiside meetodile.
Mäng lõppeb, kui salakood on ära arvatud.

**Klassid, nende eesmärk ja peamised meetodid**
*ManguJuhtimine klass*
Eesmärk: Mängu juhtimise klass vastutab mängu loogika, mängija andmete, salakoodi genereerimise ning tagasiside hindamise haldamise eest. See klass defineerib ka mängu reeglid, nagu kas värvid võivad korduda ja kas tagasiside antakse positsiooniliselt.
Olulisemad meetodid:
ManguJuhtimine(Mängija mängija, boolean korduvad, boolean positsioonilineTagasiside, int nupuArv) - konstruktor, mis määrab mängija, värvisüsteemi ja tagasiside tüübid.
List<String> genereeriVärvideValik(int nupuArv) - genereerib ja tagastab juhusliku valiku värve mängu nupudeks.
Salakood genereeriSalakood(int nupuArv) - genereerib ja tagastab salakoodi, kasutades määratud värvide valikut.
String kontrolliKoodi(List<String> pakutudKood) - kontrollib, kas pakutud kood vastab salakoodile, ja väljastab tagasiside.

*Tagasiside klass*
Eesmärk: Tagasiside klass vastutab mängija tagasiside andmise ja hindamise eest. See klass loob erinevat tüüpi tagasiside, sõltuvalt sellest, kas tagasiside on positsiooniline või mitte.
Olulisemad meetodid:
Tagasiside(int õige, int vale, int täiestiVale) - konstruktor, mis määrab õigete, valede ja täiesti valede arvu.
int getÕige() / int getVale() / int getTäiestiVale() - getter meetodid, mis tagastavad vastava tagasiside tüübi arvu.
void prindiTagasiside(boolean positsiooniline) - prindib tagasiside vastavalt positsioonilisele või mittepositsioonilisele sümbolite järjestusele.
static String kontrolliKoodi(List<String> pakutudKood, List<String> salakoodList, boolean positsioonilineTagasiside) - analüüsib pakutud koodi võrreldes salakoodiga ja tagastab vastava tagasiside string.

*Salakood klass*
Eesmärk: Salakood klass haldab mängu salakoodi, mis on juhuslikult genereeritud värvide kombinatsioon. See klass salvestab salakoodi ja võimaldab sellele juurde pääseda.
Olulisemad meetodid:
Salakood(List<String> salakood) - konstruktor, mis salvestab arvatud salakoodi.
List<String> getSalakood() - tagastab salakoodi.

*Mängija klass*
Eesmärk: Mängija klass salvestab mängija nime ja haldab sellega seotud andmeid. See klass on vajalik, et hoida mängija informatsiooni mängu käigus.
Olulisemad meetodid:
Mängija(String nimi) - konstruktor, mis määrab mängija nime.
String getNimi() - tagastab mängija nime.

*Mottemeister klass*
Eesmärk: Mottemeister on peaklass, mis käivitab mängu ja haldab kasutajaliidest. Kõik mängu interaktsioonid ja sisendite töötlemine toimuvad selle klassi kaudu. See klass koondab kogu mängu käivitamise ja mängija sisendi töötlemise loogika.
Olulisemad meetodid:
main(String[] args) - peamine meetod, mis käivitab mängu, küsib kasutajalt sisendeid (nagu nimi, mängu seaded jne) ja haldab mängu tsüklit.

**Projekti protsessi kirjeldus**
1. Idee genereerimine - Esimesena pidasime plaani, mida võiks projektitööna koostada. Püüdsime silmas pidada ka seda, et projekti saaks edasi rakendada ka projektitöö II etapis.
2. Genereeritud idee kirjeldamine, mida programm peab tegema, millised võiksid olla erinevad versioonid mängust.
3. Klasside nimede, nende eesmärgi ja meetodite kokkuleppimine.
4. Erinevate klasside koodide koostamine.
5. Programmitöö testimine.
Kõik etapid said mitmeid kordi omavahel arutatud ja nõu peetud.
Protsessiliselt Victoria kirjutas põhjad klassidesse ja Anne tegeles peamiselt tagasiside klassi korrektselt tööle saamisega, nii et oleksid võimalikud nii kõikvõimalikud nuppude värvide valikute lahendused kui ka tagasisisde andmise võimalused.

**Ajakulu**
Ajakulu - Victorial kulus umbkaudu 15h, Anne kulutas projektile aega umbes 25h.

**Peamised mured**
Kõige keerukam oli tabada, kuidas kõige efektiivsemalt saada tagasiside tööle mõlema tagasisidestamise variandi korral - nii näidates tagasisidet iga nupu kohta eraldi, ja teisel juhul kui antakse tagasiside, aga mitte näidates, millise nupu kohta tagasiside käib. Samuti osutas vastupanu juhus, kui oli värvide kordusi, et ka siis oskaks kood tagasisidestada mõlemal juhul õigesti.

**Hinnang**
Koostöö toimis ja programm hakkas tööle nii nagu ootasime. 

**Testimine**
Testimine toimus pidevalt koodi muutes, et programm kompileeriks, et annaks tagasisidet ootuspäraselt. Samuti sai testimisse kaasatud lapsi ja teisi pereliikmeid, et mängu selgitus oleks arusaadav ja programm töötaks korrektselt.


 
