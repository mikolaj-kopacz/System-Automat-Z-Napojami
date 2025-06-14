import java.math.BigDecimal;

public class NapojGazowany extends Napoj {
    private boolean czySlodzony;

    public NapojGazowany(int id, String nazwa, BigDecimal cena, int pojemnoscMl, boolean czySlodzony) {
        super(id, nazwa, cena, pojemnoscMl);
        this.czySlodzony = czySlodzony;
    }

    @Override
    public String getSzczegolowyOpis() {
        String cukierInfo = czySlodzony ? "słodzony cukrem" : "bez cukru";
        return String.format("Napój gazowany: %s\nPojemność: %d ml\nCena: %.2f zł\n(%s)",
                nazwa, pojemnoscMl, cena, cukierInfo);
    }
}