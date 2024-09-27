package bg.sofia.uni.fmi.mjt.firsttime;

public class BrokenKeyboard
{
    public static int calculateFullyTypedWords(String message, String brokenKeys)
    {
        int counter = 0;
        String[] myarr = message.split(" ");
        for(int i = 0; i < myarr.length; i++)
        {

            if (myarr[i].isBlank()) 
            {
                counter++;
                continue;
            }

            for(int j = 0; j < brokenKeys.length(); j++)
            {
                if(myarr[i].indexOf(brokenKeys.charAt(j)) != -1)
                {
                    counter++;
                    break;
                }
            }
        }
        return myarr.length - counter;
    }
    
    public static void main(String[] args)
    {
        System.out.println(calculateFullyTypedWords("i love mjt", "qsf3o"));
        System.out.println(calculateFullyTypedWords("secret      message info      ", "sms"));
        System.out.println(calculateFullyTypedWords("dve po 2 4isto novi beli kecove", "o2sf"));
        System.out.println(calculateFullyTypedWords("     ", "asd"));
        System.out.println(calculateFullyTypedWords(" - 1 @ - 4", "s"));
    }
}
