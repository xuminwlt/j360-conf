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

import org.tanukisoftware.wrapper.WrapperJNIError;
import org.tanukisoftware.wrapper.WrapperLicenseError;
import org.tanukisoftware.wrapper.WrapperManager;
import org.tanukisoftware.wrapper.WrapperProcess;
import org.tanukisoftware.wrapper.WrapperProcessConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 *
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class ChildWrapper
{
    private static String c_encoding;
    
    /*---------------------------------------------------------------
     * Static Methods
     *-------------------------------------------------------------*/
    static
    {
        // In order to read the output from some processes correctly we need to get the correct encoding.
        //  On some systems, the underlying system encoding is different than the file encoding.
        c_encoding = System.getProperty( "sun.jnu.encoding" );
        if ( c_encoding == null )
        {
            c_encoding = System.getProperty( "file.encoding" );
            if ( c_encoding == null )
            {
                // Default to Latin1
                c_encoding = "Cp1252";
            }
        }
    }
    
    private static void handleJavaProcess( String command )
        throws IOException, InterruptedException
    {
        Process process = Runtime.getRuntime().exec( command );
        try
        {
            BufferedReader br;
            String line;
            
            // Dump all stdout
            br = new BufferedReader( new InputStreamReader( process.getInputStream(), c_encoding ) );
            try
            {
                while ( ( line = br.readLine() ) != null )
                {
                    System.out.println( "stdout: " + line );
                }
            }
            finally
            {
                br.close();
            }
            
            // Dump all stderr
            br = new BufferedReader( new InputStreamReader( process.getErrorStream(), c_encoding ) );
            try
            {
                while ( ( line = br.readLine() ) != null )
                {
                    System.out.println( "stderr: " + line );
                }
            }
            finally
            {
                br.close();
            }
        }
        finally
        {
            int exitCode = process.waitFor();
            System.out.println( "exitCode: " + exitCode );
        }
    }

    private static void handleWrapperProcess( String command )
        throws IOException, InterruptedException
    {
        WrapperProcess process = WrapperManager.exec( command );
        try
        {
            BufferedReader br;
            String line;
            
            // Dump all stdout
            br = new BufferedReader( new InputStreamReader( process.getInputStream(), c_encoding ) );
            try
            {
                while ( ( line = br.readLine() ) != null )
                {
                    System.out.println( "stdout: " + line );
                }
            }
            finally
            {
                br.close();
            }
            
            // Dump all stderr
            br = new BufferedReader( new InputStreamReader( process.getErrorStream(), c_encoding ) );
            try
            {
                while ( ( line = br.readLine() ) != null )
                {
                    System.out.println( "stderr: " + line );
                }
            }
            finally
            {
                br.close();
            }
        }
        finally
        {
            int exitCode = process.waitFor();
            System.out.println( "exitCode: " + exitCode );
        }
    }
    
    /*---------------------------------------------------------------
     * Main Method
     *-------------------------------------------------------------*/
    public static void main( String[] args )
    {
        System.out.println( "Communicate with child processes using encoding: " + c_encoding );
        
        try
        {
            String wrapperCmdVersion;
            String wrapperCmdTestWrapper;
            if ( WrapperManager.isWindows() )
            {
                wrapperCmdVersion = "..\\bin\\wrapper.exe -v";
                wrapperCmdTestWrapper = "..\\bin\\wrapper.exe -c ..\\conf\\wrapper.conf -- exit0";
            }
            else
            {
                wrapperCmdVersion = "../bin/wrapper -v";
                wrapperCmdTestWrapper = "../bin/wrapper -c ../conf/wrapper.conf -- exit0";
            }
            String batCmd = "cmd /c ..\\bin\\TestWrapper.bat exit0";
            String batDirect = "..\\bin\\TestWrapper.bat exit0";
            
            System.out.println( Main.getRes().getString( "Runtime.exec test (Version)." ) );
            handleJavaProcess( wrapperCmdVersion );
            
            System.out.println( Main.getRes().getString( "Runtime.exec test (TestWrapper)." ) );
            handleJavaProcess( wrapperCmdTestWrapper );
            
            if ( WrapperManager.isStandardEdition() )
            {
                System.out.println( Main.getRes().getString( "WrapperManager.exec test (Version)." ) );
                handleWrapperProcess( wrapperCmdVersion );
                
                System.out.println( Main.getRes().getString( "WrapperManager.exec test (TestWrapper)." ) );
                handleWrapperProcess( wrapperCmdTestWrapper );
            }
            
            if ( WrapperManager.isWindows() )
            {
                System.out.println( Main.getRes().getString( "Runtime.exec test (Bat with cmd)." ) );
                handleJavaProcess( batCmd );
                
                System.out.println( Main.getRes().getString( "Runtime.exec test (Bat direct)." ) );
                handleJavaProcess( batDirect );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
