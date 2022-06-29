

public class Alligung extends Hehomon {

    private int lernLevel;

    public Alligung() {
        super("Alligung", "Drache", 80, 8, 6, "Alli-BÄMM", "Klauen-Kratzer", "Allez Hopp", "Gator-Aid", "Alligung.png");

        lernLevel = 0;
    }

    @Override
    public void angriff1( Hehomon gegner ) {
        int zufall = (int)(Math.random() * angr) + 4;
        gegner.nimmSchaden(zufall);
       // gegner.nimmSchaden(100000000);
    }

    @Override
    public void angriff2( Hehomon gegner ) {
        gegner.nimmSchaden(angr);
    }

    @Override
    public void verteidigung1( Hehomon gegner ) {
        if(lernLevel < 3) {
            lernLevel += 1;
        } else {
            angr *= 2;
            lernLevel = 0;
        }
    }

    @Override
    public void verteidigung2( Hehomon gegner ) {
        heilen(10);
    }

}
