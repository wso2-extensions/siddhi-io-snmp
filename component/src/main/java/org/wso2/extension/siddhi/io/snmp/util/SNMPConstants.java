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
package org.wso2.extension.siddhi.io.snmp.util;

import org.snmp4j.mp.SnmpConstants;

/**
 * SNMP-SP Connector constants
 */
public class SNMPConstants {

    // static
    public static final String HOST = "host";
    public static final String VERSION = "version";
    public static final String REQUEST_INTERVAL = "request.interval";
    public static final String COMMUNITY = "community";
    public static final String OIDS = "oids";

    // optional
    public static final String AGENT_PORT = "agent.port";
    public static final String TRANSPORT_PROTOCOL = "transport.protocol";
    public static final String RETRIES = "retries";
    public static final String TIMEOUT = "timeout";

    // v3 // optional
    public static final String USER_NAME = "user.name";
    public static final String SECURITY_LVL = "security.lvl";
    public static final String PRIV_PROTOCOL = "priv.protocol";
    public static final String PRIV_PASSWORD = "priv.password";
    public static final String AUTH_PROTOCOL = "auth.protocol";
    public static final String AUTH_PASSWORD = "auth.password";
    public static final String LOCAL_ENGINE_ID = "engine.id";
    public static final String ENGINE_BOOT = "engine.boot";

    public static final int V1  = SnmpConstants.version1;
    public static final int V2C = SnmpConstants.version2c;
    public static final int V3  = SnmpConstants.version3;

    // default values
    public static final String DEFAULT_AGENT_PORT = "161";
    public static final String DEFAULT_RETRIES = "5";
    public static final String DEFAULT_TIMEOUT = "1500";

    public static final String DEFAULT_USERNAME = "noUser";
    public static final String DEFAULT_SECURITY_LVL = "AUTH_PRIVE";
    public static final String DEFAULT_AUTH_PROTOCOL = "NO_AUTH";
    public static final String DEFAULT_PRIV_PASSWORD = "privpass";
    public static final String DEFAULT_PRIV_PROTOCOL = "NO_PRIV";
    public static final String DEFAULT_AUT_PASSWORD = "authpass";
    public static final String DEFAULT_COMMUNITY = "public";
    public static final String DEFAULT_LOCAL_ENGINE_ID = "Empty";
    public static final String DEFAULT_ENGINE_BOOT = "0";
    public static final String DEFAULT_REQUEST_INTERVAL = "5000";
    public static final String DEFAULT_TRANSPORT_PROTOCOL = "udp";

    private SNMPConstants() {

    }
}
