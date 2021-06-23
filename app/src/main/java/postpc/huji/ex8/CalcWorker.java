package postpc.huji.ex8;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.Nullable;

public class CalcWorker extends Worker{
    public static final int MAX_TIME = 500;
    private int cur_progress = 0;
    Data.Builder dataBuilder;

    public CalcWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        setProgressAsync(new Data.Builder().putInt("prog", 0).build());
    }

    @NonNull
    @Override
    public Result doWork() {
        dataBuilder = new Data.Builder();
        long startTimeMS = System.currentTimeMillis();
        int id = getInputData().getInt("id", -1);
        long numToCalc = getInputData().getLong("numToCalc", 0);
        long currentNum = getInputData().getLong("curNum", 2);
        for (long i = currentNum; i < numToCalc / 2 + 1; i++) {
            if(maxTimePassed(startTimeMS, id, numToCalc, i)){
                return Result.failure(this.dataBuilder.build());
            }

            updateProgressBar(numToCalc, i);

            if (numToCalc % i == 0) {
                this.dataBuilder.putInt("id", id);
                this.dataBuilder.putLong("root1", i);
                this.dataBuilder.putLong("root2",numToCalc / i);
                this.dataBuilder.putLong("numToCalc", numToCalc);
                this.dataBuilder.putInt("prog", this.cur_progress);
                return Result.success(this.dataBuilder.build());
            }

        }
        this.dataBuilder.putInt("id", id);
        this.dataBuilder.putLong("numToCalc", numToCalc);
        this.dataBuilder.putBoolean("continueCalc", false);
        return Result.failure(this.dataBuilder.build());
    }

    @Nullable
    private boolean maxTimePassed(long startTimeMS, int id, long numToCalc, long curNum) {
        long curTime = System.currentTimeMillis() - startTimeMS;
        if (curTime >= MAX_TIME) {
            updateProgressBar(numToCalc, curNum);
            this.dataBuilder.putLong("numToCalc", numToCalc);
            this.dataBuilder.putLong("curNum", curNum);
            this.dataBuilder.putInt("id", id);
            this.dataBuilder.putBoolean("continueCalc", true);
            this.dataBuilder.putInt("prog", this.cur_progress);
            return true;
        }
        return false;
    }

    private void updateProgressBar(long numToCalc, long curNum){
        double tempProg = ((double) ((double) curNum / (double) numToCalc) * (double) 100);
        int progLevel = (int) tempProg;
        if (this.cur_progress != progLevel) {
            this.cur_progress = progLevel;
            setProgressAsync(new Data.Builder().putInt("prog", progLevel).build());
        }
    }
}
