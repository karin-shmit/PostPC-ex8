package postpc.huji.ex8;


import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.annotation.Nullable;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity {
    EditText insertCalculation;
    FloatingActionButton buttonNewCalc;
    RecyclerView recycler;
    CalcAdapter adapter;
    CalcItemsHolder holder;
    App app;
    Data.Builder dataBuilder;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.app = new App(this);
        this.context = MainActivity.this;

        this.insertCalculation = findViewById(R.id.insertCalculation);
        this.buttonNewCalc = findViewById(R.id.buttonNewCalculation);
        this.recycler = findViewById(R.id.recyclerView);

        this.dataBuilder = new Data.Builder();
        this.holder = new CalcItemsHolder();
        this.holder.calcItems = app.calcList;
        if (this.context != null){
            this.adapter = new CalcAdapter(holder, WorkManager.getInstance(this.context), app);
        }
        this.recycler.setAdapter(this.adapter);
        this.recycler.setLayoutManager(new LinearLayoutManager(this));
        this.recycler.addItemDecoration(new DividerItemDecoration(this, VERTICAL));

        this.buttonNewCalc.setOnClickListener(v -> {
            try{
                long numToCalc = Long.parseLong(this.insertCalculation.getText().toString());
                if (! this.holder.findCalc(numToCalc)){
                    startCalc(new CalcItem(numToCalc), true);
                }
            }
            catch (NumberFormatException e){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }

            this.insertCalculation.setText("");
        });

        for (CalcItem item: holder.calcItems){
            if (!item.isFinished()){
                startCalc(item, false);
            }
        }
    }

    private void startCalc(CalcItem calc, boolean newCalc){
        if (newCalc){
            this.holder.addCalc(calc);
            this.app.saveCalcs(this.holder.calcItems);
            this.adapter.notifyItemInserted(this.holder.getIndex(calc));
        }

        this.dataBuilder.putInt("id", calc.getId());
        this.dataBuilder.putLong("numToCalc", calc.getOriginalNum());
        this.dataBuilder.putLong("curNum", calc.getCurNum());
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CalcWorker.class).setInputData(dataBuilder.build()).build();
        WorkManager.getInstance(this).enqueue(workRequest);
        calc.setWorkId(workRequest.getId().toString());
        LiveData<WorkInfo> workInfo = WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(workRequest.getId());
        workInfo.observeForever(info -> {
            if (info != null){
                WorkInfo.State state = info.getState();
                Data output = info.getOutputData();
                int prog = output.getInt("prog", 0);
                if (state == WorkInfo.State.FAILED){
                    if (isContinue(output)){
                        calc.setCurNum(output.getLong("curNum", 2));
                        calc.setCalcProgress(prog);
                        holder.calcItems.set(holder.getIndex(calc), calc);
                        updateProgress(info.getId().toString(), prog);
                        app.saveCalcs(holder.calcItems);
                        startCalc(calc, false);
                    }
                }
                else if (state == WorkInfo.State.SUCCEEDED){
                    calc.setRoot1(output.getLong("root1", 0));
                    calc.setRoot2(output.getLong("root2", 0));
                    holder.setCalcFinished(calc);
                    app.saveCalcs(holder.calcItems);
                    adapter.notifyDataSetChanged();
                    CalcAdapter.ViewHolder viewHolder = (CalcAdapter.ViewHolder) recycler.
                            findViewHolderForLayoutPosition(holder.getIndex(calc));
                    if (viewHolder != null){
                        viewHolder.calcComplete(calc);
                    }
                }
                updateProgress(info.getId().toString(), prog);
            }
        });
    }

    private boolean isContinue(Data output){
        if (output.getBoolean("continueCalc", true)){
            return true;
        }
        setPrime(output);
        return false;
    }

    private void setPrime(Data output){
        CalcItem item = this.holder.getCalcById(output.getInt("id", -1));
        item.setIsPrime(true);
        this.adapter.notifyDataSetChanged();
        CalcAdapter.ViewHolder viewHolder = (CalcAdapter.ViewHolder) this.recycler.findViewHolderForLayoutPosition(this.holder.getIndex(item));
        if (viewHolder != null){
            viewHolder.calcComplete(item);
        }
        this.holder.setCalcFinished(item);
        app.saveCalcs(holder.calcItems);
    }

    private void updateProgress(String workId, int progress){
        for (int i = 0; i < this.holder.calcItems.size(); i++) {
            CalcItem calc = this.holder.calcItems.get(i);
            if (calc.getWorkId().equals(workId)) {
                calc.setCalcProgress(progress);
                CalcAdapter.ViewHolder viewHolder =
                        (CalcAdapter.ViewHolder) this.recycler.findViewHolderForLayoutPosition(i);
                if (viewHolder != null) {
                    if (progress <= 1) {
                        viewHolder.setCalcProgressBar(0);
                    }
                    else if (progress >= 99) {
                        viewHolder.setCalcProgressBar(100);
                    }
                    viewHolder.setCalcProgressBar(progress);
                }
            }
        }
    }
}