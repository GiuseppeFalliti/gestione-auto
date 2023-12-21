package Gestione_auto;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.prefs.Preferences;


/**
 * GUI class
 */
public class Application extends JFrame {
    private static final String WIDTH_KEY = "width";
    private static final String HEIGHT_KEY = "height";
    private static final String POS_X = "x";
    private static final String POS_Y = "y";
    private Container cp;
    private Preferences preferences;
    private Dialog dialog;
    private Auto auto;
    private JTextArea textArea;
  
    public Application(){
        super();
        cp=this.getContentPane();
        cp.setLayout(new BoxLayout(cp,BoxLayout.PAGE_AXIS));
        this.setTitle("Application");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        preferences = Preferences.userNodeForPackage(Application.class);
        int width = preferences.getInt(WIDTH_KEY, 300);
        int height = preferences.getInt(HEIGHT_KEY, 400);
        int posx = preferences.getInt(POS_X, 100);
        int posy = preferences.getInt(POS_Y, 100);
        
        this.setSize(width, height);
        this.setLocation(posx, posy);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveUserDimensions();
                System.exit(0);
            }
        });
        this.setupApp();
    }
    /**
     * parte grafica 
     */
    private void setupApp() {
        
        /**
         * crezione e personalizzazione  della textarea in cui verranno assegnati le caratteristiche dell auto.  
         */
        textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        textArea.setBackground(new Color(19, 19, 30));
        textArea.setForeground(new Color(255,255,255));
   
        JScrollPane scrollPane = new JScrollPane(textArea);
    

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(255,255,255));
        
    
        JMenu editMenu = new JMenu("Edit");
       

        /**
         * creo l item aggiungi macchina in cui creo l action event in cui creo un ogetto macchina e lo
         * aggiungo al textArea. 
         */
    JMenuItem addCarItem = new JMenuItem("Aggiungi Macchina");

    addCarItem.addActionListener(e -> {
        
    dialog = new JDialog(this, "Aggiungi Macchina", true);
    dialog.setLocation(this.getX(),this.getY());

    JPanel panel = new JPanel(new GridLayout(4, 2));

    //combobox per i colori
    JComboBox<String> colore;
    String[] colori = {"White", "Black", "Red", "Blue", "Green", "Yellow", "Cyan", "Gray", "Rosa", "Orange"};
    colore = new JComboBox<>(colori);
    colore.setFont(new Font("Arial", Font.BOLD, 14));
    colore.setBackground(Color.WHITE); 
    colore.setForeground(Color.BLACK);
    colore.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    JTextField matricola = new JTextField(10);
    JTextField velocita = new JTextField(10);

    panel.add(new JLabel("Colore dell'auto:"));
    panel.add(colore);
    panel.add(new JLabel("Matricola dell'auto:"));
    panel.add(matricola);
    panel.add(new JLabel("Velocità dell'auto (in km/h):"));
    panel.add(velocita);

    JButton addButton = new JButton("Aggiungi");
    addButton.addActionListener(event -> {
        String coloreString =(String) colore.getSelectedItem();
        String matricolaString = matricola.getText();
        String velocitaString = velocita.getText();
        int velocitaInt = Integer.parseInt(velocitaString);

         auto = new Auto(coloreString, matricolaString, velocitaInt);
        textArea.append("Nuova auto aggiunta: ");
        textArea.append("Colore: " +  auto.getColore());
        textArea.append(" Matricola: " +  auto.getMatricola());
        textArea.append(" Velocità: " +  auto.getVelocita() + " km/h\n");

        dialog.dispose();
    });

    panel.add(addButton);
    dialog.add(panel);
    dialog.pack();
    dialog.setVisible(true);

});
    
        /**
         * creo l item per convetire la velocita in ms e lo aggiungo alla text area.  
         */
        JMenuItem convertSpeedItem = new JMenuItem("Converti Velocità");

        convertSpeedItem.addActionListener(e -> {

            dialog = new JDialog(this,"Converti Velocità", true);
           dialog.setLocation(this.getX(),this.getY());

            JPanel panel = new JPanel(new GridLayout(3,0));

            JTextField velocita = new JTextField(10);
            panel.add(new JLabel("Inserisci velocità dell'auto (in km/h):"));
            panel.add(velocita);
            

            JButton convertiButton = new JButton("converti");

            convertiButton.addActionListener(event->{
                int velocitaInt = Integer.parseInt(velocita.getText());
            double velocitaMetriAlSecondo = velocitaInt * 1000 / 3600.0;
            JOptionPane.showMessageDialog(this,"Velocità convertita in metri al secondo: " + velocitaMetriAlSecondo + " m/s\n\n");
            
            dialog.dispose();   
            });
      
           panel.add(convertiButton);
            dialog.add(panel);
            dialog.pack();
            dialog.setVisible(true);

            
        });

        JMenuItem exportItem = new JMenuItem("esporta file CSV");

        exportItem.addActionListener(e->{
            exportCSV();

        });

         JMenuItem importItem = new JMenuItem("importa file CSV");

        importItem.addActionListener(e->{
            importCSV();

        });

        

        editMenu.add(addCarItem);
        editMenu.add(convertSpeedItem);
        editMenu.add(exportItem);
        editMenu.add(importItem);
        menuBar.add(editMenu);
        setJMenuBar(menuBar);
        cp.add(scrollPane);
    }

    public void exportCSV() {
        String filePath = "C:\\Users\\giuseppe\\Downloads\\fileCSV\\Macchina2CSV.csv"; 
    
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
    
            String velocitaString =Integer.toString(auto.getVelocita());
            bw.write(auto.getColore().toString() + "," + auto.getMatricola().toString() + "," + velocitaString);
            bw.newLine();
    
           JOptionPane.showMessageDialog(this,"File CSV creato con successo!");
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void importCSV() {
        String filePath = "C:\\Users\\giuseppe\\Downloads\\fileCSV\\Macchina2CSV.csv";
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            
            
            String line;
            while ((line = br.readLine()) != null) {
                String[] valori = line.split(",");
                
                // Verifico se ci sonoi 3 valori nella righa
                if (valori.length >= 3) {
                    String colore = "Colore: " + valori[0] + " ";
                    String matricola = "Matricola: " + valori[1] + " ";
                    String velocita = "Velocità: " + valori[2] + " ";
                    textArea.append(colore + matricola + velocita + "\n");
                } else {
                   
                    JOptionPane.showMessageDialog(this, "Formato CSV non valido.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  

    public void saveUserDimensions() {
        preferences.putInt(WIDTH_KEY, getWidth());
        preferences.putInt(HEIGHT_KEY, getHeight());
        preferences.putInt(POS_X, getX());
        preferences.putInt(POS_Y, getY());
    }

    public void startApp(boolean packElements){
        if(packElements) this.pack();
        this.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            new Application().startApp(false);
        });
    }
}
