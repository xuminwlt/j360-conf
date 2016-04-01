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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.tanukisoftware.wrapper.WrapperPropertyUtil;

/**
 * This test is designed to make sure the Wrapper handles the case where the
 *  JVM is restarted while the Wrapper is blocking due to a long disk IO queue.
 * Prior to 3.5.11, the Wrapper would sometimes exit before it noticed that
 *  the JVM had wanted to restart.
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class TestUtils
{
    /*---------------------------------------------------------------
     * Constructors
     *-------------------------------------------------------------*/
    private TestUtils()
    {
    }
    
    /*---------------------------------------------------------------
     * Static Methods
     *-------------------------------------------------------------*/
    /**
     * Sleep for the specified time without any exceptions.
     *
     * @param ms Time to sleep.
     */
    public static void sleep( long ms )
    {
        try
        {
            Thread.sleep( ms );
        }
        catch ( InterruptedException e )
        {
        }
    }
    
    /**
     * Writes the specified text to a file.  This method is not an efficient
     *  way to write multiple lines of text as the file is opened and closed
     *  each time this is called.  It can be useful when it is necessary to
     *  quickly write out some text however.
     *
     * @param file File to be written to.
     * @param text Text to be written to the file.  May contain multiple lines.
     *             No additional line feeds will be written out so it is
     *             important to include them if needed.
     * @param encoding Encoding to use when writing the text.  Null implies
     *                 that the system default will be used.
     * @param append If true and the file already exists then the text will be
     *               appended to the end of the file.  If false then a new file
     *               will always be created.
     *
     * @throws IOException If there are any problems writing to the file.
     */
    public static void writeTextFile( File file, String text, String encoding, boolean append )
        throws IOException
    {
        OutputStream fos = new FileOutputStream( file, append );
        try
        {
            Writer osw;
            if ( encoding == null )
            {
                osw = new OutputStreamWriter( fos );
            }
            else
            {
                osw = new OutputStreamWriter( fos, encoding );
            }
            BufferedWriter writer = new BufferedWriter( osw );
            try
            {
                writer.write( text );
            }
            finally
            {
                writer.close();
            }
        }
        finally
        {
            fos.close();
        }
    }
    
    /**
     * Writes the specified text to a file.
     *
     * @param file File to be written to.
     * @param text Text to be written to the file.  May contain multiple lines.
     *             No additional line feeds will be written out so it is
     *             important to include them if needed.
     *
     * @throws IOException If there are any problems writing to the file.
     */
    public static void writeTextFile( File file, String text )
        throws IOException
    {
        writeTextFile( file, text, "UTF-8", false );
    }
    
    /**
     * Writes a command to the Wrapper command file.
     *
     * If the file already exists then a warning will be logged as the file will
     *  be overwritten.  The Wrapper polls for the file at regular intervals and
     *  this can happen if two command are written in rapid succession.  If two
     *  commands must be written then they should be written to the same file at
     *  the same time to avoid any synchronization problems between the Wrapper
     *  and this process.
     *
     * @param command Command to write to the configured Wrapper command file.
     *
     * @throws IOException If there are any problems writing to the file.
     * @throws IllegalStateException If the wrapper.commandfile property has not
     *                               been defined.
     */
    public static void writeWrapperCommand( String command )
        throws IOException, IllegalStateException
    {
        String commandFilename = WrapperPropertyUtil.getStringProperty( "wrapper.commandfile", null );
        if ( commandFilename == null )
        {
            throw new IllegalStateException( "The wrapper.commandfile property has not been configured." );
        }
        
        File commandFile = new File( commandFilename );
        if ( commandFile.exists() )
        {
            System.out.println( Main.getRes().getString( "WARNING - Command file already exists when trying to write a new command: {0}", commandFile.toString() ) );
        }
        
        writeTextFile( commandFile, command );
    }
    
    
    /**
     * Writes a test command to the Wrapper command file.
     *
     * If the file already exists then a warning will be logged as the file will
     *  be overwritten.  The Wrapper polls for the file at regular intervals and
     *  this can happen if two command are written in rapid succession.  If two
     *  commands must be written then they should be written to the same file at
     *  the same time to avoid any synchronization problems between the Wrapper
     *  and this process.
     *
     * @param command Command to write to the configured Wrapper command file.
     *
     * @throws IOException If there are any problems writing to the file.
     * @throws IllegalStateException If the wrapper.commandfile property has not
     *                               been defined or the wrapper.command.enable_tests
     *                               property has not been set to true.
     */
    public static void writeWrapperTestCommand( String command )
        throws IOException, IllegalStateException
    {
        boolean enableTests = WrapperPropertyUtil.getBooleanProperty( "wrapper.commandfile.enable_tests", false );
        if ( !enableTests )
        {
            throw new IllegalStateException( "The wrapper.command.enable_tests property has not been set to true." );
        }
        writeWrapperCommand( command );
    }
}
