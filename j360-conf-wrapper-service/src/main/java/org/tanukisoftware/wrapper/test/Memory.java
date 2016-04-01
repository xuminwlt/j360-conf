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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 *
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class Memory implements Runnable
{
    private static Memory c_theInstance;
    
    private Thread m_runner;
    
    /*---------------------------------------------------------------
     * Constructor
     *-------------------------------------------------------------*/
    private Memory()
    {
        // Start the runner
        m_runner = new Thread( this, "runner" );
        m_runner.start();
    }
    
    /*---------------------------------------------------------------
     * Runnable Method
     *-------------------------------------------------------------*/
    public void run()
    {
        long startTime = System.currentTimeMillis();
        long lastTest = startTime;
        try
        {
            File file = new File( "../logs/memory.log" );
            System.out.println( Main.getRes().getString( "Writing memory Log to: {0}", file ) );
            
            Writer writer = new FileWriter( file );
            try
            {
                writer.write( Main.getRes().getString( "--> Starting Memory Log\n" ) );
                writer.flush();
                
                while( m_runner != null )
                {
                    long now = System.currentTimeMillis();
                    System.out.println( Main.getRes().getString( "Running for {0}ms...", new Long( now - startTime ) ) );
                    
                    if ( now - lastTest > 15000 )
                    {
                        Runtime rt = Runtime.getRuntime();
                        System.gc();
                        long totalMemory = rt.totalMemory();
                        long freeMemory = rt.freeMemory();
                        long usedMemory = totalMemory - freeMemory;
                        
                        writer.write( Main.getRes().getString( "total memory=" ) + pad( totalMemory, 10 )
                            + Main.getRes().getString( ", used=" ) + pad( usedMemory, 10 )
                            + Main.getRes().getString( ", free=" ) + pad( freeMemory, 10 ) + "\n" );
                        writer.flush();
                        
                        lastTest = now;
                    }
                    
                    try
                    {
                        Thread.sleep( 250 );
                    }
                    catch ( InterruptedException e )
                    {
                    }
                }
                
                writer.write( Main.getRes().getString( "<-- Stopping Memory Log\n" ) );
                writer.flush();
            }
            finally
            {
                writer.close();
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }
    
    /*---------------------------------------------------------------
     * Methods
     *-------------------------------------------------------------*/
    private static final String PADDING = "                ";
    private String pad( long n, int len )
    {
        String s = Long.toString( n );
        int sLen = s.length();
        if ( sLen < len )
        {
            s = s + PADDING.substring( 0, len - sLen );
        }
        return s;
    }
    
    /*---------------------------------------------------------------
     * Main Method
     *-------------------------------------------------------------*/
    public static void main(String[] args)
    {
        System.out.println( Main.getRes().getString( "Memory Tester Running...") );
        
        c_theInstance = new Memory();
        
        // Register a shutdown hook.
        Runtime.getRuntime().addShutdownHook( new Thread( "shutdown-hook" )
            {
                public void run()
                {
                    System.out.println(Main.getRes().getString( "Stopping..." ) );
                    
                    Thread runner = c_theInstance.m_runner;
                    
                    // Tell the main thread to stop.
                    c_theInstance.m_runner = null;
                    
                    // Wait for the thread to actually stop cleanly.
                    try
                    {
                        runner.join();
                    }
                    catch ( InterruptedException e )
                    {
                    }
                    
                    System.out.println(Main.getRes().getString( "Stopped." ) );
                }
            } );
    }
}
