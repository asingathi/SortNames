package com.ssi.app;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class SortNamesTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SortNamesTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( SortNamesTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testSortNames() {
    	try {
    		//SortNames srt = new SortNames( 10, "input.txt", "output.txt" );
    		SortNames srt = new SortNames( 10, "./input/input.txt", "output.txt" );
    		srt.startProcess();
    		assertTrue( "Success", true );
    	} catch( Exception e ) {
    		assertFalse( e.toString(), true );
    	}
    }
}
