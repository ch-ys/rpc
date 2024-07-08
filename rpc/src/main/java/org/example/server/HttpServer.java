package org.example.server;

import java.io.IOException;

public interface HttpServer {
    void start(int port) throws IOException;
}
