package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotCompletedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;

public abstract class AccountBase implements Account{

    private static final int MAX_CAPACITY = 100;
    private static final double MIN_GRADE = 2.00;
    private static final double MAX_GRADE = 6.00;

    private final String username;
    private double balance;
    private Course[] courses;
    private int courseCounter;

    public AccountBase(String username, double balance)
    {
        this(username, balance, new Course[MAX_CAPACITY], 0);
    }

    AccountBase(String username, double balance, Course[] courses, int courseCounter)
    {
        this.username = username;
        this.balance = balance;
        this.courses = courses;
        this.courseCounter = courseCounter;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void addToBalance(double amount)
    {
        if(amount < 0)
        {
            throw new IllegalArgumentException("Amount can not be negative!");
        }
        this.balance = this.balance + amount;
    }

    public double getBalance()
    {
        return this.balance;
    }

    private Course findCourse(Course course)
    {
        for(int i = 0; i < courseCounter; i++)
        {
            if(courses[courseCounter].equals(course) && courses[courseCounter].isPurchased())
            {
                return courses[courseCounter];
            }
        }
        return null;
    }

    public abstract double applyDiscount(Course course);

    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException
    {
        if(this.balance < course.getPrice())
        {
            throw new InsufficientBalanceException("Not enough money to buy the course!");
        }

        if(findCourse(course) != null)
        {
            throw new CourseAlreadyPurchasedException("The course is already purchased!");
        }

        if(courseCounter == MAX_CAPACITY)
        {
            throw new MaxCourseCapacityReachedException("Can not buy any more courses!");
        }

        double newPrice = applyDiscount(course);
        this.balance = this.balance - newPrice;
        Course courseToAdd = new Course(course);
        this.courses[courseCounter] = courseToAdd;
        courseToAdd.purchase();
    }

    public void completeResourcesFromCourse(Course course, Resource[] resourcesToComplete) throws CourseNotPurchasedException, ResourceNotFoundException
    {
        if(findCourse(course) == null)
        {
            throw new CourseNotPurchasedException("The course is not purchased yet!");
        }

        for(Resource curr : resourcesToComplete)
        {
            course.completeResource(curr);
        }
    }

    public void completeCourse(Course course, double grade) throws CourseNotPurchasedException, CourseNotCompletedException
    {
        if(grade < MIN_GRADE || grade > MAX_GRADE)
        {
            throw new IllegalArgumentException("Grade can not be such number!");
        }

        Course courseToComplete = findCourse(course);

        if(courseToComplete == null)
        {
            throw new CourseNotPurchasedException("The course is not purchased!");
        }

        if(!courseToComplete.isCompleted())
        {
            throw new CourseNotCompletedException("The course is not completed yet!");
        }
    }

    public Course getLeastCompletedCourse()
    {
        if(courseCounter == 0)
        {
            return null;
        }

        Course leastOne = courses[0];
        int leastPercentage = leastOne.getCompletionPercentage();

        for(Course curr : courses)
        {
            if(curr == null)
            {
                break;
            }

            int currPercentage = curr.getCompletionPercentage();
            if(currPercentage < leastPercentage)
            {
                leastOne = curr;
                leastPercentage = currPercentage;
            }
        }
        return leastOne;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }

        if(obj instanceof AccountBase other)
        {
            return this.username.equals(other.username);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return this.username.hashCode();
    }
}
