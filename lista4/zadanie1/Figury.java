package zadanie1;

import java.util.ArrayList;

public class Figury {
    public enum Figura1 implements IFigura{
        OKRAG {
            @Override
            public double Obwod() {
                return 2 * Math.PI * p1;
            }

            @Override
            public double Pole() {
                return Math.PI * p1;
            }
        },
        KWADRAT{
            public double Obwod() {
                return 4*p1;
            }

            public double Pole() {
                return p1*p1;
            }
        },
        PIECIAKAT{
            public double Obwod() {
                return 5*p1;
            }

            public double Pole() {
                return 5 / 4.0 * (p1*p1) / Math.tan(Math.PI / 5);
            }
        },
        SZCZESCIOKAT{
            public double Obwod() {
                return 6*p1;
            }

            public double Pole() {
                return 6 / 4.0 * (p1*p1) / Math.tan(Math.PI / 6);
            }
        };

        int p1;

        public void setParameter(int p1){
            this.p1 = p1;
        }

    }
    public enum Figura2 implements IFigura{
        PROSTOKAT{
            @Override
            public double Obwod() {
                return 2*(p1+p2);
            }

            @Override
            public double Pole() {
                return p1*p2;
            }
        },
        ROMB{
            @Override
            public double Obwod() {
                return 4*p1;
            }

            @Override
            public double Pole() {
                return p1 * p1 * Math.sin(p2*Math.PI/180.0);
            }
        };

        int p1, p2;

        public void setParameters(int p1, int p2){
            this.p1 = p1;
            this.p2 = p2;
        }
    }

    public static void main(String[] args){
        if(args.length==0){
            System.out.println("Brak argumentow!");
            System.exit(0);
        }
        String typyFigur = args[0];
        ArrayList<IFigura> figury = new ArrayList<>();

        int wskaznik = 1;
        int iloscArgumentow = args.length;
        for (int i = 0; i < typyFigur.length(); i++){
            char znak = typyFigur.charAt(i);
            try {
                if (znak == 'o') { //koło
                    if (wskaznik + 1 > iloscArgumentow) {
                        System.out.println("Brakujace parametry!");
                        System.exit(0);
                    }
                    int promien = Integer.valueOf(args[wskaznik]);
                    wskaznik++;
                    Figura1 figura = Figura1.OKRAG;
                    figura.setParameter(promien);
                    figury.add(figura);

                } else if (znak == 'c') { //czworokat
                    if (wskaznik + 5 > iloscArgumentow) {
                        System.out.println("Brakujace parametry!");
                        System.exit(0);
                    }
                    int bok1 = Integer.valueOf(args[wskaznik]);
                    wskaznik++;
                    int bok2 = Integer.valueOf(args[wskaznik]);
                    wskaznik++;
                    int bok3 = Integer.valueOf(args[wskaznik]);
                    wskaznik++;
                    int bok4 = Integer.valueOf(args[wskaznik]);
                    wskaznik++;
                    int kat = Integer.valueOf(args[wskaznik]);
                    wskaznik++;

                    if((bok1==bok2 && bok3==bok4)||(bok1==bok3 && bok2==bok4)||(bok1==bok4 && bok2==bok3)){//jesli rownoleglobok
                        if(bok1==bok2&&bok2==bok3&&bok3==bok4){//jesli rowne boki
                            if(kat==90){ //tworzymy kwadrat
                                Figura1 figura = Figura1.KWADRAT;
                                figura.setParameter(bok1);
                                figury.add(figura);
                            }
                            else         //tworzymy romb
                            {
                                Figura2 figura = Figura2.ROMB;
                                figura.setParameters(bok1, kat);
                                figury.add(figura);
                            }
                        }
                        else if(kat==90){ //tworzymy prostokat
                            int bokX, bokY;
                            if(bok1!=bok2){
                                bokX = bok1;
                                bokY = bok2;
                            }
                            else{
                                bokX = bok1;
                                bokY = bok3;
                            }
                            Figura2 figura = Figura2.PROSTOKAT;
                            figura.setParameters(bokX, bokY);
                            figury.add(figura);
                        }
                        else{ // rownoleglobok nie jest prostokatem, rombem ani kwadratem
                            throw new Exception("Figura jest rownoleglobokiem, ale nie jest prostokatem, rombem ani kwadratem");
                        }
                    }
                    else{ // rownoleglobok nie jest prostokatem, rombem ani kwadratem
                        throw new Exception("Figura nie jest prostokatem, rombem ani kwadratem");
                    }

                } else if (znak == 'p') { //pieciokat
                    if (wskaznik + 1 > iloscArgumentow) {
                        System.out.println("Brakujace parametry!");
                        System.exit(0);
                    }
                    int bok = Integer.valueOf(args[wskaznik]);
                    wskaznik++;

                    Figura1 figura = Figura1.PIECIAKAT;
                    figura.setParameter(bok);
                    figury.add(figura);

                } else if (znak == 's') { //szesciokat
                    if (wskaznik + 1 > iloscArgumentow) {
                        System.out.println("Brakujace parametry!");
                        System.exit(0);
                    }
                    int bok = Integer.valueOf(args[wskaznik]);
                    wskaznik++;

                    Figura1 figura = Figura1.SZCZESCIOKAT;
                    figura.setParameter(bok);
                    figury.add(figura);

                }
            }
            catch(Exception e){
                System.out.println(e);
            }
        }

        for(IFigura figura : figury){
            if(figura != null)
                System.out.printf("Pole: %.2f obwod: %.2f\n", figura.Pole(),figura.Obwod());
        }
    }
}


