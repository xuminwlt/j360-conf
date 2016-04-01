package org.tanukisoftware.wrapper.test;

import java.lang.reflect.InvocationTargetException;

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
 * This is test is designed to simulate various deadlock cases using either
 * ReentrantLock instances or synchronized objects.
 * 
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class DeadLockBase
{
    private Integer      m_id;
    private Object       m_lock1;
    private Object       m_lock2;
    private static Class rentrantLockClass;

    static
    {
        try
        {
            rentrantLockClass = Class.forName( "java.util.concurrent.locks.ReentrantLock" );
        }
        catch ( ClassNotFoundException e )
        {
            e.printStackTrace();
        }
    }

    private DeadLockBase( int id, Object lock1, Object lock2 )
    {
        m_id = new Integer( id );
        m_lock1 = lock1;
        m_lock2 = lock2;

        Thread runner = new Thread( "Locker-" + m_id )
        {
            public void run()
            {
                System.out.println( Main.getRes().getString( "Locker-{0}: Started",
                                                             m_id ) );
                try
                {
                    firstLock();
                }
                catch ( Throwable t )
                {
                    t.printStackTrace();
                }

                System.out.println( Main.getRes().getString( "Locker-{0}: Complete",
                                                             m_id ) );
            }
        };
        runner.start();

        // Always sleep for a moment to try and keep the test cases running consistently.
        try
        {
            Thread.sleep( 50 );
        }
        catch ( InterruptedException e )
        {
        }
    }

    /*---------------------------------------------------------------
     * Methods
     *-------------------------------------------------------------*/
    private void secondLocked()
    {
        System.out.println( Main.getRes().getString( "Locker-{0}: Oops! Locked {1}",
                                                     m_id, m_lock2.toString() ) );
    }

    private void secondLock()
    {
        System.out.println( Main.getRes().getString( "Locker-{0}: Try locking {1}...",
                                                     m_id, m_lock2.toString() ) );
        try
        {
            if ( rentrantLockClass.isInstance( m_lock2 ) )
            {
                Object lock2 = m_lock2;
                rentrantLockClass.getMethod( "lock", null ).invoke( lock2,
                                                                    ( Object[] ) null );
                try
                {
                    secondLocked();
                }
                finally
                {
                    rentrantLockClass.getMethod( "unlock", null ).invoke( lock2,
                                                                          ( Object[] ) null );
                }
            }
            else
            {
                synchronized ( m_lock2 )
                {
                    secondLocked();
                }
            }
        }
        catch ( NoSuchMethodException nsme )
        {
            nsme.printStackTrace();
        }
        catch ( IllegalArgumentException e )
        {
            e.printStackTrace();
        }
        catch ( SecurityException e )
        {
            e.printStackTrace();
        }
        catch ( IllegalAccessException e )
        {
            e.printStackTrace();
        }
        catch ( InvocationTargetException e )
        {
            e.printStackTrace();
        }
    }

    private void firstLocked()
    {
        System.out.println( Main.getRes().getString( "Locker-{0}: Locked {1}",
                                                     m_id, m_lock1.toString() ) );
        try
        {
            Thread.sleep( 2000 );
        }
        catch ( InterruptedException e )
        {
        }

        secondLock();
    }

    private void firstLock()
    {
        System.out.println( Main.getRes().getString( "Locker-{0}: Locking {1}",
                                                     m_id, m_lock1.toString() ) );
        if ( rentrantLockClass.isInstance( m_lock1 ) )
        {
            try
            {
                Object lock1 = m_lock1;
                rentrantLockClass.getMethod( "lock", null ).invoke( lock1,
                                                                    ( Object[] ) null );
                try
                {
                    firstLocked();
                }
                finally
                {
                    rentrantLockClass.getMethod( "unlock", null ).invoke( lock1,
                                                                          ( Object[] ) null );
                }
            }
            catch ( NoSuchMethodException nsme )
            {
                nsme.printStackTrace();
            }
            catch ( IllegalArgumentException e )
            {
                e.printStackTrace();
            }
            catch ( SecurityException e )
            {
                e.printStackTrace();
            }
            catch ( IllegalAccessException e )
            {
                e.printStackTrace();
            }
            catch ( InvocationTargetException e )
            {
                e.printStackTrace();
            }
        }
        else
        {
            synchronized ( m_lock1 )
            {
                firstLocked();
            }
        }
    }

    /*---------------------------------------------------------------
     * Static Methods
     *-------------------------------------------------------------*/
    private static Object createLock( boolean reentrant )
    {
        if ( reentrant )
        {
            try
            {
                return rentrantLockClass.newInstance();
            }
            catch ( InstantiationException e )
            {
                e.printStackTrace();
            }
            catch ( IllegalAccessException e )
            {
                e.printStackTrace();
            }
            return null;
        }
        else
        {
            return new Object();
        }
    }

    public static void create2ObjectDeadlock( boolean reentrant1,
                                              boolean reentrant2 )
    {
        Object lock1 = createLock( reentrant1 );
        Object lock2 = createLock( reentrant2 );

        new DeadLockBase( 1, lock1, lock2 );
        new DeadLockBase( 2, lock2, lock1 );
    }

    public static void create3ObjectDeadlock( boolean reentrant1,
                                              boolean reentrant2,
                                              boolean reentrant3 )
    {
        Object lock1 = createLock( reentrant1 );
        Object lock2 = createLock( reentrant2 );
        Object lock3 = createLock( reentrant3 );

        new DeadLockBase( 1, lock1, lock2 );
        new DeadLockBase( 2, lock2, lock3 );
        new DeadLockBase( 3, lock3, lock1 );
    }
}
