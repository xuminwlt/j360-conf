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
public class DeadLockMixed
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
            System.out.println( Main.getRes().getString( "2-object deadlock (owned, reentrant)." ) );
            DeadLockBase.create2ObjectDeadlock( false, true );
            break;
            
        case 2:
            System.out.println( Main.getRes().getString( "2-object deadlock (reentrant, owned)." ) );
            DeadLockBase.create2ObjectDeadlock( true, false );
            break;
            
        case 3:
            System.out.println( Main.getRes().getString( "3-object deadlock (owned, owned, reentrant)." ) );
            DeadLockBase.create3ObjectDeadlock( false, false, true );
            break;
            
        case 4:
            System.out.println( Main.getRes().getString( "3-object deadlock (owned, reentrant, owned)." ) );
            DeadLockBase.create3ObjectDeadlock( false, true, false );
            break;
            
        case 5:
            System.out.println( Main.getRes().getString( "3-object deadlock (reentrant, owned, owned)." ) );
            DeadLockBase.create3ObjectDeadlock( true, false, false );
            break;
            
        case 6:
            System.out.println( Main.getRes().getString( "3-object deadlock (reentrant, owned, reentrant)." ) );
            DeadLockBase.create3ObjectDeadlock( true, false, true );
            break;
            
        case 7:
            System.out.println( Main.getRes().getString( "3-object deadlock (reentrant, reentrant, owned)." ) );
            DeadLockBase.create3ObjectDeadlock( true, true, false );
            break;
            
        case 8:
            System.out.println( Main.getRes().getString( "3-object deadlock (owned, reentrant, reentrant)." ) );
            DeadLockBase.create3ObjectDeadlock( false, true, true );
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
