package api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import game.Team;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import static api.ChessAPI.CONNECT_PATH;
import static api.ChessAPI.USERSTATE_PATH;


public class ChessAPIServer {
    private final int port;
    private HttpServer server;
    private Delegate delegate;
    private final Gson gson;

    public ChessAPIServer(int port) {
        this.port = port;
        this.gson = new Gson();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public void start() throws Exception {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/" + CONNECT_PATH, this::handleConnectionRequest);
        server.createContext("/" + USERSTATE_PATH, this::handleUserState);
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);// no delay
        }
    }

    public String getIPAddress() throws UnknownHostException {
        String hostName = InetAddress.getLocalHost().getHostName();
        InetAddress[] inetAddresses = InetAddress.getAllByName(hostName);
        return inetAddresses[0].getHostAddress();
    }

    private void handleConnectionRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, 0);
        ConnectionRequest request = gson.fromJson(new InputStreamReader(httpExchange.getRequestBody()), ConnectionRequest.class);
        httpExchange.getResponseBody().close();
        if (delegate != null) {
            delegate.receiveConnectionRequest(request);
        }
    }

    private void handleUserState(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, 0);
        UserState state = gson.fromJson(new InputStreamReader(httpExchange.getRequestBody()), UserState.class);
        httpExchange.getResponseBody().close();
        if (delegate != null) {
            delegate.receiveUserStateFromNetwork(state);
        }
    }

    public interface Delegate {
        void receiveConnectionRequest(ConnectionRequest request);

        void receiveUserStateFromNetwork(UserState state);
    }
}
