package additionPacakge;

import java.util.concurrent.*;

public class CheckHour {
    private int time;
    private final long startTime;
    private long timeMillisecond = 0;
    private ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService ses2 = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService ses3 = Executors.newSingleThreadScheduledExecutor();
    private final int durationOfHourMILLISECONDS;
    private int localTime = 0;
    public CheckHour (long startTime,int durationOfHourMILLISECONDS){
        this.startTime = startTime;
        this.durationOfHourMILLISECONDS = durationOfHourMILLISECONDS;
        ses.scheduleAtFixedRate(() -> time++, durationOfHourMILLISECONDS, durationOfHourMILLISECONDS, TimeUnit.MILLISECONDS);
        ses.scheduleAtFixedRate(() -> localTime++, durationOfHourMILLISECONDS, durationOfHourMILLISECONDS, TimeUnit.MILLISECONDS);
        ses2.scheduleAtFixedRate(() ->time = 0, durationOfHourMILLISECONDS*24, durationOfHourMILLISECONDS*24, TimeUnit.MILLISECONDS);
    }
    public int returnCurrentTime (){
        if (time < 24){
            return time;
        }else{
            return -1000;
        }
    }
    public long millisecondsRemain (){
        if (time < 24){
            return (durationOfHourMILLISECONDS - (System.currentTimeMillis()-durationOfHourMILLISECONDS*localTime-startTime));
        } else{
            return -1000;
        }
    }
}
