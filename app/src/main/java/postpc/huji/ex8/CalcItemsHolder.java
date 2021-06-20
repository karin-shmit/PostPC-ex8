package postpc.huji.ex8;

import android.app.Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CalcItemsHolder extends Activity {
    ArrayList<CalcItem> calcItems;

    public CalcItemsHolder(App app){
        this.calcItems = new ArrayList<>();
    }

    public void addCalc(CalcItem calc){
        calcItems.add(calc);
        Collections.sort(this.calcItems);
    }

    public void deleteCalc(CalcItem calc){
        this.calcItems.remove(calc);
        Collections.sort(this.calcItems);
    }

    public void setCalcFinished(CalcItem calc){
        calc.setFinished(true);
        calc.setCalcProgress(100);
        Collections.sort(this.calcItems);
    }



}
