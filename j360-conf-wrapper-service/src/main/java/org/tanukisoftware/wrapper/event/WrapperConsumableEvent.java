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
 * WrapperConsumableEvent is used to keep trace whether
 *  the event has been handled or not.
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public abstract class WrapperConsumableEvent
    extends WrapperEvent
{
    /** True if the event has been consumed. */
    private boolean m_consumed;

    /*---------------------------------------------------------------
     * Constructors
     *-------------------------------------------------------------*/
    /**
     * Creates a new WrapperConsumableEvent.
     */
    public WrapperConsumableEvent()
    {
    }
    
    /*---------------------------------------------------------------
     * WrapperConsumableEvent Methods
     *-------------------------------------------------------------*/
    
    /**
     * Mark the event as consumed.  This should be done if the event
     *  has been handled.
     * <p>
     * On Windows, some events are sent both to the JVM and Wrapper processes.
     *  Event if the CTRL-C event is ignored within the JVM, the Wrapper
     *  process may still initiate a shutdown.
     */
    public void consume()
    {
        m_consumed = true;
    }
    
    /**
     * Returns true if the event has been consumed.
     *
     * @return True if the event has been consumed.
     */
    public boolean isConsumed()
    {
        return m_consumed;
    }
}
