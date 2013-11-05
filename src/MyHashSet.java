import java.util.HashSet;


public class MyHashSet<T> extends HashSet<String> {
public String toString(){
	StringBuilder returnS=new StringBuilder("");
	for(String s:this){
		returnS.append(s+"\n");
	}
	return returnS.toString();
}
}
