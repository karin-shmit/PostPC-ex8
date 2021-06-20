package postpc.huji.ex8;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import java.util.UUID;

import javax.annotation.Nonnull;


public class CalcAdapter extends RecyclerView.Adapter<CalcAdapter.ViewHolder> {
    public WorkManager wm;
    public CalcItemsHolder calcHolder;
    App app;

    public CalcAdapter(CalcItemsHolder holder, WorkManager wm, App app){
        this.calcHolder = holder;
        this.wm = wm;
        this.app = app;
    }

    @Nonnull
    @Override
    public CalcAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_calculation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalcAdapter.ViewHolder viewHolder, int position) {
        int pos = viewHolder.getLayoutPosition();
        CalcItem calc = calcHolder.calcItems.get(pos);
        viewHolder.calcDescription.setText(calc.getStatus());
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = viewHolder.getLayoutPosition();
                CalcItem calc = calcHolder.calcItems.get(pos);

                if (!calc.isFinished()) {
                    wm.cancelWorkById(UUID.fromString(calc.getCalcId()));
                }
                calcHolder.deleteCalc(calc);
                notifyItemRangeRemoved(pos, 1);
            }
        });
        changeProgressBar(viewHolder, calc);
    }

    private void changeProgressBar(@NonNull ViewHolder holder, CalcItem calc) {
        if (calc.isFinished()) {
            holder.calcProgress.setVisibility(View.INVISIBLE);
        } else {
            holder.calcProgress.setProgress(calc.getProgress());
        }
    }

    @Override
    public int getItemCount() {
        return calcHolder.calcItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout calculationRow;
        TextView calcDescription;
        Button deleteButton;
        ProgressBar calcProgress;
        TextView progressPercentage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.calculationRow = itemView.findViewById(R.id.calculationRow);
            this.calcDescription = itemView.findViewById(R.id.calcDescription);
            this.deleteButton = itemView.findViewById(R.id.deleteCalcButton);
            this.calcProgress = itemView.findViewById(R.id.calcProgress);
            this.progressPercentage = itemView.findViewById(R.id.progressPercentage);

        }

        public void calcComplete(CalcItem calc){
            this.calcDescription.setText(calc.getStatus());
            calcProgress.setVisibility(View.GONE);
            progressPercentage.setVisibility(View.INVISIBLE);
        }

        public void setCalcProgress(int progress) {
            if (progress == 0) {
                progressPercentage.setVisibility(View.VISIBLE);
            }
            calcProgress.setProgress(progress);
        }

        public void setCalcProgressPercentage(int progress) {
            String strPercentage = Float.toString(progress) + "%";
            progressPercentage.setText(strPercentage);
            if (progress >= 99) {
                calcProgress.setVisibility(View.GONE);
                progressPercentage.setVisibility(View.INVISIBLE);
            }
        }
    }
}
