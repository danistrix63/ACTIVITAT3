
### Opcions explicades:

1. **Generar un nou encàrrec**:
    - Aquesta opció permet crear un nou encàrrec amb els atributs següents: nom del client, telèfon, data d'encàrrec i una llista d'articles amb la quantitat, tipus d'unitat i preu.
    - Després de crear l'encàrrec, aquest serà afegit a la llista d'encàrrecs.

2. **Modificar un encàrrec**:
    - Permet modificar un encàrrec existent.
    - Pots canviar el nom del client, el telèfon, la data o qualsevol altre detall de l'encàrrec seleccionat.

3. **Mostrar encàrrecs**:
    - Mostra tots els encàrrecs actuals amb els seus detalls, com ara el nom del client, telèfon, articles, quantitat, tipus d'unitat, preu, etc.

4. **Generar XML amb DOM**:
    - Genera un fitxer XML amb els encàrrecs actuals utilitzant l'API DOM.
    - El fitxer generat contindrà tots els detalls dels encàrrecs i articles en format XML.

5. **Llegir XML amb DOM**:
    - Permet llegir un fitxer XML existent i mostrar el contingut dels encàrrecs.
    - L'usuari haurà d'introduir el nom del fitxer XML que vol llegir.

    Exemple de sortida:

    ```
    Introdueix el nom del fitxer XML a llegir: encarrecs_1731876112384.xml
    Encàrrec ID: 1
    Client: david
    Telèfon: 638877788
    Data Encàrrec: 12/08/2025
      Article: pera
      Quantitat: 1.0
      Tipus Unit: u
      Preu: 1.34
    -------------------------
    ```

6. **Llegir XML amb SAX**:
    - Aquesta opció permet llegir un fitxer XML utilitzant l'API SAX, que és una forma més eficient de processar grans fitxers XML de manera seqüencial.

7. **Sortir**:
    - Aquesta opció tanca el programa.

## Exemples d'ús

1. **Llegir XML amb DOM**:
    - Quan seleccionis l'opció "5", el programa et demanarà que introdueixis el nom del fitxer XML a llegir. Un exemple de sortida seria:

    ```
    Introdueix el nom del fitxer XML a llegir: encarrecs_1731876112384.xml
    Encàrrec ID: 1
    Client: david
    Telèfon: 638877788
    Data Encàrrec: 12/08/2025
      Article: pera
      Quantitat: 1.0
      Tipus Unit: u
      Preu: 1.34
    -------------------------
    ```

2. **Generar un XML amb DOM**:
    - Selecciona l'opció "4" per generar un fitxer XML amb els encàrrecs actuals. El fitxer es guardarà en el directori especificat.

## Requisits

- Java 8 o superior.
- Un editor de text per editar el codi (opcional).
- Un entorn de desenvolupament per executar el codi (per exemple, IntelliJ IDEA, Eclipse, o línia de comandes).

## Com executar el programa

1. **Clona el repositori**:

    git clone git@github.com:danistrix63/ACTIVITAT3.git

2. **Compila i executa el codi**:

    cd ACTIVITAT3/
    javac *.java
    java Main.java

3. **Selecciona les opcions que desitgis** seguint les instruccions que apareixen al terminal.