import javax.swing.*;
//import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;



/*--------------------------------------------------------------------*/
//PLOTNO
class MyCanvas extends JPanel implements MouseMotionListener, MouseListener {
    private final static int dragRadius = 15;
    private final static int closeCurveRadius = 20;
    private final MyJFrame myJFrame;
    private final MyFooter myFooter;
    private ArrayList<Figura> figures;
    private FiguraProstokatna selectedFigure = null;
    private int selectedVertice = -1;
    private ActionState action = ActionState.NOTHING;
    private Point paintingPivot;
    private ArrayList<Point> paintingPoints = new ArrayList<>();
    private String selectedFigureName;
    private Color selectedColor = Color.DARK_GRAY;
    private int prevX;
    private int prevY;

    /**
     * konstruktor
     * @param myJFrame odniesienie do elementu MyJFrame
     * @param myFooter odniesienie do elementu MyFooter
     */
    MyCanvas(MyJFrame myJFrame, MyFooter myFooter){
        this.myJFrame = myJFrame;
        this.myFooter = myFooter;
        setAction(ActionState.CREATING);
        setSelectedColor(Color.BLACK);
        prevX = prevY = -1;
        selectedFigureName = "Wielokat";
        figures = new ArrayList<>();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * czyści aktualne figury i dodaje figury z podanej listy
     * @param figuresArray tablica typu Figura
     */
    private void setFigures(Figura[] figuresArray){
        figures.clear();
        if(figuresArray==null)
            return;
        figures = new ArrayList<>(Arrays.asList(figuresArray));
    }


    /**
     * @return lista aktualnych figur
     */
    private Figura[] getFigures(){
        return figures.toArray(new Figura[0]);
    }


    /**
     * @return plik wybrany przez użytkownika
     */
    private File selectFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new MyFileFilter());
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    /**
     * usuwa z listy figur wybraną figurę
     */
    void removeSelectedFigure(){
        figures.remove(selectedFigure);
        selectedFigure = null;
        myJFrame.repaint();
    }

    /**
     * podwyższa/obniża warstwę aktualnej figury
     * @param moveHigher czy podnieść figurę w górę
     */
    void changeFigureLayer(boolean moveHigher){
        int selectedIndex = figures.indexOf(selectedFigure);
        int nextIndex = moveHigher ? selectedIndex+1:selectedIndex-1;
        if(nextIndex >= 0 && nextIndex < figures.size()){
            Collections.swap(figures, selectedIndex, nextIndex);
        }
        myJFrame.repaint();
    }

    /**
     * pokazuje panel zapisu pliku
     */
    void showSavePanel(){
        File file = selectFile();
        if(file!=null) {
            setAction(ActionState.EDITING);
            FigureManager.save(file,getFigures());
            myJFrame.repaint();
        }
    }

    /**
     * pokazuje panel wczytu pliku
     */
    void showLoadPanel(){
        File file = selectFile();
        if(file!=null) {
            setAction(ActionState.EDITING);
            setFigures(FigureManager.load(file));
            selectedFigure = null;
            myJFrame.repaint();
        }
    }

    /**
     * rozpoczyna edycję
     */
    void startEditing(){
        if(getAction() == ActionState.NOTHING  ||
           getAction() == ActionState.CREATING ||
           getAction() == ActionState.SELECTING_POINTS) {
            stopDrawing(ActionState.EDITING);
        }
    }

    /**
     * rozpoczyna tworzenie figury
     */
    void startCreating(){
        if(getAction() ==ActionState.NOTHING || getAction() ==ActionState.EDITING)
            setAction(ActionState.CREATING);
    }

    /**
     * ustawia nazwę figury, która ma byćrysowana
     * @param selectedFigureName nazwa figury
     */
    void selectFigureName(String selectedFigureName){
        this.selectedFigureName = selectedFigureName;
        stopDrawing(ActionState.CREATING);
    }

    /**
     * przestaje rysowanie, dodaje narysowaną figurędo listy figur, zmienia akcjęna przyszłą akcję
     * @param newAction przyszła akcja
     */
    private void stopDrawing(ActionState newAction){
        if(selectedFigure!=null){
            if(action==ActionState.SELECTING_POINTS || action == ActionState.PAINTING)
                figures.add(selectedFigure);
        }
        if(newAction!=null)
            setAction(newAction);

//        selectedFigure = null;
        myJFrame.validate();
        myJFrame.repaint();
    }

    @Override public void mouseMoved(MouseEvent e){
        if(getAction() == ActionState.CREATING){
            myJFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            return;
        }
        else if(getAction() == ActionState.SELECTING_POINTS || getAction() ==  ActionState.PAINTING){
            myJFrame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            return;
        }
        int x = e.getX();
        int y = e.getY();
        Cursor cursor =  null;
        for (int i=figures.size()-1; i>=0;i--) {
            Figura figure = figures.get(i);
            int nearest = figure.getNearestVertice(x,y,dragRadius);
            if(nearest!=-1){ //mamy wierzcholek
                if(nearest==0)
                    cursor = new Cursor(Cursor.NW_RESIZE_CURSOR);
                else if(nearest==1)
                    cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
                else if(nearest==2)
                    cursor = new Cursor(Cursor.SW_RESIZE_CURSOR);
                else if(nearest==3)
                    cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
            }
            else if(figure.isPointInside(x,y)){
                cursor = new Cursor(Cursor.MOVE_CURSOR);
            }
            if(cursor!=null){
                break;
            }
        }
        if(cursor==null)
            cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        myJFrame.setCursor(cursor);
    }
    @Override public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(!SwingUtilities.isRightMouseButton(e)) {
            paintingPivot = new Point(x, y);
            if (getAction() == ActionState.CREATING) { //dopiero cos ma zostac narysowane
                FiguraProstokatna temporaryFigure = FiguresFabric.getFigure(selectedFigureName, paintingPivot, paintingPivot);
                temporaryFigure.setColor(getSelectedColor());
                if (temporaryFigure.getDrawingType() == DrawingType.POINT) {
                    setAction(ActionState.SELECTING_POINTS);

                    selectedFigure = temporaryFigure;
                    paintingPoints = new ArrayList<>();
                    paintingPoints.add(paintingPivot);
                } else
                    setAction(ActionState.PAINTING);
            } else if (getAction() == ActionState.PAINTING) { //aktualnie rysujemy figure
                paintingPivot = new Point(x, y);
            } else if (getAction() == ActionState.SELECTING_POINTS) { //aktualnie wybieramy punkty
                myFooter.showLabel(selectedFigure);
                if (paintingPivot.distance(paintingPoints.get(0)) <= closeCurveRadius) {
                    ((Wielokat) selectedFigure).addVertices(paintingPoints.toArray(new Point[0]));
                    stopDrawing(ActionState.EDITING);
                } else {
                    paintingPoints.add(paintingPivot);
                    ((Wielokat) selectedFigure).addVertices(paintingPoints.toArray(new Point[0]));
                    myFooter.showLabel(selectedFigure);
                }

            } else if (getAction() == ActionState.EDITING) {
                for (int i=figures.size()-1; i>=0;i--) {
                    Figura figure = figures.get(i);
                    int nearest = figure.getNearestVertice(x, y, 20);
                    if (nearest != -1) { //mamy wierzcholek
                        setAction(ActionState.RESIZING);
                        selectedVertice = nearest;
                        selectedFigure = (FiguraProstokatna) figure;
                        selectedFigure.moveVertice(x, y, selectedVertice);
                        break;
                    } else if (figure.isPointInside(x, y)) {
                        setAction(ActionState.MOVING);
                        selectedFigure = (FiguraProstokatna) figure;
                        break;
                    }
                }
            }
        }
        else{ //prawy przycisk myszy klikniety zaznacza figurę
            if(getAction() == ActionState.EDITING){
                for (int i=figures.size()-1; i>=0;i--) {
                    Figura figure = figures.get(i);
                    if (figure.isPointInside(x, y)) {
                        selectedFigure = (FiguraProstokatna) figure;
                        break;
                    }
                }
            }
        }


        myJFrame.validate();
        myJFrame.repaint();
        prevX = x;
        prevY = y;
//        System.out.println("Mouse pressed:"+x+","+y);
    }
    @Override public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e) && (getAction() == ActionState.MOVING || getAction() == ActionState.RESIZING || getAction() == ActionState.EDITING)){
            if(selectedFigure!=null){
                Color color = JColorChooser.showDialog(this, "Wybierz kolor", selectedFigure.getColor());
                if(color!=null)
                    selectedFigure.setColor(color);
                setAction(ActionState.EDITING);
            }
            myJFrame.repaint();
        }

        if(!SwingUtilities.isRightMouseButton(e)) {
            if (getAction() == ActionState.PAINTING) {
                stopDrawing(ActionState.CREATING);
            } else if (getAction() == ActionState.MOVING) {
                setAction(ActionState.EDITING);
            } else if (getAction() == ActionState.RESIZING) {
                setAction(ActionState.EDITING);
            }
        }
    }
    @Override public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        boolean shouldRepaint = false;
        if(getAction() ==ActionState.PAINTING){
            myJFrame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            Point p = new Point(x,y);
            selectedFigure = FiguresFabric.getFigure(selectedFigureName, paintingPivot, p);
            selectedFigure.setColor(getSelectedColor());
            myFooter.showLabel(selectedFigure);
            shouldRepaint=true;
        }
//        System.out.println("Mouse dragged:"+x+","+y);
//        mouseUsed(x,y);

        int dx = x - prevX;
        int dy = y - prevY;

        if(getAction() == ActionState.RESIZING){
            selectedFigure.moveVertice(x, y, selectedVertice);
            myFooter.showLabel(selectedFigure);
            shouldRepaint=true;
        }
        else if(getAction() == ActionState.MOVING){
            selectedFigure.move(dx,dy);
            myFooter.showLabel(selectedFigure);
            shouldRepaint=true;
        }

        if(shouldRepaint){
            myJFrame.validate();
            myJFrame.repaint();
        }
        prevX = x;
        prevY = y;
    }
    @Override public void mouseEntered(MouseEvent e) {System.out.println(action);}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}

    public void paintComponent(Graphics g) {
        boolean onTop = action==ActionState.PAINTING || action==ActionState.SELECTING_POINTS;
        if(!onTop){
            if(selectedFigure !=null)
                selectedFigure.draw(g);
        }
        for(Figura figure : figures){
            figure.draw(g);
        }
        if(onTop) {
            if (selectedFigure != null)
                selectedFigure.draw(g);
        }
    }

    /**
     * zwraca akcję
     * @return aktualna akcja
     */
    private ActionState getAction() {
        return action;
    }

    /**
     * ustawia akcję
     * @param action nowa akcja
     */
    private void setAction(ActionState action) {
        this.action = action;
        try {
            //czesc odpowiedzialna za stopke
            if(!(action == ActionState.RESIZING || action == ActionState.MOVING ||
                action == ActionState.PAINTING || action == ActionState.SELECTING_POINTS)) {
                myFooter.hideLabel();
                System.out.println("Chowam stopke");
            }

            //koniec stopki
            if (action == ActionState.EDITING || action == ActionState.RESIZING || action == ActionState.MOVING) {
                myJFrame.myToolbar.selectMode(RunningMode.EDITING);
            } else if (action == ActionState.CREATING || action == ActionState.PAINTING || action == ActionState.SELECTING_POINTS) {
                myJFrame.myToolbar.selectMode(RunningMode.CREATING);
            }
        }
        catch(Exception ignored){}
    }

    /**
     * zwraca aktualny kolor
     * @return aktualny kolor
     */
    private Color getSelectedColor() {
        return selectedColor;
    }

    /**
     * ustawia aktualny kolor
     * @param selectedColor nowy kolor
     */
    void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
        if(action==ActionState.EDITING){
            if(selectedFigure!=null) {
                selectedFigure.setColor(selectedColor);
            }
        }
    }
}

/*--------------------------------------------------------------------*/
//DOLNY PANEL
class MyFooter extends JPanel{
    private final JLabel selectionLabel;
    void showLabel(Figura figure){
        selectionLabel.setVisible(true);
        if(figure!=null)
            selectionLabel.setText(figure.toString());
        else
            selectionLabel.setText("");
        repaint();
    }
    void hideLabel(){selectionLabel.setVisible(false);  repaint();}
    MyFooter(){
        setLayout(new GridLayout(2,3));
        selectionLabel = new JLabel("TEST");
        selectionLabel.setVerticalAlignment(SwingConstants.CENTER);
        setBackground(MyToolbar.backgroundColor);
        add(selectionLabel);
        setVisible(true);
    }
}
/*--------------------------------------------------------------------*/
//GORNY PANEL
class MyToolbar extends JPanel{
    private final MyJFrame myJFrame;
    private final JButton bDraw;
    private final JButton bEdit;
    private final JButton bProstokat;
    private final JButton bWielokat;
    private final JButton bKolo;
    private final JButton bColor;
    private final JPanel editingPanel;
    private final JPanel figuresPanel;
    final static Color
            backgroundColor = new Color(220, 220, 220),
            activeColor = new Color(192,194, 196),
            unactiveColor = new Color(243,245, 247);
    void selectMode(RunningMode mode){
        bEdit.setBackground(unactiveColor);
        bDraw.setBackground(unactiveColor);
        switch(mode){
            case CREATING:{
                bDraw.setBackground(activeColor);
                figuresPanel.setVisible(true);
                editingPanel.setVisible(false);
                break;
            }
            case EDITING:{
                bEdit.setBackground(activeColor);
                figuresPanel.setVisible(false);
                editingPanel.setVisible(true);
                break;
            }
        }

        myJFrame.validate();
    }
    MyToolbar(MyJFrame myJFrame, MyCanvas myCanvas) {
        this.myJFrame = myJFrame;
        setLayout(new GridLayout(2,1));
        bDraw = new JButton("Tryb rysowania");
        bEdit = new JButton("Tryb edycji");
        JButton bInfo = new JButton("Info");
        JButton bBorders = new JButton("Przełącz obramowania");
        JButton bUp = new JButton("Do góry");
        JButton bDown = new JButton("W dół");
        JButton bDelete = new JButton("Usuń");
        JButton bSave = new JButton("Zapisz");
        JButton bLoad = new JButton("Wczytaj");
        bProstokat = new JButton("Prostokat");
        bWielokat = new JButton("Wielokat");
        bKolo = new JButton("Kolo");
        bColor = new JButton("Zmień kolor");
        JPanel buttonsPanel = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        figuresPanel = new JPanel();
        editingPanel = new JPanel();
        JPanel filePanel = new JPanel();
        figuresPanel.setVisible(false);


        bDraw.addActionListener(e -> {
            selectMode(RunningMode.CREATING);
            myCanvas.startCreating();
        });
        bEdit.addActionListener(e -> {
            selectMode(RunningMode.EDITING);
            myCanvas.startEditing();
        });
        bInfo.addActionListener(e -> myJFrame.showInfo());
        bBorders.addActionListener(e -> {
            FiguraProstokatna.paintBorders = !FiguraProstokatna.paintBorders;
            myJFrame.repaint();
        });
        bDown.addActionListener(e -> myCanvas.changeFigureLayer(false));
        bUp.addActionListener(e -> myCanvas.changeFigureLayer(true));
        bDelete.addActionListener(e -> myCanvas.removeSelectedFigure());
        bProstokat.addActionListener(e -> {
            myCanvas.selectFigureName("Prostokat");
            bKolo.setBackground(unactiveColor);
            bWielokat.setBackground(unactiveColor);
            bProstokat.setBackground(unactiveColor);

            bProstokat.setBackground(activeColor);
        });
        bKolo.addActionListener(e -> {
            myCanvas.selectFigureName("Kolo");
            bKolo.setBackground(unactiveColor);
            bWielokat.setBackground(unactiveColor);
            bProstokat.setBackground(unactiveColor);

            bKolo.setBackground(activeColor);
        });
        bWielokat.addActionListener(e -> {
            myCanvas.selectFigureName("Wielokat");
            bKolo.setBackground(unactiveColor);
            bWielokat.setBackground(unactiveColor);
            bProstokat.setBackground(unactiveColor);

            bWielokat.setBackground(activeColor);
        });
        bSave.addActionListener(e -> myCanvas.showSavePanel());
        bLoad.addActionListener(e -> myCanvas.showLoadPanel());
        bColor.addActionListener(e ->{
            Color c = JColorChooser.showDialog(this, "Wybierz kolor", bColor.getBackground());
            if(c!=null) {
                bColor.setBackground(c);
                myCanvas.setSelectedColor(c);
                myJFrame.repaint();
            }
        });




        figuresPanel.add(bProstokat);
        figuresPanel.add(bWielokat);
        figuresPanel.add(bKolo);

        editingPanel.add(bDown);
        editingPanel.add(bUp);
        editingPanel.add(bDelete);

        filePanel.add(bSave);
        filePanel.add(bLoad);

        buttonsPanel.add(bDraw);
        buttonsPanel.add(bEdit);
        buttonsPanel.add(bBorders);
        buttonsPanel.add(bInfo);

        topPanel.add(filePanel);
        topPanel.add(buttonsPanel);

        bottomPanel.add(figuresPanel);
        bottomPanel.add(editingPanel);
        bottomPanel.add(bColor);
        add(topPanel);
        add(bottomPanel);

        topPanel.setLayout(new GridLayout(1,2));
        figuresPanel.setBackground(backgroundColor);
        buttonsPanel.setBackground(backgroundColor);
        filePanel.setBackground(backgroundColor);
        topPanel.setBackground(backgroundColor);
        bottomPanel.setBackground(backgroundColor);
        editingPanel.setBackground(backgroundColor);
        setBackground(backgroundColor);
        setBorder(BorderFactory.createLineBorder(Color.black));


        setVisible(true);
    }
}

/*--------------------------------------------------------------------*/
//GLOWNA KLATKA
class MyJFrame extends JFrame{
    final MyToolbar myToolbar;
    private final MyFooter myFooter;
    void showInfo(){
        JDialog d = new JDialog(this, "Informacje", true );
        d.setLayout(new GridLayout(3,2,8,8));
        JLabel lab1 = new JLabel("Nazwa programu: FiGURU", SwingConstants.CENTER);
        JLabel lab2 = new JLabel("Autor: Michał Janik", SwingConstants.CENTER);
        JLabel lab3 = new JLabel("<html>Przeznaczenie: Rysowanie figur geometrycznych, ich skalowanie i przesuwanie. <br>Możliwość zapisu/odczytu z pliku.</html>", SwingConstants.CENTER);

        lab1.setVerticalAlignment(SwingConstants.CENTER);
        lab2.setVerticalAlignment(SwingConstants.CENTER);
        lab3.setVerticalAlignment(SwingConstants.CENTER);
        lab1.setBorder(BorderFactory.createLineBorder(Color.black));
        lab2.setBorder(BorderFactory.createLineBorder(Color.black));
        lab3.setBorder(BorderFactory.createLineBorder(Color.black));
        d.add(lab1);
        d.add(lab2);
        d.add(lab3);
        d.setSize(600, 300);
        d.setVisible(true);
    }
    MyJFrame(){
        setTitle("FiGURU");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        myFooter = new MyFooter();
        MyCanvas myCanvas = new MyCanvas(this, myFooter);
        myToolbar = new MyToolbar(this, myCanvas);

        setBounds(30, 30, 1200, 800);
        getContentPane().add(myToolbar,BorderLayout.PAGE_START);
        getContentPane().add(myFooter,BorderLayout.PAGE_END);
        getContentPane().add(myCanvas);
        setVisible(true);
    }
}
class GUI {
    public static void main(String[] args){
        JFrame window = new MyJFrame();
    }
}