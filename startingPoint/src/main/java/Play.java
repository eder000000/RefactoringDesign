public class Play {
    private String playID;
    private String name;
    private String type;

    // Constructor
    public Play(String playID, String name, String type) {
        this.playID = playID;
        this.name = name;
        this.type = type;
    }

    // Getter methods
    public String getPlayID() {
        return playID;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
