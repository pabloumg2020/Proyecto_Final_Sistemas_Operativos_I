
public class RandomNumber{
    
    public static int generateRandom() {
        int max = 10000;
        int min = 1000;
        int range = max - min + 1;
        int random = (int) (Math.random() * range) + min;
        return random;
    }

    public RandomNumber() {
      
    }  
}
