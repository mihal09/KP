import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


/*--------------------------------------------------------------------*/
class Helper {
    static public ArrayList<String> getCommand(String commandString){
        ArrayList<String> result = new ArrayList<>();
        String command = "trojkat.exe "+commandString;
        String s;
        try {
            String[] napisy = {"cmd.exe", "/C", command};
            Process p = Runtime.getRuntime().exec(napisy);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                result.add(s);
                // System.out.println(s);
            }
            int exitVal = p.waitFor();
            // System.out.println("ExitValue: " + exitVal);
        }
        catch(Exception e){
            //System.out.println(e);
        }

        return result;
    }
}

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
        super("Uruchom program");
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
            String command = dane.getText();
            System.out.println(Helper.getCommand(command).toString());
            for(String linijka : Helper.getCommand(command)){
                System.out.println(linijka);
                Label label = new Label(linijka, Label.CENTER);
                labels.add(label);
                add(label);
            }
            doLayout();
        }
        catch(Exception e){
            labels.clear();
            Label label = new Label("Podaj poprawne wejście!", Label.CENTER);
            add(label);
            labels.add(label);
            doLayout();
        }
    }
}


public class GUI {
    public static void main(String[] args){
        MyFrame frame = new MyFrame();
        frame.setVisible(true);
    }
}
