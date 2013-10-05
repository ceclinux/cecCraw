import java.net.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class URLConnectionReader {
	private static final String GET_URL_REGEX = ".*href=\"([^\"]*)\".*";

	public static void main(String[] args) throws Exception {
		String url = "http://en.wikipedia.org/wiki/Studio_album";
		String keyword = args[0];
		LinkedList<String> hyperLinkList = new LinkedList<String>();
		int depth = 1;
		System.out.println(getURLKey(keyword, url, hyperLinkList, depth));

	}

	public static LinkedList<String> getURLKey(String keyword, String url,
			LinkedList<String> returnList, int depth) throws Exception {
		URL searchURL = new URL(url);
		if (depth == 0) {
			return returnList;
		}

		String regexSearchString = "<a([^>]*)>([^<]*" + keyword + "[^>]*)</a>";
		Pattern p = Pattern.compile(regexSearchString);
		Matcher matcher = p.matcher(getUrlContent(searchURL));
		String urlLink = "";
		while (matcher.find() != false) {
			urlLink = matcher.group(1).replaceAll(GET_URL_REGEX, "$1");
			String keySentence = matcher.group(2);
			// System.out.println(m.group());
			// System.out.println(m.group(2));
			if (!returnList.contains(keySentence)) {
				returnList.add(keySentence);
				String domainKey = getDomainKey(searchURL);

				if (!urlLink.equals("") && urlLink.contains(domainKey)) {
					getURLKey(keyword, urlLink, returnList, depth - 1);
				}
			}
		}

		return returnList;

	}
/**When my url string is www.sina.com.cn,the getDomainkey is sina.com.cn.
 * When my url string is en.wikipedia.com,the getDomainKey is en.wikipedia.com
 * The key is used for recursive search,to prevent searching at outer link
 * 
 * @param url 
 * @return the key words of the domain,to filter the recursion
 */
	private static String getDomainKey(URL url) {
		String host = url.getHost();
		String domainKey;

		if(!host.split("\\.")[0].equals("www")){
			 domainKey= host;
		}
		else{
			domainKey = host.replaceAll("[^\\.]*\\.((\\w+\\.)+\\w+)", "$1");
			
		}
		System.out.println(domainKey);
		return domainKey;
	}

	// private static void filterRecur(String keyword,
	// LinkedList<String> returnList, String urlLink, String keySentence)
	// throws Exception {
	//
	// }
	public static String getUrlContent(URL url) throws IOException {

		URLConnection urlConnect = url.openConnection();
		// solve the Chinese character problems
		BufferedReader in = new BufferedReader(new InputStreamReader(
				urlConnect.getInputStream(), "gb2312"));
		String inputLine;
		StringBuilder textBuilder = new StringBuilder("");
		while ((inputLine = in.readLine()) != null) {
			textBuilder.append(inputLine);
		}
		in.close();
		String t = textBuilder.toString();
		return t;
	}
}
