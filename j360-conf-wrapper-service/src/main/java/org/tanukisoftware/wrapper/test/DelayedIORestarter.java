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

import java.io.IOException;

import org.tanukisoftware.wrapper.WrapperManager;
import org.tanukisoftware.wrapper.WrapperPropertyUtil;

/**
 * This test is designed to make sure the Wrapper handles the case where the
 *  JVM is restarted while the Wrapper is blocking due to a long disk IO queue.
 * Prior to 3.5.11, the Wrapper would sometimes exit before it noticed that
 *  the JVM had wanted to restart.
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class DelayedIORestarter
{
    
    /*---------------------------------------------------------------
     * Main Method
     *-------------------------------------------------------------*/
    public static void main(String[] args) {
        // Let everything start up correctly.
        TestUtils.sleep( 2000 );
        
        int pauseDelay;
        boolean restart;
        switch ( WrapperManager.getJVMId() )
        {
        case 1:
            pauseDelay = 1;
            restart = true;
            break;
            
        case 2:
            pauseDelay = 5;
            restart = true;
            break;
            
        case 3:
            pauseDelay = 10;
            restart = true;
            break;
            
        default:
            pauseDelay = 10;
            restart = false;
            break;
        }
        
        
        System.out.println( Main.getRes().getString( "Asking Wrapper to pause logger for {0} seconds.", new Integer( pauseDelay ) ) );
        try
        {
            TestUtils.writeWrapperTestCommand( "PAUSE_LOGGER " + pauseDelay );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        
        int pollInterval = WrapperPropertyUtil.getIntProperty( "wrapper.commandfile.poll_interval", 5 ) + 1;
        
        if ( restart )
        {
            System.out.println( Main.getRes().getString( "Restart JVM after {0} second delay...", new Integer( pollInterval ) ) );
        }
        else
        {
            System.out.println( Main.getRes().getString( "Stop JVM after {0} second delay...", new Integer( pollInterval ) ) );
        }
        
        // Give the Wrapper time to notice the command file.
        TestUtils.sleep( pollInterval * 1000 );
        
        if ( restart )
        {
            WrapperManager.restart();
        }
        else
        {
            WrapperManager.stop( 0 );
        }
    }
}

