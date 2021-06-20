package postpc.huji.ex8;

import java.io.Serializable;
import java.util.UUID;

public class CalcItem implements Serializable, Comparable<CalcItem>{

    private final long originalNum;
    private long root1;
    private long root2;
    private int progress;
    private boolean isFinished;
    private boolean isPrime;
    private final String calcId;

    public CalcItem(long numToCalculate){
        this.originalNum = numToCalculate;
        this.root1 = -1;
        this.root2 = -1;
        this.progress = 0;
        this.isFinished = false;
        this.isPrime = false;
        this.calcId = UUID.randomUUID().toString();
    }

    public CalcItem(long numToCalculate, long root1, long root2, String requestId,int progress, boolean isFinished, boolean isPrime){
        this.originalNum = numToCalculate;
        this.root1 = root1;
        this.root2 = root2;
        this.calcId = requestId;
        this.progress = progress;
        this.isFinished = isFinished;
        this.isPrime = isPrime;
    }

    public long getOriginalNum() {
        return this.originalNum;
    }

    public long getRoot1() {
        return this.root1;
    }

    public long getRoot2() {
        return this.root2;
    }

    public String getCalcId() {
        return this.calcId;
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public void setFinished(boolean finished) {
        this.isFinished = finished;
    }

    public void setRoot1(long root1) {
        this.root1 = root1;
    }

    public void setRoot2(long root2) {
        this.root2 = root2;
    }

    public void setIsPrime(boolean isPrime){
        this.isPrime = isPrime;
    }

    public boolean getIsPrime(){
        return this.isPrime;
    }

    public String getStatus() {
        if (!this.isFinished) {
            return "Finding roots for number " + originalNum;
        }
        if (!this.isPrime) {
            return "The roots for " + originalNum + " are - " + root1 + " , " + root2;
        } else {
            return "The number " + originalNum + " is a prime number";
        }
    }

    public void setCalcProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public int compareTo(CalcItem other) {
        if (!this.isFinished() && other.isFinished()){
            return -1;
        }
        if (this.isFinished() && !other.isFinished()){
            return 1;
        }
        if (this.getOriginalNum() > other.getOriginalNum()) {
            return 1;
        }
        return -1;
    }
}
