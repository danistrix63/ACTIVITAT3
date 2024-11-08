import java.io.*;
import java.util.ArrayList;

/**
 * Clase para gestionar la escritura y lectura de encargos en archivos serializables y en acceso aleatorio.
 */
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
}
