package org.example.server;

import io.vertx.core.Vertx;

import java.io.IOException;

public class VertxHttpServer implements HttpServer {

    @Override
    public void start(int port) throws IOException {

        //vertx实例
        Vertx vertx = Vertx.vertx();

        //server
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();

        //server处理方案
        httpServer.requestHandler(new HttpSeverHandler());

        //server启动
        httpServer.listen(port,result ->{
            if (result.succeeded()) {
                System.out.println("HTTP server started on port " + port);
            }else {
                System.out.println("HTTP server failed to start on port " + port);
            }
        });
    }
}
