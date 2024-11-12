import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class GestorEncarrecs {

    public static void generarXMLDOM(ArrayList<Encarrec> encarrecs, String filename) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("encarrecs");
            doc.appendChild(root);

            for (Encarrec enc : encarrecs) {
                Element encElement = doc.createElement("encarrec");
                encElement.setAttribute("id", String.valueOf(enc.getId()));

                Element client = doc.createElement("client");
                client.appendChild(doc.createTextNode(enc.getNomClient()));
                encElement.appendChild(client);

                Element telefon = doc.createElement("telefon");
                telefon.appendChild(doc.createTextNode(enc.getTelefon()));
                encElement.appendChild(telefon);

                Element data = doc.createElement("dataEncàrrec");
                data.appendChild(doc.createTextNode(enc.getDataEncàrrec()));
                encElement.appendChild(data);

                root.appendChild(encElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));

            transformer.transform(source, result);
            System.out.println("XML generat correctament.");

        } catch (Exception e) {
            System.err.println("Error al generar XML: " + e.getMessage());
        }
    }

    public static void llegirXMLDOM(String filename) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filename));

            NodeList encarrecs = doc.getElementsByTagName("encarrec");

            for (int i = 0; i < encarrecs.getLength(); i++) {
                Element encElement = (Element) encarrecs.item(i);
                String id = encElement.getAttribute("id");
                String client = encElement.getElementsByTagName("client").item(0).getTextContent();
                String telefon = encElement.getElementsByTagName("telefon").item(0).getTextContent();
                String data = encElement.getElementsByTagName("dataEncàrrec").item(0).getTextContent();

                System.out.println("ID: " + id + ", Client: " + client + ", Telèfon: " + telefon + ", Data: " + data);
            }
        } catch (Exception e) {
            System.err.println("Error al llegir XML amb DOM: " + e.getMessage());
        }
    }

    public static void llegirXMLSAX(String filename, String clientNom) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                boolean isClient = false;
                String currentClient = "";

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    if (qName.equalsIgnoreCase("client")) {
                        isClient = true;
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) {
                    if (isClient) {
                        currentClient = new String(ch, start, length);
                        isClient = false;
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) {
                    if (qName.equalsIgnoreCase("client") && currentClient.equalsIgnoreCase(clientNom)) {
                        System.out.println("Encàrrec trobat per al client: " + clientNom);
                    }
                }
            };

            saxParser.parse(new File(filename), handler);

        } catch (Exception e) {
            System.err.println("Error al llegir XML amb SAX: " + e.getMessage());
        }
    }
}
