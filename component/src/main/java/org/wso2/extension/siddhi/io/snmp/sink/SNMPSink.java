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
package org.wso2.extension.siddhi.io.snmp.sink;

import org.apache.log4j.Logger;
import org.wso2.extension.siddhi.io.snmp.sink.exceptions.SNMPSinkRuntimeException;
import org.wso2.extension.siddhi.io.snmp.util.SNMPConstants;
import org.wso2.extension.siddhi.io.snmp.util.SNMPManager;
import org.wso2.extension.siddhi.io.snmp.util.SNMPManagerConfig;
import org.wso2.extension.siddhi.io.snmp.util.SNMPValidator;
import org.wso2.extension.siddhi.io.snmp.util.exceptions.SNMPRuntimeException;
import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.annotation.Parameter;
import org.wso2.siddhi.annotation.util.DataType;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.exception.ConnectionUnavailableException;
import org.wso2.siddhi.core.stream.output.sink.Sink;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.core.util.transport.DynamicOptions;
import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhi.query.api.definition.StreamDefinition;

import java.io.IOException;
import java.util.Map;

/**
 * Handle the SNMP set request task.
 */

@Extension(
        name = "snmp",
        namespace = "sink",
        description = "The SNMP Sink allows you to make set requests as a manager and make changes on agent. ",
        parameters = {
                @Parameter(name = SNMPConstants.HOST,
                        description = "The host name or the IP of the target that functions as the SNMP agent.",
                        type = DataType.STRING),
                @Parameter(name = SNMPConstants.VERSION,
                        description = "The version of the SNMP protocol. Possible values are 'V1' for version1, " +
                                "'V2C' for versionv2c, and 'V3' for versionv3.",
                        type = DataType.STRING),
                @Parameter(name = SNMPConstants.COMMUNITY,
                        description = "The community string of the target SNMP agent. The default value is 'public'." +
                                " This property is needed only when using SNMP versions V1 and  V2C. It is not" +
                                " applicable when using V3.",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_COMMUNITY),
                @Parameter(name = SNMPConstants.AGENT_PORT,
                        description = "The port of the target SNMP agent.",
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
                // v3 parameters
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
                        syntax = "@Sink(type='snmp',\n" +
                                "@map(type='keyvalue', @payload('1.3.6.1.2.1.1.6.0' = 'sysLocation')),\n" +
                                "host = '127.0.0.1',\n" +
                                "version = 'v1',\n" +
                                "community = 'public',\n" +
                                "agent.port = '161',\n" +
                                "retries = '5')\n" +
                                "define stream outputStream(sysLocation string);\n",
                        description = "This example shows how to make set request using SNMP version v1 " +
                                "It uses keyvalue mapping and the default transport protocol is UDP. The @map " +
                                "annotation specifies that '1.3.6.1.2.1.1.6.0' represents the system location. " +
                                "Therefore, when you send the message, the system location of your localhost's " +
                                "SNMP agent is sent in the encoded form. The recipient of the massage can derive " +
                                "the system location from '1.3.6.1.2.1.1.6.0' sent with the message. After sending" +
                                " the message, it can configure a target agent in the localhost sysLocation (related" +
                                " to OID)."
                ),
                @Example(
                        syntax = "@Sink(type='snmp',\n" +
                                "@map(type='keyvalue', @payload('1.3.6.1.2.1.1.6.0' = 'sysLocation')),\n" +
                                "host = '127.0.0.1',\n" +
                                "version = 'v2c',\n" +
                                "community = 'public',\n" +
                                "agent.port = '161',\n" +
                                "retries = '5')\n" +
                                "define stream outputStream(sysLocation string);\n",

                        description = "The above query shows how to make a set request using SNMP version v2c. "
                ),
                @Example(
                        syntax = "@Sink(type='snmp',\n" +
                                "@map(type='keyvalue', " +
                                "@payload('1.3.6.1.2.1.1.4.0' = 'sysLocation', '1.3.6.1.2.1.1.1.0' = 'sysDscr')),\n" +
                                "host = '127.0.0.1',\n" +
                                "version = 'v3',\n" +
                                "agent.port = '161',\n" +
                                "priv.password = 'privpass',\n" +
                                "auth.protocol = 'AUTHMD5',\n" +
                                "priv.protocol = 'PRIVDES',\n" +
                                "auth.password = 'authpass',\n" +
                                "priv.password = 'privpass',\n" +
                                "user.name = 'agent5', \n" +
                                "retries = '5')\n" +
                                "define stream outputStream(sysLocation string, sysDscr string);\n",
                        description = "The above query shows how to make a set request using SNMP version v3. Note " +
                                "that in this example, you can also configure security protocols related to the " +
                                "target agent."
                ),
        }
)

// for more information refer https://wso2.github.io/siddhi/documentation/siddhi-4.0/#sinks

public class SNMPSink extends Sink {

    private static final Logger LOG = Logger.getLogger(SNMPSink.class);
    private SNMPManagerConfig managerConfig;
    private SNMPManager manager;
    private StreamDefinition streamDefinition;
    private SiddhiAppContext siddhiAppContext;

    @Override
    public Class[] getSupportedInputEventClasses() {
        return new Class[]{Map.class};
    }

    @Override
    public String[] getSupportedDynamicOptions() {
        return new String[0];
    }

    @Override
    protected void init(StreamDefinition streamDefinition, OptionHolder optionHolder, ConfigReader configReader,
                        SiddhiAppContext siddhiAppContext) {
        this.siddhiAppContext = siddhiAppContext;
        this.streamDefinition = streamDefinition;
        managerConfig = SNMPValidator.validateAndGetManagerConfig(optionHolder,
                this.streamDefinition.getId(), false);
        manager = new SNMPManager(managerConfig);
    }


    @Override
    public void publish(Object payload, DynamicOptions dynamicOptions) {
        Map<String, String> data = (Map) payload;
        try {
            manager.setRequestAndValidate(data);
        } catch (IOException e) {
            throw new SNMPSinkRuntimeException("snmp request timeout in : " + siddhiAppContext.getName() + " "
                    + this.streamDefinition.getId(), e);
        } catch (SNMPRuntimeException ex) {
            throw new SNMPSinkRuntimeException("Error in setting on agent in" + siddhiAppContext.getName() + " "
                    + this.streamDefinition.getId() , ex);
        } finally {
            managerConfig.clear();
        }
    }

    @Override
    public void connect() throws ConnectionUnavailableException {
        try {
            manager.listen();
        } catch (IOException e) {
            throw new ConnectionUnavailableException("Error in granting a port from OS in stream " +
                    siddhiAppContext.getName() + " " + this.streamDefinition.getId(), e);
        }
    }

    @Override
    public void disconnect() {
        if (manager != null) {
            manager.close();
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public Map<String, Object> currentState() {
        return null;
    }

    @Override
    public void restoreState(Map<String, Object> map) {
    }
}

