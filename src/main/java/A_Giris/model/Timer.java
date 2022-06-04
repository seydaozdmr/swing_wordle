package A_Giris.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Timer {
    private AtomicInteger timer=new AtomicInteger(0);

    public Timer() {

    }

    public int incrementTimer(){
        return timer.incrementAndGet();
    }

    public void reset(){
        timer.set(0);
    }

    public int getTime(){
        return this.timer.get();
    }
}
