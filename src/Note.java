/**
 *
 *
 */
public class Note {
    private int channel;
    private int pitch;
    private int velocity;
    private long startTic;
    private long durationTics;

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public long getDurationTics() {
        return durationTics;
    }

    public void setDurationTics(long endTic) {
        this.durationTics = endTic;
    }

    public long getStartTic() {
        return startTic;
    }

    public void setStartTic(long startTic) {
        this.startTic = startTic;
    }

    /***
     *
     * @param pitch
     * @param velocity
     * @param startTic
     * @param endTic
     */
    public Note(int channel, int pitch, int velocity, long startTic, long endTic) {
        this.channel = channel;
        this.pitch = pitch;
        this.velocity = velocity;
        this.startTic = startTic;
        this.durationTics = endTic;
    }
}
