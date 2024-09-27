package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.ResourceDuration;

import java.util.Objects;

public class Resource implements Completable{

    public static final int MAX_COMPLETION_PERCENT = 100;

    private final String name;
    private boolean isCompleted;
    private final ResourceDuration duration;

    Resource(String name, ResourceDuration duration)
    {
        this(name, false, duration);
    }

    Resource(Resource other)
    {
        this(other.name, other.isCompleted, other.duration);
    }

    Resource(String name, boolean isCompleted, ResourceDuration duration)
    {
        this.name = name;
        this.isCompleted = isCompleted;
        this.duration = duration;
    }

    public String getName()
    {
        return this.name;
    }

    public ResourceDuration getDuration()
    {
        return this.duration;
    }

    public void complete()
    {
        this.isCompleted = true;
    }

    @Override
    public boolean isCompleted()
    {
        return isCompleted;
    }

    @Override
    public int getCompletionPercentage()
    {
        if(isCompleted)
        {
            return MAX_COMPLETION_PERCENT;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if(obj instanceof Resource other)
        {
            return this.name.equals(other.name);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return this.name.hashCode();
    }
}
