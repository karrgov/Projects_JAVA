package bg.sofia.uni.fmi.mjt.udemy;

import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccounNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotFoundException;

public interface LearningPlatform {

    Course findByName(String name) throws CourseNotFoundException;

    Course[] findByKeyword(String keyword);

    Course[] getAllCoursesByCategory(Category category);

    Account getAccount(String name) throws AccounNotFoundException;

    Course getLongestCourse();

    Course getCheapestByCategory(Category category);

}
