
package business;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.annotations.Expose;

import edu.salleurl.profile.Profileable;

public class Rapper implements Comparable<Rapper>, Profileable {
    @Expose
    private String realName;
    @Expose
    private String stageName;
    @Expose
    private Date birth;
    @Expose
    private String nationality;
    @Expose
    private int level;
    @Expose
    private String photo;

    private double score;
    private boolean qualified;
    private boolean isYou;
    private boolean isOut;
    private int position;

    public Rapper(String realName, String stageName, Date birth, String nationality, int level, String photo) {
        this.realName = realName;
        this.stageName = stageName;
        this.birth = birth;
        this.nationality = nationality;
        this.level = level;
        this.photo = photo;
        this.score = 0;
        this.qualified = false;
        this.isYou = false;
        this.isOut = false;
        this.position = 0;
    }

    /**
     * @return Nom artístic del raper
     */
    public String getStageName() {
        return this.stageName;
    }

    /**
     * @return Data de naixement del raper
     */
    public Date getBirth() {
        return this.birth;
    }

    /**
     * @return Puntuació que té el raper en la competició
     */
    public double getScore() {
        return this.score;
    }

    /**
     * @return Nom real del raper
     */
    public String getRealName() {
        return this.realName;
    }

    /**
     * @return Nacionalitat del raper
     */
    public String getNationality() {
        return this.nationality;
    }

    /**
     * @return String amb la url on es troba la foto del raper
     */
    public String getPhoto() {
        return this.photo;
    }

    /**
     * @return Nivell del raper
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * @return Si ha estat classificat o no per la segona fase
     */
    public boolean isQualified() {
        return this.qualified;
    }

    /**
     * @return Si el raper es la mateixa persona que el propi usuari que ha inciat sessió
     */
    public boolean isYou() {
        return this.isYou;
    }

    /**
     * Funció que determina quin dels rapers es el mateix que l'usuari que ha realitzat inici de sessió
     */
    public void setYou(){
        this.isYou=true;
    }
    
    /**
     * Funcio que rep per paràmetre un boolea i l'adjudica a l'atribut isYou de la classe
     * @param isYou boolea que te valor a false si no ets el mateix raper que l'usuari, true en cas que si
     */
    public void setYou(boolean isYou) {
        this.isYou = isYou;
    }

    /**
     * Funció que rep una puntuació per paràmetre i la suma dins de l'atribut score
     * @param score puntuació decimal que es vol adjudicar al raper
     */
    public void addScore(double score) {
        this.score += score;
    }

    public void leave(){
        this.isOut = true;
    }

    /**
     * Funció que actualitzat la variable qualified a true en cas que el raper guanyi la fase
     */
    public void win() {
        this.qualified = true;
    }

    /**
     * Funció que rep un raper per paràmetre per comparar la nostra puntuació amb la seva
     * @param rapper    Rapper amb el qual ens volem comparar
     * @return 0 si les puntuacions son iguals, -1 si la nostra puntuació es superior o igual, 1 si la nostra puntuació es inferior a la seva
     */
    @Override
    public int compareTo(Rapper rapper) {
        if (this.score >= rapper.score) {
            if (this.score == rapper.score)
                return 0;
            return -1;
        }
        return 1;
    }

    public boolean isOut() {
        return isOut;
    }

    @Override
    public String getName() {
        return getRealName();
    }

    @Override
    public String getNickname() {
        return getStageName();
    }

    @Override
    public String getBirthdate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return String.valueOf(dateFormat.format(getBirth()));
    }

    @Override
    public String getPictureUrl() {
        return getPhoto();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
