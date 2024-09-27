package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;

public class BusinessAccount extends AccountBase{

    private Category[] allowedCategories;

    BusinessAccount(String username, double balance, Category[] allowedCategories)
    {
        super(username, balance);
        this.allowedCategories = allowedCategories;
    }

    private boolean isCategoryAllowed(Category category)
    {
        for(Category curr : allowedCategories)
        {
            if(curr.equals(category))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public double applyDiscount(Course course)
    {
        if(!isCategoryAllowed(course.getCategory()))
        {
            throw new IllegalArgumentException("Category is not allowed for business account!");
        }
        return course.getPrice() - course.getPrice() * AccountType.BUSINESS.getDiscount();
    }
}
