import java.awt.Color;

public class SearchThread implements Runnable {
	String url;
	String keyword;
	boolean c;
	int depth;

	public SearchThread(String url, String keyword, boolean c, int depth) {
		this.url = url;
		this.keyword = keyword;
		this.c = c;
		this.depth = depth;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		URLConnectionUtils urlconn; 
		if(c){
		urlconn= new URLConnectionIgnoreCase(url, keyword);
		}else{
		urlconn = new URLConnectionUtils(url, keyword);
		}
		// System.out.println(urlconn.getUrl());
		try {
			urlconn.getURLKey(urlconn.getUrl(), 2);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SimFontEnd.resultArea.setForeground(Color.BLUE);
		StringBuilder sb = new StringBuilder();
		sb.append("Here is hyperlinks\n");
		sb.append("--------------------------------------------------------------------------------------------------------------这是一条华丽丽的分割线亮瞎你的氪黄金狗眼\n");
		sb.append(urlconn.getHyperLinkTestList());
		sb.append("Here is h\n");
		sb.append("--------------------------------------------------------------------------------------------------------------这是一条华丽丽的分割线亮瞎你的氪黄金狗眼\n");
		sb.append(urlconn.gethCon());
		sb.append("Here is p\n");
		sb.append("--------------------------------------------------------------------------------------------------------------这是一条华丽丽的分割线亮瞎你的氪黄金狗眼\n");
		sb.append(urlconn.getpCon());
	
		SimFontEnd.resultArea.setText(sb.toString());
		
		urlconn.dataList.clearAll();
		
	}

}
