import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Sorter {
    //Sort based on price
    public ArrayList<Product> SortPrice(ArrayList<Product> list, boolean reversed){
        if(!reversed){
            //Collections apllies comparator logic, comparator get all prices using method reference and compares them
            Collections.sort(list, Comparator.comparingDouble(Product::getPrice));
        }else{
            //Same thing but reversed
            Collections.sort(list, Comparator.comparingDouble(Product::getPrice).reversed());
        }
        return list;
    }

    //Sort based on name
    public  ArrayList<Product> SortName(ArrayList<Product> list, boolean reversed){
        if(!reversed){
            //Uses lambda as comparitor to sort the list names
            list.sort((o1, o2) -> o1.getName().compareTo(o2.getName())); //lambda   (parameters) -> expression
        }else{
            //Reversed
            list.sort(Collections.reverseOrder((o1, o2) -> o1.getName().compareTo(o2.getName())));
        }
        return list;
    }
}