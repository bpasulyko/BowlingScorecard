package bpasulyko.bowlingscorecard.models;

public class ScoreCard {
    private Integer id;
    private String name;

    public ScoreCard(String name) {
        this.name = name;
    }

    public ScoreCard(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
