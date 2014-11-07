package Helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 11/7/14.
 */
public class TimeAndMemoryRecorder {

    private double startTime;
    private List<Double> times;
    private List<Double> memories;

    public TimeAndMemoryRecorder() {
        times = new ArrayList<Double>();
        memories = new ArrayList<Double>();
    }

    public void start() throws Exception {
        if (startTime == 0) {
            startTime = System.nanoTime() / (double)1000000;
            times.add(0D);
            memories.add(getMemory());
        } else {
            throw new Exception("Recorder has already been started.");
        }
    }

    /*
    Time is in milliseconds, memory is in KB
     */
    public void poll() {
        double elapsedTime = System.nanoTime() / (double)1000000 - startTime;
        times.add(elapsedTime);
        memories.add(getMemory());
    }

    private double getMemory() {
        return (double)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (double)(1024);
    }
}
