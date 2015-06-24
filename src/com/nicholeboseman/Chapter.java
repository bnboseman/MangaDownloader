package com.nicholeboseman;

import java.util.ArrayList;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Chapter {
	private String name;
	private String url = null;
	private Manga manga = null;
	private int pages;
	private ArrayList<String> pageLinks;
	
	Chapter( Manga manga, String name, String url,int  pages) {
		this.manga = manga;
		this.name = name;
		this.url = url;
		this.pages = pages;
	}
	
	public String toString() {
		return name;
	}
	public int hashCode() {
		return new HashCodeBuilder( 31, 5).
				append(manga.getTitle()).
				append(name).
				toHashCode();
	}
	
	public boolean equals( Object obj) {
		if (!(obj instanceof Chapter)) {
			return false;
		}
		
		if (obj == this ) {
			return true;
		}
		
		Chapter rhs = (Chapter) obj;
		return new EqualsBuilder().
				append(manga.getTitle(), rhs.manga.getTitle()).
				append(name, rhs.name).
				isEquals();
	}

	public String getUrl() {
		return url;
	}
	
	public String getName() {
		return name;
	}
	
	public int getNumberOfPages() {
		return pages;
	}
	
	public void setPageLinks( ArrayList<String> pageUrls ) {
		this.pageLinks = pageUrls;
	}

	public ArrayList<String> getPageLinks() {
		return pageLinks;
	}
	
	public String getMangaTitle() {
		return this.manga.getTitle();
	}

}
