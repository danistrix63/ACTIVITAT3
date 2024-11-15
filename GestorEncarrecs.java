import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.util.ArrayList;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class GestorEncarrecs {
     // Método para escribir la lista de encàrrecs en un archivo serializable
    public static void guardarEncàrrecs(ArrayList<Encarrec> encarrecs, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(encarrecs);
            System.out.println("Encàrrecs guardados correctamente en el archivo: " + filename);
        } catch (IOException e) {
            System.err.println("Error al guardar los encàrrecs: " + e.getMessage());
        }
    }

    // Método para leer la lista de encàrrecs desde un archivo serializable
    @SuppressWarnings("unchecked")
    public static ArrayList<Encarrec> cargarEncàrrecs(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            ArrayList<Encarrec> encarrecs = (ArrayList<Encarrec>) ois.readObject();
            System.out.println("Encàrrecs cargados correctamente desde el archivo: " + filename);
            return encarrecs;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar los encàrrecs: " + e.getMessage());
            return new ArrayList<>(); // Retorna una lista vacía en caso de error
        }
    }

    // Método para escribir en un archivo de acceso aleatorio
    public static void guardarEncarrecAleatorio(Encarrec encarrecs, String filename) {
        try (RandomAccessFile raf = new RandomAccessFile(filename, "rw")) {
            raf.seek(raf.length());
            long recordStart = raf.getFilePointer(); // Guardar inicio del registro

            // Escritura de los campos
            raf.writeInt(encarrecs.getId());
            raf.writeUTF(encarrecs.getNomClient());
            raf.writeUTF(encarrecs.getTelefon());
            raf.writeUTF(encarrecs.getDataEncàrrec());
            raf.writeFloat(encarrecs.getPreuTotal());

            for (Article article : encarrecs.getArticles()) {
                raf.writeUTF(article.getNom());
                raf.writeFloat(article.getQuantitat());
                raf.writeUTF(article.getTipusUnitat());
                raf.writeFloat(article.getPreu());
            }
            raf.writeUTF("/");  // Delimitador para el final de los artículos

            long recordEnd = raf.getFilePointer();
            int recordLength = (int) (recordEnd - recordStart); // Calcular longitud del registro
            raf.writeInt(recordLength);  // Guardar longitud al final

            System.out.println("Encàrrec guardado en modo aleatorio en el archivo: " + filename);
        } catch (IOException e) {
            System.err.println("Error al guardar en modo aleatorio: " + e.getMessage());
        }
    }

    // Método para leer un encargo de un archivo de acceso aleatorio por ID
    public static Encarrec cargarEncarrecAleatorio(String filename, int idBuscado) {
        try (RandomAccessFile raf = new RandomAccessFile(filename, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                int id = raf.readInt();  // Lee el ID del encargo

                // Verificar si el ID leído coincide con el ID buscado
                if (id == idBuscado) {
                    // Si el ID coincide, leer el resto de los datos del encargo
                    String nomClient = raf.readUTF();
                    String telefon = raf.readUTF();
                    String dataEncàrrec = raf.readUTF();
                    float preuTotal = raf.readFloat();

                    ArrayList<Article> articles = new ArrayList<>();
                    String nomArticle;
                    while (!(nomArticle = raf.readUTF()).equals("/")) {
                        float quantitat = raf.readFloat();
                        String tipusUnitat = raf.readUTF();
                        float preu = raf.readFloat();
                        articles.add(new Article(nomArticle, quantitat, tipusUnitat, preu));
                    }

                    // Crear el objeto Encarrec con los datos leídos
                    Encarrec encarrecs = new Encarrec(id, nomClient, telefon, dataEncàrrec);
                    encarrecs.setArticles(articles);  // Agregar los artículos al encargo
                    return encarrecs;  // Devolver el encargo encontrado
                } else {
                    // Si el ID no coincide, saltar al siguiente registro
                    raf.readUTF();  // Leer y saltar el nomClient
                    raf.readUTF();  // Leer y saltar el telefon
                    raf.readUTF();  // Leer y saltar el dataEncàrrec
                    raf.readFloat();  // Leer y saltar el preuTotal

                    // Saltar los artículos hasta el delimitador "/"
                    while (!raf.readUTF().equals("/")) {
                        raf.readFloat();  // Saltar cantidad
                        raf.readUTF();    // Saltar tipusUnitat
                        raf.readFloat();  // Saltar preu
                    }

                    raf.readInt();  // Saltar la longitud del registro
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar en modo aleatorio: " + e.getMessage());
        }
        return null; // Si no se encuentra el encargo
    }

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

                    // Mostrar los datos básicos del encargo
                    System.out.println("Encàrrec ID: " + id);
                    System.out.println("Client: " + client);
                    System.out.println("Telèfon: " + telefon);
                    System.out.println("Data Encàrrec: " + dataEncàrrec);

                    // Leer los artículos
                    NodeList articlesList = encElement.getElementsByTagName("article");
                    for (int j = 0; j < articlesList.getLength(); j++) {
                        Node articleNode = articlesList.item(j);
                        if (articleNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element articleElement = (Element) articleNode;

                            String articleName = articleElement.getElementsByTagName("nom").item(0).getTextContent();
                            String quantity = articleElement.getElementsByTagName("quantitat").item(0).getTextContent();
                            String unitType = articleElement.getElementsByTagName("tipusUnitat").item(0).getTextContent();
                            String price = articleElement.getElementsByTagName("preu").item(0).getTextContent();

                            // Mostrar los datos de cada artículo
                            System.out.println("  Article: " + articleName);
                            System.out.println("  Quantitat: " + quantity);
                            System.out.println("  Tipus Unit: " + unitType);
                            System.out.println("  Preu: " + price);
                        }
                    }
                    System.out.println("-------------------------");
                }
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
