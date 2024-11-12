import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;

public class GestorEncarrecs {

    public static void llegirXMLDOM(String filename) {
        try {
            // Configuración del parser DOM
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filename));

            // Normalizar el documento XML
            doc.getDocumentElement().normalize();

            // Obtener todos los elementos "encarrec"
            NodeList encarrecsList = doc.getElementsByTagName("encarrec");

            // Recorrer cada encargo y mostrar los datos
            for (int i = 0; i < encarrecsList.getLength(); i++) {
                Node node = encarrecsList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element encElement = (Element) node;

                    String id = encElement.getAttribute("id");
                    String client = encElement.getElementsByTagName("client").item(0).getTextContent();
                    String telefon = encElement.getElementsByTagName("telefon").item(0).getTextContent();
                    String dataEncàrrec = encElement.getElementsByTagName("dataEncàrrec").item(0).getTextContent();

                    System.out.println("Encàrrec ID: " + id);
                    System.out.println("Client: " + client);
                    System.out.println("Telèfon: " + telefon);
                    System.out.println("Data Encàrrec: " + dataEncàrrec);
                    System.out.println("-------------------------");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al llegir XML amb DOM: " + e.getMessage());
        }
    }
}
