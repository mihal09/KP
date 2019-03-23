public class Kwadrat extends Czworokat{
    public Kwadrat(int bok) throws Exception {
        super(bok, bok, bok, bok, 90);
        this.nazwa = "Kwadrat";
        if(!(bok1 == bok2 && bok2 == bok3 && bok3 == bok4))
            throw new Exception("Nie wszystkie boki sa rowne!");
        if(kat!=90)
            throw new Exception("Kat nie jest prosty!");
    }

    @Override
    public double Obwod() {
        return bok1*4;
    }

    @Override
    public double Pole() {
        return bok1*bok1;
    }
}
