import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GestorSax extends DefaultHandler {
    private boolean hasClient = false;
    private String clientName = null;
    private boolean isClient = false;

    public GestorSax(String clientName) {
        super();
        this.clientName = clientName;
        hasClient = true;

    }

    public GestorSax() {
        super();
    }

    public Boolean isClient(String name, String clientName) {
        if (hasClient) {
            if (name.equals(clientName)) return true;
        }
        return false;
    }

    @Override
    public void startDocument() {
        System.out.println("Inici del document XML");
    }

    @Override
    public void endDocument() {  
        System.out.println("Final del document XML");
    }

    @Override
    public void startElement(String uri, String nom, String nomC, Attributes atts) { 
        for (int i = 0; i < atts.getLength(); i++) {
            String nomAtribut = atts.getQName(i); // Nom de l'atribut (si l'element en té)
            String valorAtribut = atts.getValue(i);   // Valor d'aquest atribut
            System.out.printf("\t\tAtribut: %s, Valor: %s %n", nomAtribut, valorAtribut);
        }
        System.out.printf("\tPrincipi Element: %s %n", nomC);
        
    }

    @Override
    public void characters (char[] ch, int inicio, int longitud) throws SAXException {
        String info = new String (ch, inicio, longitud).trim();
            if (!info.isEmpty()) {
                System.out.printf("\tCaracters: %s %n", info);
            }
    }
}
