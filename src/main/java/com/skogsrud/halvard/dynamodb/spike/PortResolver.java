package com.skogsrud.halvard.dynamodb.spike;

import java.io.IOException;
import java.net.ServerSocket;

public class PortResolver {
    /**
     * Useful Java trick to find an unused port.
     */
    public static int findOpenPort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }
}
