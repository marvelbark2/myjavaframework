package ws.prospeak.myweb.framework.Illuminate.network.rmi;

import ws.prospeak.myweb.framework.Illuminate.collection.CollectionEntity;
import ws.prospeak.myweb.framework.Illuminate.database.orm.Model;
import ws.prospeak.myweb.framework.Illuminate.database.orm.Models;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiModel extends UnicastRemoteObject implements Model {
    private Models model;

    public RmiModel(Models model) throws RemoteException {
        this.model = model;
        System.out.println("Model in rmi is " + model);
    }

    @Override
    public String json() throws Exception {
        return model.json();
    }

    @Override
    public <T extends Models> T save() throws Exception {
        return model.save();
    }

    @Override
    public Boolean checkEncrypted(String checkpw) throws Exception {
        return model.checkEncrypted(checkpw);
    }

    @Override
    public <T extends Models> CollectionEntity<T> all() throws Exception {
        return model.all();
    }

    @Override
    public <T extends Models> T find(Object id) throws Exception {
        return model.find(id);
    }
}
