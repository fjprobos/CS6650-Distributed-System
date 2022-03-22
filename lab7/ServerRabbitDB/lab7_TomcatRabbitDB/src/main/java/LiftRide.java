public class LiftRide {

    private int resortID;
    private int seasonID;
    private int dayID;
    private int skierID;
    private int waitTime;
    private int liftID;

    public LiftRide() {
    }

    public LiftRide(int resortID, int seasonID, int dayID, int skierID, int waitTime, int liftID) {
        this.resortID = resortID;
        this.seasonID = seasonID;
        this.dayID = dayID;
        this.skierID = skierID;
        this.waitTime = waitTime;
        this.liftID = liftID;
    }

    public int getResortID() {
        return resortID;
    }

    public void setResortID(int resortID) {
        this.resortID = resortID;
    }

    public int getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(int seasonID) {
        this.seasonID = seasonID;
    }

    public int getDayID() {
        return dayID;
    }

    public void setDayID(int dayID) {
        this.dayID = dayID;
    }

    public int getSkierID() {
        return skierID;
    }

    public void setSkierID(int skierID) {
        this.skierID = skierID;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getLiftID() {
        return liftID;
    }
    public void setLiftID(int liftID) {
        this.liftID = liftID;
    }
}