package uk.ac.gla.dcs.bigdata.studentfunctions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.util.LongAccumulator;

import uk.ac.gla.dcs.bigdata.providedstructures.ContentItem;
import uk.ac.gla.dcs.bigdata.providedstructures.NewsArticle;
import uk.ac.gla.dcs.bigdata.providedutilities.TextPreProcessor;

public class DocumentTermsProcessingFlatMap implements FlatMapFunction<NewsArticle, NewsArticle> {
	LongAccumulator documentLengthAccumulator;
	LongAccumulator totalDocsInCorpusAccumulator;
	
	public DocumentTermsProcessingFlatMap(LongAccumulator documentLengthAccumulator,
			LongAccumulator totalDocsInCorpusAccumulator) {
		super();
		this.documentLengthAccumulator = documentLengthAccumulator;
		this.totalDocsInCorpusAccumulator = totalDocsInCorpusAccumulator;
	}
	
	@Override
	public Iterator<NewsArticle> call(NewsArticle t) throws Exception{
		TextPreProcessor textprocessor = new TextPreProcessor();
		List<ContentItem> termsInContent = t.getContents();
//		System.out.println("---------------------------------------------");
//		System.out.println(termsInContent);
		List<ContentItem> stemedContent = new ArrayList<ContentItem>();
//		try {
		for (ContentItem contentItem : termsInContent) {
//			System.out.println(contentItem.getContent() +"SUBTYPE START"+ contentItem.getSubtype()+"SUBTYPE END--");
//			System.out.println("Content Start" + contentItem);
			System.out.println("Content ENDS");
			if (contentItem != null){
			if( contentItem.getContent() != null &  contentItem.getSubtype() != null){
			if("paragraph".equals(contentItem.getSubtype().toLowerCase())) {
			String content = "";
			List<String> tokens = textprocessor.process(contentItem.getContent());
			for (String token : tokens) {
				content = content + " " + token;
			}
			documentLengthAccumulator.add(tokens.size());
			contentItem.setContent(content);
			stemedContent.add(contentItem);
			}
			}
			}
		}
//		}
//		catch(Exception e) {
//			
//			System.out.println(e.getMessage());
//		}
		t.setContents(stemedContent);
		List<NewsArticle> newsArticle = new ArrayList<NewsArticle>(1);
		if (t.getTitle() == null || t.getTitle().isEmpty() || t.getTitle().trim().isEmpty()) {
			
		}
		else {
		totalDocsInCorpusAccumulator.add(1);
		newsArticle.add(t);
		}
		return newsArticle.iterator();
	}


}
