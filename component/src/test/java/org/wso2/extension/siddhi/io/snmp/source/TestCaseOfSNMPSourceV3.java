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

import io.siddhi.core.SiddhiAppRuntime;
import io.siddhi.core.SiddhiManager;
import io.siddhi.core.event.Event;
import io.siddhi.core.exception.SiddhiAppCreationException;
import io.siddhi.core.stream.output.StreamCallback;
import io.siddhi.core.util.EventPrinter;
import io.siddhi.core.util.SiddhiTestHelper;
import io.siddhi.query.api.exception.SiddhiAppValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snmp4j.mp.MPv3;
import org.snmp4j.smi.OctetString;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.extension.siddhi.io.snmp.utils.AdvancedCommandProcessor;
import org.wso2.extension.siddhi.io.snmp.utils.Agent;
import org.wso2.extension.siddhi.io.snmp.utils.EventHolder;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Test siddhi-io-snmp source for snmp v3
 */
public class TestCaseOfSNMPSourceV3 {

    private static final Logger log = LogManager.getLogger(TestCaseOfSNMPSourceV3.class);
    private AtomicInteger eventCount = new AtomicInteger(0);
    private AtomicBoolean eventArrived = new AtomicBoolean(false);
    private String port = "2022";
    private String ip = "127.0.0.1";
    private Agent agent;
    private EventHolder eventHolder;
    private AdvancedCommandProcessor advancedCommandProcessor;
    private int sleepTime = 3000;
    private int timeout = 3000;

    @BeforeClass
    public void startAgent() throws IOException, InterruptedException {
        log.info("agent starting.. ");
        eventHolder = new EventHolder(1);
        advancedCommandProcessor = new AdvancedCommandProcessor();
        advancedCommandProcessor.setEventListener(eventHolder);
        agent = new Agent(advancedCommandProcessor);
        agent.start(ip, port);
    }

    @AfterClass
    public void stopAgent() {

        agent.stop();
        log.info("agent stopped ");
    }

    @BeforeMethod
    public void init() {

        eventCount.set(0);
        eventArrived.set(false);
    }

    @Test
    public void snmpVersion3TestBasic() throws InterruptedException {

        log.info("-------------------------------------------------------------------------------------");
        log.info("                    SNMP Version 3 Basic Source Test Case                            ");
        log.info("-------------------------------------------------------------------------------------");

        SiddhiManager siddhiManager = new SiddhiManager();

        String siddhiApp = "@App:name('test') \n" +
                "@source(type='snmp', \n" +
                "@map(type='keyvalue', " +
                "   @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'value2' = '1.3.6.1.2.1.1.1.0') ),\n" +
                "host ='" + ip + "',\n" +
                "version = 'v3',\n" +
                "timeout = '1000',\n" +
                "request.interval = '500',\n" +
                "agent.port = '" + port + "',\n" +
                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',\n" +
                "auth.protocol = 'AUTHMD5',\n" +
                "priv.protocol = 'PRIVDES',\n" +
                "priv.password = 'privpass',\n" +
                "auth.password = 'authpass',\n" +
                "security.lvl = 'AUTH_PRIV',\n" +
                "user.name = 'agent5') \n" +
                " define stream inputStream(value1 string, value2 string);\n";

        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        executionPlanRuntime.addCallback("inputStream", new StreamCallback() {
            @Override
            public void receive(Event[] events) {

                EventPrinter.print(events);
                for (Event event : events) {
                    eventCount.getAndIncrement();
                    eventArrived.set(true);
                }
            }
        });

        executionPlanRuntime.start();
        SiddhiTestHelper.waitForEvents(sleepTime, 5, eventCount, timeout);
        Assert.assertTrue(eventArrived.get());
        siddhiManager.shutdown();
    }

    @Test
    public void snmpVersion3SecLevel() throws InterruptedException {

        log.info("-------------------------------------------------------------------------------------");
        log.info("                SNMP Version 3 Source Test Case Sec Lvl AUTH_NOPRIV                  ");
        log.info("-------------------------------------------------------------------------------------");

        SiddhiManager siddhiManager = new SiddhiManager();

        String siddhiApp = "@App:name('test') \n" +
                "@source(type='snmp', \n" +
                "@map(type='keyvalue', " +
                "   @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'value2' = '1.3.6.1.2.1.1.1.0') ),\n" +
                "host ='" + ip + "',\n" +
                "version = 'v3',\n" +
                "timeout = '100',\n" +
                "request.interval = '1000',\n" +
                "agent.port = '" + port + "',\n" +
                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',\n" +
                "auth.protocol = 'AUTHSHA',\n" +
                "auth.password = 'SHAAuthPassword',\n" +
                "security.lvl = 'AUTH_NOPRIV',\n" +
                "user.name = 'SHA') \n" +
                " define stream inputStream(value1 string, value2 string);\n";

        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        executionPlanRuntime.addCallback("inputStream", new StreamCallback() {
            @Override
            public void receive(Event[] events) {

                EventPrinter.print(events);
                for (Event event : events) {
                    eventCount.getAndIncrement();
                    eventArrived.set(true);
                }
            }
        });

        executionPlanRuntime.start();
        SiddhiTestHelper.waitForEvents(sleepTime, 5, eventCount, timeout);
        Assert.assertTrue(eventArrived.get());
        siddhiManager.shutdown();
    }

    @Test
    public void snmpVersionVersion3UserTest() throws InterruptedException {

        log.info("-------------------------------------------------------------------------------------");
        log.info("                SNMP Version 3 Source Test Case Sec Lvl 3 Agent5                     ");
        log.info("-------------------------------------------------------------------------------------");

        SiddhiManager siddhiManager = new SiddhiManager();
        String engineId = new OctetString(MPv3.createLocalEngineID()).substring(0, 9).toString();

        String siddhiApp = "@App:name('test') \n" +
                "@source(type='snmp', \n" +
                "@map(type='keyvalue', " +
                "   @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'value2' = '1.3.6.1.2.1.1.1.0') ),\n" +
                "host ='" + ip + "',\n" +
                "version = 'v3',\n" +
                "timeout = '100',\n" +
                "request.interval = '1000',\n" +
                "agent.port = '" + port + "',\n" +
                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',\n" +
                "auth.protocol = 'AuthHMAC192SHA256',\n" +
                "priv.protocol = 'PrivAES192',\n" +
                "priv.password = 'MD5AES192AuthPassword',\n" +
                "auth.password = 'MD5AES192PrivPassword',\n" +
                "security.lvl = 'AUTH_PRIV',\n" +
                "engine.id = '" + engineId + "',\n" +
                "engine.boot = '0',\n" +
                "user.name = 'MD5AES192') \n" +
                " define stream inputStream(value1 string, value2 string);\n";

        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        executionPlanRuntime.addCallback("inputStream", new StreamCallback() {
            @Override
            public void receive(Event[] events) {

                EventPrinter.print(events);
                for (Event event : events) {
                    eventCount.getAndIncrement();
                    eventArrived.set(true);
                }
            }
        });

        executionPlanRuntime.start();
        SiddhiTestHelper.waitForEvents(sleepTime, 5, eventCount, timeout);
        siddhiManager.shutdown();
    }

    @Test
    public void snmpVersion3EngineID() throws InterruptedException {

        log.info("-------------------------------------------------------------------------------------");
        log.info("                        SNMP Version 3 Source Engine Id Test                         ");
        log.info("-------------------------------------------------------------------------------------");

        SiddhiManager siddhiManager = new SiddhiManager();
        String engineId = new OctetString(MPv3.createLocalEngineID()).substring(0, 9).toString();

        String siddhiApp = "@App:name('test') \n" +
                "@source(type='snmp', \n" +
                "@map(type='keyvalue', " +
                "   @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'value2' = '1.3.6.1.2.1.1.1.0') ),\n" +
                "host ='" + ip + "',\n" +
                "version = 'v3',\n" +
                "timeout = '100',\n" +
                "request.interval = '1000',\n" +
                "agent.port = '" + port + "',\n" +
                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',\n" +
                "auth.protocol = 'AUTHSHA',\n" +
                "priv.protocol = 'PRIVDES',\n" +
                "priv.password = 'privpass',\n" +
                "auth.password = 'authpass',\n" +
                "security.lvl = 'AUTH_PRIV',\n" +
                "engine.id = '" + engineId + "',\n" +
                "user.name = 'agent5') \n" +
                " define stream inputStream(value1 string, value2 string);\n";

        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        executionPlanRuntime.addCallback("inputStream", new StreamCallback() {
            @Override
            public void receive(Event[] events) {

                EventPrinter.print(events);
                for (Event event : events) {
                    eventCount.getAndIncrement();
                    eventArrived.set(true);
                }
            }
        });

        executionPlanRuntime.start();
        SiddhiTestHelper.waitForEvents(sleepTime, 5, eventCount, timeout);
        siddhiManager.shutdown();
    }

    @Test
    public void snmpVersion3EngineBoot() throws InterruptedException {

        log.info("-------------------------------------------------------------------------------------");
        log.info("                         SNMP Version 3 Engine Boot Test                             ");
        log.info("-------------------------------------------------------------------------------------");

        SiddhiManager siddhiManager = new SiddhiManager();
        String engineId = new OctetString(MPv3.createLocalEngineID()).substring(0, 9).toString();

        String siddhiApp = "@App:name('test') \n" +
                "@source(type='snmp', \n" +
                "@map(type='keyvalue', " +
                "   @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'value2' = '1.3.6.1.2.1.1.1.0') ),\n" +
                "host ='" + ip + "',\n" +
                "version = 'v3',\n" +
                "timeout = '100',\n" +
                "request.interval = '1000',\n" +
                "agent.port = '" + port + "',\n" +
                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',\n" +
                "auth.protocol = 'AUTHSHA',\n" +
                "priv.protocol = 'PRIVDES',\n" +
                "priv.password = 'privpass',\n" +
                "auth.password = 'authpass',\n" +
                "security.lvl = 'AUTH_PRIV',\n" +
                "engine.id = '" + engineId + "',\n" +
                "engine.id = '0',\n" +
                "user.name = 'agent5') \n" +
                " define stream inputStream(value1 string, value2 string);\n";

        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        executionPlanRuntime.addCallback("inputStream", new StreamCallback() {
            @Override
            public void receive(Event[] events) {

                EventPrinter.print(events);
                for (Event event : events) {
                    eventCount.getAndIncrement();
                    eventArrived.set(true);
                }
            }
        });

        executionPlanRuntime.start();
        SiddhiTestHelper.waitForEvents(sleepTime, 5, eventCount, timeout);
        siddhiManager.shutdown();
    }

    @Test
    public void snmpVersion3AuthPriv() throws InterruptedException {

        log.info("-------------------------------------------------------------------------------------");
        log.info("                        SNMP Version 3 Security Protocol Test                        ");
        log.info("-------------------------------------------------------------------------------------");

        SiddhiManager siddhiManager = new SiddhiManager();

        String siddhiApp = "@App:name('test') \n" +
                "@source(type='snmp', \n" +
                "@map(type='keyvalue', " +
                "   @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'value2' = '1.3.6.1.2.1.1.1.0') ),\n" +
                "host ='" + ip + "',\n" +
                "version = 'v3',\n" +
                "timeout = '100',\n" +
                "request.interval = '1000',\n" +
                "agent.port = '" + port + "',\n" +
                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',\n" +
                "auth.protocol = 'AUTHSHA',\n" +
                "priv.protocol = 'PRIVDES',\n" +
                "priv.password = 'privpass',\n" +
                "auth.password = 'authpass',\n" +
                "security.lvl = 'AUTH_PRIV',\n" +
                "user.name = 'agent5') \n" +
                " define stream inputStream(value1 string, value2 string);\n";

        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        executionPlanRuntime.addCallback("inputStream", new StreamCallback() {
            @Override
            public void receive(Event[] events) {

                EventPrinter.print(events);
                for (Event event : events) {
                    eventCount.getAndIncrement();
                    eventArrived.set(true);
                }
            }
        });

        executionPlanRuntime.start();
        SiddhiTestHelper.waitForEvents(sleepTime, 5, eventCount, timeout);
        Assert.assertTrue(eventArrived.get());
        siddhiManager.shutdown();
    }

    //Commenting test case to fix later
//    @Test
//    public void snmpVersion3AuthPriv2() throws InterruptedException {
//
//        log.info("-------------------------------------------------------------------------------------");
//        log.info("                        SNMP Version 3 Security Protocol Test                        ");
//        log.info("-------------------------------------------------------------------------------------");
//
//        SiddhiManager siddhiManager = new SiddhiManager();
//
//        String siddhiApp = "@App:name('test') \n" +
//                "@source(type='snmp', \n" +
//                "@map(type='keyvalue', " +
//                "   @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'value2' = '1.3.6.1.2.1.1.1.0') ),\n" +
//                "host ='" + ip + "',\n" +
//                "version = 'v3',\n" +
//                "timeout = '100',\n" +
//                "request.interval = '1000',\n" +
//                "agent.port = '" + port + "',\n" +
//                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',\n" +
//                "auth.protocol = 'AUTHMD5',\n" +
//                "priv.protocol = 'PRIVAES256',\n" +
//                "priv.password = 'MD5AES256PrivPassword',\n" +
//                "auth.password = 'MD5AES256AuthPassword',\n" +
//                "security.lvl = 'AUTH_PRIV',\n" +
//                "user.name = 'MD5AES256') \n" +
//                " define stream inputStream(value1 string, value2 string);\n";
//
//        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
//        executionPlanRuntime.addCallback("inputStream", new StreamCallback() {
//            @Override
//            public void receive(Event[] events) {
//
//                EventPrinter.print(events);
//                for (Event event : events) {
//                    eventCount.getAndIncrement();
//                    eventArrived.set(true);
//                }
//            }
//        });
//
//        executionPlanRuntime.start();
//        SiddhiTestHelper.waitForEvents(sleepTime, 5, eventCount, timeout);
//        Assert.assertTrue(eventArrived.get());
//        siddhiManager.shutdown();
//    }

    // giving invalid `security.lvl`
    @Test(expectedExceptions = SiddhiAppValidationException.class)
    public void snmpValidationTest1() {

        log.info("-------------------------------------------------------------------------------------");
        log.info("                 SNMP Version 3 Siddhi app validation for `security.lvl`             ");
        log.info("-------------------------------------------------------------------------------------");

        SiddhiManager siddhiManager = new SiddhiManager();

        String siddhiApp = "@App:name('test') \n" +
                "@source(type='snmp', \n" +
                "@map(type='keyvalue', " +
                "   @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'value2' = '1.3.6.1.2.1.1.1.0') ),\n" +
                "host ='" + ip + "',\n" +
                "version = 'v3',\n" +
                "timeout = '100',\n" +
                "request.interval = '1000',\n" +
                "agent.port = '" + port + "',\n" +
                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',\n" +
                "auth.protocol = 'AUTHSHA',\n" +
                "priv.protocol = 'PRIVDES',\n" +
                "priv.password = 'privpass',\n" +
                "auth.password = 'authpass',\n" +
                "security.lvl = 'AUTH_PRI',\n" +
                "user.name = 'agent5') \n" +
                " define stream inputStream(value1 string, value2 string);\n";

        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        executionPlanRuntime.start();
        siddhiManager.shutdown();
    }

    // for invalid `priv.protocol`
    @Test(expectedExceptions = SiddhiAppValidationException.class)
    public void snmpValidationTest2() {

        log.info("-------------------------------------------------------------------------------------");
        log.info("                SNMP Version 3 Siddhi app validation for `priv.protocol`             ");
        log.info("-------------------------------------------------------------------------------------");

        SiddhiManager siddhiManager = new SiddhiManager();

        String siddhiApp = "@App:name('test') \n" +
                "@source(type='snmp', \n" +
                "@map(type='keyvalue', " +
                "   @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'value2' = '1.3.6.1.2.1.1.1.0') ),\n" +
                "host ='" + ip + "',\n" +
                "version = 'v3',\n" +
                "timeout = '100',\n" +
                "request.interval = '500',\n" +
                "agent.port = '" + 200 + "',\n" +
                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',\n" +
                "auth.protocol = 'AUTHSHA',\n" +
                "priv.protocol = 'PRIVDE',\n" +
                "priv.password = 'privpass',\n" +
                "auth.password = 'authpass',\n" +
                "security.lvl = 'AUTH_PRIV',\n" +
                "user.name = 'agent5') \n" +
                " define stream inputStream(value1 string, value2 string);\n";

        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        executionPlanRuntime.start();
        siddhiManager.shutdown();
    }

    // for invalid `auth.protocol`
    @Test(expectedExceptions = SiddhiAppValidationException.class)
    public void snmpValidationTest3() {

        log.info("-------------------------------------------------------------------------------------");
        log.info("                SNMP Version 3 Siddhi app validation for `auth.protocol`             ");
        log.info("-------------------------------------------------------------------------------------");

        SiddhiManager siddhiManager = new SiddhiManager();

        String siddhiApp = "@App:name('test') \n" +
                "@source(type='snmp', \n" +
                "@map(type='keyvalue', " +
                "   @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'value2' = '1.3.6.1.2.1.1.1.0') ),\n" +
                "host ='" + ip + "',\n" +
                "version = 'v3',\n" +
                "timeout = '100',\n" +
                "request.interval = '500',\n" +
                "agent.port = '" + 200 + "',\n" +
                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',\n" +
                "auth.protocol = 'AUTHSHAE',\n" +
                "priv.protocol = 'PRIVDES',\n" +
                "priv.password = 'privpass',\n" +
                "auth.password = 'authpass',\n" +
                "security.lvl = 'AUTH_PRIV',\n" +
                "user.name = 'agent5') \n" +
                " define stream inputStream(value1 string, value2 string);\n";

        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        executionPlanRuntime.start();
        log.info("Siddhi manager shutting down");
        siddhiManager.shutdown();
    }

    // for invalid `transport.protocol`
    @Test(expectedExceptions = SiddhiAppValidationException.class)
    public void snmpValidationTest4() {
        log.info("-------------------------------------------------------------------------------------");
        log.info("            SNMP Version 3 Siddhi app validation for `transport.protocol`            ");
        log.info("-------------------------------------------------------------------------------------");

        SiddhiManager siddhiManager = new SiddhiManager();

        String siddhiApp = "@App:name('test') \n" +
                "@source(type='snmp', \n" +
                "@map(type='keyvalue', " +
                "   @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'value2' = '1.3.6.1.2.1.1.1.0') ),\n" +
                "host ='" + ip + "',\n" +
                "version = 'v3',\n" +
                "transport.protocol = ';-)',\n" +
                "timeout = '100',\n" +
                "request.interval = '500',\n" +
                "agent.port = '" + 200 + "',\n" +
                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',\n" +
                "auth.protocol = 'AUTHSHAE',\n" +
                "priv.protocol = 'PRIVDES',\n" +
                "priv.password = 'privpass',\n" +
                "auth.password = 'authpass',\n" +
                "security.lvl = 'AUTH_PRIV',\n" +
                "user.name = 'agent5') \n" +
                " define stream inputStream(value1 string, value2 string);\n";

        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        executionPlanRuntime.start();
        siddhiManager.shutdown();
    }

    // for invalid `version`
    @Test(expectedExceptions = SiddhiAppValidationException.class)
    public void snmpValidationTest5() {

        log.info("-------------------------------------------------------------------------------------");
        log.info("                SNMP Version 3 Siddhi app validation for `priv.protocol`             ");
        log.info("-------------------------------------------------------------------------------------");

        SiddhiManager siddhiManager = new SiddhiManager();

        String siddhiApp = "@App:name('test') \n" +
                "@source(type='snmp', \n" +
                "@map(type='keyvalue', " +
                "   @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'value2' = '1.3.6.1.2.1.1.1.0') ),\n" +
                "host ='" + ip + "',\n" +
                "version = 'v2u',\n" +
                "transport.protocol = 'tcp',\n" +
                "timeout = '100',\n" +
                "request.interval = '500',\n" +
                "agent.port = '" + port + "',\n" +
                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',\n" +
                "auth.protocol = 'AUTHSHA',\n" +
                "priv.protocol = 'PRIVDES',\n" +
                "priv.password = 'privpass',\n" +
                "auth.password = 'authpass',\n" +
                "security.lvl = 'AUTH_PRIV',\n" +
                "user.name = 'agent5') \n" +
                " define stream inputStream(value1 string, value2 string);\n";

        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        executionPlanRuntime.start();
        siddhiManager.shutdown();
    }

    // for invalid `oid validation`
    @Test(expectedExceptions = SiddhiAppValidationException.class)
    public void snmpValidationTest6() {

        log.info("-------------------------------------------------------------------------------------");
        log.info("                SNMP Version 3 Siddhi app validation for `priv.protocol`             ");
        log.info("-------------------------------------------------------------------------------------");

        SiddhiManager siddhiManager = new SiddhiManager();

        String siddhiApp = "@App:name('test') \n" +
                "@source(type='snmp', \n" +
                "@map(type='keyvalue', " +
                "   @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'value2' = '1.3.6.1.2.1.1.1.0') ),\n" +
                "host ='" + ip + "',\n" +
                "version = 'v2u',\n" +
                "transport.protocol = 'tcp',\n" +
                "timeout = '100',\n" +
                "request.interval = '500',\n" +
                "agent.port = '" + port + "',\n" +
                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.u.0',\n" +
                "auth.protocol = 'AUTHSHAE',\n" +
                "priv.protocol = 'PRIVDES',\n" +
                "priv.password = 'privpass',\n" +
                "auth.password = 'authpass',\n" +
                "security.lvl = 'AUTH_PRIV',\n" +
                "user.name = 'agent5') \n" +
                " define stream inputStream(value1 string, value2 string);\n";

        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        executionPlanRuntime.start();
        siddhiManager.shutdown();
    }

    // for invalid `port`
    @Test(expectedExceptions = SiddhiAppValidationException.class)
    public void snmpValidationTest7() {

        log.info("-------------------------------------------------------------------------------------");
        log.info("                SNMP Version 3 Siddhi app validation for `agent.port`             ");
        log.info("-------------------------------------------------------------------------------------");

        SiddhiManager siddhiManager = new SiddhiManager();

        String siddhiApp = "@App:name('test') \n" +
                "@source(type='snmp', \n" +
                "@map(type='keyvalue', " +
                "   @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'value2' = '1.3.6.1.2.1.1.1.0') ),\n" +
                "host ='" + ip + "',\n" +
                "version = 'v2c',\n" +
                "transport.protocol = 'tcp',\n" +
                "timeout = '100',\n" +
                "request.interval = '500',\n" +
                "agent.port = '999865',\n" +
                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.u.0',\n" +
                "auth.protocol = 'AUTHSHAE',\n" +
                "priv.protocol = 'PRIVDES',\n" +
                "priv.password = 'privpass',\n" +
                "auth.password = 'authpass',\n" +
                "security.lvl = 'AUTH_PRIV',\n" +
                "user.name = 'agent5') \n" +
                " define stream inputStream(value1 string, value2 string);\n";

        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        executionPlanRuntime.start();
        siddhiManager.shutdown();
    }

    // if the map annotation is not include in the siddhi app
    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void snmpCreationExceptionTest6() {

        log.info("-------------------------------------------------------------------------------------");
        log.info("                              Siddhi App Creation Exception                          ");
        log.info("-------------------------------------------------------------------------------------");

        SiddhiManager siddhiManager = new SiddhiManager();

        String siddhiApp = "@App:name('test') \n" +
                "@source(type='snmp', \n" +
                "host ='" + ip + "',\n" +
                "version = 'v2u',\n" +
                "transport.protocol = 'tcp',\n" +
                "timeout = '100',\n" +
                "request.interval = '500',\n" +
                "agent.port = '" + port + "',\n" +
                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',\n" +
                "auth.protocol = 'AUTHSHAE',\n" +
                "priv.protocol = 'PRIVDES',\n" +
                "priv.password = 'privpass',\n" +
                "auth.password = 'authpass',\n" +
                "security.lvl = 'AUTH_PRIV',\n" +
                "user.name = 'agent5') \n" +
                " define stream inputStream(value1 string, value2 string);\n";

        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
        executionPlanRuntime.start();
        siddhiManager.shutdown();
    }

    //Commenting test case to fix later
//    // pause and resume
//    @Test
//    public void snmpPauseAndResume() throws InterruptedException {
//
//        log.info("-------------------------------------------------------------------------------------");
//        log.info("                SNMP Version 3 Source Test Case Sec Lvl 3 Agent5                     ");
//        log.info("-------------------------------------------------------------------------------------");
//
//        SiddhiManager siddhiManager = new SiddhiManager();
//        String engineId = new OctetString(MPv3.createLocalEngineID()).substring(0, 9).toString();
//
//        String siddhiApp = "@App:name('test') \n" +
//                "@source(type='snmp', \n" +
//                "@map(type='keyvalue', " +
//                "   @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'value2' = '1.3.6.1.2.1.1.1.0') ),\n" +
//                "host ='" + ip + "',\n" +
//                "version = 'v3',\n" +
//                "timeout = '100',\n" +
//                "request.interval = '500',\n" +
//                "agent.port = '" + port + "',\n" +
//                "oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',\n" +
//                "auth.protocol = 'AuthHMAC192SHA256',\n" +
//                "priv.protocol = 'PrivAES192',\n" +
//                "priv.password = 'MD5AES192AuthPassword',\n" +
//                "auth.password = 'MD5AES192PrivPassword',\n" +
//                "security.lvl = 'AUTH_PRIV',\n" +
//                "engine.id = '" + engineId + "',\n" +
//                "engine.boot = '0',\n" +
//                "user.name = 'MD5AES192') \n" +
//                " define stream inputStream(value1 string, value2 string);\n";
//
//        SiddhiAppRuntime executionPlanRuntime = siddhiManager.createSiddhiAppRuntime(siddhiApp);
//        Collection<List<Source>> sources = executionPlanRuntime.getSources();
//        executionPlanRuntime.addCallback("inputStream", new StreamCallback() {
//            @Override
//            public void receive(Event[] events) {
//
//                EventPrinter.print(events);
//                for (Event event : events) {
//                    eventCount.getAndIncrement();
//                    eventArrived.set(true);
//                }
//            }
//        });
//
//
//        executionPlanRuntime.start();
//        SiddhiTestHelper.waitForEvents(sleepTime, 5, eventCount, timeout);
//
//        log.info("pause");
//        sources.forEach(e -> e.forEach(Source::pause));
//        Thread.sleep(6000);
//        sources.forEach(e -> e.forEach(Source::resume));
//        log.info("resume");
//        eventArrived.set(false);
//        SiddhiTestHelper.waitForEvents(sleepTime, 5, eventCount, timeout);
//        Thread.sleep(6000);
//        Assert.assertTrue(eventArrived.get());
//        siddhiManager.shutdown();
//    }

}
