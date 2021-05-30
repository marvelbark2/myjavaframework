package ws.prospeak.myweb.framework.Illuminate.database.orm;

import ws.prospeak.myweb.framework.Illuminate.collection.CollectionEntity;

import java.rmi.Remote;

public interface Model extends Remote {
    String json() throws Exception;
    <T extends Models> T save() throws Exception;
    Boolean checkEncrypted(String checkpw) throws Exception;
    <T extends Models> CollectionEntity<T> all() throws Exception;
    <T extends  Models>  T find(Object id) throws Exception;

}
