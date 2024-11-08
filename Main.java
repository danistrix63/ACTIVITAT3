import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
/*
 * Codigo principal
 * Uso de Terminal
 * Ejecutable
 */

public class Main {

    public static void main(String[] args) {
        ArrayList<Encarrec> encarrecs = new ArrayList<>();
        String filename = System.currentTimeMillis() + "_encarrecs.dat";  // Nombre único de archivo
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean continuar = true;

        int idCounter = 1; // ID secuencial para cada encargo

        while (continuar) {
            System.out.println("Selecciona una opció:");
            System.out.println("1: Generar un nou encàrrec");
            System.out.println("2: Modificar un encàrrec");
            System.out.println("3: Mostrar encàrrecs");
            System.out.println("4: Sortir");

            try {
                String opcio = reader.readLine();

                switch (opcio) {
                    case "1": // Generar un nuevo encargo
                        Encarrec encarrecsNou = crearEncarrec(reader, idCounter++);
                        encarrecs.add(encarrecsNou);
                        System.out.println("Nou encàrrec afegit.");

                        System.out.print("Desitja generar un fitxer per aquest encàrrec? (S/N): ");
                        String respuesta = reader.readLine();
                        if (respuesta.equalsIgnoreCase("S")) {
                            String individualFilename = System.currentTimeMillis() + "_encarrec_individual.dat";
                            ArrayList<Encarrec> individualEncarrecList = new ArrayList<>();
                            individualEncarrecList.add(encarrecsNou);
                            GestorEncarrecs.guardarEncàrrecs(individualEncarrecList, individualFilename);
                            System.out.println("Encàrrec guardat en el fitxer: " + individualFilename);
                        }
                        break;

                    case "2": // Modificar encargo
                        System.out.print("Introdueix el nom de l'arxiu: ");
                        String nomArxiu = reader.readLine();

                        System.out.print("Introdueix la ID de l'encàrrec a modificar: ");
                        int id = Integer.parseInt(reader.readLine());

                        ArrayList<Encarrec> encarrecsExistents = GestorEncarrecs.cargarEncàrrecs(nomArxiu);
                        Encarrec encarrecsModificat = buscarEncarrecPorId(encarrecsExistents, id);

                        if (encarrecsModificat != null) {
                            // Volver a crear el encargo con los nuevos datos
                            System.out.println("Modificant encàrrec...");
                            modificarEncarrec(reader, encarrecsModificat);
                            GestorEncarrecs.guardarEncàrrecs(encarrecsExistents, nomArxiu);
                            System.out.println("Encàrrec modificat correctament.");
                        } else {
                            System.out.println("Encàrrec no trobat.");
                        }
                        break;

                    case "3": // Mostrar encargos
                        System.out.print("Introdueix el nom de l'arxiu a mostrar: ");
                        String arxiuMostrar = reader.readLine();
                        ArrayList<Encarrec> encarrecsMostrar = GestorEncarrecs.cargarEncàrrecs(arxiuMostrar);

                        for (Encarrec enc : encarrecsMostrar) {
                            System.out.println(enc);
                        }
                        break;

                    case "4": // Salir y guardar todos los encargos
                        System.out.print("Guardant tots els encàrrecs abans de sortir...");
                        GestorEncarrecs.guardarEncàrrecs(encarrecs, filename);
                        System.out.println("Tots els encàrrecs guardats correctament.");
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

    private static Encarrec crearEncarrec(BufferedReader reader, int id) throws IOException {
        System.out.print("Introdueix el nom del client: ");
        String nomClient = reader.readLine();

        String telefon;
        while (true) {
            System.out.print("Introdueix el telèfon del client (9 dígits): ");
            telefon = reader.readLine();
            if (telefon.matches("\\d{9}")) {
                break;
            } else {
                System.out.println("Telèfon invàlid. Ha de tenir exactament 9 dígits.");
            }
        }

        String dataEncàrrec;
        while (true) {
            System.out.print("Introdueix la data de l'encàrrec (dd/mm/aaaa): ");
            dataEncàrrec = reader.readLine();
            if (dataEncàrrec.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$")) {
                String[] parts = dataEncàrrec.split("/");
                int dia = Integer.parseInt(parts[0]);
                int mes = Integer.parseInt(parts[1]);
                if (dia >= 1 && dia <= 31 && mes >= 1 && mes <= 12) {
                    break;
                } else {
                    System.out.println("Data invàlida. El dia o el mes estan fora de rang.");
                }
            } else {
                System.out.println("Format de data invàlid. Usa el format dd/mm/aaaa.");
            }
        }

        Encarrec encarrecs = new Encarrec(id, nomClient, telefon, dataEncàrrec);
        boolean afegirArticles = true;
        while (afegirArticles) {
            System.out.print("Nom de l'article: ");
            String nomArticle = reader.readLine();
            System.out.print("Quantitat de l'article: ");
            float quantitat = Float.parseFloat(reader.readLine());
            System.out.print("Tipus d'unitat (kg, g, u, etc.): ");
            String tipusUnitat = reader.readLine();
            System.out.print("Preu unitari de l'article: ");
            float preu = Float.parseFloat(reader.readLine());

            Article article = new Article(nomArticle, quantitat, tipusUnitat, preu);
            encarrecs.addArticle(article);

            System.out.print("Desitja afegir un altre article? (S/N): ");
            String resposta = reader.readLine();
            afegirArticles = resposta.equalsIgnoreCase("S");
        }
        return encarrecs;
    }

    private static Encarrec buscarEncarrecPorId(ArrayList<Encarrec> encarrecs, int id) {
        for (Encarrec enc : encarrecs) {
            if (enc.getId() == id) {
                return enc;
            }
        }
        return null;
    }

    private static void modificarEncarrec(BufferedReader reader, Encarrec encarrecs) throws IOException {
        System.out.print("Introdueix el nou nom del client: ");
        encarrecs.setNomClient(reader.readLine());

        String telefon;
        while (true) {
            System.out.print("Introdueix el nou telèfon del client (9 dígits): ");
            telefon = reader.readLine();
            if (telefon.matches("\\d{9}")) {
                encarrecs.setTelefon(telefon);
                break;
            } else {
                System.out.println("Telèfon invàlid. Ha de tenir exactament 9 dígits.");
            }
        }

        String dataEncàrrec;
        while (true) {
            System.out.print("Introdueix la nova data de l'encàrrec (dd/mm/aaaa): ");
            dataEncàrrec = reader.readLine();
            if (dataEncàrrec.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$")) {
                String[] parts = dataEncàrrec.split("/");
                int dia = Integer.parseInt(parts[0]);
                int mes = Integer.parseInt(parts[1]);
                if (dia >= 1 && dia <= 31 && mes >= 1 && mes <= 12) {
                    encarrecs.setDataEncàrrec(dataEncàrrec);
                    break;
                } else {
                    System.out.println("Data invàlida. El dia o el mes estan fora de rang.");
                }
            } else {
                System.out.println("Format de data invàlid. Usa el format dd/mm/aaaa.");
            }
        }

        boolean afegirArticles = true;
        encarrecs.getArticles().clear(); // Limpiar artículos actuales para añadir de nuevo
        while (afegirArticles) {
            System.out.print("Nom de l'article: ");
            String nomArticle = reader.readLine();
            System.out.print("Quantitat de l'article: ");
            float quantitat = Float.parseFloat(reader.readLine());
            System.out.print("Tipus d'unitat (kg, g, u, etc.): ");
            String tipusUnitat = reader.readLine();
            System.out.print("Preu unitari de l'article: ");
            float preu = Float.parseFloat(reader.readLine());

            Article article = new Article(nomArticle, quantitat, tipusUnitat, preu);
            encarrecs.addArticle(article);

            System.out.print("Desitja afegir un altre article? (S/N): ");
            String resposta = reader.readLine();
            afegirArticles = resposta.equalsIgnoreCase("S");
        }
    }
}
