package org.tanukisoftware.wrapper.test;

/*
 * Copyright (c) 1999, 2016 Tanuki Software, Ltd.
 * http://www.tanukisoftware.com
 * All rights reserved.
 *
 * This software is the proprietary information of Tanuki Software.
 * You shall use it only in accordance with the terms of the
 * license agreement you entered into with Tanuki Software.
 * http://wrapper.tanukisoftware.com/doc/english/licenseOverview.html
 */

/**
 *
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class LogStdoutStderr {
    private static int m_lineNum = 1;
    private static int m_outLineNum = 1;
    private static int m_errLineNum = 1;
    
    /*---------------------------------------------------------------
     * Static Methods
     *-------------------------------------------------------------*/
    private static void logOut( String message )
    {
        System.out.println( "All:" + (m_lineNum++) + " Out:" + (m_outLineNum++) + " " + message );
    }
    private static void logErr( String message )
    {
        System.err.println( "All:" + (m_lineNum++) + " Err:" + (m_errLineNum++) + " " + message );
    }
    
    /*---------------------------------------------------------------
     * Main Method
     *-------------------------------------------------------------*/
    public static void main(String[] args) {
        System.out.println( Main.getRes().getString( "Logs several lines of output to stdout and stderr." ) );
        System.out.println( Main.getRes().getString( "Make sure that all of the line numbers are in order and that they show up in the right places." ) );
        
        logOut( "Test Output" );
        logErr( "Test Error" );
        
        for ( int i = 0; i < 100; i++ )
        {
            logOut( "Loop #" + i );
            logErr( "Loop #" + i );
        }
        
        StringBuffer sb = new StringBuffer();
        for ( int i = 0; i < 100; i++ )
        {
            sb.append( "abcdefghijklmnopqrstuvwxyz" );
            
            logOut( "Loop #" + i + " " + sb.toString() );
            logErr( "Loop #" + i + " " + sb.toString() );
        }
        
        logOut( "All done." );
        logErr( "All done." );
    }
}

