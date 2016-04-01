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

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class GrowingLogOutput {
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
        System.out.println( "Log several lines of output of growing length.  This is to massage the internal logging buffers." );
        
        SimpleDateFormat df = new SimpleDateFormat( "HH:mm:ss.SSS" );
        
        // Build up a long base string.  We will be sending varying substring instances of this to the output.
        StringBuffer messageSB = new StringBuffer();
        for ( int i = 0; i < 1000; i++ )
        {
            messageSB.append( "ThisIsAVeryLongStringWithoutSpaces." ); // 35 chars
        }
        String message = messageSB.toString(); // 35000 chars.
        int messageLen = message.length();
        
        long allStart = System.currentTimeMillis();
        System.out.println( df.format( new Date() ) + " Starting..." );
        for ( int subMessageLen = 0; subMessageLen < messageLen; subMessageLen++ )
        {
            String subMessage = message.substring( 0, subMessageLen );
            System.out.println( df.format( new Date() ) + " " + lPad( subMessageLen + 20, 6, "          " ) + ":" + subMessage );
        }
        long allTime = System.currentTimeMillis() - allStart;
        System.out.println( df.format( new Date() ) + " Max length should be: " + ( messageLen - 1 + 20 ) );
        System.out.println( df.format( new Date() ) + " Total time: " + allTime );
        System.out.println( df.format( new Date() ) + " All done." );
    }
}

