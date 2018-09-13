package cochrane_ronak;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class ExtractLoadReviews {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println();	
		System.out.println( new Date().toString());	
		//Right now i cannot resolve topic in the URL so programming only for Allergy and intorelance.
		//String topic = args[0];
		String topic = "Allergy & intolerance";
		System.out.println("Reviews for Topic:" + topic); //For Log Console
		String fileName = "cochrane_reviews.txt";
		
		//Initial Title to file
		try { 
            BufferedWriter out = new BufferedWriter( 
                          new FileWriter(fileName)); 
            out.write("\n"+ "Reviews for Topic:" + topic); 
            out.close(); 
        } 
        catch (IOException e) { 
            System.out.println("Exception Occurred" + e); 
        } 
		
		//****************************************************************************
		// Connecting to the URL
		String url = "https://www.cochranelibrary.com/search?p_p_id=scolarissearchresultsportlet_WAR_scolarissearchresults&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchText=*&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchType=basic&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryField=topic_id&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchBy=6&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetDisplayName=Allergy+%26+intolerance&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryTerm=z1506030924307755598196034641807&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetCategory=Topics";
        
//		List<NameValuePair> parameters = new ArrayList<>();
//		parameters.add(new BasicNameValuePair("p_p_id", "scolarissearchresultsportlet_WAR_scolarissearchresults"));
//		parameters.add(new BasicNameValuePair("p_p_lifecycle", "0"));
//		parameters.add(new BasicNameValuePair("p_p_state", "normal"));
//		parameters.add(new BasicNameValuePair("p_p_mode", "view"));
//		parameters.add(new BasicNameValuePair("p_p_col_id", "column-1"));
//		parameters.add(new BasicNameValuePair("p_p_col_count", "1"));
//		parameters.add(new BasicNameValuePair("_scolarissearchresultsportlet_WAR_scolarissearchresults_searchText", "*"));
//		parameters.add(new BasicNameValuePair("_scolarissearchresultsportlet_WAR_scolarissearchresults_searchType", "basic"));
//		parameters.add(new BasicNameValuePair("_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryField", "topic_id"));
//		parameters.add(new BasicNameValuePair("_scolarissearchresultsportlet_WAR_scolarissearchresults_searchBy", "6"));
//		parameters.add(new BasicNameValuePair("_scolarissearchresultsportlet_WAR_scolarissearchresults_facetDisplayName", topic));
//		parameters.add(new BasicNameValuePair("_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryTerm", ""));
//		parameters.add(new BasicNameValuePair("_scolarissearchresultsportlet_WAR_scolarissearchresults_facetCategory", "Topics"));
		
		
		
		
		try {
//		URI uri2 = new URIBuilder().setScheme("https").setHost("www.cochranelibrary.com").setPath("/search").addParameters(parameters).build();
		
			URI uri = new URI(url);
		HttpGet requestGet = new HttpGet(uri);
		requestGet.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 Firefox/26.0 Chrome/68.0.3440.106 Safari/537.36");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.client.protocol.ResponseProcessCookies", "fatal");
//		
		HttpClient client = HttpClientBuilder.create()
	            .setDefaultRequestConfig(RequestConfig.custom()
	                    .setCookieSpec(CookieSpecs.STANDARD).build()).build();
//		HttpClient client = HttpClientBuilder.create().build();
		
		
		HttpResponse response = client.execute(requestGet);
		HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
		
        List<String> URLs = new ArrayList<>();
        List<String> Titles = new ArrayList<>();
        List<String> Authors = new ArrayList<>();
        List<String> Dates = new ArrayList<>();
        
        //Using Regular Expression library of Java for pattern matching through the string object.
        
        Matcher m = Pattern.compile(
                Pattern.quote("<h3 class=\"result-title\"> <a target=\"_blank\" href=")
                + "(.*?)"
                + Pattern.quote("</a> </h3> <div class=\"search-result-authors\">")
       ).matcher(responseString);
        
        
        Matcher auth = Pattern.compile(
                Pattern.quote("<div class=\"search-result-authors\"> <div>")
                + "(.*?)"
                + Pattern.quote("</div> </div> <div class=\"search-result-metadata-block\">")
       ).matcher(responseString);
        
        Matcher datePattern = Pattern.compile(
                Pattern.quote("<div class=\"search-result-date\"> <div>")
                + "(.*?)"
                + Pattern.quote("</div> </div> </div> <div class=\"search-result-metadata-item\">")
       ).matcher(responseString);
        
        while(m.find()){
            String match = m.group(1).substring(1); //Removing the first character ".
//            String test = match.split("\">",-1)[0];
//            System.out.println(test);
            //System.out.println(match);
            //here you insert 'match' into the list
            URLs.add("https://www.cochranelibrary.com"+match.split("\">",-1)[0]);
            Titles.add(match.split("\">",-1)[1]);
        }
        
        while(auth.find()) {
        	String a_match = auth.group(1);
            //System.out.println(match);
            //here you insert 'match' into the list
        	Authors.add(a_match);  
        }
        
        while(datePattern.find()) {
        	String d_match = datePattern.group(1);
            //System.out.println(match);
            //here you insert 'match' into the list
        	Dates.add(d_match);  
        }    
        
        for (int i=0;i<URLs.size();i++) {
        	appendStrToFile(fileName, URLs.get(i)+"|"+topic+"|"+Titles.get(i)
        					+"|"+  Authors.get(i) +"|"+ Dates.get(i));
        }
        
        
//  System.out.println(URLs.size() +" " + Titles.size());
        
        
        System.out.println(
				  "HTTP Status of response: " + response.getStatusLine().getStatusCode());
        
        
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		
		
		
		
		//****************************************************************************
		System.out.println("Reviews can be found in the text file - cochrane_reviews.txt");	
		System.out.println( new Date().toString());	
	}
	
	public static void appendStrToFile(String fileName, String str) { 
		
		try {
			// Open given file in append mode. 
			BufferedWriter out = new BufferedWriter( 
			new FileWriter(fileName, true)); 
			out.newLine();
			out.newLine();
			out.write("\n\n" + str); 
			out.close();
		} 
		catch (IOException e) { 
			System.out.println("exception occoured" + e); 
		} 
	} 

}
