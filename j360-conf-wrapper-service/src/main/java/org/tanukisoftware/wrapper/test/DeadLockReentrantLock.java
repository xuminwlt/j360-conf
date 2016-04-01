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

/**
 * This is test is designed to simulate a series of deadlock cases using
 *  ReentrantLocks, which will can be detected and restarted by the Standard
 *  Edition.
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class DeadLockReentrantLock
{
    /*---------------------------------------------------------------
     * Main Method
     *-------------------------------------------------------------*/
    public static void main( String[] args )
    {
        System.out.println( Main.getRes().getString( "Deadlock Tester Running..." ) );

        int exitCode = WrapperManager.getJVMId();
        switch ( exitCode )
        {
        case 1:
            System.out.println( Main.getRes().getString( "2-object deadlock." ) );
            DeadLockBase.create2ObjectDeadlock( true, true );
            break;
            
        case 2:
            System.out.println( Main.getRes().getString( "Wait then 2-object deadlock." ) );
            try
            {
                Thread.sleep( 10000 );
            }
            catch ( InterruptedException e )
            {
            }
            DeadLockBase.create2ObjectDeadlock( true, true );
            break;
            
        case 3:
            System.out.println( Main.getRes().getString( "3-object deadlock." ) );
            DeadLockBase.create3ObjectDeadlock( true, true, true );
            break;
            
        default:
            System.out.println( Main.getRes().getString( "Done." ) );
            return;
        }
        
        // Always wait a couple seconds to make sure the above threads have time to start.
        try
        {
            Thread.sleep( 2000 );
        }
        catch ( InterruptedException e )
        {
        }
        System.out.println( Main.getRes().getString( "Threads Launched." ) );
        
        // Always wait long enough for the deadlock to be detected.  Expecting a 10 second interval.
        try
        {
            Thread.sleep( 10000 );
        }
        catch ( InterruptedException e )
        {
        }
        System.out.println( Main.getRes().getString( "Failed to detect a deadlock." ) );
    }
}
