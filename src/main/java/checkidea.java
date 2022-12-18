import additionPacakge.CheckHour;
import lombok.SneakyThrows;

public class checkidea {
    @SneakyThrows
    public static void main(String[] args) {
        CheckHour checkHour = new CheckHour(System.currentTimeMillis(), 5000);
//        for (int i = 0; i < 300; i++){
//            System.out.println(checkHour.returnCurrentTime());
//            Thread.sleep(5001);
//        }
        for (int i = 0; i < 300; i++){
            System.out.println(checkHour.millisecondsRemain());
            Thread.sleep(5001);
        }

    }
}
