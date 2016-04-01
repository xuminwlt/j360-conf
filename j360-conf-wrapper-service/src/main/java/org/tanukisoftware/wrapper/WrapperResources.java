package org.tanukisoftware.wrapper;

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

import java.text.MessageFormat;

/**
 * A resource bundle which is used to help localize applications to the default
 *  locale of the JVM.   Resources are stored in MO files using the standard UNIX
 *  gettext resources.
 *
 * For example,<P>
 * <CODE>
 * WrapperResources res = WrapperManager.loadWrapperResources( "myapp", "../lang/" );
 * </CODE>
 *
 * To use the WrapperResources, make a call to any of the <CODE>getString()</CODE>
 *  methods.  If the resource files are not found, or the specific key is not found
 *  then the key is returned unmodified.
 *
 * All resource keys passed to <CODE>getString()</CODE> will be processed using the
 *  java.util.MessageFormat class.  As such, single quotes must be escaped.
 *  This class can optionally validate all such keys and logs warnings about
 *  keys which fail these checks.  It is possible to enable this validation with
 *  the following property.  (Defaults to FALSE)
 *  -Dorg.tanukisoftware.wrapper.WrapperResources.validateResourceKeys=TRUE
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public final class WrapperResources
{
    /** Error level log channel */
    private static WrapperPrintStream m_outError;
    
    /** True if resource keys should be validated. */
    private static boolean m_validateResourceKeys;
    
    /** Helper object to reduce number of new objects. */
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    
    /** Id of the resource.  Assigned within native code. */
    private long m_Id;
    
    /*---------------------------------------------------------------
     * Static Methods
     *-------------------------------------------------------------*/
    static
    {
        m_outError = new WrapperPrintStream( System.out, "WrapperResources Error: " );
        m_validateResourceKeys = WrapperSystemPropertyUtil.getBooleanProperty( WrapperResources.class.getName() + ".validateResourceKeys", false );
    }
    
    /*---------------------------------------------------------------
     * Constructors
     *-------------------------------------------------------------*/
    /**
     * WrapperResources instances are created using the WrapperManager.loadWrapperResources method.
     */
    protected WrapperResources()
    {
    }
    
    /*---------------------------------------------------------------
     * Finalizers
     *-------------------------------------------------------------*/
    protected void finalize()
        throws Throwable
    {
        try
        {
            if ( WrapperManager.isLoggingFinalizers() )
            {
                // This can't be localized because of when it happens.
                System.out.println( "WrapperResources.finalize Id=" + m_Id );
            }
            
            if ( m_Id != 0 )
            {
                if ( WrapperManager.isNativeLibraryOk() )
                {
                    // clean up after the resource.
                    nativeDestroyResource();
                }
            }
        }
        finally
        {
            super.finalize();
        }
    }
    
    /*---------------------------------------------------------------
     * Native Methods
     *-------------------------------------------------------------*/
    private native String nativeGetLocalizedString(String key);
    private native void nativeDestroyResource();

    /*---------------------------------------------------------------
     * Methods
     *-------------------------------------------------------------*/
    /**
     * Checks the resource key to make sure that all of its single quotes are excaped correctly.
     *
     * @param str String which was or will be localized.
     * @param localized True if str is a localized string, false if it is the key.
     */
    private void validateResourceKey( String str, boolean localized )
    {
        int pos = 0;
        int len = str.length();
        do
        {
            pos = str.indexOf( '\'', pos );
            if ( pos < 0 )
            {
                break;
            }
            
            pos++;
            if ( ( pos >= len ) || ( str.charAt( pos ) != '\'' ) )
            {
                // Unescaped quote at end of string.
                //  If one of the following keys or their localized strings have such a problem this will recurse.
                if ( localized )
                {
                    m_outError.println( WrapperManager.getRes().getString( "Localized resource string''s single quotes not escaped correctly: {0}", str ) );
                }
                else
                {
                    m_outError.println( WrapperManager.getRes().getString( "Resource key''s single quotes not escaped correctly: {0}", str ) );
                }
                break;
            }
            pos++;
        }
        while ( pos < len );
    }

    /**
     * Request a localized version of the specified key.
     *
     * The returned string will be the raw string which has not yet
     *  been processed by MessageFormat.
     *
     * @param key Resource to be localized.
     *
     * @return The localized version of the key.
     */
    private String getStringInner( String key )
    {
        if ( m_validateResourceKeys )
        {
            validateResourceKey( key, false );
        }
        
        if ( ( m_Id != 0 ) && WrapperManager.isNativeLibraryOk() )
        {
            String str = nativeGetLocalizedString( key );
            if ( !str.equals( key ) )
            {
                if ( m_validateResourceKeys )
                {
                    validateResourceKey( str, true );
                }
            }
            return str;
        }
        else
        {
            return key;
        }
    }
    
    /**
     * Request a localized version of the specified key.
     *
     * @param key Resource to be localized.
     *
     * @return The localized version of the key.
     */
    public String getString( String key )
    {
        // Even through we are not replacing any tokens, always call the format
        //  method so things like escaped quotes will be handled in a consistent way.
        return MessageFormat.format( getStringInner( key ), EMPTY_OBJECT_ARRAY );
    }
    
    /**
     * Request a localized version of the specified key.
     * <p>
     * Individual tokens will be replaced with the specified parameters using the
     *  Java MessageFormat format method.
     *
     * @param key Resource to be localized.
     * @param arguments An array of argumens to be replaced in the resource.
     *
     * @return The localized version of the key.
     *
     * @see MessageFormat
     */
    public String getString( String key, Object[] arguments )
    {
        return MessageFormat.format( getStringInner( key ), arguments );
    }
    
    /**
     * Request a localized version of the specified key.
     * <p>
     * Individual tokens will be replaced with the specified parameters using the
     *  Java MessageFormat format method.
     *
     * @param key Resource to be localized.
     * @param arg0 An argument to be replaced in the resource.
     *
     * @return The localized version of the key.
     *
     * @see MessageFormat
     */
    public String getString( String key, Object arg0 )
    {
        return MessageFormat.format( getStringInner( key ), new Object[] { arg0 } );
    }
    
    /**
     * Request a localized version of the specified key.
     * <p>
     * Individual tokens will be replaced with the specified parameters using the
     *  Java MessageFormat format method.
     *
     * @param key Resource to be localized.
     * @param arg0 An argument to be replaced in the resource.
     * @param arg1 An argument to be replaced in the resource.
     *
     * @return The localized version of the key.
     *
     * @see MessageFormat
     */
    public String getString( String key, Object arg0, Object arg1 )
    {
        return MessageFormat.format( getStringInner( key ), new Object[] { arg0, arg1 } );
    }
    
    /**
     * Request a localized version of the specified key.
     * <p>
     * Individual tokens will be replaced with the specified parameters using the
     *  Java MessageFormat format method.
     *
     * @param key Resource to be localized.
     * @param arg0 An argument to be replaced in the resource.
     * @param arg1 An argument to be replaced in the resource.
     * @param arg2 An argument to be replaced in the resource.
     *
     * @return The localized version of the key.
     *
     * @see MessageFormat
     */
    public String getString( String key, Object arg0, Object arg1, Object arg2 )
    {
        return MessageFormat.format( getStringInner( key ), new Object[] { arg0, arg1, arg2 } );
    }
    
    /**
     * Request a localized version of the specified key.
     * <p>
     * Individual tokens will be replaced with the specified parameters using the
     *  Java MessageFormat format method.
     *
     * @param key Resource to be localized.
     * @param arg0 An argument to be replaced in the resource.
     * @param arg1 An argument to be replaced in the resource.
     * @param arg2 An argument to be replaced in the resource.
     * @param arg3 An argument to be replaced in the resource.
     *
     * @return The localized version of the key.
     *
     * @see MessageFormat
     */
    public String getString( String key, Object arg0, Object arg1, Object arg2, Object arg3 )
    {
        return MessageFormat.format( getStringInner( key ), new Object[] { arg0, arg1, arg2, arg3 } );
    }
    
    /**
     * Request a localized version of the specified key.
     * <p>
     * Individual tokens will be replaced with the specified parameters using the
     *  Java MessageFormat format method.
     *
     * @param key Resource to be localized.
     * @param arg0 An argument to be replaced in the resource.
     * @param arg1 An argument to be replaced in the resource.
     * @param arg2 An argument to be replaced in the resource.
     * @param arg3 An argument to be replaced in the resource.
     * @param arg4 An argument to be replaced in the resource.
     *
     * @return The localized version of the key.
     *
     * @see MessageFormat
     */
    public String getString( String key, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4 )
    {
        return MessageFormat.format( getStringInner( key ), new Object[] { arg0, arg1, arg2, arg3, arg4 } );
    }
}

