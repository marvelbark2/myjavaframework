package ws.prospeak.myweb.framework.Illuminate.network.sockets;

import com.fasterxml.jackson.databind.JsonNode;

public class Request {
    private String event;
    private JsonNode data;

    public Request(String event, JsonNode data) {
        this.event = event;
        this.data = data;
    }

    public Request() {
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }
}
