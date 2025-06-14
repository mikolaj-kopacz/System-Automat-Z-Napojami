import java.math.BigDecimal;

public abstract class Produkt {

    protected int id;
    protected String nazwa;
    protected BigDecimal cena;

    public Produkt(int id, String nazwa, BigDecimal cena) {
        this.id = id;
        this.nazwa = nazwa;
        this.cena = cena;
    }

    public int getId() { return id; }
    public String getNazwa() { return nazwa; }
    public BigDecimal getCena() { return cena; }

    public abstract String getSzczegolowyOpis();
}