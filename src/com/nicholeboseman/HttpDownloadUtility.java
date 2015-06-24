package com.nicholeboseman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
 
/**
 * A utility that downloads a file from a URL.
 * @author www.codejava.net
 *
 */
public class HttpDownloadUtility {
    private static final int BUFFER_SIZE = 4096;
 
    /**
     * Downloads a file from a URL
     * @param fileURL HTTP URL of the file to be downloaded
     * @param saveDir path of the directory to save the file
     * @throws FileNotFoundException 
     * @throws IOException
     */
    
    public static void createZip( String fileName, String[] files) throws FileNotFoundException, IOException {
    	FileOutputStream fos = new FileOutputStream(fileName);
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		for (int i = 0; i < files.length; i++ ) {
			addToZipFile(files[i], zos);
		}
		zos.close();
		fos.close();
    }
    
    public static void deleteFiles ( String[] files ) {
    	for ( String file : files ) {
    		if (new File( file ).delete() ) {
    			System.out.println( file + " has been deleted.");
    		};
    	}
    }
    public static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

		System.out.println("Writing '" + fileName + "' to zip file");

		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

    
    public static String downloadFile(String fileURL, String saveDir, String referer)
            throws IOException {
    	String fileName = "";
    	String saveFilePath = "";
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.addRequestProperty("REFERER",  referer);
        int responseCode = httpConn.getResponseCode();
       
        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();
            
            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }
            
            System.out.println("Downloading " + fileURL);
 
            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            saveFilePath = saveDir + File.separator + fileName;
             
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
 
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            outputStream.close();
            inputStream.close();
 
            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
        return saveFilePath;
    }  
    
    
}