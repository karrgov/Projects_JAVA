package bg.sofia.uni.fmi.mjt.udemy.course.duration;

public record ResourceDuration(int minutes) {

    private static final int MIN_HOUR = 60;
    public ResourceDuration
    {
        if(minutes < 0 || minutes > MIN_HOUR)
        {
            throw new IllegalArgumentException("Minutes can not be such number!");
        }
    }

}
