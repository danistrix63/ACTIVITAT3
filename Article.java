/**
 * Clase Article
 *
 * Representa un artículo que forma parte de un encargo, con atributos de nombre,
 * cantidad, tipo de unidad y precio unitario.
 */
import java.io.Serializable;

public class Article implements Serializable {
    private String nom;
    private float quantitat;
    private String tipusUnitat;
    private float preu; // Nuevo atributo para el precio unitario

    // Constructor con todos los campos
    public Article(String nomArticle, float quantitat, String tipusUnitat, float preu) {
        this.nom = nomArticle;
        this.quantitat = quantitat;
        this.tipusUnitat = tipusUnitat;
        this.preu = preu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getQuantitat() {
        return quantitat;
    }

    public void setQuantitat(float quantitat) {
        this.quantitat = quantitat;
    }

    public String getTipusUnitat() {
        return tipusUnitat;
    }

    public void setTipusUnitat(String tipusUnitat) {
        this.tipusUnitat = tipusUnitat;
    }

    public float getPreu() {
        return preu;
    }

    public void setPreu(float preu) {
        this.preu = preu;
    }

    @Override
    public String toString() {
        return "Article [nom=" + nom + ", quantitat=" + quantitat + ", tipusUnitat=" + tipusUnitat + ", preu=" + preu + "]";
    }

    public String toCSV() {
        return nom + ";" + quantitat + ";" + tipusUnitat + ";" + preu + ";";
    }
}
