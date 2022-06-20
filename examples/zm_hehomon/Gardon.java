

public class Gardon extends Hehomon {

    private boolean doppelterSchaden = false;

    public Gardon() {
        super("Gardon", "Elektro", 100, 8, 6, "Gardoschlag", "Hauer", "Konzentration", "Heilung", "mia_Salamanyte.png");
    }

    @Override
    public void angriff1( Hehomon gegner ) {
        int zufall = (int)(Math.random() * angr) + 4;
        if( doppelterSchaden ) {
            zufall *= 2;
            doppelterSchaden = false;
        }
        gegner.nimmSchaden(zufall);
    }

    @Override
    public void angriff2( Hehomon gegner ) {
        if( doppelterSchaden ) {
            gegner.nimmSchaden(angr*2);
            doppelterSchaden = false;
        } else {
            gegner.nimmSchaden(angr);
        }
    }

    @Override
    public void verteidigung1( Hehomon gegner ) {
        doppelterSchaden = true;
    }

    @Override
    public void verteidigung2( Hehomon gegner ) {
        heilen(10);
        doppelterSchaden = false;
    }

}
