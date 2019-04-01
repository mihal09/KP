package zadanie2;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


/*--------------------------------------------------------------------*/
//ZAMYKANIE
class MyWindowAdapter extends WindowAdapter {
    public void windowClosing(WindowEvent e) { System.exit(0); }
}


/*--------------------------------------------------------------------*/
//PRZYCISK RYSOWANIA
class MyButtonAdapter implements ActionListener {
    MyFrame f;
    MyButtonAdapter(MyFrame f) { this.f = f; }
    public void actionPerformed(ActionEvent e) { f.action(); }
}

class MyButton extends Button {
    MyButton(MyFrame f) {
        super("Narysuj");
        addActionListener(new MyButtonAdapter(f));
    }
}

/*--------------------------------------------------------------------*/
//GLOWNA KLATKA
class MyFrame extends Frame {
    ArrayList<Label> labels;
    TextField dane;

    MyFrame() {
        super("Trojkat Pascala");
        setBounds(100,100,1280,720);
        addWindowListener(new MyWindowAdapter());
        setFont(new Font(Font.SANS_SERIF,Font.PLAIN,40));
        setLayout(new GridLayout(15,30));
        setBackground(Color.lightGray);

        MyButton akcja = new MyButton(this);
        labels = new ArrayList<>();
        dane = new TextField(30);

        add(dane);
        add(akcja);

        setResizable(true);
    }

    public void action() {
        try{
            for(Label label : labels){
                remove(label);
            }
            labels.clear();
            int n = Integer.parseInt(dane.getText())-1;
            if(n<0)
                throw new Exception("Podaj nieujemną liczbę!");
            TrojkatPascala t = new TrojkatPascala(n);
            for(String linijka : t.trojkatWiersze){
                System.out.println(linijka);
                Label label = new Label(linijka, Label.CENTER);
                labels.add(label);
                add(label);
            }
            doLayout();
        }
        catch(Exception e){
            labels.clear();
            Label label = new Label("Podaj poprawną liczbę!", Label.CENTER);
            add(label);
            labels.add(label);
            doLayout();
        }
    }
}

/*--------------------------------------------------------------------*/


public class GUI {
    public static void main(String[] args) {
        MyFrame frame = new MyFrame();
        frame.setVisible(true);
    }
}
