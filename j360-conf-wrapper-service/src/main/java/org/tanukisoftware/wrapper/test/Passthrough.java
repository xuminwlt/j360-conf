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

import org.tanukisoftware.wrapper.WrapperManager;

/**
 * @author Support <support@tanukisoftware.com>
 */
public class Passthrough {
    
    /*---------------------------------------------------------------
     * Main Method
     *-------------------------------------------------------------*/
    public static void main(String[] args) {
        System.out.println( Main.getRes().getString( "\nTest passthrough parameters...") );
       
        if ( args.length > 0 ) {
            if ( "start".compareTo(args[0]) == 0 ) {
                System.out.println( Main.getRes().getString( "\nStarting the application with the following parameter(s):") );
            } else if ( "stop".compareTo(args[0]) == 0 ) {
                System.out.println( Main.getRes().getString( "\nStopping the application with the following parameter(s):") );
            } else {
                printError();
            }
            for (int i = 1; i < args.length; i++) {
                System.out.println(args[i]);
            }
        } else {
            printError();
        }
    }
    
    /**
     * Just print a general error message 
     */
    public static void printError() {
        System.out.println( Main.getRes().getString( "Invalid parameter...") );
    }
}
