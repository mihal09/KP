public class Prostokat extends Czworokat{
    public Prostokat(int _bok1, int _bok2) throws Exception {
        super(_bok1, _bok1, _bok2, _bok2, 90);
        this.nazwa = "Prostokat";
        if(bok1!=bok2 || bok3!=bok4)
            throw new Exception("Naprzemianlegle boki nie sa rowne!");
        if(kat!=90)
            throw new Exception("Kat nie jest prosty!");
    }

    @Override
    public double Obwod() {
        return 2*(bok1+bok3);
    }

    @Override
    public double Pole() {
        return bok1*bok3;
    }
}
