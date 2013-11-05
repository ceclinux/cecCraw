import java.util.ArrayList;
import java.util.HashSet;


public class MyArrayList<T> extends ArrayList<HashSet<String>> {
public void clearAll(){
	for(HashSet<String> h:this)
		h.clear();
}
}
