package org.tanukisoftware.wrapper.test;

/*
 * Copyright (c) 1999, 2012 Tanuki Software, Ltd.
 * http://www.tanukisoftware.com
 * All rights reserved.
 *
 * This software is the proprietary information of Tanuki Software.
 * You shall use it only in accordance with the terms of the
 * license agreement you entered into with Tanuki Software.
 * http://wrapper.tanukisoftware.com/doc/english/licenseOverview.html
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ListIterator;
import java.util.Arrays;

public class WrapperPrintParam
{
    public static void main( String[] args )
    {
        System.out.println( "Dump all JVM parameters using RuntimeMXBean:" );
        System.out.println( "  (There is a bug in Java that was fixed in 1.7 which causes all parameters being displayed below to be split into different arguments when spaces are encountered.)" );
        try
        {
            Class cRuntimeMXBean = Class.forName( "java.lang.management.RuntimeMXBean" );
            Class cManagementFactory = Class.forName( "java.lang.management.ManagementFactory" );
            Method mGetRuntimeMXBean = cManagementFactory.getMethod( "getRuntimeMXBean",
                                                                     null );
            Method mGetInputArguments = cRuntimeMXBean.getMethod( "getInputArguments",
                                                                  null );
            Object runtimemxBean = mGetRuntimeMXBean.invoke( null,
                                                             ( Object[] ) null );
            List jvm_args = ( List ) mGetInputArguments.invoke( runtimemxBean,
                                                                ( Object[] ) null );
            System.out.println( jvm_args.size() + " JVM Parameter(s):" );
            for ( ListIterator i = jvm_args.listIterator(); i.hasNext(); )
            {
                String arg = (String)i.next();
                System.out.println( "  " + arg );
            }

            List app_args = Arrays.asList( args );
            System.out.println( app_args.size() + " Application Parameter(s):" );
            for ( ListIterator i = app_args.listIterator(); i.hasNext(); )
            {
                String arg = ( String ) i.next();
                System.out.println( "  " + arg );
            }
            
            System.out.println();
            System.out.println( "Resulting System Properties:" );
            for ( ListIterator i = jvm_args.listIterator(); i.hasNext(); )
            {
                String arg = (String)i.next();
                if ( arg.startsWith( "-D" ) )
                {
                    int pos = arg.indexOf( '=' );
                    if ( pos >= 0 )
                    {
                        String name = arg.substring( 2, pos );
                        System.out.println( "  " + name + "=" + System.getProperty( name ) );
                    }
                }
            }
            
        }
        catch ( ClassNotFoundException e )
        {
            e.printStackTrace();
        }
        catch ( IllegalArgumentException e )
        {
            e.printStackTrace();
        }
        catch ( IllegalAccessException e )
        {
            e.printStackTrace();
        }
        catch ( InvocationTargetException e )
        {
            e.printStackTrace();
        }
        catch ( SecurityException e )
        {
            e.printStackTrace();
        }
        catch ( NoSuchMethodException e )
        {
            e.printStackTrace();
        }
    }
}
