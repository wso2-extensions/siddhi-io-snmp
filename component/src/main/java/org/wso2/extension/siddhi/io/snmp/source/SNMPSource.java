/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.extension.siddhi.io.snmp.source;

import org.apache.log4j.Logger;
import org.wso2.extension.siddhi.io.snmp.util.SNMPConstants;
import org.wso2.extension.siddhi.io.snmp.util.SNMPManager;
import org.wso2.extension.siddhi.io.snmp.util.SNMPManagerConfig;
import org.wso2.extension.siddhi.io.snmp.util.SNMPValidator;
import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.annotation.Parameter;
import org.wso2.siddhi.annotation.util.DataType;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.stream.input.source.Source;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.exception.SiddhiAppValidationException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Handle the SNMP get request task.
 */
@Extension(
        name = "snmp",
        namespace = "source",
        description = "The SNMP source allows you to make GET request as manager in order to retrieve the agent " +
                "status periodically. ",
        parameters = {
                @Parameter(name = SNMPConstants.HOST,
                        description = "The address or the IP of the target SNMP agent.",
                        type = DataType.STRING),
                @Parameter(name = SNMPConstants.VERSION,
                        description = "The version of the SNMP protocol. Possible values are 'V1' for version1, " +
                                "'V2C' for versionv2c, and 'V3' for versionv3.",
                        type = DataType.STRING),
                @Parameter(name = SNMPConstants.OIDS,
                        description = "The list of Object Identifiers (OIDs) separated by commas.\n" +
                                "e.g., '1.3.6.1.2.1.1.1.0, 1.3.6.1.2.1.1.6.0'\n OIDs are objects to be identified " +
                                "from the received message. The same objects need to be included in the source " +
                                "mapping as attribute values for Siddhi to identify what attributes they represent.",
                        type = DataType.STRING),
                @Parameter(name = SNMPConstants.REQUEST_INTERVAL,
                        description = "The time interval between two requests. Once you send a request and then " +
                                "send another before the request interval has elapsed, the second request is not" +
                                " delivered.",
                        optional = true,
                        type = DataType.INT,
                        defaultValue = SNMPConstants.DEFAULT_REQUEST_INTERVAL),
                @Parameter(name = SNMPConstants.COMMUNITY,
                        optional = true,
                        description = "The community string of the target SNMP agent. The default value is 'public'." +
                                " This property is required only when the V1 and V2C SNMP versions are used. It is" +
                                " not applicable when you use the V3 version.",
                        defaultValue = SNMPConstants.DEFAULT_COMMUNITY,
                        type = DataType.STRING),
                @Parameter(name = SNMPConstants.AGENT_PORT,
                        description = "The port number of the target SNMP agent.",
                        optional = true,
                        type = DataType.INT,
                        defaultValue = SNMPConstants.DEFAULT_AGENT_PORT),
                @Parameter(name = SNMPConstants.TRANSPORT_PROTOCOL,
                        description = "The transport protocol used by the source to communicate. Supported " +
                                "transport protocols are 'TCP' and 'UDP'.",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_TRANSPORT_PROTOCOL),
                @Parameter(name = SNMPConstants.TIMEOUT,
                        description = "The number of milliseconds for which the system must wait for a response " +
                                "after a request is sent. If the time interval specified here elapses before a " +
                                "response is received, the system retries sending the request. If no response is " +
                                "received even after the number of retries reaches the maximum number specified via" +
                                " the 'SNMPConstantsRETRIES' parameter, this can be due to the transport agent not" +
                                " being available or due to the occurence of a timeout error. An error is logged to" +
                                " inform you of the reason.",
                        optional = true,
                        type = DataType.INT,
                        defaultValue = SNMPConstants.DEFAULT_TIMEOUT),
                @Parameter(name = SNMPConstants.RETRIES,
                        description = "The maximum number of retries that must be attempted by the system if a " +
                                "request does not receive a response within the time interval specifed via the " +
                                "'SNMPConstants.TIMEOUT' parameter.",
                        optional = true,
                        type = DataType.INT,
                        defaultValue = SNMPConstants.DEFAULT_RETRIES),
                // this parameters for v3
                @Parameter(name = SNMPConstants.USER_NAME,
                        description = "The user name of the user that is configured on the transport agent. " +
                                "This property is required only when using SNMP version 3. It is not applicable when" +
                                " using the v2c or v1 version.",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_USERNAME),
                @Parameter(name = SNMPConstants.SECURITY_LVL,
                        description = "The SNMP security level. Possible values are 'AUTH_PRIV', 'AUTH_NO_PRIVE', " +
                        "and 'NO_AUTH_NO_PRIVE'. For more information about these security levels, see " +
                        "[SNMP Security Model and Levels]" +
                        "(https://www.ccexpert.us/wide-area-networks-2/snmp-security-models-and-levels.html)." +
                        " This property is required only for SNMP version 3. It is not applicable when using" +
                        " the v2c or v1 version.",
                        optional = true,
                        type = DataType.INT,
                        defaultValue = SNMPConstants.DEFAULT_SECURITY_LVL),
                @Parameter(name = SNMPConstants.PRIV_PROTOCOL,
                        description = "The privacy protocol of the target SNMP agent. Possible values are 'NO_PRIV'," +
                                " 'PRIVDES', 'PRIVDES128', 'PRIVAES192', 'PRIVAES256', and 'PRIV3DES'. This " +
                                "property is only required for SNMP version 3. It is not applicable when using " +
                                "the v2c or v1 version.",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_PRIV_PROTOCOL),

                @Parameter(name = SNMPConstants.PRIV_PASSWORD,
                        description = "The privacy protocol passphrase of the target SNMP agent. The passphrase " +
                                "must have more than eight characters. This property is required only for SNMP " +
                                "version 3. It is not applicable when using the v2c or v1 version.",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_PRIV_PASSWORD),

                @Parameter(name = SNMPConstants.AUTH_PROTOCOL,
                        description = "The authentication protocol of the target SNMP agent. Possible values are" +
                                " 'NO_AUTH', 'AUTHMD5', 'AUTHSHA', 'AUTHHMAC192SHA256', and 'AUTHHMAC192SHA512'. " +
                                "This property is required only for SNMP version 3. It is not applicable when using" +
                                " the v2c or v1 version.",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_AUTH_PROTOCOL),

                @Parameter(name = SNMPConstants.AUTH_PASSWORD,
                        description = "The authentication protocol passphrase of the target SNMP agent. The" +
                                "passphrase must have more than eight characters. This property is required only " +
                                "for SNMP version 3. It is not applicable when using the v2c or v1 version.",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_AUT_PASSWORD),

                @Parameter(name = SNMPConstants.LOCAL_ENGINE_ID,
                        description = "The local engine ID of the target SNMP agent. The default value is the" +
                                "device-generated ID based on the local IP address and four additional random bytes." +
                                " This property is required only for SNMP version 3. It is not applicable when using" +
                                " the v2c or v1 version.",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_LOCAL_ENGINE_ID),
                @Parameter(name = SNMPConstants.ENGINE_BOOT,
                        description = "The engine boot of the SNMP engine of the target SNMP agent. The default " +
                                "value is '0'. This property is required only for SNMP version 3. It is not " +
                                "applicable when using the v2c or v1 version.",
                        optional = true,
                        type = DataType.INT,
                        defaultValue = SNMPConstants.DEFAULT_ENGINE_BOOT)
        },
        examples = {
                @Example(
                        syntax = "@source(type='snmp', \n" +
                                "@map(type='keyvalue', " +
                                "   @attributes(" +
                                        "'sysUpTime' = '1.3.6.1.2.1.1.3.0', " +
                                        "'sysLocation' = '1.3.6.1.2.1.1.6.0') " +
                                        "),\n" +
                                "host ='127.0.0.1',\n" +
                                "version = 'v1',\n" +
                                "agent.port = '161',\n" +
                                "request.interval = '60000',\n" +
                                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.6.0',\n" +
                                "community = 'public') \n" +
                                " define stream inputStream(sysUpTime string, sysLocation string);\n",
                        description = "This query shows how to make a get request for SNMP version 1. " +
                                "It uses key-value mapping. The transport protocol is UDP by default." +
                                "After starting the Siddhi application, it can get information of the target " +
                                "periodically. The @map annotation specifies that '1.3.6.1.2.1.1.3.0' is the system" +
                                " up time, and '1.3.6.1.2.1.1.6.0' is the system location. The same values are " +
                                "defined as OIDs. Therefore, when a message is received with those values, the " +
                                "system returns the system up time and the system location derived from those values."
                ),
                @Example(
                        syntax = "@source(type='snmp', \n" +
                                "@map(type='keyvalue', " +
                                "   @attributes(" +
                                        "'sysUpTime' = '1.3.6.1.2.1.1.3.0', " +
                                        "'sysLocation' = '1.3.6.1.2.1.1.6.0') " +
                                        "),\n" +
                                "host ='127.0.0.1',\n" +
                                "version = 'v2c',\n" +
                                "agent.port = '161',\n" +
                                "request.interval = '60000',\n" +
                                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.6.0',\n" +
                                "community = 'public') \n" +
                                " define stream inputStream(sysUpTime string, sysLocation string);\n",
                        description = "This example shows how to make get request for SNMP version 2c."
                ),
                @Example(
                        syntax = "@source(type ='snmp', \n" +
                                "@map(type='keyvalue', " +
                                "   @attributes(" +
                                        "'sysUpTime' = '1.3.6.1.2.1.1.3.0', " +
                                        "'sysDescr' = '1.3.6.1.2.1.1.1.0') " +
                                    "),\n" +
                                "host ='127.0.0.1',\n" +
                                "version = 'v3',\n" +
                                "timeout = '1500',\n" +
                                "request.interval = '60000',\n" +
                                "agent.port = '161',\n" +
                                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',\n" +
                                "auth.protocol = 'AUTHMD5',\n" +
                                "priv.protocol = 'PRIVDES',\n" +
                                "priv.password = 'privpass',\n" +
                                "auth.password = 'authpass',\n" +
                                "user.name = 'agent5') \n" +
                                "define stream inputStream(sysUpTime string, sysDescr string);\n",
                        description = "This example shows how to make get request for SNMP version 3. Note that " +
                                "because this example uses version 3, you can also configure security protocols " +
                                "related to the target agent."
                ),
        }
)

public class SNMPSource extends Source {

    private static final Logger LOG = Logger.getLogger(SNMPSource.class);
    private int requestInterval;
    private SNMPManager manager;
    private SourceEventListener sourceEventListener;
    private StreamDefinition streamDefinition;
    private ScheduledFuture future;
    private ScheduledExecutorService scheduledExecutorService;
    private SNMPListener listener;
    private SiddhiAppContext siddhiAppContext;

    @Override
    public void init(SourceEventListener sourceEventListener, OptionHolder optionHolder,
                     String[] requestedTransportPropertyNames, ConfigReader configReader,
                     SiddhiAppContext siddhiAppContext) {
        SNMPManagerConfig managerConfig = SNMPValidator.validateAndGetManagerConfig(optionHolder,
                sourceEventListener.getStreamDefinition().getId(), true);
        this.siddhiAppContext = siddhiAppContext;
        this.sourceEventListener = sourceEventListener;
        this.manager = new SNMPManager(managerConfig);
        this.requestInterval = validateRequestInterval(optionHolder, sourceEventListener.getStreamDefinition().getId());
        this.streamDefinition = sourceEventListener.getStreamDefinition();
        scheduledExecutorService = siddhiAppContext.getScheduledExecutorService();
    }

    private int validateRequestInterval(OptionHolder optionHolder, String streamName) {
        try {
            return Integer.parseInt(optionHolder.validateAndGetStaticValue(SNMPConstants.REQUEST_INTERVAL,
                    SNMPConstants.DEFAULT_REQUEST_INTERVAL));
        } catch (NumberFormatException e) {
            throw new SiddhiAppValidationException(streamName + " Request interval accept only positive integers");
        }
    }

    @Override
    public Class[] getOutputEventClasses() {
        return new Class[]{Map.class};
    }

    @Override
    public void connect(ConnectionCallback connectionCallback) throws ConnectionUnavailableException {
        try {
            manager.listen();
        } catch (IOException e) {
            throw new ConnectionUnavailableException("Error in granting a port from OS in stream " +
                    siddhiAppContext.getName() + " " + this.streamDefinition.getId(), e);
        }
        listener = new SNMPListener(manager, sourceEventListener);
        future = scheduledExecutorService.scheduleAtFixedRate(listener, 0, requestInterval, TimeUnit.MILLISECONDS);
    }

    @Override
    public void disconnect() {
    }

    @Override
    public void destroy() {
        if (future != null) {
            future.cancel(true);
        }
        scheduledExecutorService.shutdown();
        manager.close();
    }

    @Override
    public void pause() {
        listener.pause();
    }

    @Override
    public void resume() {
        listener.resume();
    }

    @Override
    public Map<String, Object> currentState() {
        return null;
    }

    @Override
    public void restoreState(Map<String, Object> map) {
    }
}

