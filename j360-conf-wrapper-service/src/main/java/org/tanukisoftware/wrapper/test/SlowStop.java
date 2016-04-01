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

import org.tanukisoftware.wrapper.WrapperManager;
import org.tanukisoftware.wrapper.WrapperListener;

/**
 * This test is to make sure the Wrapper handles applications whose stop method
 *  take too much time to complete.
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class SlowStop
    implements WrapperListener
{
    /**************************************************************************
     * Constructors
     *************************************************************************/
    private SlowStop()
    {
    }
    
    /**************************************************************************
     * WrapperListener Methods
     *************************************************************************/
    public Integer start( String[] args )
    {
        System.out.println( "start()" );
        return null;
    }
    
    public int stop( int exitCode )
    {
        System.out.println( "stop(" + exitCode + ")" );
        System.out.println( Main.getRes().getString( "This stop method will wait for 45 seconds before continuing." ) );
        System.out.println( Main.getRes().getString( "The Wrapper will timeout unless wrapper.shutdown.timeout is set to a larger value." ) );
        
        try
        {
            Thread.sleep( 45000 );
        }
        catch ( InterruptedException e )
        {
        }
        
        System.out.println( Main.getRes().getString( "Stop complete." ) );
        return exitCode;
    }
    
    public void controlEvent( int event )
    {
        System.out.println( "controlEvent(" + event + ")" );
    }
    
    /**************************************************************************
     * Main Method
     *************************************************************************/
    public static void main( String[] args )
    {
        System.out.println( Main.getRes().getString( "Initializing..." ) );
        
        // Start the application.  If the JVM was launched from the native
        //  Wrapper then the application will wait for the native Wrapper to
        //  call the application's start method.  Otherwise the start method
        //  will be called immediately.
        WrapperManager.start( new SlowStop(), args );
    }
}

