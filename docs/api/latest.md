# API Docs - v1.0.0-SNAPSHOT

## Sink

### snmp *<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#sink">(Sink)</a>*

<p style="word-wrap: break-word"> SNMP Sink allows user to make set requests as a manager and make changes on agent.</p>

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
        <td style="vertical-align: top; word-wrap: break-word">Host name or ip of the target which is SNMP agent.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">version</td>
        <td style="vertical-align: top; word-wrap: break-word">Version of the snmp protocol. Acceptance parameters are V1 for version1, V2C for versionv2c, V3 for versionv3. </td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">community</td>
        <td style="vertical-align: top; word-wrap: break-word">Community string of the target SNMP agent. Default value is 'public'. This property only uses SNMP V1, V2C and do not need to provide this when using V3</td>
        <td style="vertical-align: top">public</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">agent.port</td>
        <td style="vertical-align: top; word-wrap: break-word">Port of the target SNMP agent.</td>
        <td style="vertical-align: top">161</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">transport.protocol</td>
        <td style="vertical-align: top; word-wrap: break-word">Transport protocol. Acceptance parameters TCP, UDP</td>
        <td style="vertical-align: top">udp</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timeout</td>
        <td style="vertical-align: top; word-wrap: break-word">Waiting time for response of a request in milliseconds.</td>
        <td style="vertical-align: top">1500</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">retries</td>
        <td style="vertical-align: top; word-wrap: break-word">Number of retries when a request fails.</td>
        <td style="vertical-align: top">5</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">user.name</td>
        <td style="vertical-align: top; word-wrap: break-word">User Name of the user that configured on target agent. This property only uses for SNMP version 3 and do not need to provide this when using other versions v2c, v1.</td>
        <td style="vertical-align: top">noUser</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">security.lvl</td>
        <td style="vertical-align: top; word-wrap: break-word">Security level. Acceptance parameters AUTH_PRIV, AUTH_NO_PRIVE, NO_AUTH_NO_PRIVE. This property only uses for SNMP version 3 and do not need to provide this when using other versions v2c, v1.</td>
        <td style="vertical-align: top">AUTH_PRIVE</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">priv.protocol</td>
        <td style="vertical-align: top; word-wrap: break-word">Privacy protocol of the target SNMP agent. Acceptance parameters NO_PRIV, PRIVDES, PRIVDES128, PRIVAES192, PRIVAES256, PRIV3DES. This property only uses for SNMP version 3 and do not need to provide this when using other versions v2c, v1.</td>
        <td style="vertical-align: top">NO_PRIV</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">priv.password</td>
        <td style="vertical-align: top; word-wrap: break-word">Privacy protocol passphrase of the target SNMP agent. Passphrase should have more than 8 characters. This property only uses for SNMP version 3 and do not need to provide this when using other versions v2c, v1.</td>
        <td style="vertical-align: top">privpass</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">auth.protocol</td>
        <td style="vertical-align: top; word-wrap: break-word">Authentication protocol of the target SNMP agent. Can use NO_AUTH, AUTHMD5, AUTHSHA, AUTHHMAC192SHA256, AUTHHMAC192SHA512. This property only uses for SNMP version 3 and do not need to provide this when using other versions v2c, v1.</td>
        <td style="vertical-align: top">NO_AUTH</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">auth.password</td>
        <td style="vertical-align: top; word-wrap: break-word">Authentication protocol passphrase of the target SNMP agent.Passphrase should have more than 8 characters. This property only uses for SNMP version 3 and do not need to provide this when using other versions v2c, v1.</td>
        <td style="vertical-align: top">authpass</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">engine.id</td>
        <td style="vertical-align: top; word-wrap: break-word">Local engine ID of the target SNMP agent. Default value is device-generated ID based on the local IP address and additional four random bytes. This property only uses for SNMP version 3 and do not need to provide this when using other versions v2c, v1.</td>
        <td style="vertical-align: top">Empty</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">engine.boot</td>
        <td style="vertical-align: top; word-wrap: break-word">Engine boot of the snmp engine of the target SNMP agent. Default value is 0. This property only uses for SNMP version 3 and do not need to provide this when using other versions v2c, v1.</td>
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
@map(type='keyvalue', @payload('1.3.6.1.2.1.1.1.0' = 'sysDscr')),
host = '127.0.0.1',
version = 'v1',
community = 'public',
agent.port = '161',
retries = '5')
define stream outputStream(sysDscr string);

```
<p style="word-wrap: break-word">This example shows how to make set request using snmp version v1 </p>

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
<p style="word-wrap: break-word">This example shows how to make set request using snmp version v2c </p>

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
<p style="word-wrap: break-word">This example shows how to make set request using snmp version v3 </p>

## Source

### snmp *<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#source">(Source)</a>*

<p style="word-wrap: break-word"> SNMP Source allows user to make get request as manager and get agent status in periodically</p>

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
        <td style="vertical-align: top; word-wrap: break-word">Address or ip of the target SNMP agent.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">version</td>
        <td style="vertical-align: top; word-wrap: break-word">Version of the snmp protocol. Acceptance parameters 'V1' - version1, 'V2C' - versionv2c, 'V3' - versionv3. </td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">oids</td>
        <td style="vertical-align: top; word-wrap: break-word">list of the OIDs separated by comma. ex :- '1.3.6.1.2.1.1.1.0, 1.3.6.1.2.1.1.6.0' </td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">request.interval</td>
        <td style="vertical-align: top; word-wrap: break-word">Request interval between two requests.</td>
        <td style="vertical-align: top">5000</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">community</td>
        <td style="vertical-align: top; word-wrap: break-word">Community string of the target SNMP agent. Default value is 'public'. This property only uses SNMP V1, V2C and do not need to provide while using V3</td>
        <td style="vertical-align: top">public</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">agent.port</td>
        <td style="vertical-align: top; word-wrap: break-word">Port number of the target SNMP agent.</td>
        <td style="vertical-align: top">161</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">transport.protocol</td>
        <td style="vertical-align: top; word-wrap: break-word">Transport protocol. Acceptance parameters TCP, UDP</td>
        <td style="vertical-align: top">udp</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timeout</td>
        <td style="vertical-align: top; word-wrap: break-word">Waiting time for response of a request in milliseconds.</td>
        <td style="vertical-align: top">1500</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">retries</td>
        <td style="vertical-align: top; word-wrap: break-word">Number of retries of when a request fails.</td>
        <td style="vertical-align: top">5</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">user.name</td>
        <td style="vertical-align: top; word-wrap: break-word">User Name of the user that configured on target agent. This property only uses for SNMP version 3 and do not need to provide this when using other versions(v2c, v1).</td>
        <td style="vertical-align: top">noUser</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">security.lvl</td>
        <td style="vertical-align: top; word-wrap: break-word">Security level. Acceptance parameters AUTH_PRIV, AUTH_NO_PRIVE, NO_AUTH_NO_PRIVE.This property only uses for SNMP version 3 and do not need to provide this when using other versions(v2c, v1).</td>
        <td style="vertical-align: top">AUTH_PRIVE</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">priv.protocol</td>
        <td style="vertical-align: top; word-wrap: break-word">Privacy protocol of the target SNMP agent. Acceptance parameters NO_PRIV, PRIVDES, PRIVDES128, PRIVAES192, PRIVAES256, PRIV3DES. This property only uses for SNMP version 3 and do not need to provide this when using other versions(v2c, v1).</td>
        <td style="vertical-align: top">NO_PRIV</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">priv.password</td>
        <td style="vertical-align: top; word-wrap: break-word">Privacy protocol passphrase of the target SNMP agent. Passphrase should have more than 8 characters. This property only uses for SNMP version 3 and do not need to provide this when using other versions(v2c, v1).</td>
        <td style="vertical-align: top">privpass</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">auth.protocol</td>
        <td style="vertical-align: top; word-wrap: break-word">Authentication protocol of the target SNMP agent. Can use NO_AUTH, AUTHMD5, AUTHSHA, AUTHHMAC192SHA256, AUTHHMAC192SHA512. This property only uses for SNMP version 3 and do not need to provide this when using other versions(v2c, v1).</td>
        <td style="vertical-align: top">NO_AUTH</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">auth.password</td>
        <td style="vertical-align: top; word-wrap: break-word">Authentication protocol passphrase of the target SNMP agent.Passphrase should have more than 8 characters. This property only uses for SNMP version 3 and do not need to provide this when using other versions(v2c, v1).</td>
        <td style="vertical-align: top">authpass</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">engine.id</td>
        <td style="vertical-align: top; word-wrap: break-word">Local engine ID of the target SNMP agent. Default value is device-generated ID, based on the local IP address and additional four random bytes. This property only uses for SNMP version 3 and do not need to provide this when using other versions(v2c, v1).</td>
        <td style="vertical-align: top">Empty</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">engine.boot</td>
        <td style="vertical-align: top; word-wrap: break-word">Engine boot of the snmp engine of the target SNMP agent. Default value is 0. This property only uses for SNMP version 3 and do not need to provide this when using other versions(v2c, v1).</td>
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
@map(type='keyvalue',    @attributes('value1' = '1.3.6.1.2.1.1.3.0', 'sysLocation' = '1.3.6.1.2.1.1.6.0') ),
host ='127.0.0.1',
version = 'v1',
agent.port = '161',
request.interval = '60000',
oids='1.3.6.1.2.1.1.3.0, 1.3.6.1.2.1.1.6.0',
community = 'public') 
 define stream inputStream(sysUpTime string, sysLocation string);

```
<p style="word-wrap: break-word">This example shows how to make get request for snmp version 1 </p>

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
<p style="word-wrap: break-word">This example shows how to make get request for snmp version 2c </p>

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
<p style="word-wrap: break-word">This example shows how to make get request for snmp version 3 </p>

