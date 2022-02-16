public class LiftRide {

    private Integer time;
    private Integer liftID;
    private Integer waitTime;

    public LiftRide(Integer time, Integer liftID, Integer waitTime) throws IllegalArgumentException {
        if ( time == null || liftID == null ||waitTime == null ) {
            throw new IllegalArgumentException("Body elemnts cannot be null");
        }

        this.time = time;
        this.liftID = liftID;
        this.waitTime = waitTime;
    }

    public Integer getTime() {
        return time;
    }

    public Integer getLiftID() {
        return liftID;
    }

    public Integer getWaitTime() {
        return waitTime;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setLiftID(int liftID) {
        this.liftID = liftID;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}
