import java.net.*;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class URLConnectionReader {
	private static final String GET_URL_REGEX = ".*href=\"([^\"]*)\".*";

	public static void main(String[] args) throws Exception {
		String url = "http://www.amazon.com";
		String keyword = args[0];
		LinkedList<String> hyperLinkList = new LinkedList<String>();
		int depth = 1;
		System.out.println(getURLContent(keyword, url, hyperLinkList, depth));

	}

	public static LinkedList<String> getURLContent(String keyword, String url,
			LinkedList<String> returnList, int depth) throws Exception {
		if (depth == 0) {
			return returnList;
		}
		URL searchURL = new URL(url);
		URLConnection urlConnect = searchURL.openConnection();
		// solve the Chinese character problems
		BufferedReader in = new BufferedReader(new InputStreamReader(
				urlConnect.getInputStream(), "gb2312"));
		String inputLine;
		StringBuilder textBuilder = new StringBuilder("");
		while ((inputLine = in.readLine()) != null) {
			textBuilder.append(inputLine);
		}
		String urlLink = "";
		String t = textBuilder.toString();
		String regexSearchString = "<a([^>]*)>([^<]*" + keyword + "[^>]*)</a>";
		Pattern p = Pattern.compile(regexSearchString);
		Matcher matcher = p.matcher(t);
		while (matcher.find() != false) {
			urlLink = matcher.group(1).replaceAll(GET_URL_REGEX, "$1");
			String keySentence = matcher.group(2);
			// System.out.println(m.group());
			// System.out.println(m.group(2));
			if (!returnList.contains(keySentence)) {
				returnList.add(keySentence);
				if (!urlLink.equals("") && urlLink.contains("qq")) {
					getURLContent(keyword, urlLink, returnList, depth - 1);
				}
			}
		}

		in.close();

		return returnList;

	}

	// private static void filterRecur(String keyword,
	// LinkedList<String> returnList, String urlLink, String keySentence)
	// throws Exception {
	//
	// }

}
