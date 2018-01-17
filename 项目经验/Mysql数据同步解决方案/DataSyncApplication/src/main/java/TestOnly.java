/**
 * Created by huxianyang on 2017/12/12.
 */
public class TestOnly {
    public static void main(String[] args){

        System.out.println("start to sleep...");
        try {
            Thread.sleep(10000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
