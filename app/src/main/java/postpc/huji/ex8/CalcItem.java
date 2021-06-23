package postpc.huji.ex8;

import java.io.Serializable;

public class CalcItem implements Serializable, Comparable<CalcItem>{
    static int itemsCount = 0;
    private final long originalNum;
    private long curNum;
    private long root1;
    private long root2;
    private int progress;
    private boolean isFinished;
    private boolean isPrime;
    private int id;
    private String workId;

    public CalcItem(long numToCalculate){
        itemsCount += 1;
        this.originalNum = numToCalculate;
        this.curNum = 2;
        this.root1 = -1;
        this.root2 = -1;
        this.progress = 0;
        this.isFinished = false;
        this.isPrime = false;
        this.workId = "";
        this.id = itemsCount;
    }

    public CalcItem(long numToCalculate, long root1, long root2, String requestId,int progress, boolean isFinished, boolean isPrime){
        this.originalNum = numToCalculate;
        this.root1 = root1;
        this.root2 = root2;
        this.workId = requestId;
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

    public long getCurNum(){
        return this.curNum;
    }

    public void setCurNum(long num){
        this.curNum = num;
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

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setWorkId(String id){
        this.workId = id;
    }

    public String getWorkId(){
        return this.workId;
    }

    public String getStatus() {
        if (!this.isFinished) {
            return "Finding roots for number " + originalNum;
        }
        if (!this.isPrime) {
            return "The roots for " + originalNum + " are -\n" + root1 + " , " + root2;
        } else {
            return "The number " + originalNum + " is a prime number";
        }
    }

    public void setCalcProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return this.progress;
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
