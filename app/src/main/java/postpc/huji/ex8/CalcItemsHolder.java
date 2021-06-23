package postpc.huji.ex8;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Collections;

public class CalcItemsHolder extends Activity {
    ArrayList<CalcItem> calcItems;

    public CalcItemsHolder(){
        this.calcItems = new ArrayList<>();
    }

    public void addCalc(CalcItem item){
        this.calcItems.add(item);
        Collections.sort(this.calcItems);
    }

    public void deleteCalc(CalcItem item){
        this.calcItems.remove(item);
        Collections.sort(this.calcItems);
    }

    public void setCalcFinished(CalcItem item){
        item.setFinished(true);
        item.setCalcProgress(100);
        Collections.sort(this.calcItems);
    }

    public boolean findCalc(long numToCalc){
        for (CalcItem item: this.calcItems){
            if (item.getOriginalNum() == numToCalc){
                return true;
            }
        }
        return false;
    }

    public int getIndex(CalcItem item){
        return this.calcItems.indexOf(item);
    }

    public CalcItem getCalcById(int id){
        for (CalcItem item: this.calcItems){
            if (item.getId() == id){
                return item;
            }
        }
        return null;
    }
}
