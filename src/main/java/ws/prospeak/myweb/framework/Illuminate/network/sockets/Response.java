package ws.prospeak.myweb.framework.Illuminate.network.sockets;

public class Response {
    private boolean success;
    private Object message;
    private String dataType;
    private String event;

    public Response() {
    }

    public Response(boolean success, Object message, String dataType, String event) {
        this.success = success;
        this.message = message;
        this.dataType = dataType;
        this.event = event;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
