
import schule.ngb.zm.Zeichenmaschine;

import java.util.List;

/**
 * Beschreiben Sie hier die Klasse BluejTest.
 *
 * @author (Ihr Name)
 * @version (eine Versionsnummer oder ein Datum)
 */
public class BluejTest extends Zeichenmaschine
{
    public void setup() {
        if( !IN_BLUEJ ) {
            drawing.clear(schule.ngb.zm.Color.HGRED);
        } else {
            drawing.clear(schule.ngb.zm.Color.HGGREEN);
            
        }
    }

    public void listClasses() {
        // find all classes in classpath
        List<String> allClasses = ClasspathInspector.getAllKnownClassNames();
        System.out.printf("There are %s classes available in the classpath\n", allClasses.size());

        for (String clazz : allClasses) {
            if( clazz.contains("Boot") || clazz.contains("Main") ) {
                System.out.printf("%s\n", clazz);
            }
        }
    }
}
