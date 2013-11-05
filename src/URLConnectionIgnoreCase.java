import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class URLConnectionIgnoreCase extends URLConnectionUtils{

	public URLConnectionIgnoreCase(String url, String keyword) {
		super(url, keyword.toLowerCase());
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setKeyword(String keyword) {
		this.keyword = keyword.toLowerCase();
	}
	
	@Override
	public void getURLKey(URL url, int depth) throws Exception {

		if (depth > 0) {

			// get paragraph
			System.out.println("url: "+url);
			getUrlText(url, "p",pCon);
		
			// get header
			getUrlText(url, "h",hCon);

			Matcher hyperMatcher = getHyperLinkMatcher(url);
	

			while (hyperMatcher.find() != false) {
				// the first group of the hyperMatcher is the url
//				System.out.println(hyperMatcher.group());
				String urlLink = hyperMatcher.group(1).replaceAll(
						GET_URL_REGEX, "$1");
//				System.out.println("urlLink: "+urlLink);
				try{
					// make the new url
//					System.out.println("urlLink: "+urlLink);
					URL y = new URL(url, urlLink);
					String newURL=y.toString();
					
//					System.out.println("newURL: "+newURL);
					// get the keySentence of the new URL
					String keySentence = getKeySentence(hyperMatcher);
					// if the array which stores the key sentence has already
					// had this sentence
					if (!hyperLinkTestSet.contains(keySentence)) {
						// add the key sentence
						hyperLinkTestSet.add(keySentence);
//						System.out.println(keySentence);
						// get the domain key word of the url to determine
						// whether this hyperlink is an external link
						String domainKey = URLConnectionUtils.getDomainKey(url);
//						System.out.println("domainKey:"+domainKey);
						if (!newURL.equals("") && newURL.contains(domainKey)) {
							getURLKey(new URL(newURL), depth - 1);
						}
					}
				} catch (MalformedURLException e) {
					System.out.println("SORRY!!!I can't parse " + urlLink + " now");
//					e.printStackTrace();
				}

			}
		}

	}
	
	@Override
	protected void getUrlText(URL url, String contentTag,HashSet<String> h) throws IOException {
		String urlContent = URLConnectionUtils.getUrlContent(url);
//		 System.out.println("urlContent: "+urlContent);
		try {
			Pattern p = Pattern.compile(regex_content_map.get(contentTag),Pattern.CASE_INSENSITIVE);
			Matcher matcher = p.matcher(urlContent);
			while (matcher.find()) {
				String content = matcher.group(1).replaceAll(A_TAG_REGEX, "");
//				System.out.println("content: "+content);
//				System.out.println("keyword: "+keyword);
				if ((content.toLowerCase()).contains(keyword)) {
					h.add(content);
					System.out.println("added-content: "+content);
					System.out.println("content-url: "+url);
				}
			}
		} catch (NullPointerException e) {
			System.out
					.println("Sorry,this content tag has not been supported yet = =");
		}

	}
	protected static Matcher getHyperLinkMatcher(URL url) {
		String keywordSearchString = HYPERLINK_REGEX;
		Pattern p2 = Pattern.compile(keywordSearchString,Pattern.CASE_INSENSITIVE);
		String urlContent = URLConnectionUtils.getUrlContent(url);
		Matcher matcher2 = p2.matcher(urlContent);
		return matcher2;
	}
	protected String getKeySentence(Matcher hyperMatcher) {
		// the second group of the hyperMatch is the keysentence
		String keySent = hyperMatcher.group(2)
				.replaceAll("<[^>]*>|</[^>]*>", "");
		if ((keySent.toLowerCase()).contains(keyword)) {
			return keySent;
		}
		return null;
	}
}
