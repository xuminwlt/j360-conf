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
public class PostShutdownGC {
    /*---------------------------------------------------------------
     * Main Method
     *-------------------------------------------------------------*/
    public static void main(String[] args) {
        System.out.println( "This application registers a shutdown hook which will run for 15 seconds after the Wrapper has completely shutdown." );
        System.out.println( "It will call GC several times to try and completely get rid of the WrapperManager related code." );
        
        // This test intentionally does NOT use any localization to make sure that there is nothing holding the Wrapper related classes in memory.
        
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            public void run() {
                System.out.println( "Starting shutdown hook. Loop for 15 seconds." );

                long start = System.currentTimeMillis();
                while ( System.currentTimeMillis() - start < 15000 )
                {
                    try
                    {
                        Thread.sleep( 1000 );
                    }
                    catch ( InterruptedException e )
                    {
                        // Ignore
                    }
                    
                    // Try to prod the garbage collector into collecting unneeded classes and objects.
                    char[] buffer = new char[10 * 1024 * 1024];
                    buffer = null;
                    
                    System.gc();
                }
                System.out.println( "Shutdown hook complete. Should exit now." );
            }
        } );
        
        System.out.println( "Application complete.  Wrapper should stop, invoking the shutdown hooks." );
        System.out.println();
    }
}

