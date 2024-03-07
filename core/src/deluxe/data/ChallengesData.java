package deluxe.data;

public class ChallengesData {
    private boolean laserPointer;
    private boolean hardCassette;
    private boolean classicCat;
    private boolean freeScroll;

    private byte laserPointerValue;
    private byte hardCassetteValue;

    public void setLaserPointer(boolean laserPointer) {
        this.laserPointer = laserPointer;
    }

    public void setHardCassette(boolean hardCassette) {
        this.hardCassette = hardCassette;
    }

    public void setClassicCat(boolean classicCat) {
        this.classicCat = classicCat;
    }

    public void setFreeScroll(boolean freeScroll) {
        this.freeScroll = freeScroll;
    }

    public void setLaserPointerValue(byte laserPointerValue) {
        this.laserPointerValue = laserPointerValue;
    }

    public void setHardCassetteValue(byte hardCassetteValue) {
        this.hardCassetteValue = hardCassetteValue;
    }

    public boolean isLaserPointer() {
        return laserPointer;
    }

    public boolean isHardCassette() {
        return hardCassette;
    }

    public boolean isClassicCat() {
        return classicCat;
    }

    public boolean isFreeScroll() {
        return freeScroll;
    }

    public byte getLaserPointerValue() {
        return laserPointerValue;
    }

    public byte getHardCassetteValue() {
        return hardCassetteValue;
    }
}
