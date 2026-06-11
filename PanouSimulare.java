//--------------------------------------------
//-------------   Sistem Solar   -------------
//---------------   Bud Radu   ---------------
//---------   20/05/2026 11:40:31   ----------
//--------------------------------------------
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class PanouSimulare extends JPanel implements ActionListener {
    private CorpCeresc soare;
    private Timer timer;

    public PanouSimulare() {
        setBackground(Color.BLACK);

        soare = new CorpCeresc("Soare", 40, Color.YELLOW, 0, 0);
        CorpCeresc pamant = new CorpCeresc("Pământ", 15, Color.BLUE, 150, 0.015);
        CorpCeresc luna = new CorpCeresc("Lună", 5, Color.LIGHT_GRAY, 35, 0.06);

        pamant.adaugaSatelit(luna);
        soare.adaugaSatelit(pamant);

        timer = new Timer(16, this);
        timer.start();
    }

    public CorpCeresc getSoare() {
        return soare;
    }

    public java.util.List<CorpCeresc> getPlanete() {
        if (soare != null) {
            return soare.getSateliti();
        }
        return new ArrayList<>();
    }

    public void adaugaPlanetaLaSoare(CorpCeresc planeta) {
        if (soare != null) {
            soare.adaugaSatelit(planeta);
        }
    }
    public void stergePlaneta(CorpCeresc planeta) {
        if (soare != null) {
            List<CorpCeresc> planete = getPlanete();

            planete.sort((p1, p2) -> Double.compare(p1.getDistantaOrbita(), p2.getDistantaOrbita()));

            int index = planete.indexOf(planeta);
            if (index != -1) {
                double distantaAnterioara = (index == 0) ? 0 : planete.get(index - 1).getDistantaOrbita();
                double spatiuEliberat = planeta.getDistantaOrbita() - distantaAnterioara;
                for (int i = index + 1; i < planete.size(); i++) {
                    CorpCeresc planetaExterioara = planete.get(i);
                    double distantaNoua = planetaExterioara.getDistantaOrbita() - spatiuEliberat;
                    planetaExterioara.setDistantaOrbita(distantaNoua);
                }
            }
            soare.stergeSatelit(planeta);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double centruX = getWidth() / 2.0;
        double centruY = getHeight() / 2.0;

        if (soare != null) {
            soare.update(centruX, centruY);
            soare.draw(g2d, centruX, centruY);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}

