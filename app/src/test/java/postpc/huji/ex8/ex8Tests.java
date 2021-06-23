package postpc.huji.ex8;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ex8Tests {

    @Test
    public void addItemToHolder(){
        CalcItemsHolder holder = new CalcItemsHolder();
        assertEquals(0, holder.calcItems.size());

        CalcItem calc1 = new CalcItem(1);
        holder.addCalc(calc1);
        assertEquals(1, holder.calcItems.size());
    }

    @Test
    public void deleteItemFromHolder(){
        CalcItemsHolder holder = new CalcItemsHolder();
        assertEquals(0, holder.calcItems.size());

        CalcItem calc1 = new CalcItem(1);
        holder.addCalc(calc1);
        assertEquals(1, holder.calcItems.size());

        holder.deleteCalc(calc1);
        assertEquals(0, holder.calcItems.size());
    }

    @Test
    public void checkHolderMarkCalcAsFinished(){
        CalcItemsHolder holder = new CalcItemsHolder();
        CalcItem calc1 = new CalcItem(1);

        holder.addCalc(calc1);
        assertFalse(calc1.isFinished());

        holder.setCalcFinished(calc1);
        assertTrue(calc1.isFinished());
    }

    @Test
    public void checkCalculationSorting(){
        CalcItemsHolder holder = new CalcItemsHolder();
        CalcItem calc1 = new CalcItem(1);
        CalcItem calc2 = new CalcItem(2);
        CalcItem calc3 = new CalcItem(3);
        holder.addCalc(calc1);
        holder.addCalc(calc2);
        holder.addCalc(calc3);
        holder.setCalcFinished(calc1);
        CalcItem calculation1 = holder.calcItems.get(0);
        assertEquals(2, calculation1.getOriginalNum(), 0.0);
        CalcItem calculation2 = holder.calcItems.get(1);
        assertEquals(3, calculation2.getOriginalNum(), 0.0);
        CalcItem calculation3 = holder.calcItems.get(2);
        assertEquals(1, calculation3.getOriginalNum(), 0.0);
    }


}
