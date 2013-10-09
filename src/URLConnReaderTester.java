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

		String keywordSearchString = "<a([^>]*)>([^<]*" + keyword
				+ "[^>]*)</a>";
		Pattern p2 = Pattern.compile(keywordSearchString);
		String urlContent = URLConnectionUtils.getUrlContent(url);
		Matcher matcher2 = p2.matcher(urlContent);
		String urlLink = "";
//		System.out.println("下面是标题测试(h)");
//		testRegex(keyword, url, "<h[0-6][A-Za-z0-9]*>(.*?)</h[0-6]>");
		System.out.println("文本测试(p)");
		 testRegex(keyword, url, "<p\\s?[A-Za-z0-9^>]*>(.*?)</p>");
		while (matcher2.find() != false) {
			urlLink = matcher2.group(1).replaceAll(GET_URL_REGEX, "$1");
			String keySentence = matcher2.group(2);
			// System.out.println(m.group());
			// System.out.println(m.group(2));
	
			if (!returnList.contains(keySentence)) {
				returnList.add(keySentence);
				String domainKey = URLConnectionUtils.getDomainKey(url);
				if (!urlLink.equals("") && urlLink.contains(domainKey)) {
					getURLKey(new URL(urlLink), returnList, depth - 1);
				}
			}
		}

		return returnList;

	}

	private static void testRegex(String keyword, URL searchURL,
			String paraseString) throws IOException {
		String urlContent = URLConnectionUtils.getUrlContent(searchURL);
		System.out.println(searchURL);
		Pattern p = Pattern.compile(paraseString);
		Matcher matcher = p.matcher("下面的内容出自 "+urlContent);
		String para = "";
		int n = 0;
		while (matcher.find() != false) {
			if (matcher.group(1).contains(keyword)) {

				para = matcher.group(1).replaceAll(
						"<[a-z]+[^>]*>|</[a-z]*[^>]*>", "");
				System.out.println(++n+": "+para);
			}
		}

	}

}
