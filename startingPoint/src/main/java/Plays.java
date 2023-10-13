import java.util.HashMap;
import java.util.Map;

public class Plays {

    private Map<String, Play> playMap;

    public Plays() {
        playMap = new HashMap<>();
    }
    public void addPlay(Play play) {
        playMap.put(play.getPlayID(), play);
    }

    public Play getPlay(String playID) {
        return playMap.get(playID);
    }
}
