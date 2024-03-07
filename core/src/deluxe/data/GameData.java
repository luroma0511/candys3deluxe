package deluxe.data;

public class GameData {
    private byte night;
    private byte ratAI;
    private byte catAI;
    private byte vinnieAI;
    private byte shadowRatAI;
    private byte shadowCatAI;

    public GameData() {
    }

    public void writeData(byte night, byte ratAI, byte catAI, byte vinnieAI, byte shadowRatAI, byte shadowCatAI){
        this.night = night;
        this.ratAI = ratAI;
        this.catAI = catAI;
        this.vinnieAI = vinnieAI;
        this.shadowRatAI = shadowRatAI;
        this.shadowCatAI = shadowCatAI;
    }

    public byte getRatAI() {
        return ratAI;
    }

    public byte getCatAI() {
        return catAI;
    }

    public byte getVinnieAI() {
        return vinnieAI;
    }

    public byte getShadowRatAI() {
        return shadowRatAI;
    }

    public byte getShadowCatAI() {
        return shadowCatAI;
    }
}
