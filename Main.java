//--------------------------------------------
//-------------   Sistem Solar   -------------
//---------------   Bud Radu   ---------------
//---------   20/05/2026 11:40:31   ----------
//--------------------------------------------
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private JPanel panel1;
    private JPanel panouSimulareUI;
    private JButton btnAdauga;
    private JButton btnSterge;
    private JPanel Butoane;
    private JScrollPane scrollTabel;
    private JTable tabelInformatii;
    private DefaultTableModel modelTabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sistem solar");
            Main aplicatie = new Main();
            frame.setContentPane(aplicatie.panel1);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public Main() {
        String[] coloane = {"Planeta", "Satelit", "Coordonate fata de Soare"};
        modelTabel = new DefaultTableModel(coloane, 0) {};
        tabelInformatii.setModel(modelTabel);
        tabelInformatii.getTableHeader().setReorderingAllowed(false);

        scrollTabel.setBorder(BorderFactory.createEmptyBorder());
        scrollTabel.setOpaque(false);
        scrollTabel.getViewport().setOpaque(false);
        scrollTabel.setBackground(new Color(0, 0, 0, 0));

        tabelInformatii.setForeground(Color.WHITE);
        tabelInformatii.setGridColor(new Color(150, 150, 150, 100));
        tabelInformatii.setOpaque(false);
        tabelInformatii.setBackground(new Color(0, 0, 0, 0));
        ((javax.swing.table.DefaultTableCellRenderer)tabelInformatii.getDefaultRenderer(Object.class)).setOpaque(false);

        tabelInformatii.getTableHeader().setOpaque(false);
        tabelInformatii.getTableHeader().setBackground(new Color(0, 0, 0, 100));
        tabelInformatii.getTableHeader().setForeground(Color.WHITE);

        // =========================================================
        // --- TIMER PENTRU ACTUALIZAREA TABELULUI ---
        // =========================================================
        Timer timerTabel = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizeazaDateTabel();
            }
        });
        timerTabel.start();

        btnAdauga.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanouSimulare simulare = (PanouSimulare) panouSimulareUI;
                CorpCeresc soare = simulare.getSoare();
                List<CorpCeresc> planete = simulare.getPlanete();

                List<CorpCeresc> optiuniParinte = new ArrayList<>();
                if (soare != null) optiuniParinte.add(soare);
                optiuniParinte.addAll(planete);

                JComboBox<CorpCeresc> comboParinte = new JComboBox<>(optiuniParinte.toArray(new CorpCeresc[0]));
                JTextField txtNume = new JTextField("Planeta");
                JTextField txtRaza = new JTextField("10");
                JTextField txtDistanta = new JTextField("230");
                JTextField txtViteza = new JTextField("0.01");

                Object[] formular = {
                        "Orbiteaza în jurul:", comboParinte,
                        "Nume corp ceresc:", txtNume,
                        "Raza:", txtRaza,
                        "Distanta fata de soare:", txtDistanta,
                        "Viteza de orbitare:", txtViteza
                };

                int raspuns = JOptionPane.showConfirmDialog(panel1, formular, "Adauga Corp Ceresc", JOptionPane.OK_CANCEL_OPTION);

                if (raspuns == JOptionPane.OK_OPTION) {
                    try {
                        String nume = txtNume.getText().trim();
                        int raza = Integer.parseInt(txtRaza.getText());
                        CorpCeresc parinteAles = (CorpCeresc) comboParinte.getSelectedItem();
                        boolean estePlaneta = (parinteAles == soare);

                        if (estePlaneta && raza > 36) {
                            JOptionPane.showMessageDialog(panel1, "Raza planetei nu poate fi mai mare de 36", "Eroare", JOptionPane.WARNING_MESSAGE);
                            return;
                        } else if (!estePlaneta) {
                            if (raza > 10) {
                                JOptionPane.showMessageDialog(panel1, "Raza unui satelit nu poate depasi 10", "Eroare", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            if (raza >= parinteAles.getRaza()) {
                                JOptionPane.showMessageDialog(panel1, "Satelitul (" + raza + ") nu poate fi mai mare sau egal cu planeta sa (" + parinteAles.getRaza() + ")!", "Eroare Logică", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }

                        double distanta = Double.parseDouble(txtDistanta.getText());
                        double viteza = Double.parseDouble(txtViteza.getText());

                        if (estePlaneta) {
                            if (distanta < 80) {
                                JOptionPane.showMessageDialog(panel1, "Planeta e prea aproape de Soare Introdu o distanță de minim 80", "Prea aproape", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            for (CorpCeresc p : planete) {
                                if (p.toString().equalsIgnoreCase(nume)) {
                                    JOptionPane.showMessageDialog(panel1, "Exista deja o planeta cu nume asemanator", "Nume duplicat", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                if (Math.abs(p.getDistantaOrbita() - distanta) < 80) {
                                    JOptionPane.showMessageDialog(panel1, "Orbita prea aproape de planeta '" + p.toString() + "'!\nLasa un spatiu de minim 80 intre planete.", "Coliziune", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                            }
                        } else {
                            if (distanta > 20) {
                                JOptionPane.showMessageDialog(panel1, "Satelitul e prea departe de planeta (max 20)\nRisc de ciocnire cu alte planete.", "Orbita corupta", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            for (CorpCeresc s : parinteAles.getSateliti()) {
                                if (s.toString().equalsIgnoreCase(nume)) {
                                    JOptionPane.showMessageDialog(panel1, "Exista deja un satelit cu nume asemanator", "Nume duplicat", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                double distantaNecesara = raza + s.getRaza() + 2.0;
                                if (Math.abs(s.getDistantaOrbita() - distanta) < distantaNecesara) {
                                    JOptionPane.showMessageDialog(panel1, "Traiectoria se intersecteaza cu satelitul '" + s.toString() + "'!\nSchimba distanta cu minim " + (int)distantaNecesara + " unitati.", "Coliziune Sateliti", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                            }
                        }

                        Color culoareRandom = new Color((int)(Math.random() * 0x1000000));
                        CorpCeresc corpNou = new CorpCeresc(nume, raza, culoareRandom, distanta, viteza);
                        parinteAles.adaugaSatelit(corpNou);

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(panel1, "Introdu numere", "Eroare date", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnSterge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanouSimulare simulare = (PanouSimulare) panouSimulareUI;
                List<CorpCeresc> planete = simulare.getPlanete();

                if (planete.isEmpty()) {
                    JOptionPane.showMessageDialog(panel1, "Nu exista planete de sters", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                CorpCeresc[] arrayPlanete = planete.toArray(new CorpCeresc[0]);
                CorpCeresc planetaAleasa = (CorpCeresc) JOptionPane.showInputDialog(
                        panel1,
                        "Alege planeta:",
                        "Sterge planeta",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        arrayPlanete,
                        arrayPlanete[0]
                );

                if (planetaAleasa != null) {
                    simulare.stergePlaneta(planetaAleasa);
                }
            }
        });
    }

    // =========================================================
    // --- METODA DE ACTUALIZARE A TABELULUI ---
    // =========================================================
    private void actualizeazaDateTabel() {
        PanouSimulare simulare = (PanouSimulare) panouSimulareUI;
        List<CorpCeresc> planete = simulare.getPlanete();

        double centruX = simulare.getWidth() / 2.0;
        double centruY = simulare.getHeight() / 2.0;

        modelTabel.setRowCount(0);

        for (CorpCeresc p : planete) {
            int pX = (int) (p.getX() - centruX);
            int pY = (int) (p.getY() - centruY);
            String coordPlaneta = "X: " + pX + ", Y: " + pY;

            String textSateliti = "-";
            List<CorpCeresc> sateliti = p.getSateliti();

            if (!sateliti.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < sateliti.size(); i++) {
                    sb.append(sateliti.get(i).toString());
                    if (i < sateliti.size() - 1) {
                        sb.append(", ");
                    }
                }
                textSateliti = sb.toString();
            }

            modelTabel.addRow(new Object[]{p.toString(), textSateliti, coordPlaneta});
        }
    }

    private void createUIComponents() {
        panouSimulareUI = new PanouSimulare();
    }

}