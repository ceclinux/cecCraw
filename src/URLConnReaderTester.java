import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLConnReaderTester {
	private static final String GET_URL_REGEX = ".*href=\"([^\"]*)\".*";
	static URLConnectionUtils urlconn = new URLConnectionUtils(
			"http://www.sina.com.cn", "李克强");
	static String keyword = urlconn.getKeyword();
	static URL url = urlconn.getUrl();

	public static void main(String[] args) throws Exception {

		String keyword = urlconn.getKeyword();
		URL url = urlconn.getUrl();
		LinkedList<String> hyperLinkList = new LinkedList<String>();
		int depth = 2;
		System.out.println(getURLKey(url, hyperLinkList, depth));

	}

	public static LinkedList<String> getURLKey(URL url,
			LinkedList<String> returnList, int depth) throws Exception {

		if (depth == 0) {
			return returnList;
		}

		Matcher matcher2 = getHyperLinkMatcher(url);

//		 System.out.println("下面是标题测试(h)");
//		 testRegex(keyword, url, "<h[0-6][\\s\"=A-Za-z0-9^>]*>(.*?)</h[0-6]>");
//		System.out.println("文本测试(p)");
	testRegex(keyword, url, "<[p][\\sa-z0-9\"-^>]*>(.*?)</[p]>");
		String urlLink = "";
		while (matcher2.find() != false) {
//			 System.out.println("enter find");
//			 System.out.println(matcher2.group());
			urlLink = matcher2.group(1).replaceAll(GET_URL_REGEX, "$1");
			
			if(!urlLink.contains(".")) continue;
//			System.out.println(urlLink);
			 if(!urlLink.startsWith("http://")&&!urlLink.startsWith("https://"))
			 urlLink=url.toString()+urlLink;
//			 System.out.println(urlLink);
			String keySentence = getKeySentence(matcher2);
			// System.out.println(m.group());
			// System.out.println(m.group(2));

			if (!returnList.contains(keySentence)) {
				returnList.add(keySentence);
				String domainKey = URLConnectionUtils.getDomainKey(url);
				if (!urlLink.equals("") && urlLink.contains(domainKey)) {
//					System.out.println(urlLink);
					getURLKey(new URL(urlLink), returnList, depth - 1);
				}
			}
		}

		return returnList;

	}

	private static Matcher getHyperLinkMatcher(URL url) {
		String keywordSearchString = "<a\\s[^>]*href\\s*=\\s*\"([^\"]*)\"[^>]*>(.*?)</a>";
		Pattern p2 = Pattern.compile(keywordSearchString,Pattern.CASE_INSENSITIVE);
		String urlContent = URLConnectionUtils.getUrlContent(url);
		Matcher matcher2 = p2.matcher(urlContent);
		return matcher2;
	}

	private static String getKeySentence(Matcher matcher2) {
		String a = matcher2.group(2).replaceAll("<[^>]*>|</[^>]*>", "");
		if (a.contains(keyword)) {
			return a;
		}
		return null;
	}

	private static void testRegex(String keyword, URL searchURL,
			String paraseString) throws IOException {
		String urlContent = URLConnectionUtils.getUrlContent(searchURL);
		System.out.println(searchURL);
		Pattern p = Pattern.compile(paraseString,Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(urlContent);
		int n = 0;
		while (matcher.find() != false) {
//			 System.out.println(matcher.group());
String pp=matcher.group(1).replaceAll(
		"<[a-zA-Z]+[^>]*>|</[a-zA-Z]*[^>]*>", "");
			if (pp.contains(keyword)) {
				System.out.println(++n + ": " + pp);
			}
		}
//		System.out.println("endTestRegex");

	}

}
