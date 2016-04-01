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

import java.util.Random;

/**
 *
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class TimedLogOutput {
    /*---------------------------------------------------------------
     * Methods
     *-------------------------------------------------------------*/
    private static String lPad( int n, int len, String padding )
    {
        String s = Integer.toString( n );
        int len2 = s.length();
        if ( len2 < len )
        {
            return padding.substring( 0, len - len2 ) + s;
        }
        else
        {
            return s;
        }
    }
    
    /*---------------------------------------------------------------
     * Main Method
     *-------------------------------------------------------------*/
    public static void main(String[] args) {
        // This class is not localized so we can run it without the Wrapper code.
        System.out.println( "Log lots of output of varying length with varying delays between entries.  Each line shows the time it took to log the previous entry.  This is done in a repeatable random series." );
        
        // Build up a long base string.  We will be sending varying substring instances of this to the output.
        StringBuffer messageSB = new StringBuffer();
        for ( int i = 0; i < 1000; i++ )
        {
            messageSB.append( "ThisIsAVeryLongStringWithoutSpaces." ); // 35 chars
        }
        String message = messageSB.toString(); // 35000 chars.
        int messageLen = message.length();
        
        // Always seed the random generator so we get the same results for every run.
        Random rand = new Random( 0 );
        
        long allStart = System.currentTimeMillis();
        System.out.println( "Starting..." );
        long prevTime = 0;
        for ( int i = 0; i < 1000; i++ )
        {
            int subMessageLen = (int)( rand.nextFloat() * (messageLen - 1) );
            String subMessage = message.substring( 0, subMessageLen );
            
            String line = "#" + lPad( i, 6, "0000000000" ) + ":" + lPad( (int)prevTime, 6, "          " ) + "ms:" + lPad( subMessageLen, 6, "          " ) + ":" + subMessage;
        
            long lineStart = System.currentTimeMillis();
            System.out.println( line );
            long lineTime = System.currentTimeMillis() - lineStart;
            prevTime = lineTime;
            
            // Only sleep on 10% of the lines, then on those do a random lengthed sleep.
            if ( rand.nextFloat() < 0.10f )
            {
                long sleepTime = (long)( rand.nextFloat() * 50 );
                try
                {
                    Thread.sleep( sleepTime );
                }
                catch ( InterruptedException e )
                {
                }
            }
        }
        long allTime = System.currentTimeMillis() - allStart;
        System.out.println( "Total time: " + allTime );
        System.out.println( "All done." );
    }
}

