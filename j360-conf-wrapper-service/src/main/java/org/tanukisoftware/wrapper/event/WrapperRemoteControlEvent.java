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

/**
 * WrapperRemoteControlEvent are fired when a signal is caught from 
 *  outside the Wrapper (for example a signal coming from a pipe). 
 * <p>
 * WARNING - Those events should be handled carefully as they may be originally 
 *  triggered by unauthenticated sources.
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public abstract class WrapperRemoteControlEvent
    extends WrapperEvent
{
    /*---------------------------------------------------------------
     * Constructors
     *-------------------------------------------------------------*/
    /**
     * Creates a new WrapperRemoteControlEvent.
     */
    public WrapperRemoteControlEvent()
    {
    }
    
    /*---------------------------------------------------------------
     * WrapperRemoteControlEvent Methods
     *-------------------------------------------------------------*/
    /**
     * Returns a set of event flags for which the event should be fired.
     *  This value is compared with the mask supplied when when a
     *  WrapperEventListener is registered to decide which listeners should
     *  receive the event.
     * <p>
     * If subclassed, the return value of the super class should usually
     *  be ORed with any additional flags.
     *
     * @return a set of event flags.
     */
    public long getFlags()
    {
        return super.getFlags() | WrapperEventListener.EVENT_FLAG_REMOTE_CONTROL;
    }
}
