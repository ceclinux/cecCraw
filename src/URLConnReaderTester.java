
public class URLConnReaderTester {
	static String URL = "http://www.bbc.co.uk";
	static String KEYWORD = "africa";
	static int DEPTH=2;

	public static void main(String[] args) throws Exception {
		URLConnectionUtils urlconn = new URLConnectionUtils(URL, KEYWORD);
//		System.out.println(urlconn.getUrl());
		urlconn.getURLKey(urlconn.getUrl(), DEPTH);
		System.out.println(urlconn.hyperLinkTestSet);
		for(String h:urlconn.gethCon()){
			System.out.println("h :"+h);
		}
		for(String p:urlconn.getpCon()){
			System.out.println("p :"+p);
		}
	
	}

}
