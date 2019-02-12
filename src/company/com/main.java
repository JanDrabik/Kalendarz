package company.com;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class main {
    static JLabel lblMiesiac, lblRok;
    static JButton poprzedni, nastepny;
    static JTable tblKalendarz;
    static JComboBox comboRok;
    static JFrame frmMain;
    static Container pane;
    static DefaultTableModel mtblKalendarz;
    static JScrollPane stblKalendarz;
    static JPanel pnlKalendarz;
    static int obecnyRok, obecnyMiesiac, obecnyDzien, tenRok, tenMiesiac;

    public static void main (String args[]){

        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}

        //Ramka
        frmMain = new JFrame ("Kalendarz");
        frmMain.setSize(330, 375);
        pane = frmMain.getContentPane();
        pane.setLayout(null);
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Przyciski
        lblMiesiac = new JLabel ("Styczeń");
        lblRok = new JLabel ("Wybierz rok:");
        comboRok = new JComboBox();
        poprzedni = new JButton ("←");
        nastepny = new JButton ("→");
        mtblKalendarz = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
        tblKalendarz = new JTable(mtblKalendarz);
        stblKalendarz = new JScrollPane(tblKalendarz);
        pnlKalendarz = new JPanel(null);

        //Obramowanie
        pnlKalendarz.setBorder(BorderFactory.createTitledBorder("Program zaliczeniowy: Jan Drabik"));

        //Wciskanie guzikow
        poprzedni.addActionListener(new poprzedni_Action());
        nastepny.addActionListener(new nastepny_Action());
        comboRok.addActionListener(new comboRok_Action());

        pane.add(pnlKalendarz);
        pnlKalendarz.add(lblMiesiac);
        pnlKalendarz.add(lblRok);
        pnlKalendarz.add(comboRok);
        pnlKalendarz.add(poprzedni);
        pnlKalendarz.add(nastepny);
        pnlKalendarz.add(stblKalendarz);

        //Wielkosc przyciskow i tabeli
        pnlKalendarz.setBounds(0, 0, 320, 335);
        lblMiesiac.setBounds(160-lblMiesiac.getPreferredSize().width/2, 25, 100, 25);
        lblRok.setBounds(10, 305, 80, 20);
        comboRok.setBounds(230, 305, 80, 20);
        poprzedni.setBounds(10, 25, 50, 25);
        nastepny.setBounds(260, 25, 50, 25);
        stblKalendarz.setBounds(10, 50, 300, 250);
        frmMain.setResizable(false);
        frmMain.setVisible(true);

        //Obliczanie obecnej daty
        GregorianCalendar cal = new GregorianCalendar(); //Tworze kalendarz
        obecnyDzien = cal.get(GregorianCalendar.DAY_OF_MONTH);
        obecnyMiesiac = cal.get(GregorianCalendar.MONTH);
        obecnyRok = cal.get(GregorianCalendar.YEAR);
        tenMiesiac = obecnyMiesiac;
        tenRok = obecnyRok;

        //Naglowki
        String[] headers = {"Pon.", "Wto.", "Śro.", "Czw.", "Pt.", "Sob.", "Ndz."};
        for (int i=0; i<7; i++){
            mtblKalendarz.addColumn(headers[i]);
        }

        tblKalendarz.getParent().setBackground(tblKalendarz.getBackground());

        //Wylaczenie zmiany wielkosci tabeli
        tblKalendarz.getTableHeader().setResizingAllowed(false);
        tblKalendarz.getTableHeader().setReorderingAllowed(false);

        //Wybieranie pojedynczej komorki
        tblKalendarz.setColumnSelectionAllowed(true);
        tblKalendarz.setRowSelectionAllowed(true);
        tblKalendarz.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Ustawienie ilosci kolumn i wierszy
        tblKalendarz.setRowHeight(38);
        mtblKalendarz.setColumnCount(7);
        mtblKalendarz.setRowCount(6);

        //Zapelnienie tabeli
        for (int i=obecnyRok-100; i<=obecnyRok+100; i++){
            comboRok.addItem(String.valueOf(i));
        }

        //Na koniec odswiez kalendarz
        odswiezKalendarz (obecnyMiesiac, obecnyRok);
    }

    private static void odswiezKalendarz(int month, int year){

        String[] months =  {"Styczeń","Luty","Marzec","Kwiecień","Maj","Czerwiec","Lipiec","Sierpień","Wrzesień","Październik","Listopad","Grudzień"};
        int iloscDni, poczatekMiesiaca;

        //Wl/Wyl przyciski
        poprzedni.setEnabled(true);
        nastepny.setEnabled(true);
        if (month == 0 && year <= obecnyRok-10){poprzedni.setEnabled(false);} //Wychodzi przed
        if (month == 11 && year >= obecnyRok+100){nastepny.setEnabled(false);} //Wychodzi za
        lblMiesiac.setText(months[month]); //Odswiezanie etykiety miesiaca
        lblMiesiac.setBounds(160-lblMiesiac.getPreferredSize().width/2, 25, 180, 25); //Odswiezenie ram
        comboRok.setSelectedItem(String.valueOf(year)); //Wybranie obecnego roku z comboboxu

        //Czyszczenie tabeli
        for (int i=0; i<6; i++){
            for (int j=0; j<7; j++){
                mtblKalendarz.setValueAt(null, i, j);
            }
        }

        //Ustawienie pierszego dnia i ilosci dni w miesiacu
        GregorianCalendar cal = new GregorianCalendar(year, month, 1);
        iloscDni = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        poczatekMiesiaca = cal.get(GregorianCalendar.DAY_OF_WEEK);

        //Rysowanie kalendarza
        for (int i=1; i<=iloscDni; i++){
            int row = new Integer((i+poczatekMiesiaca-2)/7);
            int column  =  (i+poczatekMiesiaca-2)%7;
            mtblKalendarz.setValueAt(i, row, column);
        }

        //Wyrenderuj
        tblKalendarz.setDefaultRenderer(tblKalendarz.getColumnClass(0), new tblKalendarzRenderer());
    }

    static class tblKalendarzRenderer extends DefaultTableCellRenderer{
        public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            if (column == 5 || column == 6){ //Weekend
                setBackground(new Color(255, 249, 168));
            }
            else{ //Tydzien
                setBackground(new Color(255, 255, 255));
            }
            if (value != null){
                if (Integer.parseInt(value.toString()) == obecnyDzien && tenMiesiac == obecnyMiesiac && tenRok == obecnyRok){ //Dzis
                    setBackground(new Color(220, 220, 255));
                }
            }
            setBorder(null);
            setForeground(Color.black);
            return this;
        }
    }

    static class poprzedni_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if (tenMiesiac == 0){ //Rok wstecz
                tenMiesiac = 11;
                tenRok -= 1;
            }
            else{ //Miesiac wstecz
                tenMiesiac -= 1;
            }
            odswiezKalendarz(tenMiesiac, tenRok);
        }
    }
    static class nastepny_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if (tenMiesiac == 11){ //Rok do przodu
                tenMiesiac = 0;
                tenRok += 1;
            }
            else{ //Miesiac do przodu
                tenMiesiac += 1;
            }
            odswiezKalendarz(tenMiesiac, tenRok);
        }
    }
    static class comboRok_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if (comboRok.getSelectedItem() != null){
                String b = comboRok.getSelectedItem().toString();
                tenRok = Integer.parseInt(b);
                odswiezKalendarz(tenMiesiac, tenRok);
            }
        }
    }
}