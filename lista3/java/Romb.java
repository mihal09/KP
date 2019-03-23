public class Romb extends Czworokat{
    public Romb(int bok, int kat) throws Exception {
        super(bok, bok, bok, bok, kat);
        this.nazwa = "Romb";
        if(!(bok1 == bok2 && bok2 == bok3 && bok3 == bok4))
            throw new Exception("Nie wszystkie boki sa rowne!");
    }

    @Override
    public double Obwod() {
        return 4*bok1;
    }

    @Override
    public double Pole() {
        return bok1 * bok1 * Math.sin(kat*Math.PI/180.0);
    }
}
