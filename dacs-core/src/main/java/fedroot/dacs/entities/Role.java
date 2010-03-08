/*
 * Role.java
 *
 * Created on February 25, 2010, 8:43 AM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.dacs.entities;


/**
 * data type for a DACS role
 * @author rmorriso
 */
public class Role {
    private String name;
    
    /**
     * create new Role with given name
     * @param name the name of the role
     */
    public Role(String name) {
        this.name = name;
    }
    
    /**
     * getter for Role name
     * @return the name of this role
     */
    public String getName() {
        return name;
    }

    /**
     * standard toString() operator
     * @return string representation of this role
     */
    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        return other.getClass() == Role.class && other != null && this.name.equals(((Role)other).name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }


}
