package com.nicholeboseman;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Downloader {

	public static void main(String[] args) {
		String response = "";
		int mangaIndex;
		int chapterNumber;
		Manga manga;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		MangaPanda mp = new MangaPanda();
		int i = 0;
		for( String title : mp.getMangaTitles() ) {
			System.out.println(i + ": " + title);
			++i;
		}
		
		while ( response != "exit" ) {
			
			try {
				printMenu(1);
				response = br.readLine();
				mangaIndex = Integer.parseInt(response);
				manga = mp.getManga( mangaIndex );
				mp.setMangaChapters( manga );
				i = 0;
				for ( Chapter chapterTitle : manga.getChapters() ) {
					System.out.println( i + ":" + chapterTitle.getName() );
					++i;
				}
				printMenu(2);
				response = br.readLine();
				
				chapterNumber = Integer.parseInt(response);
				mp.downloadChapter(manga.getChapter( chapterNumber), new File("/Users/Sirius/Downloads/manga/" + manga.getTitle().replaceAll("[^A-Za-z0-9]", "_") + "/" ) );
				
				

	        } catch (IOException e) {
	        	response = "exit";
	            System.err.println("\n System Error");
	            System.err.println( e.getMessage() );
	            System.err.println( "Exiting...");
	        } catch (NumberFormatException e ) {
	        	System.out.println( response );
	        }
		}
	}
	
	private static void printMenu(int screen) {
		switch (screen) {
			case 1:
					System.out.println("Select manga to view chapters: ");
				break;
				
			case 2:
				System.out.println("Select chapter to download: ");
				break;
		}	
	}

}
