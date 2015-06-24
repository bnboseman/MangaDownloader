package com.nicholeboseman;

import java.io.File;
import java.util.ArrayList;

public abstract class Site {
	protected ArrayList<Manga> manga;
	protected String titleList;
	protected String siteUrl;
	abstract protected void setMangaList();
	abstract public void downloadChapter( Chapter chapter, File location);	
	abstract public void setMangaChapters( Manga manga);
	abstract public  void setMangaChapters( String manga);
	abstract public  void setMangaChapters( int index);
	abstract public ArrayList<String> setUpPageUrls(Chapter chapter );
	abstract public int getNumberOfPages(String url);
	

	public ArrayList<Chapter> getMangaChapters( String manga ) {
		return ( getManga( manga ).getChapters() );
	}
	
	public ArrayList<Chapter> getMangaChapters( Manga manga ) {
		return ( manga.getChapters() );
	}
	
	public ArrayList<Chapter> getMangaChapters( int index ) {
		Manga manga = this.manga.get( index );
		return ( manga.getChapters() );
	}

	public ArrayList<String> getMangaTitles() {
		ArrayList<String> titles = new ArrayList<String>();
		
		for (int i = 0; i< manga.size(); ++i ) {
			titles.add(manga.get(i).getTitle() );
		}
		
		return titles;
	}
	
	public Manga getManga(int index ) {
		return manga.get( index );
	}
	
	public Manga getManga( String name ) {
		return manga.get( manga.indexOf(new Manga(name) ) );
	}
	
	public String getMangaLink( String title ) {
		try {
				return manga.get( manga.indexOf(new Manga( title ) ) ).getUrl();
		} catch (ArrayIndexOutOfBoundsException e ) {
			return null;
		}
	}
	
	public String getMangaLink( int index ) {
		try {
				return manga.get( index ).getUrl();
		} catch (ArrayIndexOutOfBoundsException e ) {
			return null;
		}
	}
	
	public ArrayList<Manga> getMangaList() {
		return manga;
	}
}
