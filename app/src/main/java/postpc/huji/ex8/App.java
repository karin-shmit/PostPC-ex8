package postpc.huji.ex8;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class App extends Application {
    public ArrayList<CalcItem> calcList;
    SharedPreferences sp;
    Context context;

    public App(Context context){
        this.context = context;
        this.sp = context.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
        loadCalcs();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.calcList = new ArrayList<>();
    }

    public void loadCalcs() {
        this.calcList =  new ArrayList<>();
        String itemsJson = this.sp.getString("calcs", "");
        if (!itemsJson.equals("")) {
            Type listType = new TypeToken<ArrayList<CalcItem>>(){}.getType();
            this.calcList = new Gson().fromJson(itemsJson, listType);
        }
    }

    public void saveCalcs(ArrayList<CalcItem> calcs) {
        this.calcList=calcs;
        String itemsJson = new Gson().toJson(calcs);
        this.sp.edit().putString("calcs", itemsJson).apply();
    }

}
