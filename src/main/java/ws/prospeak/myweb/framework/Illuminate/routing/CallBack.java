package ws.prospeak.myweb.framework.Illuminate.routing;

public interface CallBack {
    default Object callBack()  {return null; }
    default Object callBack(Object... t) { return null; }
}
