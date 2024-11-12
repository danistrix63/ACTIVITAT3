import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Encarrec> encarrecs = new ArrayList<>();
        String filename = System.currentTimeMillis() + "_encarrecs.dat";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean continuar = true;

        int idCounter = 1;

        while (continuar) {
            System.out.println("Selecciona una opció:");
            System.out.println("1: Generar un nou encàrrec");
            System.out.println("2: Modificar un encàrrec");
            System.out.println("3: Mostrar encàrrecs");
            System.out.println("4: Generar XML amb DOM");
            System.out.println("5: Llegir XML amb DOM");
            System.out.println("6: Llegir XML amb SAX");
            System.out.println("7: Sortir");

            try {
                String opcio = reader.readLine();

                switch (opcio) {
                    case "1":
                        Encarrec encarrecsNou = crearEncarrec(reader, idCounter++);
                        encarrecs.add(encarrecsNou);
                        System.out.println("Nou encàrrec afegit.");
                        break;

                    case "2":
                        System.out.print("Introdueix el nom de l'arxiu: ");
                        String nomArxiu = reader.readLine();
                        System.out.print("Introdueix la ID de l'encàrrec a modificar: ");
                        int id = Integer.parseInt(reader.readLine());

                        ArrayList<Encarrec> encarrecsExistents = GestorEncarrecs.cargarEncàrrecs(nomArxiu);
                        Encarrec encarrecsModificat = buscarEncarrecPorId(encarrecsExistents, id);

                        if (encarrecsModificat != null) {
                            modificarEncarrec(reader, encarrecsModificat);
                            GestorEncarrecs.guardarEncàrrecs(encarrecsExistents, nomArxiu);
                            System.out.println("Encàrrec modificat correctament.");
                        } else {
                            System.out.println("Encàrrec no trobat.");
                        }
                        break;

                    case "3":
                        System.out.print("Introdueix el nom de l'arxiu a mostrar: ");
                        String arxiuMostrar = reader.readLine();
                        ArrayList<Encarrec> encarrecsMostrar = GestorEncarrecs.cargarEncàrrecs(arxiuMostrar);
                        for (Encarrec enc : encarrecsMostrar) {
                            System.out.println(enc);
                        }
                        break;

                    case "4":
                        String xmlFilename = "encarrecs_" + System.currentTimeMillis() + ".xml";
                        GestorEncarrecs.generarXMLDOM(encarrecs, xmlFilename);
                        System.out.println("XML generat: " + xmlFilename);
                        break;

                    case "5":
                        System.out.print("Introdueix el nom del fitxer XML a llegir: ");
                        String xmlFileDOM = reader.readLine();
                        GestorEncarrecs.llegirXMLDOM(xmlFileDOM);
                        break;

                    case "6":
                        System.out.print("Introdueix el nom del fitxer XML a llegir amb SAX: ");
                        String xmlFileSAX = reader.readLine();
                        System.out.print("Introdueix el nom del client a buscar: ");
                        String clientNom = reader.readLine();
                        GestorEncarrecs.llegirXMLSAX(xmlFileSAX, clientNom);
                        break;

                    case "7":
                        System.out.println("Guardant tots els encàrrecs abans de sortir...");
                        GestorEncarrecs.guardarEncàrrecs(encarrecs, filename);
                        continuar = false;
                        break;

                    default:
                        System.out.println("Opció no vàlida. Torna a intentar-ho.");
                }
            } catch (IOException | NumberFormatException e) {
                System.err.println("Error d'entrada: " + e.getMessage());
            }
        }
    }
}
