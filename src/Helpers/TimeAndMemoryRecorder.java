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
    private List<Integer> frequentItemsetCounts;

    public TimeAndMemoryRecorder() {
        setTimes(new ArrayList<Double>());
        setMemories(new ArrayList<Double>());
        setFrequentItemsetCounts(new ArrayList<Integer>());
    }

    public void start() throws Exception {
        if (startTime == 0) {
            startTime = System.nanoTime() / (double)1000000;
            getTimes().add(0D);
            getMemories().add(getMemory());
            getFrequentItemsetCounts().add(0);
        } else {
            throw new Exception("Recorder has already been started.");
        }
    }

    /*
    Time is in milliseconds, memory is in KB
     */
    public void poll(int frequentItemsetCount) {
        double elapsedTime = System.nanoTime() / (double)1000000 - startTime;
        getTimes().add(elapsedTime);
        getMemories().add(getMemory());
        getFrequentItemsetCounts().add(frequentItemsetCount);
    }

    private double getMemory() {
        return (double)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (double)(1024);
    }

    public List<Double> getTimes() {
        return times;
    }

    public void setTimes(List<Double> times) {
        this.times = times;
    }

    public List<Double> getMemories() {
        return memories;
    }

    public void setMemories(List<Double> memories) {
        this.memories = memories;
    }

    public List<Integer> getFrequentItemsetCounts() {
        return frequentItemsetCounts;
    }

    public void setFrequentItemsetCounts(List<Integer> frequentItemsetCounts) {
        this.frequentItemsetCounts = frequentItemsetCounts;
    }
}
