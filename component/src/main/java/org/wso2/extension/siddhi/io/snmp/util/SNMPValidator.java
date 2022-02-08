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

import io.siddhi.core.util.transport.OptionHolder;
import io.siddhi.query.api.exception.SiddhiAppValidationException;
import org.snmp4j.security.AuthHMAC192SHA256;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.PrivAES192;
import org.snmp4j.security.PrivAES256;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * SNMP validations for sink and source
 */
public class SNMPValidator {

    private String streamName;

    private SNMPValidator(String streamName) {
        this.streamName = streamName;
    }


    private int validateVersion(String versionString) {
        versionString = versionString.toLowerCase(Locale.ENGLISH);
        switch (versionString) {
            case "v1":
                return SNMPConstants.V1;
            case "v2c":
                return SNMPConstants.V2C;
            case "v3":
                return SNMPConstants.V3;
            default:
                throw new SiddhiAppValidationException(streamName + " version validation failed. " +
                        "snmp accept v1, v2c, v3 only. ");
        }
    }

    private List<VariableBinding> validateAndGetOidList(String oidListString) {
        oidListString = oidListString.replace(" ", "");
        if (oidListString.equals("")) {
            throw new SiddhiAppValidationException(streamName + " oid list empty!");
        }
        List<VariableBinding> list = new LinkedList<>();
        String[] oids = oidListString.split(",");
        for (String oid : oids) {
            if (!Pattern.matches("^\\d+(\\.\\d+)*$", oid)) {
                throw new SiddhiAppValidationException(streamName + " OID pattern error. " +
                        "ex :- '1.3.6.1.2.1.1.1.0, 1.3.6.1.2.1.1.2.0'");
            }
            list.add(new VariableBinding(new OID(oid)));
        }
        return list;
    }

    private OID validateAndGetPriv(String priv) {
        priv = priv.toUpperCase(Locale.ENGLISH);
        switch (priv) {
            case "PRIVDES":
                return PrivDES.ID;
            case "PRIVDES128":
                return PrivAES128.ID;
            case "PRIVAES192":
                return PrivAES192.ID;
            case "PRIVAES256":
                return PrivAES256.ID;
            case "PRIV3DES":
                return Priv3DES.ID;
            case "NO_PRIV":
                return null;
            default:
                throw new SiddhiAppValidationException(streamName + " PRIV protocol validation failed! " +
                        "only acceptable for NO_PRIV(default), PRIVDES, PRIVDES128, PRIVAES192, PRIVAES256, PRIV3DES");

        }

    }

    private OID validateAndGetAuth(String auth) {
        auth = auth.toUpperCase(Locale.ENGLISH);
        switch (auth) {
            case "AUTHMD5":
                return AuthMD5.ID;
            case "AUTHSHA":
                return AuthSHA.ID;
            case "AUTHHMAC192SHA256":
                return AuthHMAC192SHA256.ID;
            case "AUTHHMAC192SHA512":
                return AuthHMAC192SHA256.ID;
            case "NO_AUTH":
                return null;
            default:
                throw new SiddhiAppValidationException(streamName + " AUTH not acceptable! " +
                        "Only acceptable for NO_AUTH(default), AUTHMD5, AUTHSHA, AUTHHMAC192SHA256, AUTHHMAC192SHA512");
        }
    }

    private int validateSecLvl(String seclvl) {
        seclvl = seclvl.toUpperCase(Locale.ENGLISH);
        switch (seclvl) {
            case "NOAUTH_NOPRIV":
                return SecurityLevel.NOAUTH_NOPRIV;
            case "AUTH_NOPRIV":
                return SecurityLevel.AUTH_NOPRIV;
            case "AUTH_PRIV":
                return SecurityLevel.AUTH_PRIV;
            default:
                throw new SiddhiAppValidationException(streamName + " Security level not" +
                        " acceptable. only acceptable NOAUTH_NOPRIV, AUTH_NOPRIV, AUTH_PRIV");
        }
    }

    private OctetString validateEngineId(String engineId) {
        if (engineId.equals("Empty")) {
            return null;
        }
        return new OctetString(engineId);
    }

    private boolean validateTransportProtocol(String transportProtocol) {
        transportProtocol = transportProtocol.toUpperCase(Locale.ENGLISH);
        switch (transportProtocol) {
            case "TCP" : return true;
            case "UDP" : return false;
            default: throw new SiddhiAppValidationException(streamName + " transportProtocol not acceptable! " +
                    "only acceptable for TCP, UDP");
        }
    }

    private String validatePort(int port) {
        if (port >= 0 && port <=  65535) {
            return String.valueOf(port);
        }
        throw new SiddhiAppValidationException("port validation error in " + streamName);
    }

    //for validation
    public static SNMPManagerConfig validateAndGetManagerConfig(OptionHolder optionHolder,
                                                String streamName,
                                                boolean includeOids) {
        SNMPValidator validator = new SNMPValidator(streamName);
        SNMPManagerConfig managerConfig = new SNMPManagerConfig();

        String host = optionHolder.validateAndGetStaticValue(SNMPConstants.HOST);
        String port = validator.validatePort(Integer.parseInt(optionHolder
                .validateAndGetStaticValue(SNMPConstants.AGENT_PORT)));
        int timeout = Integer.parseInt(optionHolder.validateAndGetStaticValue(SNMPConstants.TIMEOUT,
                SNMPConstants.DEFAULT_TIMEOUT));
        int retries = Integer.parseInt(optionHolder.validateAndGetStaticValue(SNMPConstants.RETRIES,
                SNMPConstants.DEFAULT_RETRIES));

        boolean isTcp = validator.validateTransportProtocol(optionHolder.validateAndGetStaticValue(
                SNMPConstants.TRANSPORT_PROTOCOL, SNMPConstants.DEFAULT_TRANSPORT_PROTOCOL));
        managerConfig.isTcp(isTcp);
        managerConfig.setVersion(validator.validateVersion(optionHolder
                        .validateAndGetStaticValue(SNMPConstants.VERSION)));
        if (includeOids) {
            managerConfig.setVariableBindings(validator.validateAndGetOidList(
                    optionHolder.validateAndGetStaticValue(SNMPConstants.OIDS)));
        }
        if (managerConfig.getVersion() == SNMPConstants.V3) {
            String userName = optionHolder.validateAndGetStaticValue(SNMPConstants.USER_NAME,
                    SNMPConstants.DEFAULT_USERNAME);
            String authpass = optionHolder.validateAndGetStaticValue(SNMPConstants.AUTH_PASSWORD,
                    SNMPConstants.DEFAULT_AUT_PASSWORD);
            String privpass = optionHolder.validateAndGetStaticValue(SNMPConstants.PRIV_PASSWORD,
                    SNMPConstants.DEFAULT_PRIV_PASSWORD);
            OID priv = validator.validateAndGetPriv(optionHolder.validateAndGetStaticValue(SNMPConstants.PRIV_PROTOCOL,
                    SNMPConstants.DEFAULT_PRIV_PROTOCOL));
            OID auth = validator.validateAndGetAuth(optionHolder.validateAndGetStaticValue(SNMPConstants.AUTH_PROTOCOL,
                    SNMPConstants.DEFAULT_AUTH_PROTOCOL));
            int secLvl = validator.validateSecLvl(optionHolder.validateAndGetStaticValue(SNMPConstants.SECURITY_LVL,
                    SNMPConstants.DEFAULT_SECURITY_LVL));
            managerConfig.setUserMatrix(new OctetString(userName), auth, new OctetString(authpass),
                    priv, new OctetString(privpass), secLvl);

            managerConfig.setUserTarget(host, port, retries, timeout, managerConfig.getSecLvl());
            managerConfig.setLocalEngineID(validator.validateEngineId(optionHolder.validateAndGetStaticValue(
                    SNMPConstants.LOCAL_ENGINE_ID, SNMPConstants.DEFAULT_LOCAL_ENGINE_ID)));
            managerConfig.setEngineBoot(Integer.parseInt(optionHolder.validateAndGetStaticValue(
                    SNMPConstants.ENGINE_BOOT,
                    SNMPConstants.DEFAULT_ENGINE_BOOT)));
        } else {
            String community = optionHolder.validateAndGetStaticValue(SNMPConstants.COMMUNITY,
                    SNMPConstants.DEFAULT_COMMUNITY);
            managerConfig.setCommunityTarget(host, port, community, retries, timeout);
        }

        return managerConfig;
    }
}
