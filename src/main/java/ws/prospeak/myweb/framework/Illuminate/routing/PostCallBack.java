package ws.prospeak.myweb.framework.Illuminate.routing;

public interface PostCallBack {
    default Object callBack(Request request)  {return null; }
    default Object callBack(Request request, Object... t) { return null; }
}
