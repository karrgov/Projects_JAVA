package bg.sofia.uni.fmi.mjt.udemy;

import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccounNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotFoundException;

import java.util.Arrays;

public class Udemy implements LearningPlatform{

    private Account[] accounts;
    private Course[] courses;

    public Udemy(Account[] accounts, Course[] courses)
    {
        this.accounts = accounts;
        this.courses = courses;
    }

    @Override
    public Course findByName(String name) throws CourseNotFoundException
    {
        if(name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Name is not appropriate!");
        }
        int i = 0;
        while(i < courses.length)
        {
            if(courses[i].getName().equals(name))
            {
                return courses[i];
            }
            i++;
        }
        throw new CourseNotFoundException("Course not found!");
    }

    private boolean isAlpha(String str)
    {
        char[] arr = str.toCharArray();
        for(char c : arr)
        {
            if(!Character.isLetter(c))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public Course[] findByKeyword(String keyword)
    {
        if(keyword == null || keyword.isBlank() || !isAlpha(keyword))
        {
            throw new IllegalArgumentException("Keyword is not appropriate!");
        }

        Course[] result = new Course[courses.length];
        int j = 0;
        for(int i = 0; i < courses.length; i++)
        {
            if(courses[i].getDescription().contains(keyword) || courses[i].getName().contains(keyword))
            {
                result[j] = courses[i];
                j++;
            }
        }
        return Arrays.copyOf(result, j);
    }

    @Override
    public Course[] getAllCoursesByCategory(Category category)
    {
        if(category == null)
        {
            throw new IllegalArgumentException("Category is null!");
        }

        Course[] result = new Course[courses.length];
        int j = 0;

        for(int i = 0; i < courses.length; i++)
        {
            if(courses[i].getCategory().equals(category))
            {
                result[j] = courses[i];
                j++;
            }
        }
        return Arrays.copyOf(result, j);
    }

    @Override
    public Account getAccount(String name) throws AccounNotFoundException
    {
        if(name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Name can not be mull or empty!");
        }

        for(Account curr : accounts)
        {
            if(curr.getUsername().equals(name))
            {
                return curr;
            }
        }
        throw new AccounNotFoundException("Account could not be found!");
    }

    @Override
    public Course getLongestCourse()
    {
        Course longestCourse = null;
        CourseDuration longestDuration = new CourseDuration(0, 0);

        for(Course curr : courses)
        {
            if(curr.getTotalTime().isLongerThan(longestDuration))
            {
                longestDuration = curr.getTotalTime();
                longestCourse = curr;
            }
        }
        return longestCourse;
    }

    @Override
    public Course getCheapestByCategory(Category category)
    {
        if(category == null)
        {
            throw new IllegalArgumentException("Category can not be null!");
        }

        if(courses.length == 0)
        {
            return null;
        }

        Course cheapestCourse = null;
        double cheapestPrice = Double.MAX_VALUE;

        for(Course curr : courses)
        {
            if(curr.getCategory().equals(category) && curr.getPrice() < cheapestPrice)
            {
                cheapestPrice = curr.getPrice();
                cheapestCourse = curr;
            }
        }
        return cheapestCourse;
    }


}
