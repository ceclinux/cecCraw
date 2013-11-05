import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.font.ImageGraphicAttribute;
import java.io.*;

public class URLConnectionUtils {
	
	protected static final String GET_URL_REGEX = ".*href=\"([^\"]*)\".*";
	protected static final String A_TAG_REGEX = "<[a-zA-Z]+[0-9]?[^>]*>|</[a-zA-Z]+[0-9]?[^>]*>";
	protected static final String HYPERLINK_REGEX = "<a\\s[^>]*href\\s*=\\s*\"([^\"]*)\"[^>]*>(.*?)</a>";
	protected static HashMap<String, String> regex_content_map = new HashMap<String, String>();
	protected static HashSet<String> pCon = new MyHashSet<String>();
	protected static HashSet<String> hCon = new MyHashSet<String>();
	protected static HashSet<String> hyperLinkTestSet = new MyHashSet<String>();
	protected static MyArrayList<HashSet<String>> dataList = new MyArrayList<HashSet<String>>();

	public URL getUrl() {
		return url;
	}

	public void setUrl(String url) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("This is not a valid URL");
		}
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	private URL url;
	protected String keyword;

	public URLConnectionUtils(String url, String keyword) {
		try {
			this.url = new URL(url);
			this.keyword = keyword;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("This is not a valid URL");

		}
		regex_content_map.put("h",
				"<[hH][1-6][\\-\\s\"=A-Za-z0-9:^>]*>(.*?)</[hH][1-6]>");
		regex_content_map.put("p",
				"<[pP][\\sa-zA-Z0-9\":\\-=_^>]*>(.*?)</[pP]>");
		dataList.add(pCon);
		dataList.add(hCon);
		dataList.add(hyperLinkTestSet);
	}

	public HashSet<String> getHyperLinkTestList() {
		return hyperLinkTestSet;
	}

	public void setHyperLinkTestSet(HashSet<String> hyperLinkTestSet) {
		this.hyperLinkTestSet = hyperLinkTestSet;
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
	public static String getDomainKey(URL url) {
		String host = url.getHost();
		String domainKey;

		if (!host.split("\\.")[0].equals("www")) {
			domainKey = host;
		} else {
			domainKey = host.replaceAll("[^\\.]*\\.((\\w+\\.)+\\w+)", "$1");

		}
		// System.out.println(domainKey);
		return domainKey;
	}

	// private static void filterRecur(String keyword,
	// LinkedList<String> returnList, String urlLink, String keySentence)
	// throws Exception {
	//
	// }
	public static String getUrlContent(URL url) {
		try {
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
		} catch (IOException e) {
			System.out.println("Sorry,can't open connection");
		}
		return null;
	}

	/**
	 * catch the HTTP header:Content-Type,the problem is,some http header
	 * doesn't include Content-type
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String getCharset(URL url) throws IOException {
		URLConnection conn = url.openConnection();
		Map<String, List<String>> map = conn.getHeaderFields();
		List<?> l = map.get("Content-Type");
		// System.out.println(l.toString());
		String regex = ".*charset\\=(.+)[\\W\\D^-]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(l.toString());
		String find = null;
		while (m.find() != false) {
			find = m.group(1);
		}
		// System.out.println(find);
		return find == null ? deepDectectChar(conn) : find;
	}

	/**
	 * If there no content-type in http header(eg sina.com.cn), try to analysis
	 * html <meta tag content to detect encoding
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
				// System.out.println(inputLine);
				String[] charSetArray = inputLine.replaceAll("<|/>|\"", "")
						.split("=");

				for (int i = 0; i < charSetArray.length; i++) {
					// System.out.println(charSetArray[i].trim());
					if ((charSetArray[i].trim()).endsWith("charset")) {
						// System.out.println(charSetArray[i+1].split(" ")[0]);
						return charSetArray[i + 1].split(" ")[0];

					}
				}

			}
		}
		return "UTF-8";

	}

	/**
	 * 
	 * @param recursive
	 *            url to search the key word
	 * @param returnList
	 * @param depth
	 *            the current depth,when enter a new deeper url,depth - 1
	 * @return
	 * @throws Exception
	 */
	public void getURLKey(URL url, int depth) throws Exception {

		if (depth > 0) {

			// get paragraph
			System.out.println("url: " + url);
			getUrlText(url, "p", pCon);

			// get header
			getUrlText(url, "h", hCon);

			Matcher hyperMatcher = getHyperLinkMatcher(url);

			while (hyperMatcher.find() != false) {
				// the first group of the hyperMatcher is the url
				// System.out.println(hyperMatcher.group());
				String urlLink = hyperMatcher.group(1).replaceAll(
						GET_URL_REGEX, "$1");
				// System.out.println(urlLink);
				try {
					// make the new url
					// System.out.println("urlLink: "+urlLink);
					URL y = new URL(url, urlLink);
					String newURL = y.toString();

					// get the keySentence of the new URL
					String keySentence = getKeySentence(hyperMatcher);
					// if the array which stores the key sentence has already
					// had this sentence
					if (!hyperLinkTestSet.contains(keySentence)) {
						// add the key sentence
						hyperLinkTestSet.add(keySentence);
						// System.out.println(keySentence);
						// get the domain key word of the url to determine
						// whether this hyperlink is an external link
						String domainKey = URLConnectionUtils.getDomainKey(url);
						if (!newURL.equals("") && newURL.contains(domainKey)) {
							getURLKey(new URL(newURL), depth - 1);
						}
					}
				} catch (MalformedURLException e) {
					System.out.println("SORRY!!!I can't parse " + urlLink
							+ " now");
					// e.printStackTrace();
				}

			}
		}

	}

	public static HashSet<String> getpCon() {
		return pCon;
	}

	public static void setpCon(HashSet<String> pCon) {
		URLConnectionUtils.pCon = pCon;
	}

	public static HashSet<String> gethCon() {
		return hCon;
	}

	public static void sethCon(HashSet<String> hCon) {
		URLConnectionUtils.hCon = hCon;
	}

	/**
	 * 
	 * @param url
	 * @return the matcher that find and parse the hyperLink of the url
	 */
	protected static Matcher getHyperLinkMatcher(URL url) {
		String keywordSearchString = HYPERLINK_REGEX;
		Pattern p2 = Pattern.compile(keywordSearchString);
		String urlContent = URLConnectionUtils.getUrlContent(url);
		Matcher matcher2 = p2.matcher(urlContent);
		return matcher2;
	}

	protected String getKeySentence(Matcher hyperMatcher) {
		// the second group of the hyperMatch is the keysentence
		String keySent = hyperMatcher.group(2).replaceAll("<[^>]*>|</[^>]*>",
				"");
		if (keySent.contains(keyword)) {
			return keySent;
		}
		return null;
	}

	protected void getUrlText(URL url, String contentTag, HashSet<String> h)
			throws IOException {
		String urlContent = URLConnectionUtils.getUrlContent(url);
		// System.out.println("urlContent: "+urlContent);
		try {
			Pattern p = Pattern.compile(regex_content_map.get(contentTag));
			Matcher matcher = p.matcher(urlContent);
			while (matcher.find()) {
				String content = matcher.group(1).replaceAll(A_TAG_REGEX, "");
				System.out.println("content: " + content);
				// System.out.println("keyword: "+keyword);
				if (content.contains(keyword)) {
					h.add(content);
				}
			}
		} catch (NullPointerException e) {
			System.out
					.println("Sorry,this content tag has not been supported yet = =");
		}

	}
}
