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
 * 
 * 
 * Portions of the Software have been derived from source code
 * developed by Silver Egg Technology under the following license:
 * 
 * Copyright (c) 2001 Silver Egg Technology
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sub-license, and/or 
 * sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 */

/**
 *
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class ShutdownHook {
    /*---------------------------------------------------------------
     * Main Method
     *-------------------------------------------------------------*/
    public static void main(String[] args) {
        System.out.println( Main.getRes().getString( "This application registers a shutdown hook which") );
        System.out.println( Main.getRes().getString( "should be executed after the JVM has told the Wrapper") );
        System.out.println( Main.getRes().getString( "it is exiting." ) );
        System.out.println( Main.getRes().getString( "This is to test the wrapper.jvm_exit.timeout property" ) );
        
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            public void run() {
                System.out.println( Main.getRes().getString( "Starting shutdown hook. Loop for 25 seconds.") );
                System.out.println( Main.getRes().getString( "Should timeout unless this property is set: wrapper.jvm_exit.timeout=30" ) );

                long start = System.currentTimeMillis();
                while ( System.currentTimeMillis() - start < 25000 )
                {
                    try
                    {
                        Thread.sleep( 250 );
                    }
                    catch ( InterruptedException e )
                    {
                        // Ignore
                    }
                }
                System.out.println( Main.getRes().getString( "Shutdown hook complete. Should exit now." ) );
                
                // Run GC to invoke finalize on unsed objects to test the shutdown process.
                System.out.println( "GC BEGIN" );
                System.gc();
                System.out.println( "GC END" );
                try
                {
                    Thread.sleep( 2000 );
                }
                catch ( InterruptedException e )
                {
                    // Ignore
                }
                System.out.println( "DONE" );
            }
        } );
        
        System.out.println( Main.getRes().getString( "Application complete.  Wrapper should stop, invoking the shutdown hooks." ) );
        System.out.println();
    }
}

