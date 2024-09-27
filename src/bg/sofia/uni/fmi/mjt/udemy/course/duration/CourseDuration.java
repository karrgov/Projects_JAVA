package bg.sofia.uni.fmi.mjt.udemy.course.duration;

import bg.sofia.uni.fmi.mjt.udemy.course.Resource;

public record CourseDuration(int hours, int minutes) {

    private static final int HOURS_DAY = 24;
    private static final int MIN_HOUR = 60;

    public CourseDuration
    {
        if(hours < 0 || hours > HOURS_DAY)
        {
            throw new IllegalArgumentException("Hours can not be such number!");
        }

        if(minutes < 0 || minutes > 60)
        {
            throw new IllegalArgumentException("Minutes can not be such number!");
        }
    }

    public static CourseDuration of(Resource[] content)
    {
        int mins = 0;

        for(Resource curr : content)
        {
            mins = mins + curr.getDuration().minutes();
        }

        int hours = mins / MIN_HOUR;
        mins = mins - hours * MIN_HOUR;
        return new CourseDuration(mins, hours);
    }

    public boolean isLongerThan(CourseDuration other)
    {
        int fstTotalMins = this.minutes + this.hours * MIN_HOUR;
        int sndTotalMins = other.minutes + other.hours * MIN_HOUR;

        return fstTotalMins > sndTotalMins;
    }
}
