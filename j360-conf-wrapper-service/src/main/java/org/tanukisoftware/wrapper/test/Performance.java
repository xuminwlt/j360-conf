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
 * Can be run without the Wrapper as:
 *  java -classpath lib/wrappertest.jar org.tanukisoftware.wrapper.test.Performance
 *
 * NOTE - This class intentionally does not use resources for localization because it
 *        needs to be able to be run without the Wrapper components being loaded.
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class Performance {
    /*---------------------------------------------------------------
     * Main Method
     *-------------------------------------------------------------*/
    private static void initialize()
    {
        System.out.println( "Run some loops for a few seconds to give some time for the JVM to get up and running..." );
        long now = System.currentTimeMillis();
        long count = 1000000000L;
        double total = 0;
        for ( long i = 0; i < count; i++ )
        {
            // Take up some time.
            double x = 10.5 + i;
            double y = 3.14159 * i;
            double z = x * y;
            total += z;
        }
        
        long time = System.currentTimeMillis() - now;
        System.out.println( "  Complete.  Total time=" + ( (double)time / 1000 ) + " seconds. (" + total + ")" );
    }
    
    private static void dumpThreadGroup( ThreadGroup threadGroup, String indent )
    {
        System.out.println( indent + threadGroup.toString() );
        
        Thread[] threads = new Thread[(int)Math.ceil(threadGroup.activeCount() * 1.2)];
        threadGroup.enumerate( threads, false );
        System.out.println( indent + "Threads:" );
        for ( int i = 0; i < threads.length; i++ )
        {
            if ( threads[i] != null )
            {
                System.out.println( indent + "  " + threads[i] );
            }
        }
        
        ThreadGroup[] threadGroups = new ThreadGroup[(int)Math.ceil(threadGroup.activeGroupCount() * 1.2)];
        threadGroup.enumerate( threadGroups, false );
        System.out.println( indent + "Thread Groups:" );
        for ( int i = 0; i < threadGroups.length; i++ )
        {
            if ( threadGroups[i] != null )
            {
                dumpThreadGroup( threadGroups[i], indent + "  " );
            }
        }
    }
    
    private static void dumpThreadInfo()
    {
        Thread thread = Thread.currentThread();
        System.out.println( "Thread: " + thread );
/*
 *       Thread.getId() was introduced in 1.5, not available in java 1.4
 *       System.out.println( "  Id: " + thread.getId() );
 */
        System.out.println( "  Name: " + thread.getName() );
        System.out.println( "  Priority: " + thread.getPriority() );
        System.out.println( "  ThreadGroup Name: " + thread.getThreadGroup().getName() );
        System.out.println( "  isDaemon: " + thread.isDaemon() );
        System.out.println( "All Threads:" );
        
        ThreadGroup topThreadGroup = thread.getThreadGroup();
        while ( topThreadGroup.getParent() != null )
        {
            topThreadGroup = topThreadGroup.getParent();
        }
        dumpThreadGroup( topThreadGroup, "  " );
    }
    
    private static void inMemoryLoopsMath()
    {
        System.out.println( "Starting in memory loop test (Math)..." );
        long now = System.currentTimeMillis();
        long count = 10000000000L;
        double total = 0;
        for ( long i = 0; i < count; i++ )
        {
            // Do some math work.
            double x = 10.5 + i;
            double y = 3.14159 * i;
            double z = x * y;
            total += z;
        }
        
        long time = System.currentTimeMillis() - now;
        System.out.println( "  Complete.  Total time=" + ( (double)time / 1000 ) + " seconds.  (" + ( (double)time * 1000 / count ) + " usec per cycle.) (" + total + ")" );
    }
    
    private static void inMemoryLoopsString()
    {
        System.out.println( "Starting in memory loop test (Strings)..." );
        long now = System.currentTimeMillis();
        long count = 100000000L;
        double total = 0;
        for ( long i = 0; i < count; i++ )
        {
            // Do some string work.  This causes the stack to be used because and is thus slower.
            String a = "This is a test.";
            String b = "This is the end.";
            String c = a + i + b;
            total += c.length();
        }
        
        long time = System.currentTimeMillis() - now;
        System.out.println( "  Complete.  Total time=" + ( (double)time / 1000 ) + " seconds.  (" + ( (double)time * 1000 / count ) + " usec per cycle.) (" + total + ")" );
    }
    
    
    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        System.out.println( "This test will run a number of tests to check Java performance.  It should be run both with and without the Wrapper." );
        
        String fullVersion = System.getProperty( "java.fullversion" );
        String vendor = System.getProperty( "java.vm.vendor", "" );
        String os = System.getProperty( "os.name", "" ).toLowerCase();
        if ( fullVersion == null )
        {
            fullVersion = System.getProperty( "java.runtime.version" ) + " "
                + System.getProperty( "java.vm.name" );
        }
        System.out.println( "Java Version: " + fullVersion );
        
        initialize();
        dumpThreadInfo();
        inMemoryLoopsMath();
        inMemoryLoopsString();
        
        Thread runner = new Thread( "runner" )
            {
                public void run()
                {
                    System.out.println( "Do the same tests in a background thread..." );
                    dumpThreadInfo();
                    inMemoryLoopsMath();
                    inMemoryLoopsString();
                    System.out.println( "Background thread completed." );
                }
            };
        runner.start();
        try
        {
            runner.join();
        }
        catch ( InterruptedException e )
        {
        }
        
        long time = System.currentTimeMillis() - now;
        System.out.println( "Completed in " + ( (double)time / 1000 ) + " seconds.  Exiting." );
    }
}

