import java.net.*;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class URLConnectionReader {
	public static void main(String[] args) throws Exception {
		System.out.println(getURLContent(args[0], "http://www.qq.com",
				new LinkedList<String>()));

	}

	public static LinkedList<String> getURLContent(String keyword, String url,
			LinkedList<String> l) throws Exception {
		URL oracle = new URL(url);
		URLConnection yc = oracle.openConnection();
		// solve the Chinese character problems
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream(), "gb2312"));
		String inputLine;
		StringBuilder s = new StringBuilder("");
		while ((inputLine = in.readLine()) != null) {
			s.append(inputLine.replaceAll(" ", ""));
		}
		String urlin = "";
		String t = s.toString();
		String r = "<a([^>]*)>([^<]*" + keyword + "[^>]*)</a>";
		Pattern p = Pattern.compile(r);
		Matcher m = p.matcher(t);
		while (m.find() != false) {
			urlin = m.group(1).replaceAll(".*href=\"([^\"]*)\".*", "$1");
			// System.out.println(m.group());
			// System.out.println(m.group(2));
			if (!l.contains(m.group(2))) {
				l.add(m.group(2));
				// System.out.println(l);
				if (!urlin.equals("") && urlin.contains("qq")) {
					getURLContent(keyword, urlin, l);
				}
			}
		}
		in.close();
		return l;
	}

}
