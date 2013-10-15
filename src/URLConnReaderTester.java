import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLConnReaderTester {
//	static HashMap<String, Character> htmlSpecEnti = new HashMap<String, Character>();

	private static final String GET_URL_REGEX = ".*href=\"([^\"]*)\".*";
	static URLConnectionUtils urlconn = new URLConnectionUtils(
			"http://www.sina.com.cn", "李克强");
	static String keyword = urlconn.getKeyword();
	static URL url = urlconn.getUrl();

	public static void main(String[] args) throws Exception {
//		htmlSpecEnti.put("&amp;", '&');
//		htmlSpecEnti.put("&quot;", '\"');
//		htmlSpecEnti.put("&lt;", '<');
//		htmlSpecEnti.put("&gt;", '>');
//		htmlSpecEnti.put("&circ;", 'ˆ');
//		htmlSpecEnti.put("&tilde;", '˜');
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

		// System.out.println("下面是标题测试(h)");
//		 testRegex(keyword, url,"<h[0-6][\\s\"=A-Za-z0-9^>]*>(.*?)</h[0-6]>");
		// System.out.println("文本测试(p)");
		testRegex(keyword, url, "<p[\\sa-z0-9\"\\-=_^>]*>(.*?)</p>");
		// <p[\\s a-z0-9\"=-^>]*>(.*?)</p>
		String urlLink = "";
		while (matcher2.find() != false) {
			// System.out.println("enter find");
//			 System.out.println(matcher2.group());
			urlLink = matcher2.group(1).replaceAll(GET_URL_REGEX, "$1");

			if (!urlLink.contains("."))
				continue;
			// System.out.println(urlLink);
//			for (String a : htmlSpecEnti.keySet()) {
//				if (urlLink.contains(a)) {
//					// System.out.println("special urlLink: "+urlLink);
//					urlLink = urlLink.replaceAll(a, htmlSpecEnti.get(a) + "");
//				}
//			}
			// System.out.println("new url: "+urlLink);
			// System.out.println("old url: "+url.toString());
//			String equalPart = "";
//			char c;
//			int slashAtnew = urlLink.in
//			int slashAtold = urlLink.lastIndexOf("/");
//			if (slashAtnew != -1) {
//
//				while((c=urlLink.charAt(slashAtnew))==url.toString().charAt(slashAtold)){
//					equalPart=c+equalPart;
//				}
//			}
//			urlLink = equalPart + urlLink.substring(i, urlLink.length());
//			System.out.println(urlLink);
			try{
			URL y=new URL(url,urlLink);
			urlLink=y.toString();

			// System.out.println("final url: "+urlLink);
			String keySentence = getKeySentence(matcher2);
			// System.out.println(m.group());
			// System.out.println(m.group(2));

			if (!returnList.contains(keySentence)) {
				returnList.add(keySentence);
				String domainKey = URLConnectionUtils.getDomainKey(url);
				if (!urlLink.equals("") && urlLink.contains(domainKey)) {
					System.out.println(urlLink);
					getURLKey(new URL(urlLink), returnList, depth - 1);
				}
			}
			}
			catch(Exception e){
				System.out.println("SORRY!!!I can't parse "+urlLink+"now");
			}
			
		}

		return returnList;

	}

	private static Matcher getHyperLinkMatcher(URL url) {
		String keywordSearchString = "<a\\s[^>]*href\\s*=\\s*\"([^\"]*)\"[^>]*>(.*?)</a>";
		Pattern p2 = Pattern.compile(keywordSearchString,
				Pattern.CASE_INSENSITIVE);
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
		Pattern p = Pattern.compile(paraseString, Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(urlContent);
		int n = 0;
		while (matcher.find() != false) {
			// System.out.println(matcher.group());
			String pp = matcher.group(1).replaceAll(
					"<[a-zA-Z]+[^>]*>|</[a-zA-Z]*[^>]*>", "");
			if (pp.contains(keyword)) {
				System.out.println(++n + ": " + pp);
				// System.out.println(matcher.group());
			}
		}
		// System.out.println("endTestRegex");

	}

}
