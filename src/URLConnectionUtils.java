import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class URLConnectionUtils {
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
	private String keyword;

	public URLConnectionUtils(String url, String keyword) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("This is not a valid URL");

		}
		this.keyword = keyword;
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
		List l = map.get("Content-Type");
		// System.out.println(l.toString());
		String regex = ".*charset\\=(.+)[\\W\\D^-]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(l.toString());
		String find = null;
		while (m.find() != false) {
			find = m.group(1);
		}
//		System.out.println(find);
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
//				System.out.println(inputLine);
				String[] charSetArray = inputLine.replaceAll("<|/>|\"", "")
						.split("=");

				for (int i = 0; i < charSetArray.length; i++) {
//					System.out.println(charSetArray[i].trim());
					if ((charSetArray[i].trim()).endsWith("charset")) {
//						System.out.println(charSetArray[i+1].split(" ")[0]);
						return charSetArray[i + 1].split(" ")[0];

					}
				}

			}
		}
		return "UTF-8";

	}
}
