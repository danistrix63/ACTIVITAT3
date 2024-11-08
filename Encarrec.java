/**
 * Clase Encarrec
 *
 * Representa un encargo realizado por un cliente, con información del cliente,
 */
import java.io.Serializable;
import java.util.ArrayList;

public class Encarrec implements Serializable {
    private int id;
    private String nomClient;
    private String telefon;
    private String dataEncàrrec;
    private ArrayList<Article> articles;
    private float preuTotal;

    public Encarrec(int id, String nomClient, String telefon, String dataEncàrrec) {
        this.id = id;
        this.nomClient = nomClient;
        this.telefon = telefon;
        this.dataEncàrrec = dataEncàrrec;
        this.articles = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getDataEncàrrec() {
        return dataEncàrrec;
    }

    public void setDataEncàrrec(String dataEncàrrec) {
        this.dataEncàrrec = dataEncàrrec;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
        calcularPreuTotal();
    }

    public void addArticle(Article article) {
        articles.add(article);
        calcularPreuTotal();
    }

    public float getPreuTotal() {
        return preuTotal;
    }

    private void calcularPreuTotal() {
        preuTotal = 0;
        for (Article article : articles) {
            preuTotal += article.getQuantitat() * article.getPreu();
        }
    }

    @Override
    public String toString() {
        return "Encarrec [id=" + id + ", nomClient=" + nomClient + ", telefon=" + telefon + ", dataEncàrrec=" + dataEncàrrec +
                ", preuTotal=" + preuTotal + ", articles=" + articles + "]";
    }
}
