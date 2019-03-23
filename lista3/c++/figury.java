public class figury {
    public static void main(String[] args){
        if(args.length==0){
            System.out.println("Brak argumentow!");
            System.exit(0);
        }
        String typyFigur = args[0];
        Figura [] figury = new Figura[typyFigur.length()];
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
                    figury[i] = new Kolo(promien);

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

                    figury[i] = new Czworokat(bok1, bok2, bok3, bok4, kat);

                } else if (znak == 'p') { //pieciokat
                    if (wskaznik + 1 > iloscArgumentow) {
                        System.out.println("Brakujace parametry!");
                        System.exit(0);
                    }
                    int bok = Integer.valueOf(args[wskaznik]);
                    wskaznik++;

                    figury[i] = new Pieciokat(bok);

                } else if (znak == 's') { //szesciokat
                    if (wskaznik + 1 > iloscArgumentow) {
                        System.out.println("Brakujace parametry!");
                        System.exit(0);
                    }
                    int bok = Integer.valueOf(args[wskaznik]);
                    wskaznik++;

                    figury[i] = new Szesciokat(bok);

                }
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        for(Figura figura : figury){
            if(figura != null)
                System.out.printf("Pole: %.2f Obwod: %.2f\n", figura.Pole(),figura.Obwod());
        }
    }
}
