package org.tanukisoftware.wrapper.event;

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
 * WrapperServicePauseResumeEvents are used to notify the listener that the Wrapper
 *  is requesting that the Java application be paused or resumed.  This does not
 *  mean that it should exit, only that it should internally go into an idle state.
 *
 * See the wrapper.pausable and wrapper.pausable.stop_jvm properties for more
 *  information.
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 *
 * @since Wrapper 3.5.0
 */
public abstract class WrapperServiceActionEvent
    extends WrapperServiceEvent
{
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 7901768955067874864L;
    
    /*
     *
     * Note - The following SOURCE_CODE_* values must match those in the wrapper.h file.
     *
     */
    
    /**
     * Action result of a configured filter being fired.
     *  See the wrapper.filter.action.<n> property.
     */
    public static final int SOURCE_CODE_FILTER                  = 1;
    
    /**
     * Action result of a command from a command file.
     *  See the wrapper.commandfile property.
     */
    public static final int SOURCE_CODE_COMMANDFILE             = 2;
    
    /**
     * Action resulted from the Windows Service Manager.  This can happen
     *  from a number of sources including the command line, Service Control
     *  Panel, etc.
     */
    public static final int SOURCE_CODE_WINDOWS_SERVICE_MANAGER = 3;
    
    /**
     * Action result of a matched exit code.
     *  See the wrapper.on_exit.<n> property.
     */
    public static final int SOURCE_CODE_ON_EXIT             = 4;
    
    /**
     * Action result of a deadlock being detected.
     *  See the wrapper.check.deadlock.action property.
     */
    public static final int SOURCE_CODE_DEADLOCK             = 10;
    
    /**
     * Action result of a configured timer being fired.
     *  See the wrapper.timer.<n>.action property.
     */
    public static final int SOURCE_CODE_TIMER                   = 21;
    
    /**
     * Action result of an event command's block timeout expired.
     *  See the wrapper.event.<event_name>.command.block.action property.
     */
    public static final int SOURCE_CODE_COMMAND_BLOCK_TIMEOUT   = 22;
    
    /**
     * Code which keeps track of how the service was paused.
     */
    private int m_actionSourceCode;
    
    /*---------------------------------------------------------------
     * Static Methods
     *-------------------------------------------------------------*/
    /**
     * Returns the name of the specified Source Code.
     *
     * @param actionSourceCode The Source Code whose name is being requested.
     *
     * @return The name of the Source Code.
     */
    public static String getSourceCodeName( int actionSourceCode )
    {
        switch( actionSourceCode )
        {
        case SOURCE_CODE_FILTER:
            return WrapperManager.getRes().getString( "Filter Action" );
            
        case SOURCE_CODE_COMMANDFILE:
            return WrapperManager.getRes().getString( "Command File Action" );
            
        case SOURCE_CODE_WINDOWS_SERVICE_MANAGER:
            return WrapperManager.getRes().getString( "Windows Service Manager" );
            
        case SOURCE_CODE_ON_EXIT:
            return WrapperManager.getRes().getString( "On Exit Action" );
            
        case SOURCE_CODE_DEADLOCK:
            return WrapperManager.getRes().getString( "Deadlock Action" );
            
        case SOURCE_CODE_TIMER:
            return WrapperManager.getRes().getString( "Timer Action" );
            
        case SOURCE_CODE_COMMAND_BLOCK_TIMEOUT:
            return WrapperManager.getRes().getString( "Block Timeout Action" );
            
        default:
            return WrapperManager.getRes().getString( "Unknown Code {0}", new Integer( actionSourceCode ) );
        }
    }
    
    /*---------------------------------------------------------------
     * Constructors
     *-------------------------------------------------------------*/
    /**
     * Creates a new WrapperServiceActionEvent.
     *
     * @param actionSourceCode Source Code specifying where the action originated.
     */
    public WrapperServiceActionEvent( int actionSourceCode )
    {
        m_actionSourceCode = actionSourceCode;
    }
    
    /*---------------------------------------------------------------
     * Methods
     *-------------------------------------------------------------*/
    /**
     * Returns the Source Code describing where the event originated.
     *
     * @return The Source Code.
     */
    public int getSourceCode()
    {
        return m_actionSourceCode;
    }
    
    /**
     * Returns the Source Code name.
     *
     * @return The Source Code name.
     */
    public String getSourceCodeName()
    {
        return getSourceCodeName( m_actionSourceCode );
    }
    
    /**
     * Returns a string representation of the event.
     *
     * @return A string representation of the event.
     */
    public String toString()
    {
        return "WrapperServiceActionEvent[actionSourceCode=" + getSourceCodeName() + "]";
    }
}
