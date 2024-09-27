package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotCompletedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotPurchasedException;

public class EducationalAccount extends AccountBase{

    private static final int PREVIOUS_GRADES_COUNT = 5;
    private static final double MIN_AVERAGE_OF_GRADES_FOR_DISCOUNT = 4.50;

    private final double[] grades;
    private int gradesCounter;
    private int allowedDiscounts;


    EducationalAccount(String username, double balance)
    {
        super(username, balance);
        this.grades = new double[PREVIOUS_GRADES_COUNT];
        this.gradesCounter = 0;
        this.allowedDiscounts = 0;
    }

    @Override
    public double applyDiscount(Course course)
    {
        if(allowedDiscounts > 0)
        {
            allowedDiscounts--;
            return course.getPrice() - course.getPrice() * AccountType.EDUCATION.getDiscount();
        }
        return course.getPrice();
    }

    @Override
    public void completeCourse(Course course, double grade) throws CourseNotPurchasedException, CourseNotCompletedException
    {
        super.completeCourse(course, grade);
        this.grades[gradesCounter] = grade;
        this.gradesCounter++;

        if(gradesCounter == PREVIOUS_GRADES_COUNT)
        {
            if(getAverageGradeOfPreviousCourses() >= MIN_AVERAGE_OF_GRADES_FOR_DISCOUNT)
            {
                this.allowedDiscounts++;
            }
            gradesCounter = 0;
        }
    }

    private double getAverageGradeOfPreviousCourses()
    {
        double sum = 0;

        for(double curr : grades)
        {
            sum = sum + curr;
        }
        return sum / PREVIOUS_GRADES_COUNT;
    }
}
