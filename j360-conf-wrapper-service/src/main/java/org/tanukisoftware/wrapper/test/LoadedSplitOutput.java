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
public class LoadedSplitOutput {
    /*---------------------------------------------------------------
     * Main Method
     *-------------------------------------------------------------*/
    public static void main(String[] args) {
        int maxDelay = 16384;
        
        System.out.println( Main.getRes().getString( "This test is to make sure that the Wrapper handles lines of split output correctly, only adding line feeds when the split delay is longer than the configured wrapper.log.lf_delay.threshold." ) );
        
        System.out.println();
        System.out.println( Main.getRes().getString( "This test will loop with delays between 1ms and {0}ms at increasing increments.", new Integer( maxDelay ) ) );
        System.out.println();
        
        try
        {
            Thread.sleep( 2000 );
        }
        catch ( InterruptedException e )
        {
        }
        
        int delay = 1;
        while ( delay <= maxDelay )
        {
            System.out.print( "Head then delay for " + delay + "ms ..." );
            try
            {
                Thread.sleep( delay );
            }
            catch ( InterruptedException e )
            {
            }
            System.out.println( "... Complete the line." );
            
            delay += delay;
        }

        System.out.println();
        System.out.println( Main.getRes().getString( "Print a progress bar with delays between 1ms and {0}ms at increasing increments.", new Integer( maxDelay ) ) );
        System.out.println();
        
        delay = 1;
        while ( delay <= maxDelay )
        {
            System.out.print( "Start with " + delay + "ms delay." );
            for ( int i = 0; i < 10; i++ )
            {
                try
                {
                    Thread.sleep( delay );
                }
                catch ( InterruptedException e )
                {
                }
                System.out.print( "." );
            }
            System.out.println( " Done." );
            
            delay += delay;
        }
    }
}

