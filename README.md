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
- nn tühi, ehk seda värvi nuppu ei ole reas;
- nn vale, mis tähendab, et värv on küll õige, aga vale koha peal;
- nn õige, mis tähendab, et nupu värv on õige ja ka õige koha peal.
Mängijale tagasiside andmiseks on kaks versiooni:
- tagasiside antakse iga nupu kohta eraldi;
- tagasiside antakse järjekorras x tühi, y vale ja z õige ehk tead, mis tüüpi on info nuppude kohta, kuid ei tea, millise nupu kohta info käib.

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
Tagasiside(int õige, int vale, int tühi) - konstruktor, mis määrab õigete, valede ja tühjade arvu.
int getÕige() / int getVale() / int getTühi() - getter meetodid, mis tagastavad vastava tagasiside tüübi arvu.
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

*MangulaudVaade klass*
Eesmärk: See klass loob ja haldab graafilist mänguliidest JavaFX-is mängu "Mõttemeister" jaoks. See sisaldab kogu mängulaua kasutajaliidest, kus mängija valib värve, esitab pakkumisi ja saab visuaalset tagasisidet. Klass haldab värvivaliku rida (värvid, nupud nagu Kinnita, Tagasi, Katkesta, Info), mänguridade kuvamist ja värvide sisestamist, tagasiside ringide värvimist vastavalt pakkumise ja salakoodi võrdlusele, mängu edenemise juhtimist (järgmisele reale liikumine, mängu lõpu tuvastamine), peidetud salakoodi kuvamist (hallid ringid), värvide ja stringide vahelisi teisendusi. Lihtsamalt öeldes: see on mängu põhiline kasutajaliides, mis võimaldab mängijal värve valida, pakkumisi teha ja tagasisidet saada, juhtides kogu mängulaua graafilist käitumist.

*MottemeisterFX klass*
Eesmärk: See klass käivitab JavaFX-is mängu "Mõttemeister" algseadistuste graafilise kasutajaliidese. Klass kuvab mängijale akna, kus saab sisestada mängija nime, valida värvide arvu, määrata, kas värvid võivad korduda, ja valida tagasiside tüüp. Lisaks genereerib see valikute põhjal mängu salakoodi ja avab mängulaua vaate (MangulaudVaade), kust mäng algab.

*Logija klass*
Eesmärk: See klass vastutab mängu käigus tekkivate sündmuste ja tegevuste logimise eest faili. Logija kirjutab ajatempli ja sõnumi kasutaja kodukataloogi all asuvasse "Mõttemeister/logi.txt" faili, võimaldades hiljem mängu kulgu ja vigu analüüsida.

*ManguSeadistus klass*
See klass hoiab kokku ja edastab mängu algseadistusi, mida kasutaja on määranud. Seal säilitatakse mängija nimi, värvide arv, korduste lubatavus, tagasiside tüüp ning kasutatavad värvid. Klass toimib mängu konfiguratsiooni hoidjana ja võimaldab neile seadistustele mugavalt ligi pääseda.

**Projekti protsessi kirjeldus**
1. Idee genereerimine - Esimesena pidasime plaani, mida võiks projektitööna koostada. Püüdsime silmas pidada ka seda, et projekti saaks edasi rakendada ka projektitöö II etapis.
2. Genereeritud idee kirjeldamine, mida programm peab tegema, millised võiksid olla erinevad versioonid mängust.
3. Klasside nimede, nende eesmärgi ja meetodite kokkuleppimine.
4. Erinevate klasside koodide koostamine.
5. Programmitöö testimine.
Kõik etapid said mitmeid kordi omavahel arutatud ja nõu peetud.
Protsessiliselt Victoria kirjutas põhjad klassidesse ja Anne tegeles peamiselt tagasiside klassi korrektselt tööle saamisega, nii et oleksid võimalikud nii kõikvõimalikud nuppude värvide valikute lahendused kui ka tagasisisde andmise võimalused.
Graafikaliidese testimiseks:
1. Panime kirja, mida me graafiliselt tahaksime lahendada
2. Jagasime töö kasutajalt info küsimise osaks ja mängulaua vaate loomiseks
3. Jagasime vastavalt töö ning moodustasime vastavalt uued klassid
4. Lisaks lõime ka klassi Logija.
Graafikaliidese korral Victoria tegeles kasutajalt andemte loomise graafilise lahendusega ja Anne tegeles MangulaudVaade loomisega ning sinna mänguloogika lisamisega. Ühtlasi pidasime plaani, kuidas saaks rakendada ka faili lugemist. Selleks tegime logimise loogika ja faili kirjutamise.

**Ajakulu**
Ajakulu - Victorial kulus umbkaudu 15h, Anne kulutas projektile aega umbes 25h, graafilisele mängulaua vaatele ja mänguloogika töölesaamisele graafikaga koos ligukaudu 20h.

**Peamised mured**
Kõige keerukam oli tabada, kuidas kõige efektiivsemalt saada tagasiside tööle mõlema tagasisidestamise variandi korral - nii näidates tagasisidet iga nupu kohta eraldi, ja teisel juhul kui antakse tagasiside, aga mitte näidates, millise nupu kohta tagasiside käib. Samuti osutas vastupanu juhus, kui oli värvide kordusi, et ka siis oskaks kood tagasisidestada mõlemal juhul õigesti.
Graafikaliidese loomisel oli kõige suurem väljakutse Annel, et nuppudel valitud värv hakkaks korrektselt mängulaual nupuauke täitma ja et kogu mänguloogika oleks paigas.

**Hinnang**
Koostöö toimis ja programm hakkas tööle nii nagu ootasime. 

**Testimine**
Testimine toimus pidevalt koodi muutes, et programm kompileeriks, et annaks tagasisidet ootuspäraselt. Samuti sai testimisse kaasatud lapsi ja teisi pereliikmeid, et mängu selgitus oleks arusaadav ja programm töötaks korrektselt.


 
