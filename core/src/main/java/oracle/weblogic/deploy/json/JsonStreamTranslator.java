/*
 * Copyright (c) 2017, 2018, Oracle and/or its affiliates. All rights reserved.
 * The Universal Permissive License (UPL), Version 1.0
 */
package oracle.weblogic.deploy.json;

import java.io.IOException;
import java.io.InputStream;

import oracle.weblogic.deploy.logging.PlatformLogger;
import oracle.weblogic.deploy.logging.WLSDeployLogFactory;

import org.python.core.PyDictionary;

/**
 * An implementation of the JSON parser/translator that reads the JSON input from an input stream.
 */
public class JsonStreamTranslator extends AbstractJsonTranslator {
    private static final String CLASS = JsonStreamTranslator.class.getName();
    private static final PlatformLogger LOGGER = WLSDeployLogFactory.getLogger("wlsdeploy.json");

    private String streamFileName;
    private InputStream jsonStream;

    /**
     * The constructor.
     *
     * @param streamFileName the name of the file used to create the InputStream (used only for logging purposes)
     * @param jsonStream the input stream
     */
    public JsonStreamTranslator(String streamFileName, InputStream jsonStream) {
        this(streamFileName, jsonStream, false);
    }

    /**
     * The constructor used to specify ordering or not.
     *
     * @param streamFileName the name of the file used to create the InputStream (used only for logging purposes)
     * @param jsonStream the input stream
     * @param useOrderedDict whether or not to use an ordered dictionary for storing translation results
     */
    public JsonStreamTranslator(String streamFileName, InputStream jsonStream, boolean useOrderedDict) {
        this.streamFileName = streamFileName;
        this.jsonStream = jsonStream;
        this.useOrderedDict = useOrderedDict;
    }

    /**
     * This method triggers parsing of the JSON and conversion into the Python dictionary.  Note that is closes
     * the input stream when it is finished, making the instance no longer viable.
     *
     * @return the python dictionary corresponding to the JSON input
     * @throws JsonException if an error occurs while reading the input
     */
    @Override
    public PyDictionary parse() throws JsonException {
        final String METHOD = "parse";

        LOGGER.entering(CLASS, METHOD);
        PyDictionary result = null;
        if (jsonStream != null) {
            try {
                result = parseInternal(streamFileName, jsonStream);
            } finally {
                try {
                    jsonStream.close();
                } catch (IOException ioe) {
                    LOGGER.warning("WLSDPLY-18022", ioe, streamFileName, ioe.getLocalizedMessage());
                }
                jsonStream = null;
            }
        }
        LOGGER.exiting(CLASS, METHOD);
        return result;
    }

    @Override
    protected String getClassName() {
        return CLASS;
    }

    @Override
    protected PlatformLogger getLogger() {
        return LOGGER;
    }
}
