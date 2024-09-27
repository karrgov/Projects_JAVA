package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;

import java.util.Arrays;
import java.util.Objects;

public class Course implements Completable, Purchasable {

    private String name;
    private String description;
    private double price;
    private Resource[] content;
    private Category category;
    private CourseDuration totalTime;
    private boolean isPurchased;

    public Course(String name, String description, double price, Resource[] content, Category category)
    {
        this(name, description, price, content, category, CourseDuration.of(content), false);
    }

    public Course(Course other)
    {
        this.name = other.name;
        this.description = other.description;
        this.price = other.price;
        this.content = Arrays.copyOf(other.content, other.content.length);
        this.category = other.category;
        this.totalTime = other.totalTime;
        this.isPurchased = other.isPurchased;
    }

    Course(String name, String description, double price, Resource[] content, Category category, CourseDuration totalTime, boolean isPurchased)
    {
        this.name = name;
        this.description = description;
        this.price = price;
        this.content = content;
        this.category = category;
        this.totalTime = totalTime;
        this.isPurchased = isPurchased;
    }

    @Override
    public boolean isCompleted() {
        for(Resource resource : content)
        {
            if(!resource.isCompleted())
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getCompletionPercentage()
    {
        int totalCourses = this.content.length;
        int completedCourses = 0;

        for(Resource curr : content)
        {
            if(curr.isCompleted())
            {
                completedCourses++;
            }
        }
        return (int)Math.round(((double)completedCourses / (double)totalCourses) * 100);
    }

    @Override
    public boolean isPurchased() {
        return isPurchased;
    }

    @Override
    public void purchase() {

    }

    public String getName()
    {
        return this.name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public double getPrice()
    {
        return this.price;
    }

    public CourseDuration getTotalTime()
    {
        return this.totalTime;
    }

    public Resource[] getContent()
    {
        return this.content;
    }

    public Category getCategory()
    {
        return this.category;
    }

    public void completeResource(Resource resourceToComplete) throws ResourceNotFoundException
    {
        for(Resource curr : content)
        {
            if(curr.equals(resourceToComplete))
            {
                curr.complete();
                return;
            }
        }

        throw new ResourceNotFoundException("Resource was not found in the course!");
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }

        if(obj instanceof Course other)
        {
            return this.name.equals(other.name) && this.description.equals(other.description) && this.price == other.price &&
                    Arrays.equals(this.content, other.content) && this.category.equals(other.category) &&
                    this.totalTime.equals(other.totalTime);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, description, price, Arrays.hashCode(content), category, totalTime);
    }
}
