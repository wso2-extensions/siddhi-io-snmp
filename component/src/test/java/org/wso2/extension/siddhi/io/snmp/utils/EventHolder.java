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

import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.Map;

/**
 * This class is use to hold last limited number of snmp events
 * by list of key-value map
 *
 * */
public class EventHolder {
    private Logger log = Logger.getLogger(EventHolder.class);
    private volatile LinkedList<Map<String, String>> eventList;
    private int listSize = 10;

    public EventHolder() {
        eventList = new LinkedList<>();
    }

    public EventHolder(int size) {
        eventList = new LinkedList<>();
        this.listSize = size;
    }

    public synchronized void addEvent(Map<String, String> map) {
        eventList.addFirst(map);
        if (eventList.size() > listSize) {
            eventList.removeLast();
        }
    }

    public synchronized Map<String, String> getEvent(int index) {
        if (index > listSize) {
            return eventList.get(listSize);
        } else if (index < 0) {
            return eventList.get(0);
        }
        return eventList.get(index);
    }

    public String eventToString(int index) {
        return getEvent(index).toString();
    }

    public synchronized boolean assertDataContent(String value, int index) {
        try {
            log.debug(eventList.get(index).toString());
            if (eventList.get(index).containsValue(value)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.debug("Outofbound exception : " + e);
            return false;
        }
    }


    public synchronized void clear() {
        eventList = null;
        eventList = new LinkedList<>();
    }
}
