package com.nicholeboseman;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Manga {
	private String title;
	protected String url = null;
	private ArrayList<Chapter> chapters = new ArrayList<Chapter>();
	
	Manga( String title, String url ) {
		this.title = title;
		this.url = url;
	}
	
	Manga( String title ) {
		this.title = title;
		this.url = null;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}
	
	public String toString() {
		String returnString = this.title + " (" + this.url + ")";
		return returnString;
	}
	
	/** 
	 * Overright equals and hashcode to compare Manga just by title.
	 * Url Shouldn't matter
	 */
	public int hashCode() {
		return new HashCodeBuilder( 31, 5).
				append(title).
				toHashCode();
	}
	
	public boolean equals( Object obj) {
		if ((obj instanceof String) && (obj.equals(this.title) ) ) {
			return true;
		} else if (!(obj instanceof Manga)) {
			return false;
		}
		
		if (obj == this ) {
			return true;
		}
		
		Manga rhs = (Manga) obj;
		return new EqualsBuilder().
				append(title, rhs.title).
				isEquals();
	}

	public ArrayList<Chapter> getChapters() {
		return chapters;
	}

	public void setChapters(ArrayList<Chapter> chapters) {
		this.chapters = chapters;
	}
	
	public Chapter getChapter( int index ) {
		return chapters.get( index );
	}
}
