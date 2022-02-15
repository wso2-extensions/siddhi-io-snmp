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

package org.wso2.extension.siddhi.io.snmp.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageException;
import org.snmp4j.PDU;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.event.CounterEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.mp.StateReference;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class helps to keep changes other than process request
 * extend of CommandProcessor
 * additional codes for eventHolder
 *
 * */
public class AdvancedCommandProcessor extends CommandProcessor {

    private static final Logger log = LogManager.getLogger(AdvancedCommandProcessor.class);
    private EventHolder eventHolder;


    public AdvancedCommandProcessor() {
        super(new OctetString(MPv3.createLocalEngineID()));
        //setEventListener(new EventHolder());
    }

    public EventHolder getEventListener() {
        return eventHolder;
    }

    public void setEventListener(EventHolder eventHolder) {
        this.eventHolder = eventHolder;
    }


    // from CommandProcessor
    protected void sendResponse(CommandResponderEvent requestEvent, PDU response) {
        MessageDispatcher disp = requestEvent.getMessageDispatcher();

        // for notify event holder to keep changes!
        if (eventHolder != null) {
            List<VariableBinding> vbs = (List<VariableBinding>) response.getVariableBindings();
            Map<String, String> map = new HashMap<>();
            for (VariableBinding vb : vbs) {
                map.put(vb.getOid().toString(), vb.getVariable().toString());
            }
            eventHolder.addEvent(map);
            log.debug("vb -> " + response.getVariableBindings().toString());
        }
        // end event holder

        try {
            if (response.getBERLength() > requestEvent.getMaxSizeResponsePDU()) {
                if (response.getType() != -88) {
                    if (requestEvent.getPDU().getType() == -91) {
                        while (response.size() > 0 && response.getBERLength() > requestEvent.getMaxSizeResponsePDU()) {
                            response.trim();
                        }
                    } else {
                        response.clear();
                        response.setRequestID(requestEvent.getPDU().getRequestID());
                        response.setErrorStatus(1);
                    }
                }

                if (response.getBERLength() > requestEvent.getMaxSizeResponsePDU()) {
                    this.fireIncrementCounter(new CounterEvent(this, SnmpConstants.snmpSilentDrops));
                    return;
                }
            }

            StatusInformation status = new StatusInformation();
            StateReference stateRef = requestEvent.getStateReference();
            if (stateRef == null) {
                log.warn("No state reference available for requestEvent=" +
                        requestEvent + ". Cannot return response=" + response);
            } else {
                stateRef.setTransportMapping(requestEvent.getTransportMapping());
                disp.returnResponsePdu(requestEvent.getMessageProcessingModel(),
                        requestEvent.getSecurityModel(),
                        requestEvent.getSecurityName(),
                        requestEvent.getSecurityLevel(),
                        response,
                        requestEvent.getMaxSizeResponsePDU(),
                        requestEvent.getStateReference(),
                        status);
            }
        } catch (MessageException var6) {
            log.error("Failed to send response to request " + requestEvent, var6);
        }

    }
}
