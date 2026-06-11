//--------------------------------------------
//-------------   Sistem Solar   -------------
//---------------   Bud Radu   ---------------
//---------   20/05/2026 11:40:31   ----------
//--------------------------------------------
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class CorpCeresc {
    private String nume;
    private double x, y;
    private int raza;
    private Color culoare;
    private double distantaOrbita;
    private double vitezaOrbita;
    private double unghiCurent;
    private java.util.List<CorpCeresc> sateliti;


    public CorpCeresc(String nume, int raza, Color culoare, double distantaOrbita, double vitezaOrbita) {
        this.nume = nume;
        this.raza = raza;
        this.culoare = culoare;
        this.distantaOrbita = distantaOrbita;
        this.vitezaOrbita = vitezaOrbita;
        this.unghiCurent = 0;
        this.sateliti = new ArrayList<>();
    }

    @Override
    public String toString() {
        return nume;
    }

    public List<CorpCeresc> getSateliti() {
        return sateliti;
    }

    public double getDistantaOrbita() {
        return distantaOrbita;
    }

    public void setDistantaOrbita(double distantaOrbita) {
        this.distantaOrbita = distantaOrbita;
    }

    public void adaugaSatelit(CorpCeresc satelit) {
        sateliti.add(satelit);
    }

    public void stergeSatelit(CorpCeresc satelit) {
        sateliti.remove(satelit);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void update(double centruX, double centruY) {
        unghiCurent += vitezaOrbita;
        this.x = centruX + distantaOrbita * Math.cos(unghiCurent);
        this.y = centruY + distantaOrbita * Math.sin(unghiCurent);

        for (CorpCeresc satelit : sateliti) {
            satelit.update(this.x, this.y);
        }
    }
    public int getRaza() {
        return raza;
    }

    public void draw(Graphics2D g2d, double centruX, double centruY) {
        if (distantaOrbita > 0) {
            g2d.setColor(new Color(50, 50, 50));
            int diametruOrbita = (int) (distantaOrbita * 2);
            g2d.drawOval((int) (centruX - distantaOrbita), (int) (centruY - distantaOrbita), diametruOrbita, diametruOrbita);
        }

        g2d.setColor(culoare);
        g2d.fillOval((int) (x - raza), (int) (y - raza), raza * 2, raza * 2);

        for (CorpCeresc satelit : sateliti) {
            satelit.draw(g2d, this.x, this.y);
        }
    }
}