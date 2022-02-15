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

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

import java.util.List;

/**
 * This class is used to keep data for manager
 */
public class SNMPManagerConfig {


    // version 2 & 1 properties
    private boolean isTCP = false;
    private PDU pdu;
    private CommunityTarget communityTarget;
    private int version;

    // version 3 properties
    private ScopedPDU scopedPDU;
    private UserTarget userTarget;
    private OctetString userName;
    private OID authProtocol;
    private OctetString authProtocolPass;
    private OID privProtocol;
    private OctetString privProtocolPass;
    private int secLvl;
    private OctetString localEngineID;
    private int engineBoot;

    public void isTcp(boolean b) {
        isTCP = b;
    }

    public OctetString getUserName() {
        return userName;
    }

    public void setVariableBindings(List<VariableBinding> vbs) {
        if (vbs == null) {
            pdu = new PDU();
            return;
        }
        if (version == SnmpConstants.version3) {
            scopedPDU = new ScopedPDU();
            scopedPDU.addAll(vbs);
        } else {
            pdu = new PDU();
            pdu.addAll(vbs);
        }
    }

    public void setLocalEngineID(OctetString localEngineID) {
        this.localEngineID = localEngineID;
    }

    public void setEngineBoot(int engineBoot) {
        this.engineBoot = engineBoot;
    }

    // for setting user parameters
    public void setUserMatrix(OctetString userName,
                              OID authProtocol, OctetString authProtocolPass,
                              OID privProtocol, OctetString privProtocolPass,
                              int secLvl) {
        this.userName = userName;
        this.authProtocol = authProtocol;
        this.authProtocolPass = authProtocolPass;
        this.privProtocol = privProtocol;
        this.privProtocolPass = privProtocolPass;
        this.secLvl = secLvl;
    }

    // for setting up community target
    public void setCommunityTarget(String ip, String port, String community, int retries, int timeout) {
        communityTarget = new CommunityTarget();
        communityTarget.setCommunity(new OctetString(community));
        Address address;
        if (isTCP) {
            address = GenericAddress.parse("tcp:" + ip + "/" + port);
        } else {
            address = GenericAddress.parse("udp:" + ip + "/" + port);
        }
        communityTarget.setAddress(address);
        communityTarget.setRetries(retries);
        communityTarget.setTimeout(timeout);
        if (this.version == SNMPConstants.V2C) {
            communityTarget.setVersion(SNMPConstants.V2C);
        } else {
            communityTarget.setVersion(SNMPConstants.V1);
        }
    }

    // for setting up user target
    public void setUserTarget(String ip, String port, int retries, int timeout, int securityLvl) {
        userTarget = new UserTarget();
        userTarget.setSecurityLevel(securityLvl);
        userTarget.setVersion(SnmpConstants.version3);
        userTarget.setTimeout(timeout);
        userTarget.setRetries(retries);
        Address address;
        if (isTCP) {
            address = GenericAddress.parse("tcp:" + ip + "/" + port);
        } else {
            address = GenericAddress.parse("udp:" + ip + "/" + port);
        }
        userTarget.setAddress(address);
        userTarget.setSecurityName(this.userName);
    }

    public int getSecLvl() {
        return secLvl;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isTCP() {
        return isTCP;
    }

    public OctetString getLocalEngineID() {
        return this.localEngineID;
    }

    public int getEngineBoot() {
        return engineBoot;
    }

    public UsmUser getUser() {
        return new UsmUser(this.userName, this.authProtocol, this.authProtocolPass,
                this.privProtocol, this.privProtocolPass);
    }

    public CommunityTarget getCommunityTarget() {
        return this.communityTarget;
    }

    public UserTarget getUserTarget() {
        return this.userTarget;
    }

    public PDU getPdu() {
        if (version == SnmpConstants.version3) {
            if (scopedPDU == null) {
                scopedPDU = new ScopedPDU();
            }
            return scopedPDU;
        }
        if (pdu == null) {
            pdu = new PDU();
        }
        return pdu;
    }

    public void clear() {
        if (version == SNMPConstants.V3 && scopedPDU != null) {
            this.scopedPDU.clear();
        } else {
            if (pdu != null) {
                this.pdu.clear();
            }
        }
    }
}
