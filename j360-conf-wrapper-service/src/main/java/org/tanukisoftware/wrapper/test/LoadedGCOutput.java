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
public class LoadedGCOutput {
    /*---------------------------------------------------------------
     * Main Method
     *-------------------------------------------------------------*/
    public static void main(String[] args) {
        System.out.println( Main.getRes().getString( "This test is to make sure that large amounts of GCs with -verbose:gc\nenabled do not cause extra line feeds in output." ) );
        System.out.println();
        System.out.println( Main.getRes().getString( "This test will run indefinitely." ) );
        
        try
        {
            Thread.sleep( 2000 );
        }
        catch ( InterruptedException e )
        {
        }
        
        for ( int i = 0; i < 10; i++ ) {
            // Start another thread to keep things busy creating lots of objects.
            Thread runner = new Thread( "LoadedGCOutput-busy-bee-" + i )
                {
                    public void run()
                    {
                        while ( true )
                        {
                            Object test = new Object();
                        }
                    }
                };
            runner.setDaemon( true );
            runner.start();
        }
        
        while ( true )
        {
            System.gc();
        }
    }
}

