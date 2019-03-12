public class Prostokat extends Czworokat{
    public Prostokat(int bok1, int bok2, int bok3, int bok4, int kat) throws Exception {
        super(bok1, bok2, bok3, bok4, kat);
        if(bok1!=bok3 || bok2!=bok4)
            throw new Exception("Naprzemianlegle boki nie sa rowne!");
        if(kat!=90)
            throw new Exception("Kat nie jest prosty!");
    }
}