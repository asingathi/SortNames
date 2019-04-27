package com.ssi.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ssi.business.Person;
import com.ssi.util.SortbyName;

/**
 * @author asingathi
 * 
 * Sort by Last name
 *
 */
public class SortNames {

	private final Comparator<Person> sorter;
	private int maxRecords;
	private List<File> tempOutFiles = new ArrayList<File>();
	private String tempDirectory = "./tmp";
	private String inputFileName;
	private String outputFileName;

	public SortNames( int maxRecords, String inputFileName, String outputFileName ) {
		this.maxRecords = maxRecords;
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		sorter = new SortbyName();
		createTempDirectory();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		if ( args == null || args.length != 3 ) {
			System.err.println( "Invalid arguments. valid arguments are noOfRecords inputFileName, outputFileName");
		} else {
			System.out.println( "arguments passed :"+args[0] +", "+ args[1] +", "+args[2]);
			if( args[0] != null && args[0].trim().length() > 0 ) {
				if( ! isNumber( args[0] ) ) {
					System.err.println( "Invalid data for the noOfRecords" );
				} else {
					SortNames sort = new SortNames( new Integer(args[0]), args[1], args[2] );
					sort.startProcess();
				}
			} else {
				System.err.println( "Invalid data for the noOfRecords" );
			}
		}
	}

	private void startProcess() throws Exception {
		try {
			splitFiles();
			mergeFiles();
		} catch( Exception e ) {
			System.err.println( "startProcess() - caught exception "+e.toString());
		}
	}
	
	/**
	 * 
	 * Reads the input file and splits it into sorted data to temporary files.
	 * @throws IOException
	 */
	public void splitFiles() throws IOException {
		tempOutFiles.clear();
		BufferedReader bufferedReader = null;
		List<Person> personList = new ArrayList<Person>();
		try {
			bufferedReader = new BufferedReader(new InputStreamReader( new FileInputStream( inputFileName ) ) );
			String line = null;
			int recordCntr = 0;
			int fileCntr = 1;
			while ((line = bufferedReader.readLine()) != null) {
				personList.add( new Person( line ) );
				recordCntr++;
				if (recordCntr >= maxRecords) {
					recordCntr = 0;
					Collections.sort( personList, sorter);
					File file = new File(tempDirectory, "temp_" + (fileCntr++));
					tempOutFiles.add(file);
					writeOutput( personList, new FileOutputStream(file));
					personList.clear();
				}
			}
			Collections.sort( personList, sorter);
			File file = new File(tempDirectory, "temp_" + (fileCntr++));
			tempOutFiles.add(file);
			writeOutput( personList, new FileOutputStream(file));
			personList.clear();

		} catch (IOException ioe) {
			System.err.println( "splitFiles() - caught exception "+ioe.toString() );
			throw ioe;
		} finally {
			if (bufferedReader != null)
				try {
					bufferedReader.close();
				} catch (Exception e) {}
		}
	}
	
	private void mergeFiles() throws IOException {
		List<BufferedReader> readersList = new ArrayList<BufferedReader>();
		Map<Person, BufferedReader> personsMap = new HashMap<Person, BufferedReader>();
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter( new FileOutputStream( outputFileName ) ) );
			for (int i = 0; i < tempOutFiles.size(); i++) {

				BufferedReader bufferedReader = new BufferedReader(new FileReader(tempOutFiles.get(i)));
				readersList.add( bufferedReader );
				String line = bufferedReader.readLine();
				if (line != null) {
					Person p = new Person( line );
					personsMap.put( p, bufferedReader );
				}
			}
			
			List<Person> sortedList = new LinkedList<Person>( personsMap.keySet());
			while( personsMap.size() > 0 ) {
				Collections.sort( sortedList, sorter );
				Person pKey = sortedList.remove(0);
				bufferedWriter.write( pKey.extract());
				bufferedWriter.flush();
				
				BufferedReader reader = personsMap.remove( pKey );
				String nextLine = reader.readLine();
				if( nextLine != null ) {
					Person p = new Person( nextLine );
					personsMap.put( p, reader );
					sortedList.add( p );
				}
			}
		} catch (IOException ioe) {
			System.err.println( "mergeFiles() - caught exception "+ioe.toString() );
			throw ioe;
		} finally {
			for (int i = 0; i < readersList.size(); i++) {
				try {
					readersList.get(i).close();
				} catch (Exception e) {}
			}
			for (int i = 0; i < tempOutFiles.size(); i++) {
				tempOutFiles.get(i).delete();
			}
			try {
				if( bufferedWriter != null ) {
					bufferedWriter.close();
				}
			} catch (Exception e) {}
		}
	}
	
	/**
	 * 
	 * @param personList
	 * @param os
	 * @throws IOException
	 */
	private void writeOutput( List<Person> personList, OutputStream outStream ) throws IOException {
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter( outStream ) );
			for( Person p : personList ) {
				bufferedWriter.write( p.extract() );
			}
			bufferedWriter.flush();
		} catch (IOException ioe) {
			System.err.println( "writeOutput() - caught exception "+ioe.toString() );
			throw ioe;
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (Exception e) {}
			}
		}
	}
	
	public void createTempDirectory() {
		File file = new File(tempDirectory);
		if( !file.exists() || !file.isDirectory() ) {
		    try{
		    	file.mkdir();
		    } catch(SecurityException se){
		    	System.err.println( "createTempDirectory() - unable to create a temp directory "+se.toString() );
		    }
		 } else {
			 //remove any previous files
			 String files[] = file.list();
		     for (String temp : files) {
		    	 File fileDelete = new File(file, temp);
		    	 fileDelete.delete();
		     }
		   }
	}
	
    private static boolean isNumber( String data ) {
		String regex = "[0-9]+";
		return data.matches(regex); 
	}
    
}
