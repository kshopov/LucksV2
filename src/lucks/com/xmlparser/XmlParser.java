package lucks.com.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;
import lucks.com.model.Luck;

public class XmlParser {

	private Luck currentLuck;
	private String currentTag;
	private List<Luck> lucks = new ArrayList<Luck>();

	public List<Luck> parseXml(String url) throws ClientProtocolException, 
			URISyntaxException, IOException {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			
			xpp.setInput(new InputStreamReader(getUrlData(url)));
			
			int eventType = xpp.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT) {
				if(eventType == XmlPullParser.START_TAG) {
					handleStartTag(xpp.getName());
				} else if(eventType == XmlPullParser.END_TAG) {
					currentTag = null;
				} else if(eventType == XmlPullParser.TEXT) {
					handleText(xpp.getText());
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lucks;
	}
	
	private void handleText(String text) {
		String xmlText = text;
		if(currentLuck != null && currentTag != null) {
			if(currentTag.equals("id")) {
				Integer id = Integer.parseInt(xmlText);
				currentLuck.setId(id);
			} else if(currentTag.equals("luckText")) {
				currentLuck.setLuckText(xmlText);
			} else if(currentTag.equals("author")) {
				currentLuck.setLuckAuthor(xmlText);
			} else if(currentTag.equals("langId")) {
				Integer langId = Integer.parseInt(xmlText);
				currentLuck.setLuckLanguageId(langId);
			}
		}
	}
	
	private void handleStartTag(String name) {
		if(name.equals("luck")) {
			currentLuck = new Luck();
			lucks.add(currentLuck);
		} else {
			currentTag = name;
		}
	}

	private InputStream getUrlData(String url) throws URISyntaxException,
			ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet method = new HttpGet(new URI(url));
		HttpResponse response = client.execute(method);

		return response.getEntity().getContent();
	}
}
