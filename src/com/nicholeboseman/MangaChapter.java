package com.nicholeboseman;

import java.io.File;
import java.util.ArrayList;

import org.jsoup.nodes.Document;

public class MangaChapter extends Site {

	MangaChapter() {
		titleList = "http://www.mangachapter.me/mangalist.html";
		siteUrl = "http://www.mangachapter.me";
		
		manga = new ArrayList<Manga>();
		this.setMangaList();
	}
	@Override
	protected void setMangaList() {
		ArrayList<String> titles = new ArrayList<String>();
		Document doc = null;

	}

	@Override
	public void downloadChapter(Chapter chapter, File location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMangaChapters(Manga manga) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMangaChapters(String manga) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMangaChapters(int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<String> setUpPageUrls(Chapter chapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfPages(String url) {
		// TODO Auto-generated method stub
		return 0;
	}

}
