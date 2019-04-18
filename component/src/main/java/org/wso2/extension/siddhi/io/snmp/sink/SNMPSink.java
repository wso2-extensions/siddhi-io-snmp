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

import io.siddhi.annotation.Example;
import io.siddhi.annotation.Extension;
import io.siddhi.annotation.Parameter;
import io.siddhi.annotation.util.DataType;
import io.siddhi.core.config.SiddhiAppContext;
import io.siddhi.core.exception.ConnectionUnavailableException;
import io.siddhi.core.stream.ServiceDeploymentInfo;
import io.siddhi.core.stream.output.sink.Sink;
import io.siddhi.core.util.config.ConfigReader;
import io.siddhi.core.util.snapshot.state.State;
import io.siddhi.core.util.snapshot.state.StateFactory;
import io.siddhi.core.util.transport.DynamicOptions;
import io.siddhi.core.util.transport.OptionHolder;
import io.siddhi.query.api.definition.StreamDefinition;
import org.apache.log4j.Logger;
import org.wso2.extension.siddhi.io.snmp.sink.exceptions.SNMPSinkRuntimeException;
import org.wso2.extension.siddhi.io.snmp.util.SNMPConstants;
import org.wso2.extension.siddhi.io.snmp.util.SNMPManager;
import org.wso2.extension.siddhi.io.snmp.util.SNMPManagerConfig;
import org.wso2.extension.siddhi.io.snmp.util.SNMPValidator;
import org.wso2.extension.siddhi.io.snmp.util.exceptions.SNMPRuntimeException;

import java.io.IOException;
import java.util.Map;

/**
 * Handle the SNMP set request task.
 */

@Extension(
        name = "snmp",
        namespace = "sink",
        description = " SNMP Sink allows user to make set requests as a manager and make changes on agent. " +
                "It communicate through SNMP and transport layer is optional as TCP and UDP",
        parameters = {
                @Parameter(name = SNMPConstants.HOST,
                        description = "Host name or ip of the target which is SNMP agent.",
                        type = DataType.STRING),
                @Parameter(name = SNMPConstants.VERSION,
                        description = "Version of the snmp protocol. Acceptance parameters are V1 for version1, " +
                                "V2C for versionv2c, V3 for versionv3. ",
                        type = DataType.STRING),
                @Parameter(name = SNMPConstants.COMMUNITY,
                        description = "Community string of the target SNMP agent. Default value is 'public'." +
                                " This property only uses SNMP V1, V2C and do not need to provide this when using V3",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_COMMUNITY),
                @Parameter(name = SNMPConstants.AGENT_PORT,
                        description = "Port of the target SNMP agent.",
                        optional = true,
                        type = DataType.INT,
                        defaultValue = SNMPConstants.DEFAULT_AGENT_PORT),
                @Parameter(name = SNMPConstants.TRANSPORT_PROTOCOL,
                        description = "Transport protocol. Acceptance parameters TCP, UDP",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_TRANSPORT_PROTOCOL),
                @Parameter(name = SNMPConstants.TIMEOUT,
                        description = "Waiting time for response of a request in milliseconds.",
                        optional = true,
                        type = DataType.INT,
                        defaultValue = SNMPConstants.DEFAULT_TIMEOUT),
                @Parameter(name = SNMPConstants.RETRIES,
                        description = "Number of retries when a request fails.",
                        optional = true,
                        type = DataType.INT,
                        defaultValue = SNMPConstants.DEFAULT_RETRIES),
                // v3 parameters
                @Parameter(name = SNMPConstants.USER_NAME,
                        description = "User Name of the user that configured on target agent. " +
                                "This property only uses for SNMP version 3 and do not need to provide" +
                                " this when using other versions v2c, v1.",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_USERNAME),
                @Parameter(name = SNMPConstants.SECURITY_LVL,
                        description = "Security level. Acceptance parameters AUTH_PRIV, AUTH_NO_PRIVE, " +
                                "NO_AUTH_NO_PRIVE. This property only uses for SNMP version 3 and do not need" +
                                " to provide this when using other versions v2c, v1.",
                        optional = true,
                        type = DataType.INT,
                        defaultValue = SNMPConstants.DEFAULT_SECURITY_LVL),
                @Parameter(name = SNMPConstants.PRIV_PROTOCOL,
                        description = "Privacy protocol of the target SNMP agent. Acceptance parameters NO_PRIV," +
                                " PRIVDES, PRIVDES128, PRIVAES192, PRIVAES256, PRIV3DES. This property only uses for" +
                                " SNMP version 3 and do not need to provide this when using other versions v2c, v1.",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_PRIV_PROTOCOL),

                @Parameter(name = SNMPConstants.PRIV_PASSWORD,
                        description = "Privacy protocol passphrase of the target SNMP agent." +
                                " Passphrase should have more than 8 characters. This property only uses for SNMP " +
                                "version 3 and do not need to provide this when using other versions v2c, v1.",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_PRIV_PASSWORD),

                @Parameter(name = SNMPConstants.AUTH_PROTOCOL,
                        description = "Authentication protocol of the target SNMP agent. Can use NO_AUTH, AUTHMD5," +
                                " AUTHSHA, AUTHHMAC192SHA256, AUTHHMAC192SHA512. This property only uses for SNMP " +
                                "version 3 and do not need to provide this when using other versions v2c, v1.",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_AUTH_PROTOCOL),

                @Parameter(name = SNMPConstants.AUTH_PASSWORD,
                        description = "Authentication protocol passphrase of the target SNMP agent." +
                                "Passphrase should have more than 8 characters. This property only uses for SNMP " +
                                "version 3 and do not need to provide this when using other versions v2c, v1.",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_AUT_PASSWORD),

                @Parameter(name = SNMPConstants.LOCAL_ENGINE_ID,
                        description = "Local engine ID of the target SNMP agent. Default value is " +
                                "device-generated ID based on the local IP address and additional four " +
                                "random bytes. This property only uses for SNMP version 3 and do not need to " +
                                "provide this when using other versions v2c, v1.",
                        optional = true,
                        type = DataType.STRING,
                        defaultValue = SNMPConstants.DEFAULT_LOCAL_ENGINE_ID),
                @Parameter(name = SNMPConstants.ENGINE_BOOT,
                        description = "Engine boot of the snmp engine of the target SNMP agent. " +
                                "Default value is 0. This property only uses for SNMP version 3 and do not need to " +
                                "provide this when using other versions v2c, v1.",
                        optional = true,
                        type = DataType.INT,
                        defaultValue = SNMPConstants.DEFAULT_ENGINE_BOOT)
        },
        examples = {
                @Example(
                        description = "This example shows how to make set request using snmp version v1 " +
                                "It uses keyvalue mapping and chooses transport protocol as UDP. After sending the " +
                                "message it can configure target agent in localhost sysLocation (related to oid)",

                        syntax = "@Sink(type='snmp',\n" +
                                "@map(type='keyvalue', @payload('1.3.6.1.2.1.1.6.0' = 'sysLocation')),\n" +
                                "host = '127.0.0.1',\n" +
                                "version = 'v1',\n" +
                                "community = 'public',\n" +
                                "agent.port = '161',\n" +
                                "retries = '5')\n" +
                                "define stream outputStream(sysLocation string);\n"
                ),
                @Example(
                        description = "This example shows how to make set request using snmp " +
                                "version v2c ",

                        syntax = "@Sink(type='snmp',\n" +
                                "@map(type='keyvalue', @payload('1.3.6.1.2.1.1.6.0' = 'sysLocation')),\n" +
                                "host = '127.0.0.1',\n" +
                                "version = 'v2c',\n" +
                                "community = 'public',\n" +
                                "agent.port = '161',\n" +
                                "retries = '5')\n" +
                                "define stream outputStream(sysLocation string);\n"
                ),
                @Example(
                        description = "This example shows how to make set request using snmp version v3. " +
                                "And here we can configure security protocols related to target agent as well.",

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
                                "define stream outputStream(sysLocation string, sysDscr string);\n"
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
    protected ServiceDeploymentInfo exposeServiceDeploymentInfo() {
        return null;
    }

    @Override
    public String[] getSupportedDynamicOptions() {
        return new String[0];
    }

    @Override
    protected StateFactory init(StreamDefinition streamDefinition, OptionHolder optionHolder, ConfigReader configReader,
                                SiddhiAppContext siddhiAppContext) {
        this.siddhiAppContext = siddhiAppContext;
        this.streamDefinition = streamDefinition;
        managerConfig = SNMPValidator.validateAndGetManagerConfig(optionHolder,
                this.streamDefinition.getId(), false);
        manager = new SNMPManager(managerConfig);
        return null;
    }


    @Override
    public void publish(Object payload, DynamicOptions dynamicOptions, State state) {
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
}

