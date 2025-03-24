// Tagasiside klass
public class Tagasiside {
    private int õige;    // Õige värv ja õige positsioon
    private int vale;   // Õige värv, aga vale positsioon
    private int täiestiVale;    // Vale värv

    // Konstruktor
    public Tagasiside(int õige, int vale, int täiestiVale) {
        this.õige = õige;
        this.vale = vale;
        this.täiestiVale = täiestiVale;
    }

    // Getter meetodid
    public int getÕige() {
        return õige;
    }

    /*
    public int getVale() {
        return vale;
    }

    public int getTäiestiVale() {
        return täiestiVale;
    }
     */

    @Override
    public String toString() {
        return "Õige: " + õige + ", Õige värv, vale positsioon: " + vale + ", Täiesti vale: " + täiestiVale;
    }
}