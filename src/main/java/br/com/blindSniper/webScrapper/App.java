package br.com.blindSniper.webScrapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class App {
    public static void main( String[] args ) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException {
       	WebClient client = new WebClient();
    	HtmlPage page = null;
    	File volume;
    	BufferedWriter writer;
    	
    	int chapQnt = 60;
    	int initChapNum = 320;
    	int volNum = Math.round(initChapNum / 20);
    	String firstUrl = "https://www.wuxiaworld.com/novel/overgeared/og-chapter-" + initChapNum;
    	StringBuilder fileName = new StringBuilder("/home/david-franco/Documents/personal/ebooks/overgeared/Overgeared_vol-" + volNum + ".md");
    	
    	client.getOptions().setCssEnabled(false);
    	client.getOptions().setJavaScriptEnabled(false);
    	
    	page = client.getPage(firstUrl);
    	
    	volume = new File(fileName.toString());
    	
    	if (volume.exists()) {volume.delete();}
    	
    	writer = new BufferedWriter(new FileWriter(fileName.toString(), true));
    	
    	writer.append("% Overgeared Vol. " + volNum + "\n");
    	writer.append("% Author: Park Saenal \n");
    	writer.append("% Chapters " + initChapNum + " to " + (initChapNum + 20) + "\n\n");
    	
    	for (int chapNum = initChapNum; chapNum < (initChapNum + chapQnt); chapNum++) {
    		if (chapNum > initChapNum && chapNum % 20 == 0) {
    			
    			writer.append("\n" + "----> END OF VOL " + volNum + " <----");
    			
    			writer.close();
    			
    			volNum ++;
    			fileName.delete(0, fileName.length());
	        	fileName.append("/home/david-franco/Documents/personal/ebooks/overgeared/Overgeared_vol-" + volNum + ".md");

	        	volume = new File(fileName.toString());

	        	if (volume.exists()) {volume.delete();}
	        	
	        	writer = new BufferedWriter(new FileWriter(fileName.toString(), true));
	        	
	        	writer.append("% Overgeared Vol. " + volNum + "\n");
	        	writer.append("% Author: Park Saenal \n");
	        	writer.append("% Chapters " + chapNum + " to " + (chapNum + 20) + "\n\n");
    		}
    		//writer.append("\n" + "volNum: "+ volNum + "\t" + "chapNum: " + chapNum);
    		
    		System.out.println("Getting Data from " + page.getUrl());
    		
			@SuppressWarnings("unchecked")
			List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//div[@class='fr-view']/*[not(name()='a')]") ;
			
			for (HtmlElement item : items) {
    			String itemText = item.asText();
    			if (itemText.matches("^Glossary.*Korean.*")) {
    				break;
    			} else {	    			
	    			/* if (itemText.matches("^\\[.*||^\\*.*||^(Rating|Attack|Weight|Durability|Skill).*\\:.*||^Lv\\..*")) {
	    				writer.append("\n\t" + itemText);
	    			} else */ if (itemText.matches("^Chapter.*")) { 
	    				writer.append("\n# " + itemText + "\n");
	    			}else {
	    				writer.append("\n" + itemText + "\n");
	    			}
    			}
			}
			
			writer.append("*************************\n");
			
    		System.out.println("Done Writing " + page.getUrl() + "\n\t to " + fileName.toString() + "\n Waiting 5s...");

    		Thread.sleep(5000);
    		
    		HtmlAnchor nextPageLink = page.getAnchorByText("Next Chapter");
    		
    		page = nextPageLink.click();
    	}
    	
    	writer.append("\n" + "----> END OF VOL " + volNum + " <----");
    	
    	writer.close();
    	client.close();

    }
}
