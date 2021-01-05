package business;

import edu.salleurl.profile.Profileable;

import java.util.Date;

public class Rapper {
    private String realName;
    private String stageName;
    private Date birth;
    private String nationality;
    private int level;
    private String photo;
    private int score;

    public Rapper(String realName, String stageName, Date birth, String nationality, int level, String photo) {
        this.realName = realName;
        this.stageName = stageName;
        this.birth = birth;
        this.nationality = nationality;
        this.level = level;
        this.photo = photo;
        this.score = 0;
    }

    public String getStageName() {
        return stageName;
    }

    public Date getBirth() {
        return birth;
    }

    public int getScore() {
        return score;
    }

    public String getRealName() {
        return realName;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPhoto() {
        return photo;
    }
}
