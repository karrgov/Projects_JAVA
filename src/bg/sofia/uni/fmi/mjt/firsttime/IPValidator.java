package bg.sofia.uni.fmi.mjt.firsttime;

public class IPValidator {
    public static boolean isIPAddressValid(String address)
    {
        String[] arr = address.split("\\.");

        if(arr.length != 4)
        {
            return false;
        }

        for(String el : arr)
        {
            if(!isAValidOctet(el))
            {
                return false;
            }
        }
        return true;
    }

    private static boolean isAValidOctet(String octet)
    {
        if(octet.isEmpty())
        {
            return false;
        }

        if(octet.startsWith("0") && octet.length() > 1)
        {
            return false;
        }

        int number = 0;

        for(char digit : octet.toCharArray())
        {
            if(digit < '0' || digit > '9')
            {
                return false;
            } 
            number = number * 10 + (digit - '0');
        }
        return number < 256;
    }
    
}