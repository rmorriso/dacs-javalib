/*
 * AutoComplete.java
 *
 * Created on May 31, 2006, 1:10 PM
 *
 * Copyright (c) 2006 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 
 */

package com.fedroot.util;

import java.util.Vector;

/**
 *
 * @author ricmorri
 *
 * Definition of methods for auto completing an entry in a textfield.
 * <br><b>Source Code: </b><a href="source/AutoComplete.html">AutoComplete.java</a>
 */

public class AutoComplete {
    
    private static int color_index = 0;
    private static int state_index = 0;
    private static int body_index = 0;
    private static int make_index = 0;
    
    public static int NEXT = 0;
    public static int PREV = 0;
    
    public static void registerKeys( int next, int prev  ) {
        NEXT = next;
        PREV = prev;
    }
// ======================================================
    static String states =
            "ALAKAZARCACOCTDEDCFLGAHIIDILINIAKSKYLAMEMDMAMIMNMSMOMTNENVNHNJNMNYNCNDOHOKORPAPRRISCSDTNTXUTVTVAWAWVWIWY";
    
    public static String getState( int direction ) {
        if( direction == NEXT )
            return getNextState( );
        return getPreviousState( );
        
    }
    public static String getNextState( ) {
        int t = state_index;
        if( state_index == ((states.length()-2)/2) )
            state_index = 0;
        else
            state_index++;
        return states.substring( t*2, t*2+2 );
    }
    
    public static String getPreviousState( ) {
        int t = state_index;
        if( state_index == 0 )
            state_index = (states.length()-2)/2;
        else
            state_index--;
        return states.substring( t*2, t*2+2 );
    }
// ======================================================
    private static String color_names[] =
    {
        "RED","GREEN","BLUE","BLACK","SILVER","BROWN","WHITE","TAN","MAROON","GOLD","PURPLE"
    };
    
    // loop through colors
    public static String getColor( int direction ) {
        if( direction == NEXT )
            return getNextColor( );
        return getPreviousColor( );
    }
    
    public static String getNextColor( ) {
        return color_names[++color_index%color_names.length];
    }
    
    public static String getPreviousColor( ) {
        color_index = --color_index%color_names.length;
        if( color_index < 0 )
            color_index = color_names.length-1;
        return color_names[color_index];
    }
// ======================================================
    private static String body_names[] =
    {
        "2 DOOR","4 DOOR","CONVERTIBLE","HATCHBACK","P/U","ST WAGON","SUV","VAN"
    };
    // loop through body styles
    public static String getBody( int direction ) {
        if( direction == NEXT )
            return getNextBody( );
        return getPreviousBody( );
    }
    
    public static String getNextBody( ) {
        return body_names[++body_index%body_names.length];
    }
    
    public static String getPreviousBody( ) {
        body_index = --body_index%body_names.length;
        if( body_index < 0 )
            body_index = body_names.length-1;
        return body_names[body_index];
    }
// ======================================================
    private static String make_names[] =
    {
        "ACURA","AUDI","ALFA ROMEO",
        "BUICK","BMW",
        "CHEVROLET","CHRYSLER","CADILLAC",
        "EAGLE",
        "DODGE","DAEWOO",
        "FORD","FERRARI",
        "GMC",
        "HONDA","HYUNDAI","HUMMER",
        "ISUZU","INFINITI",
        "JEEP","JAGUAR",
        "KIA",
        "LINCOLN","LEXUS","LAND ROVER","LANCIA","LAMBORGHINI","LOTUS",
        "MERCURY","MITSUBISHI MOTORS","MAZDA","MERCEDES-BENZ",
        "NISSAN MOTORS",
        "OLDSMOBILE",
        "PLYMOUTH","PONTIAC","PORSCHE","PEUGEOT",
        "ROLLS ROYCE",
        "SUBARU","SUZUKI","SAAB","SATURN",
        "TOYOTA",
        "VOLKSWAGEN","VOLVO","VAUXHALL"
    };
    
    public static String getMake( int direction ) {
        if( direction == NEXT )
            return getNextMake( );
        return getPreviousMake( );
    }
    
    public static String getPreviousMake( ) {
        make_index = --make_index%make_names.length;
        if( make_index < 0 )
            make_index = make_names.length-1;
        return make_names[make_index];
    }
    
    public static String getNextMake( ) {
        return make_names[++make_index%make_names.length];
    }
    
    public static String getMake( String target ) {
        target = target.trim();
        for( int i = 0; i < target.length(); i++ ) {
            String tmp = target.substring(0,target.length()-i);
            
            for( int j = 0; j < make_names.length; j++ ) {
                if( tmp.length() <= make_names[j].length() ) {
                    String names = make_names[j].substring(0,target.length()-i);
                    if( tmp.compareToIgnoreCase( names ) == 0 )
                        return make_names[j];
                }
            }
        }
        return target;
    }
// ======================================================
    // search a vector of auto data for an address given a name
    public static DealerData findAddress( Vector data, String key ) {
        if( data.size() > 0 ) {
            // check first element for proper data type
            // what about the rest?
            if( !(data.elementAt( 0 ) instanceof AutoElementData) )
                return null;
        } else
            return null;
        if( key == null )
            return null;
        
        for( int i = 0; i < key.length(); i++ ) // linear search
        { // version 2.0 will have a binary search method
            String tmp = key.substring(0,key.length()-i);
            
            for( int j = 0; j <  data.size(); j++ ) {
                String sold_name = ((AutoElementData)data.elementAt( j )).getSoldData().getName();
                String purchase_name = ((AutoElementData)data.elementAt( j )).getPurchaseData().getName();
                if( sold_name != null && tmp.length() <= sold_name.length()  ) {
                    String name = sold_name.substring(0,key.length()-i);
                    if( tmp.compareToIgnoreCase( name ) == 0 )
                        return ((AutoElementData)data.elementAt( j )).getSoldData();
                }
                if( purchase_name != null && tmp.length() <= purchase_name.length()  ) {
                    String name = purchase_name.substring(0,key.length()-i);
                    if( tmp.compareToIgnoreCase( name ) == 0 )
                        return ((AutoElementData)data.elementAt( j )).getPurchaseData();
                }
            }
        }
        return null;
    }
}
