package org.wso2.extension.siddhi.io.snmp.util.exceptions;

import java.io.IOException;

/**
 * Agent Not found exception
 *
 */

public class AgentNotFoundException extends IOException {

    public AgentNotFoundException() {
        super();
    }

    public AgentNotFoundException(String message) {
        super(message);
    }

    public AgentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
