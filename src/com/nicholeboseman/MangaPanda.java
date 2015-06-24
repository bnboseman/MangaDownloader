package com.nicholeboseman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MangaPanda extends Site {
	
	public MangaPanda() {
		titleList = "http://www.mangapanda.com/alphabetical";
		siteUrl = "http://www.mangapanda.com";
	
		manga = new ArrayList<Manga>();
		this.setMangaList();
	}
	
	@Override
	protected void setMangaList() {
		ArrayList<String> titles = new ArrayList<String>();
		Document doc = null;
		
		
		while ( doc == null ) {
			try {
				doc = Jsoup.connect(titleList).get();
			} catch (IOException e) {
				System.err.println("Could not connect to page");
			}
		}
		
		Elements links = doc.select("ul.series_alpha>li>a");
		for ( Element link : links ) {
			titles.add( link.html() );
			Manga currentTitle = new Manga( link.html(), siteUrl + link.toString().substring( link.toString().indexOf("\"") + 1, link.toString().lastIndexOf("\"") ) );
			//currentTitle.setChapters( getMangaChapters( currentTitle ) );
			manga.add( currentTitle);
		
		}
	}

	
	@Override
	public void setMangaChapters(Manga manga ) {
		System.out.println("Setting up chapters for " + manga.getTitle() );
		ArrayList<Chapter> chapters= new ArrayList<Chapter>();
		Document doc = null;
		Elements links = null;
		String chapterUrl;
		String name = null;
		if (manga.getUrl() != null) {
			while (doc == null ) {
				try {
					doc = Jsoup.connect( manga.getUrl() ).get();
				} catch( IOException e) {
					System.err.println("Could not connect to page");
				}
			}
			
			links = doc.select("#chapterlist td");
			for (Element link : links ) {
				if ( !( link.select( "a").attr("href").isEmpty() ) ) {
					chapterUrl = this.siteUrl + link.select("a").attr("href");
					name = link.select("a").text();
					System.out.println("Setting up chapter " + name);
					Chapter chapter = new Chapter(manga, name, chapterUrl, this.getNumberOfPages( chapterUrl));
					//chapter.setPageLinks( this.setUpPageUrls(chapter) );
					chapters.add( chapter );
					
				}
			}
			
		}
		manga.setChapters(chapters);
	}
	
	@Override
	public void setMangaChapters(int index ) {
		Manga manga = this.manga.get( index );
		setMangaChapters( manga );
	}
	
	@Override
	public void setMangaChapters(String manga ) {
		this.setMangaChapters( getManga( manga ) );
	}

	@Override
	public void downloadChapter( Chapter chapter, File location) {
		
		String cbzFile = null;
		String filename;
		chapter.setPageLinks(  this.setUpPageUrls(chapter) );
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<Integer> failedImages = new ArrayList<Integer>();
		System.out.println(location.getPath() );
		
		if (!location.exists() ) {
			location.mkdir();
		}
		
		cbzFile = location.getPath() +  "/" + chapter.getName().replaceAll("[^A-Za-z0-9]", "_") + ".cbz";
		for (int i = 0; i < chapter.getPageLinks().size(); i++ ) {
			try {
				filename = HttpDownloadUtility.downloadFile(chapter.getPageLinks().get(i), location.toString(), "http://www.mangapanda.com/");
				if (! filename.isEmpty() ) files.add(filename);
			} catch (IOException e) {
				try {
					System.err.println("Something went wrong. Trying again in 5 seconds");
					Thread.sleep(5000);
					filename = HttpDownloadUtility.downloadFile(chapter.getPageLinks().get(i), location.toString(), "http://www.mangapanda.com/");
					if (! filename.isEmpty() ) {
						files.add(filename);
					}
				} catch (IOException | InterruptedException e1) {
					failedImages.add( i );
					e.printStackTrace();
				}
			}
			
		}
		
		for ( int i = 0; i < failedImages.size(); i ++ ) {
			int index = failedImages.get(i);
			filename = null;
			int tries = 0;
			while (filename.isEmpty() && tries < 5) {
				++tries;
				try {
					filename = HttpDownloadUtility.downloadFile(chapter.getPageLinks().get(index), location.toString(), "http://www.mangapanda.com/");
					if (! filename.isEmpty() ) {
						failedImages.remove(i);
						files.add(filename);
					}
				} catch (IOException e ) {
					System.err.println( e.getMessage() );
					if (tries < 4 ) {
						System.out.println( "Waiting 5 seconds to try again...");
						try {
							Thread.sleep( 5000 );
						} catch (InterruptedException e1) {
							
						}
					}
				}
			}
		}
		try {
			HttpDownloadUtility.createZip( cbzFile, files.toArray(new String[files.size()] ));
			HttpDownloadUtility.deleteFiles(  files.toArray(new String[files.size()] ) ); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for( int image : failedImages ) {
			System.err.println("Could not download " + chapter.getPageLinks().get( image ) );
		}
		
	}
	
	public ArrayList<String> setUpPageUrls(Chapter chapter ) {
		Document doc = null;
		Elements images = null;
		
		ArrayList<String> imageLinks = new ArrayList<String>();
		for (int i = 0; i < chapter.getNumberOfPages(); i++) {
			String url = chapter.getUrl() + "/" + String.valueOf(i);
			try {
				doc = Jsoup.connect( url ).get();
			} catch (IOException e ) {
				System.err.println("Could not connect to page.");
			}
			
			images = doc.select("#img[src$=.jpg]");
			if( images.attr("src").contains(".jpg")){
				imageLinks.add( images.attr("src") );
			}
		}
		return imageLinks;
	}
	
	public int getNumberOfPages(String url) {
		int pages = 0;
		Document doc = null;
		Elements elements = null;
		try {
			doc = Jsoup.connect( url ).get();
		} catch (IOException e ) {
			System.err.println("Could not connect to page.");
			return pages;
		}
		
		elements = doc.select("#pageMenu");
		
		return (int) new Integer( elements.last().getElementsByTag("option").last().text()) ;
	}
}
