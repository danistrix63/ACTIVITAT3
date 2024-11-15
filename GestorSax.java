import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GestorSax extends DefaultHandler {
    private boolean hasClient = false;
    private String clientName = null;
    private boolean isClient = false;
    private boolean foundClient = false;
    private boolean isArticle = false;

    private ArrayList<String> dataEncarrec = new ArrayList<>();
    private ArrayList<String> dataArticle = new ArrayList<>();

    private ArrayList<Article> clientArticles = new ArrayList<>();

    private Encarrec clientToSearch = null;
    private ArrayList<Encarrec> allClients = new ArrayList<>();

    private Encarrec client;

    public GestorSax(String clientName) {
        super();
        this.clientName = clientName;
        hasClient = true;

    }

    public GestorSax() {
        super();
    }

    public Boolean isClient(String name) {
        if (hasClient) {
            if (name.equals(clientName)) return true;
        }
        return false;
    }

    @Override
    public void endDocument() {
        if (hasClient) {
            if (clientToSearch != null) {
                System.out.println(clientToSearch);
                return;
            }
            System.out.println("Cliente no encontrado");
        } else {
            for (Encarrec encarrec : allClients) {
                System.out.println(encarrec);
            }
        }
    }


    @Override
    public void startElement(String uri, String nom, String nomC, Attributes atts) { 
        if (nomC.toLowerCase().equals("encarrecs") || nomC.toLowerCase().equals("articles")) return;
        for (int i = 0; i < atts.getLength(); i++) { // Nom de l'atribut (si l'element en té)
            String idEncarrec = atts.getValue(i);
            dataEncarrec.add(idEncarrec);   // Valor d'aquest atribut
        }
        
        if (nomC.toLowerCase().equals("client")) isClient = true;

        if (nomC.toLowerCase().equals("article")) isArticle = true;
        
        
        
    }

    @Override
    public void characters (char[] ch, int inicio, int longitud) throws SAXException {
        String info = new String (ch, inicio, longitud).trim();
            if (hasClient && isClient) {
                if (info.equals(clientName)) foundClient = true;

            }
            if (isArticle) {
                if (!(info.isEmpty())) dataArticle.add(info);
            } else {
                if (!(info.isEmpty())) dataEncarrec.add(info);
            }
    }

    @Override
    public void endElement(String uri, String nom, String nomC) {

        if (nomC.toLowerCase().equals("article")) {
            clientArticles.add(new Article(dataArticle.get(0), Float.parseFloat(dataArticle.get(1)), dataArticle.get(2), Float.parseFloat(dataArticle.get(3))));
            isArticle = false;
            dataArticle.clear();
        }

        if (nomC.toLowerCase().equals("encarrec")) {
            client = new Encarrec(Integer.parseInt(dataEncarrec.get(0)), dataEncarrec.get(1), dataEncarrec.get(2), dataEncarrec.get(3));
            client.setArticles(clientArticles);
            if (foundClient) {
                clientToSearch = client;
                foundClient = false;
            }

            allClients.add(client);
            dataEncarrec.clear();
            
        }

    }
}
