package postpc.huji.ex8;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.Nullable;

public class CalcWorker extends Worker{
    public static final int MAX_TIME = 500000;
    private int cur_progress = 0;
    Data.Builder dataBuilder;

    public CalcWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        setProgressAsync(new Data.Builder().putInt("progress", 0).build());
    }

    @NonNull
    @Override
    public Result doWork() {
        dataBuilder = new Data.Builder();
        long startTimeMS = System.currentTimeMillis();
        int id = getInputData().getInt("id", -1);
        long numToCalc = getInputData().getLong("numToCalc", 0);
        long currentNum = getInputData().getLong("currentNum", 2);
        for (long i = currentNum; i < numToCalc / 2; i++) {
            if(maxTimePassed(startTimeMS, id, numToCalc, i)){
                return Result.failure(dataBuilder.build());
            }
            if (numToCalc % i == 0) {
                dataBuilder.putInt("id", id);
                dataBuilder.putLong("root1", i);
                dataBuilder.putLong("root2",numToCalc / i);
                dataBuilder.putLong("numToCalc", numToCalc);
                dataBuilder.putInt("prog", cur_progress);
                return Result.success(dataBuilder.build());
            }
        }
        dataBuilder.putInt("id", id);
        dataBuilder.putLong("numToCalc", numToCalc);
        dataBuilder.putBoolean("continueCalc", false);
        return Result.failure(dataBuilder.build());
    }

    @Nullable
    private boolean maxTimePassed(long startTimeMS, int id, long numToCalc, long curNum) {
        long curTime = System.currentTimeMillis() - startTimeMS;
        if (curTime >= MAX_TIME) {
            this.dataBuilder.putLong("numToCalc", numToCalc);
            this.dataBuilder.putLong("curNum", curNum);
            this.dataBuilder.putInt("id", id);
            this.dataBuilder.putBoolean("continueCalc", true);
            updateProgressBar(numToCalc, curNum);
            return true;
        }
        return false;
    }

    private void updateProgressBar(long numToCalc, long curNum){
        double tempProg = (((double) curNum / (double) numToCalc) * (double) 100);
        int progLevel = (int) tempProg;
        if (this.cur_progress != progLevel) {
            this.cur_progress = progLevel;
            setProgressAsync(new Data.Builder().putInt("progress", progLevel).build());
        }
    }
}
