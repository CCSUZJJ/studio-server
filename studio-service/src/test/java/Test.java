import java.util.Random;

/**
 * User: jianwl
 * Date: 2016/3/23
 * Time: 18:18
 */
public class Test {
    public static void main(String[] args) {
        Random random = new Random(5);
        for(int i=0 ;i<100;i++){
            System.out.println(random.nextInt(5^10));
        }
    }
}
