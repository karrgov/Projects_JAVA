package bg.sofia.uni.fmi.mjt.firsttime;

public class JumpGame {
    public static boolean canWin(int[] arr)
    {
        int jumps = 0;

        for(int i = 0; i < arr.length; i++)
        {
            if(i > jumps)
            {
                return false;
            }
            jumps = Math.max(jumps, i + arr[i]);
        }
        return true;
    }

    public static void main(String[] args)
    {
        // canWin(new int[]{2, 3, 1, 1, 0});
        System.out.println(canWin(new int[]{2, 1, 1, 1, 0}));
    }

}
