/*
 * AbstractEntityLoader.java
 * Created on Jan 16, 2010 5:00:08 PM.
 * Copyright (c) 2010 Metalogic Software Corporation
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */


package fedroot.dacs.entities;

import fedroot.dacs.exceptions.DacsException;
import fedroot.dacs.http.DacsClientContext;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Roderick Morrison <rmorriso at fedroot.com>
 */
abstract public class AbstractEntityLoader {


    /**
     * Returns the XML required to instantiate this entity.
     * @param dacsClientContext the dacsClientContext to issue the Web service request
     * @return the XML document returned in the Web service request
     */
    abstract protected InputStream getXmlStream(DacsClientContext dacsClientContext)  throws DacsException;

    /**
     * loads an entity from an XML document
     *
     * @param xml
     * @throws Exception
     */
    abstract protected void loadEntityFromStream(InputStream inputStream) throws DacsException;

    protected DacsClientContext dacsClientContext;

    public AbstractEntityLoader(DacsClientContext dacsClientContext) {
        this.dacsClientContext = dacsClientContext;
    }

    public void load() throws DacsException {
        loadEntityFromStream(getXmlStream(dacsClientContext));
    }

}
