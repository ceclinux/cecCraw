import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class URLConnectionReader {
	private static final String GET_URL_REGEX = ".*href=\"([^\"]*)\".*";

	public static void main(String[] args) throws Exception {
		String url = "http://en.wikipedia.com";
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

	/**
	 * When my url string is www.sina.com.cn,the getDomainkey is sina.com.cn.
	 * When my url string is en.wikipedia.com,the getDomainKey is
	 * en.wikipedia.com The key is used for recursive search,to prevent
	 * searching at outer link
	 * 
	 * @param url
	 * @return the key words of the domain,to filter the recursion
	 */
	private static String getDomainKey(URL url) {
		String host = url.getHost();
		String domainKey;

		if (!host.split("\\.")[0].equals("www")) {
			domainKey = host;
		} else {
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
				urlConnect.getInputStream(), getCharset(url)));
		String inputLine;
		StringBuilder textBuilder = new StringBuilder("");
		while ((inputLine = in.readLine()) != null) {
			textBuilder.append(inputLine);
		}
		in.close();
		String t = textBuilder.toString();
		return t;
	}

	/**
	 * catch the HTTP header:Content-Type,the problem is,some http header
	 * doesn't include Content-type
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private static String getCharset(URL url) throws IOException {
		URLConnection conn = url.openConnection();
		Map<String, List<String>> map = conn.getHeaderFields();
		List l = map.get("Content-Type");
		// System.out.println(l.toString());
		String regex = ".*charset\\=(.+)[\\W\\D^-]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(l.toString());
		String find = null;
		while (m.find() != false) {
			find = m.group(1);
		}
		System.out.println(find);
		return find == null ? deepDectectChar(conn) : find;
	}

	/**
	 * If there no content-type in http header(eg sina.com.cn), try to analysis html <meta tag
	 * content to detect encoding
	 * 
	 * @param conn
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private static String deepDectectChar(URLConnection conn)
			throws UnsupportedEncodingException, IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF-8"));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			if (inputLine.contains("<meta") && inputLine.contains("charset")) {

				String[] charSetArray = inputLine.replaceAll("<|/>|\"", "")
						.split("=");

				for (int i = 0; i < charSetArray.length; i++) {
					System.out.println(charSetArray[i].trim());
					if ((charSetArray[i].trim()).endsWith("charset")) {
						// System.out.println(charSetArray[i].split(" ")[0]);
						return charSetArray[i + 1].split(" ")[0];

					}
				}

			}
		}
		return "UTF-8";

	}
}
