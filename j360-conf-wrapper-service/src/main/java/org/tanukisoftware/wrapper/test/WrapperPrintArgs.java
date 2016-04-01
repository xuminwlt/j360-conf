package org.tanukisoftware.wrapper.test;

/*
 * Copyright (c) 1999, 2012 Tanuki Software, Ltd.
 * http://www.tanukisoftware.com
 * All rights reserved.
 *
 * This software is the proprietary information of Tanuki Software.
 * You shall use it only in accordance with the terms of the
 * license agreement you entered into with Tanuki Software.
 * http://wrapper.tanukisoftware.com/doc/english/licenseOverview.html
 */

public class WrapperPrintArgs
{
    public static void main( String[] args )
    {
        System.out.println( "Dump all Application Arguments:" );
        System.out.println( "  argv=" + args.length );
        for ( int i = 0; i < args.length; i++ )
        {
            System.out.println( "  args[" + i + "]=" + args[i] );
        }
    }
}
