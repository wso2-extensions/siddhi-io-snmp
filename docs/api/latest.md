# API Docs - v1.0.1-SNAPSHOT

## Sink

### snmp *<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#sink">(Sink)</a>*

<p style="word-wrap: break-word">The SNMP Sink allows you to make set requests as a manager and make changes on agent. </p>

<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>
```
@sink(type="snmp", host="<STRING>", version="<STRING>", community="<STRING>", agent.port="<INT>", transport.protocol="<STRING>", timeout="<INT>", retries="<INT>", user.name="<STRING>", security.lvl="<INT>", priv.protocol="<STRING>", priv.password="<STRING>", auth.protocol="<STRING>", auth.password="<STRING>", engine.id="<STRING>", engine.boot="<INT>", @map(...)))
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">host</td>
        <td style="vertical-align: top; word-wrap: break-word">The host name or the IP of the target that functions as the SNMP agent.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">version</td>
        <td style="vertical-align: top; word-wrap: break-word">The version of the SNMP protocol. Possible values are 'V1' for version1, 'V2C' for versionv2c, and 'V3' for versionv3.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">community</td>
        <td style="vertical-align: top; word-wrap: break-word">The community string of the target SNMP agent. The default value is 'public'. This property is needed only when using SNMP versions V1 and  V2C. It is not applicable when using V3.</td>
        <td style="vertical-align: top">public</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">agent.port</td>
        <td style="vertical-align: top; word-wrap: break-word">The port of the target SNMP agent.</td>
        <td style="vertical-align: top">161</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">transport.protocol</td>
        <td style="vertical-align: top; word-wrap: break-word">The transport protocol used by the source to communicate. Supported transport protocols are 'TCP' and 'UDP'.</td>
        <td style="vertical-align: top">udp</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timeout</td>
        <td style="vertical-align: top; word-wrap: break-word">The number of milliseconds for which the system must wait for a response after a request is sent. If the time interval specified here elapses before a response is received, the system retries sending the request. If no response is received even after the number of retries reaches the maximum number specified via the 'SNMPConstantsRETRIES' parameter, this can be due to the transport agent not being available or due to the occurence of a timeout error. An error is logged to inform you of the reason.</td>
        <td style="vertical-align: top">1500</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">retries</td>
        <td style="vertical-align: top; word-wrap: break-word">The maximum number of retries that must be attempted by the system if a request does not receive a response within the time interval specifed via the 'SNMPConstants.TIMEOUT' parameter.</td>
        <td style="vertical-align: top">5</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">user.name</td>
        <td style="vertical-align: top; word-wrap: break-word">The user name of the user that is configured on the transport agent. This property is required only when using SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">noUser</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">security.lvl</td>
        <td style="vertical-align: top; word-wrap: break-word">The SNMP security level. Possible values are 'AUTH_PRIV', 'AUTH_NO_PRIVE', and 'NO_AUTH_NO_PRIVE'. For more information about these security levels, see [SNMP Security Model and Levels](https://www.ccexpert.us/wide-area-networks-2/snmp-security-models-and-levels.html). This property is required only for SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">AUTH_PRIVE</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">priv.protocol</td>
        <td style="vertical-align: top; word-wrap: break-word">The privacy protocol of the target SNMP agent. Possible values are 'NO_PRIV', 'PRIVDES', 'PRIVDES128', 'PRIVAES192', 'PRIVAES256', and 'PRIV3DES'. This property is only required for SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">NO_PRIV</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">priv.password</td>
        <td style="vertical-align: top; word-wrap: break-word">The privacy protocol passphrase of the target SNMP agent. The passphrase must have more than eight characters. This property is required only for SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">privpass</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">auth.protocol</td>
        <td style="vertical-align: top; word-wrap: break-word">The authentication protocol of the target SNMP agent. Possible values are 'NO_AUTH', 'AUTHMD5', 'AUTHSHA', 'AUTHHMAC192SHA256', and 'AUTHHMAC192SHA512'. This property is required only for SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">NO_AUTH</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">auth.password</td>
        <td style="vertical-align: top; word-wrap: break-word">The authentication protocol passphrase of the target SNMP agent. Thepassphrase must have more than eight characters. This property is required only for SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">authpass</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">engine.id</td>
        <td style="vertical-align: top; word-wrap: break-word">The local engine ID of the target SNMP agent. The default value is thedevice-generated ID based on the local IP address and four additional random bytes. This property is required only for SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">Empty</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">engine.boot</td>
        <td style="vertical-align: top; word-wrap: break-word">The engine boot of the SNMP engine of the target SNMP agent. The default value is '0'. This property is required only for SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">0</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
@Sink(type='snmp',
@map(type='keyvalue', @payload('1.3.6.1.2.1.1.6.0' = 'sysLocation')),
host = '127.0.0.1',
version = 'v1',
community = 'public',
agent.port = '161',
retries = '5')
define stream outputStream(sysLocation string);

```
<p style="word-wrap: break-word">This example shows how to make set request using SNMP version v1 It uses keyvalue mapping and the default transport protocol is UDP. The @map annotation specifies that '1.3.6.1.2.1.1.6.0' represents the system location. Therefore, when you send the message, the system location of your localhost's SNMP agent is sent in the encoded form. The recipient of the massage can derive the system location from '1.3.6.1.2.1.1.6.0' sent with the message. After sending the message, it can configure a target agent in the localhost sysLocation (related to OID).</p>

<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
@Sink(type='snmp',
@map(type='keyvalue', @payload('1.3.6.1.2.1.1.6.0' = 'sysLocation')),
host = '127.0.0.1',
version = 'v2c',
community = 'public',
agent.port = '161',
retries = '5')
define stream outputStream(sysLocation string);

```
<p style="word-wrap: break-word">The above query shows how to make a set request using SNMP version v2c. </p>

<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
@Sink(type='snmp',
@map(type='keyvalue', @payload('1.3.6.1.2.1.1.4.0' = 'sysLocation', '1.3.6.1.2.1.1.1.0' = 'sysDscr')),
host = '127.0.0.1',
version = 'v3',
agent.port = '161',
priv.password = 'privpass',
auth.protocol = 'AUTHMD5',
priv.protocol = 'PRIVDES',
auth.password = 'authpass',
priv.password = 'privpass',
user.name = 'agent5', 
retries = '5')
define stream outputStream(sysLocation string, sysDscr string);

```
<p style="word-wrap: break-word">The above query shows how to make a set request using SNMP version v3. Note that in this example, you can also configure security protocols related to the target agent.</p>

## Source

### snmp *<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#source">(Source)</a>*

<p style="word-wrap: break-word">The SNMP source allows you to make GET request as manager in order to retrieve the agent status periodically. </p>

<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>
```
@source(type="snmp", host="<STRING>", version="<STRING>", oids="<STRING>", request.interval="<INT>", community="<STRING>", agent.port="<INT>", transport.protocol="<STRING>", timeout="<INT>", retries="<INT>", user.name="<STRING>", security.lvl="<INT>", priv.protocol="<STRING>", priv.password="<STRING>", auth.protocol="<STRING>", auth.password="<STRING>", engine.id="<STRING>", engine.boot="<INT>", @map(...)))
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">host</td>
        <td style="vertical-align: top; word-wrap: break-word">The address or the IP of the target SNMP agent.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">version</td>
        <td style="vertical-align: top; word-wrap: break-word">The version of the SNMP protocol. Possible values are 'V1' for version1, 'V2C' for versionv2c, and 'V3' for versionv3.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">oids</td>
        <td style="vertical-align: top; word-wrap: break-word">The list of Object Identifiers (OIDs) separated by commas.<br>e.g., '1.3.6.1.2.1.1.1.0, 1.3.6.1.2.1.1.6.0'<br>&nbsp;OIDs are objects to be identified from the received message. The same objects need to be included in the source mapping as attribute values for Siddhi to identify what attributes they represent.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">request.interval</td>
        <td style="vertical-align: top; word-wrap: break-word">The time interval between two requests. Once you send a request and then send another before the request interval has elapsed, the second request is not delivered.</td>
        <td style="vertical-align: top">5000</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">community</td>
        <td style="vertical-align: top; word-wrap: break-word">The community string of the target SNMP agent. The default value is 'public'. This property is required only when the V1 and V2C SNMP versions are used. It is not applicable when you use the V3 version.</td>
        <td style="vertical-align: top">public</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">agent.port</td>
        <td style="vertical-align: top; word-wrap: break-word">The port number of the target SNMP agent.</td>
        <td style="vertical-align: top">161</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">transport.protocol</td>
        <td style="vertical-align: top; word-wrap: break-word">The transport protocol used by the source to communicate. Supported transport protocols are 'TCP' and 'UDP'.</td>
        <td style="vertical-align: top">udp</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timeout</td>
        <td style="vertical-align: top; word-wrap: break-word">The number of milliseconds for which the system must wait for a response after a request is sent. If the time interval specified here elapses before a response is received, the system retries sending the request. If no response is received even after the number of retries reaches the maximum number specified via the 'SNMPConstantsRETRIES' parameter, this can be due to the transport agent not being available or due to the occurence of a timeout error. An error is logged to inform you of the reason.</td>
        <td style="vertical-align: top">1500</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">retries</td>
        <td style="vertical-align: top; word-wrap: break-word">The maximum number of retries that must be attempted by the system if a request does not receive a response within the time interval specifed via the 'SNMPConstants.TIMEOUT' parameter.</td>
        <td style="vertical-align: top">5</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">user.name</td>
        <td style="vertical-align: top; word-wrap: break-word">The user name of the user that is configured on the transport agent. This property is required only when using SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">noUser</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">security.lvl</td>
        <td style="vertical-align: top; word-wrap: break-word">The SNMP security level. Possible values are 'AUTH_PRIV', 'AUTH_NO_PRIVE', and 'NO_AUTH_NO_PRIVE'. For more information about these security levels, see [SNMP Security Model and Levels](https://www.ccexpert.us/wide-area-networks-2/snmp-security-models-and-levels.html). This property is required only for SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">AUTH_PRIVE</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">priv.protocol</td>
        <td style="vertical-align: top; word-wrap: break-word">The privacy protocol of the target SNMP agent. Possible values are 'NO_PRIV', 'PRIVDES', 'PRIVDES128', 'PRIVAES192', 'PRIVAES256', and 'PRIV3DES'. This property is only required for SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">NO_PRIV</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">priv.password</td>
        <td style="vertical-align: top; word-wrap: break-word">The privacy protocol passphrase of the target SNMP agent. The passphrase must have more than eight characters. This property is required only for SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">privpass</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">auth.protocol</td>
        <td style="vertical-align: top; word-wrap: break-word">The authentication protocol of the target SNMP agent. Possible values are 'NO_AUTH', 'AUTHMD5', 'AUTHSHA', 'AUTHHMAC192SHA256', and 'AUTHHMAC192SHA512'. This property is required only for SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">NO_AUTH</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">auth.password</td>
        <td style="vertical-align: top; word-wrap: break-word">The authentication protocol passphrase of the target SNMP agent. Thepassphrase must have more than eight characters. This property is required only for SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">authpass</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">engine.id</td>
        <td style="vertical-align: top; word-wrap: break-word">The local engine ID of the target SNMP agent. The default value is thedevice-generated ID based on the local IP address and four additional random bytes. This property is required only for SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">Empty</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">engine.boot</td>
        <td style="vertical-align: top; word-wrap: break-word">The engine boot of the SNMP engine of the target SNMP agent. The default value is '0'. This property is required only for SNMP version 3. It is not applicable when using the v2c or v1 version.</td>
        <td style="vertical-align: top">0</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
@source(type='snmp', 
@map(type='keyvalue',    @attributes('sysUpTime' = '1.3.6.1.2.1.1.3.0', 'sysLocation' = '1.3.6.1.2.1.1.6.0') ),
host ='127.0.0.1',
version = 'v1',
agent.port = '161',
request.interval = '60000',
oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.6.0',
community = 'public') 
 define stream inputStream(sysUpTime string, sysLocation string);

```
<p style="word-wrap: break-word">This query shows how to make a get request for SNMP version 1. It uses key-value mapping. The transport protocol is UDP by default.After starting the Siddhi application, it can get information of the target periodically. The @map annotation specifies that '1.3.6.1.2.1.1.3.0' is the system up time, and '1.3.6.1.2.1.1.6.0' is the system location. The same values are defined as OIDs. Therefore, when a message is received with those values, the system returns the system up time and the system location derived from those values.</p>

<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
@source(type='snmp', 
@map(type='keyvalue',    @attributes('sysUpTime' = '1.3.6.1.2.1.1.3.0', 'sysLocation' = '1.3.6.1.2.1.1.6.0') ),
host ='127.0.0.1',
version = 'v2c',
agent.port = '161',
request.interval = '60000',
oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.6.0',
community = 'public') 
 define stream inputStream(sysUpTime string, sysLocation string);

```
<p style="word-wrap: break-word">This example shows how to make get request for SNMP version 2c.</p>

<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
@source(type ='snmp', 
@map(type='keyvalue',    @attributes('sysUpTime' = '1.3.6.1.2.1.1.3.0', 'sysDescr' = '1.3.6.1.2.1.1.1.0') ),
host ='127.0.0.1',
version = 'v3',
timeout = '1500',
request.interval = '60000',
agent.port = '161',
oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.1.0',
auth.protocol = 'AUTHMD5',
priv.protocol = 'PRIVDES',
priv.password = 'privpass',
auth.password = 'authpass',
user.name = 'agent5') 
define stream inputStream(sysUpTime string, sysDescr string);

```
<p style="word-wrap: break-word">This example shows how to make get request for SNMP version 3. Note that because this example uses version 3, you can also configure security protocols related to the target agent.</p>

