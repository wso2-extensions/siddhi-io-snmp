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
import org.wso2.extension.siddhi.io.snmp.util.SNMPManager;
import org.wso2.extension.siddhi.io.snmp.util.exceptions.AgentNotFoundException;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * SNMP Listener thread for make request
 **/
public class SNMPListener implements Runnable {

    private boolean paused = false;
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Logger log = Logger.getLogger(SNMPListener.class);
    private SNMPManager manager;
    private SourceEventListener sourceEventListener;

    public SNMPListener(SNMPManager manager, SourceEventListener sourceEventListener) {
        this.manager = manager;
        this.sourceEventListener = sourceEventListener;
    }

    @Override
    public void run() {
        try {
            Map<String, String> map = manager.getRequestValidateAndReturn();
            if (paused) {
                lock.lock();
                try {
                    while (paused) {
                        condition.await();
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }

            sourceEventListener.onEvent(map, null);
        } catch (AgentNotFoundException ex) {
            log.info("Target error in stream : " + sourceEventListener.getStreamDefinition().getId());
        } catch (IOException e) {
            log.info("Thread was interrupted due to IO in stream " + sourceEventListener.getStreamDefinition().getId());
        }
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
        try {
            lock.lock();
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
