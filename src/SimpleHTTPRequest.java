
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleHTTPRequest {

  /**
   * @param args
 * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
	  URL obj = new URL("http://www.163.com");
		URLConnection conn = obj.openConnection();
	 
		//get all headers
		Map<String, List<String>> map = conn.getHeaderFields();
//		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
//			System.out.println("Key : " + entry.getKey() + 
//	                 " ,Value : " + entry.getValue());
//		}
	 
		//get header by 'key'
//		String server = conn.getHeaderField("Server");
		List l=map.get("Content-Type");
//		System.out.println(l.toString());
		String regex=".*charset\\=(.+)[\\W\\D^-]";
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(l.toString());
		String find;
		while(m.find()!=false){
			System.out.println(m.group(1));
		}
}
}
