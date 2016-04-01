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
 * WrapperSecondInvocationEvent is fired whenever a second
 *  instance of the Wrapper starts in single invocation mode.  
 * <p>
 * This event is fired only when the properties 'wrapper.single_invocation'
 *   and 'wrapper.single_invocation.notify' are both set to TRUE.
 * <p>
 * WARNING - This event should be handled carefully as it may be originally 
 *  triggered by unauthenticated sources.
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 *
 * @since Wrapper 3.5.28
 */
public class WrapperSecondInvocationEvent
    extends WrapperRemoteControlEvent
{
    /*---------------------------------------------------------------
     * Constructors
     *-------------------------------------------------------------*/
    /**
     * Creates a new WrapperSecondInvocationEvent.
     */
    public WrapperSecondInvocationEvent()
    {
    }
    
    /*---------------------------------------------------------------
     * WrapperSecondInvocationEvent Methods
     *-------------------------------------------------------------*/
    /**
     * Returns a string representation of the event.
     *
     * @return A string representation of the event.
     */
    public String toString()
    {
        return "WrapperSecondInvocationEvent";
    }
}
