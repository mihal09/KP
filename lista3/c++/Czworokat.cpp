class Czworokat extends Figura {
    private:
        int bok1, bok2, bok3, bok4, kat;

    public:
        virtual double Obwod() {
            return bok1 + bok2 + bok3 + bok4;
        }

        virtual double Pole() {
            return bok1 * bok3 * Math.sin(kat*Math.PI/180.0);
        }

        Czworokat(int bok1, int bok2, int bok3, int bok4, int kat){
//            if(bok1 <= 0 || bok2<=0 || bok3 <= 0 || bok4<=0)
//                throw new Exception("Niedodatni bok!");
//            if( kat<=0 ||kat>=180)
//                throw new Exception("Nieprawidlowy kat!");

            this.bok1 = bok1;
            this.bok2 = bok2;
            this.bok3 = bok3;
            this.bok4 = bok4;
            this.kat = kat;
        }
}
