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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Random;

/**
 *
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class RuntimeExec
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
    
    private static void handleInputStream( final InputStream is, final String encoding, final String label )
    {
        Thread runner = new Thread()
        {
            public void run()
            {
                BufferedReader br;
                String line;
                
                try
                {
                    br = new BufferedReader( new InputStreamReader( is, encoding ) );
                    try
                    {
                        while ( ( line = br.readLine() ) != null )
                        {
                            System.out.println( label + " " + line );
                        }
                    }
                    finally
                    {
                        br.close();
                    }
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
            }
        };
        runner.start();
    }
    
    private static void handleJavaProcessInner( String testId, Process process )
        throws IOException, InterruptedException
    {
        try
        {
            handleInputStream( process.getInputStream(), c_encoding, testId + "stdout:" );
            handleInputStream( process.getErrorStream(), c_encoding, testId + "stderr:" );
        }
        finally
        {
            int exitCode = process.waitFor();
            System.out.println( Main.getRes().getString( "{0}exitCode: {1}", testId, new Integer( exitCode ) ) );
        }
    }
    
    private static void handleJavaProcess( String testId, String command )
        throws IOException, InterruptedException
    {
        System.out.println( Main.getRes().getString( "{0}Runtime.exec command: {1}", testId, command ) );
        handleJavaProcessInner( testId, Runtime.getRuntime().exec( command ) );
    }
    
    private static void handleJavaProcess( String testId, String[] command )
        throws IOException, InterruptedException
    {
        System.out.println( Main.getRes().getString( "{0}Runtime.exec command: {1}", testId, toString( command ) ) );
        handleJavaProcessInner( testId, Runtime.getRuntime().exec( command ) );
    }

    static final int WAIT_MODE_NONE = 0;
    static final int WAIT_MODE_API = 1;
    static final int WAIT_MODE_MANUAL = 2;
    
    private static void handleWrapperProcessInner( String testId, WrapperProcess process, long timeoutMS, boolean handleOutErr, boolean closeStdOutErr, boolean closeStdIn, int waitMode )
        throws IOException, InterruptedException
    {
        try
        {
            if ( handleOutErr )
            {
                handleInputStream( process.getStdOut(), c_encoding, testId + "stdout:" );
                handleInputStream( process.getStdErr(), c_encoding, testId + "stderr:" );
            }
            else if ( closeStdOutErr )
            {
                process.getStdOut().close();
                process.getStdErr().close();
            }
            
            if ( closeStdIn )
            {
                process.getStdIn().close();
            }
            
            if ( waitMode == WAIT_MODE_API )
            {
                // We always call waitFor later.
            }
            else if ( waitMode == WAIT_MODE_MANUAL )
            {
                if ( timeoutMS > 0 )
                {
                    long start = System.currentTimeMillis();
                    while ( process.isAlive() && ( System.currentTimeMillis() - start < timeoutMS ) )
                    {
                        try
                        {
                            Thread.sleep( 100 );
                        }
                        catch ( InterruptedException e )
                        {
                        }
                    }
                    if ( process.isAlive() )
                    {
                        System.out.println( Main.getRes().getString( "{0}Timed out waiting for child.  Destroying.", testId ) );
                        process.destroy();
                    }
                }
            }
        }
        finally
        {
            if ( ( waitMode == WAIT_MODE_API ) || ( waitMode == WAIT_MODE_MANUAL ) )
            {
                int exitCode = process.waitFor();
                System.out.println( Main.getRes().getString( "{0}exitCode: {1}", testId, new Integer( exitCode ) ) );
            }
            else
            {
                System.out.println( Main.getRes().getString( "{0}leave running...", testId ) );
            }
        }
    }

    private static void handleWrapperProcess( String testId, String command, WrapperProcessConfig config, long timeoutMS, boolean handleOutErr, boolean closeStdOutErr, boolean closeStdIn, int waitMode )
        throws IOException, InterruptedException
    {
        System.out.println( Main.getRes().getString( "{0}WrapperManager.exec command: {1}", testId, command ) );
        if ( config == null )
        {
            handleWrapperProcessInner( testId, WrapperManager.exec( command ), timeoutMS, handleOutErr, closeStdOutErr, closeStdIn, waitMode );
        }
        else
        {
            handleWrapperProcessInner( testId, WrapperManager.exec( command, config ), timeoutMS, handleOutErr, closeStdOutErr, closeStdIn, waitMode );
        }
    }
    private static void handleWrapperProcess( String testId, String command )
        throws IOException, InterruptedException
    {
        handleWrapperProcess( testId, command, null, 0, true, false, false, WAIT_MODE_MANUAL );
    }

    private static void handleWrapperProcess( String testId, String[] command, WrapperProcessConfig config, long timeoutMS, boolean handleOutErr, boolean closeStdOutErr, boolean closeStdIn, int waitMode )
        throws IOException, InterruptedException
    {
        System.out.println( Main.getRes().getString( "{0}WrapperManager.exec command: {1}", testId, toString( command ) ) );
        if ( config == null )
        {
            handleWrapperProcessInner( testId, WrapperManager.exec( command ), timeoutMS, handleOutErr, closeStdOutErr, closeStdIn, waitMode );
        }
        else
        {
            handleWrapperProcessInner( testId, WrapperManager.exec( command, config ), timeoutMS, handleOutErr, closeStdOutErr, closeStdIn, waitMode );
        }
    }
    private static void handleWrapperProcess( String testId, String[] command )
        throws IOException, InterruptedException
    {
        handleWrapperProcess( testId, command, null, 0, true, false, false, WAIT_MODE_MANUAL );
    }
    
    private static void beginCase( String testId )
    {
        System.out.println();
        System.out.println( Main.getRes().getString( "{0}BEGIN ----------------------------------------", testId ) );
    }
    
    private static void endCase( String testId )
    {
        // Try to keep all output form the test within the the BEGIN/END block.
        try
        {
            Thread.sleep( 1000 );
        }
        catch ( InterruptedException e )
        {
        }
        System.out.println( Main.getRes().getString( "{0}END   ----------------------------------------", testId ) );
    }
    
    private static String toString( String[] command )
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "{" );
        for ( int i = 0; i < command.length; i++ )
        {
            String arg = command[i];
            if ( i > 0 )
            {
                sb.append( ", " );
            }
            sb.append( "\"" );
            sb.append( arg );
            sb.append( "\"" );
        }
        sb.append( "}" );
        
        return sb.toString();
    }
    
    private static void caseSimpleTestJava( final String simplewaiter )
    {
        String testId = "Simple Java : ";
        beginCase( testId );
        try
        {
            try
            {
                String command = simplewaiter + " -v \"test 123\" test 123 \"\\\"test\\\"";
                handleJavaProcess( testId, command );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    private static void caseSimpleTestWrapper( final String simplewaiter )
    {
        String testId = "Simple Wrapper : ";
        beginCase( testId );
        try
        {
            try
            {
                String command = simplewaiter + " -v \"test 123\" test 123 \"\\\"test\\\"";
                handleWrapperProcess( testId, command );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    private static void caseSimpleTestJavaAry( final String simplewaiter )
    {
        String testId = "Simple Java (Array) : ";
        beginCase( testId );
        try
        {
            try
            {
                String[] command = { simplewaiter, "-v", "\"test 123\"", "test 123", "\"\\\"test\\\"\"" };
                handleJavaProcess( testId, command );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    private static void caseSimpleTestWrapperAry( final String simplewaiter )
    {
        String testId = "Simple Wrapper (Array) : ";
        beginCase( testId );
        try
        {
            try
            {
                String[] command = { simplewaiter, "-v", "\"test 123\"", "test 123", "\"\\\"test\\\"\"" };
                handleWrapperProcess( testId, command );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    private static void caseLongCommand( final String simplewaiter, int len, boolean expectFailure )
    {
        
        String testId = "Long Command " + len + ": ";
        beginCase( testId );
        try
        {
            try
            {
                StringBuffer sb = new StringBuffer();
                sb.append( simplewaiter );
                sb.append( " -v " );
                while ( sb.length() < len - 1 )
                {
                    sb.append( "x" );
                }
                // Make the last character a y so we can verify that it is included correctly.
                sb.append( "y" );
                String command = sb.toString();
                
                handleWrapperProcess( testId, command );
                if ( expectFailure )
                {
                    System.out.println( Main.getRes().getString( "{0}Did not fail as expected.", testId ) );
                }
            }
            catch ( Exception e )
            {
                if ( expectFailure )
                {
                    System.out.println( Main.getRes().getString( "{0}Failed as expected: {1}", testId, e.toString() ) );
                }
                else
                {
                    e.printStackTrace();
                }
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    private static void caseWaitFor( final String simplewaiter )
    {
        String testId = "WaitFor : ";
        beginCase( testId );
        try
        {
            try
            {
                String command = simplewaiter + " 1 10";
                handleWrapperProcess( testId, command, null, 0, true, false, false, WAIT_MODE_API );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    private static void caseSmallChildProcess( final String simplewaiter )
    {
        String testId = "Simple Wrapper (Array) : ";
        beginCase( testId );
        try
        {
            try
            {
                String command = simplewaiter + " 65 1";
                handleWrapperProcess( testId, command, null, 0, false, false, true, WAIT_MODE_MANUAL );
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    private static void caseVFork( final String simplewaiter )
    {
        String testId = "VFork : ";
        beginCase( testId );
        try
        {
            if ( !WrapperProcessConfig.isSupported( WrapperProcessConfig.VFORK_EXEC ) ) 
            {
                System.out.println( Main.getRes().getString( "{0}vfork not supported", testId ) );
            }
            else
            {
                System.out.println( Main.getRes().getString( "{0}vfork is supported", testId ) );
                try
                {
                    String command = simplewaiter + " 20 10";
                    handleWrapperProcess( testId, command, new WrapperProcessConfig().setStartType( WrapperProcessConfig.VFORK_EXEC ), 0, true, false, false, WAIT_MODE_MANUAL );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    private static void casePosixSpawn( final String simplewaiter )
    {
        String testId = "PosixSpawn : ";
        beginCase( testId );
        try
        {
            if ( !WrapperProcessConfig.isSupported( WrapperProcessConfig.POSIX_SPAWN ) ) 
            {
                System.out.println( Main.getRes().getString( "{0}posix spawn not supported", testId ) );
            }
            else
            {
                System.out.println( Main.getRes().getString( "{0}posix spawn is supported", testId ) );
                try
                {
                    String command = simplewaiter + " 20 10";
                    handleWrapperProcess( testId, command, new WrapperProcessConfig().setStartType( WrapperProcessConfig.POSIX_SPAWN ), 0, true, false, false, WAIT_MODE_MANUAL );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    private static void caseEnvSmall( final String simplewaiter )
    {
        String testId = "Environment Small : ";
        beginCase( testId );
        try
        {
            try
            {
                WrapperProcessConfig config = new WrapperProcessConfig();
                java.util.Map environment = config.getEnvironment();
                environment.clear();
                environment.put( "TEST", "TEST123" );
                System.out.println( Main.getRes().getString( "{0}size of Environment map = {1}", testId, new Integer( environment.size() ) ) );
                
                String command = simplewaiter + " -e TEST";
                handleWrapperProcess( testId, command, config, 0, true, false, false, WAIT_MODE_MANUAL );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    private static void caseEnvLarge( final String simplewaiter, int len, boolean expectFailure )
    {
        String testId = "Environment Large " + len + ": ";
        beginCase( testId );
        try
        {
            try
            {
                int valueLen = len - 4 - 1; // "TEST="
                StringBuffer sb = new StringBuffer();
                for ( int i = 0; i < valueLen - 1; i++ )
                {
                    sb.append( "X" );
                }
                sb.append( "Y" ); // Make it so we can see the last value.
                String value = sb.toString();
                
                WrapperProcessConfig config = new WrapperProcessConfig();
                java.util.Map environment = config.getEnvironment();
                environment.clear();
                environment.put( "TEST", value );
                System.out.println( Main.getRes().getString( "{0}size of Environment map = {1}", testId, new Integer( environment.size() ) ) );
                
                String command = simplewaiter + " -e TEST";
                handleWrapperProcess( testId, command, config, 0, true, false, false, WAIT_MODE_MANUAL );
                if ( expectFailure )
                {
                    System.out.println( Main.getRes().getString( "{0}Did not fail as expected.", testId ) );
                }
            }
            catch ( Exception e )
            {
                if ( expectFailure )
                {
                    System.out.println( Main.getRes().getString( "{0}Failed as expected: {1}", testId, e.toString() ) );
                }
                else
                {
                    e.printStackTrace();
                }
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    private static void caseWorkingDir( final String simplewaiter )
    {
        String testId = "Change Working Dir : ";
        beginCase( testId );
        try
        {
            if ( WrapperProcessConfig.isSupported( WrapperProcessConfig.FORK_EXEC ) || WrapperProcessConfig.isSupported( WrapperProcessConfig.VFORK_EXEC ) )
            {
                System.out.println( Main.getRes().getString( "{0}changing the working directory is supported", testId ) );
                try
                {
                    WrapperProcessConfig config = new WrapperProcessConfig();
                    config.setStartType( WrapperProcessConfig.isSupported( WrapperProcessConfig.FORK_EXEC ) ? WrapperProcessConfig.FORK_EXEC : WrapperProcessConfig.VFORK_EXEC );
                    config.setWorkingDirectory( new File("..") );
                    
                    String command;
                    if ( WrapperManager.isWindows() )
                    {
                        command = "cmd.exe /c dir";
                    }
                    else
                    {
                        command = "ls -l";
                    }
                    handleWrapperProcess( testId, command, config, 0, true, false, false, WAIT_MODE_MANUAL );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println( Main.getRes().getString( "{0}changing the working directory is not supported", testId ) );
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    /**
     * Test a short WrapperManager.exec process whose entire lifespan is while another Runtime.exec process is running.
     */
    private static void caseWrapperDuringJava( final String simplewaiter )
    {
        String testId = "Wrapper During Java : ";
        beginCase( testId );
        try
        {
            try
            {
                String javaCommand = simplewaiter + " 5 10";
                String wrapperCommand = simplewaiter + " 6 5";
                
                Process javaProcess = Runtime.getRuntime().exec( javaCommand );
                handleInputStream( javaProcess.getInputStream(), c_encoding, testId + "Runtime.exec stdout:" );
                handleInputStream( javaProcess.getErrorStream(), c_encoding, testId + "Runtime.exec stderr:" );
                
                WrapperProcess wrapperProcess = WrapperManager.exec( wrapperCommand );
                handleInputStream( wrapperProcess.getStdOut(), c_encoding, testId + "WrapperManager.exec stdout:" );
                handleInputStream( wrapperProcess.getStdErr(), c_encoding, testId + "WrapperManager.exec stderr:" );

                System.out.println( Main.getRes().getString( "{0}WrapperManager.exec exitCode: {1}", testId, new Integer( wrapperProcess.waitFor() ) ) );
                
                System.out.println( Main.getRes().getString( "{0}Runtime.exec exitCode: {1}", testId, new Integer( javaProcess.waitFor() ) ) );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    /**
     * Test a short Runtime.exec process whose entire lifespan is while another WrapperManager.exec process is running.
     */
    private static void caseJavaDuringWrapper( final String simplewaiter )
    {
        String testId = "Java During Wrapper : ";
        beginCase( testId );
        try
        {
            try
            {
                String wrapperCommand = simplewaiter + " 5 10";
                String javaCommand = simplewaiter + " 6 5";
                
                WrapperProcess wrapperProcess = WrapperManager.exec( wrapperCommand );
                handleInputStream( wrapperProcess.getStdOut(), c_encoding, testId + "WrapperManager.exec stdout:" );
                handleInputStream( wrapperProcess.getStdErr(), c_encoding, testId + "WrapperManager.exec stderr:" );

                Process javaProcess = Runtime.getRuntime().exec( javaCommand );
                handleInputStream( javaProcess.getInputStream(), c_encoding, testId + "Runtime.exec stdout:" );
                handleInputStream( javaProcess.getErrorStream(), c_encoding, testId + "Runtime.exec stderr:" );
                
                System.out.println( Main.getRes().getString( "{0}Runtime.exec exitCode: {1}", testId, new Integer( javaProcess.waitFor() ) ) );
                
                System.out.println( Main.getRes().getString( "{0}WrapperManager.exec exitCode: {1}", testId, new Integer( wrapperProcess.waitFor() ) ) );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    private static void caseInvalid( final String simplewaiter )
    {
        String testId = "Invalid : ";
        beginCase( testId );
        try
        {
            try
            {
                String command = "invalid";
                handleWrapperProcess( testId, command, null, 0, true, false, false, WAIT_MODE_MANUAL );
                System.out.println( Main.getRes().getString( "{0}ERROR! Did not fail as expected.", testId ) );
            }
            catch ( Exception e )
            {
                System.out.println( Main.getRes().getString( "{0}Failed as expected.", testId ) );
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    private static void caseTimeoutShort( final String simplewaiter )
    {
        String testId = "Timeout Short : ";
        beginCase( testId );
        try
        {
            try
            {
                String command = simplewaiter + " 0 5";
                handleWrapperProcess( testId, command, null, 10000, true, false, false, WAIT_MODE_MANUAL );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    private static void caseTimeoutLong( final String simplewaiter )
    {
        String testId = "Timeout Long : ";
        beginCase( testId );
        try
        {
            try
            {
                String command = simplewaiter + " 0 30";
                handleWrapperProcess( testId, command, null, 10000, true, false, false, WAIT_MODE_MANUAL );
            }
            catch ( Exception e )
            {
                
                e.printStackTrace();
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    private static boolean caseLeaveRunning( final String simplewaiter )
    {
        String testId = "Leave Running : ";
        beginCase( testId );
        try
        {
            try
            {
                String command = simplewaiter + " 1 600";
                handleWrapperProcess( testId, command, null, 0, true, false, false, WAIT_MODE_NONE );
                return false;
            }
            catch ( WrapperJNIError e )
            {
                System.out.println( Main.getRes().getString( "{0}Unable to launch child process because JNI library unavailable. Normal on shutdown.", testId ) );
                return true;
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                return true;
            }
        }
        finally
        {
            endCase( testId );
        }
    }
    
    /*---------------------------------------------------------------
     * Main Method
     *-------------------------------------------------------------*/
    public static void main( String[] args )
    {
        final String simplewaiter;
        if ( WrapperManager.isWindows() )
        {
            simplewaiter = "../test/simplewaiter.exe";
        }
        else
        {
            simplewaiter = "../test/simplewaiter";
        }
        
        System.out.println( "Communicate with child processes using encoding: " + c_encoding );
        
        Random rand = new Random();
        System.out.println( Main.getRes().getString( "Is DYNAMIC supported? A:" ) + WrapperProcessConfig.isSupported( WrapperProcessConfig.DYNAMIC ) );
        System.out.println( Main.getRes().getString( "Is FORK_EXEC supported? A:" ) + WrapperProcessConfig.isSupported( WrapperProcessConfig.FORK_EXEC ) );
        System.out.println( Main.getRes().getString( "Is VFORK_EXEC supported? A:" ) + WrapperProcessConfig.isSupported( WrapperProcessConfig.VFORK_EXEC ) );
        System.out.println( Main.getRes().getString( "Is POSIX_SPAWN supported? A:" ) + WrapperProcessConfig.isSupported( WrapperProcessConfig.POSIX_SPAWN ) );

        caseSimpleTestJava( simplewaiter );
        caseSimpleTestWrapper( simplewaiter );
        caseSimpleTestJavaAry( simplewaiter );
        caseSimpleTestWrapperAry( simplewaiter );
        
        caseLongCommand( simplewaiter, 32766, false );
        caseLongCommand( simplewaiter, 32767, true );
        
        caseWaitFor( simplewaiter );
        
        caseSmallChildProcess( simplewaiter );
        
        caseVFork( simplewaiter );
        casePosixSpawn( simplewaiter );
        
        caseEnvSmall( simplewaiter );
        caseEnvLarge( simplewaiter, 32767, false );
        caseEnvLarge( simplewaiter, 32768, true );
        
        caseWorkingDir( simplewaiter );
        
        caseWrapperDuringJava( simplewaiter );
        caseJavaDuringWrapper( simplewaiter );
        
        caseInvalid( simplewaiter );
        
        caseTimeoutShort( simplewaiter );
        caseTimeoutLong( simplewaiter );
        
        // This test should be the last as it relies on the Wrapper shutting it down.
        caseLeaveRunning( simplewaiter );
        
        System.out.println();
        if ( WrapperManager.getJVMId() == 1 )
        {
            // First invocation.
            System.out.println( Main.getRes().getString( "All Done. Restarting..." ) );
            WrapperManager.restart();
        }
        else
        {
            // Second invocation.
            //  Register a long shutdownhook which will cause the Wrapper to timeout and kill the JVM.
            System.out.println( Main.getRes().getString( "All Done. Registering long shutdown hook and stopping.\nWrapper should timeout and kill the JVM, cleaning up all processes in the process." ) );
            
            Runtime.getRuntime().addShutdownHook( new Thread()
            {
                public void run() {
                    System.out.println( Main.getRes().getString( "Starting shutdown hook. Loop for 25 seconds.") );
                    System.out.println( Main.getRes().getString( "Should timeout unless this property is set: wrapper.jvm_exit.timeout=30" ) );
    
                    long start = System.currentTimeMillis();
                    boolean failed = false;
                    while ( System.currentTimeMillis() - start < 25000 )
                    {
                        if ( !failed )
                        {
                            failed = caseLeaveRunning( simplewaiter );
                            System.out.println( Main.getRes().getString( "Launched child...") );
                        }
                        
                        try
                        {
                            Thread.sleep( 250 );
                        }
                        catch ( InterruptedException e )
                        {
                            // Ignore
                        }
                    }
                    System.out.println( Main.getRes().getString( "Shutdown hook complete. Should exit now." ) );
                }
            } );
            
            System.exit( 0 );
        }
    }
}
