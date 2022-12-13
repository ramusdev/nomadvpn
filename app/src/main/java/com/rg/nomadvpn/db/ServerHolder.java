package com.rg.nomadvpn.db;

import com.rg.nomadvpn.model.ServerHolderConfiguration;
import java.util.ArrayList;
import java.util.Optional;

public class ServerHolder {
    ArrayList<ServerHolderConfiguration> serverList = new ArrayList<>();

    public ServerHolder() {
        populateServerList();
    }

    private void populateServerList() {
        ServerHolderConfiguration serverGermany = new ServerHolderConfiguration();
        serverGermany.setFileName("config_germany.ovpn");
        serverGermany.setCountry("Germany");
        serverGermany.setCity("Frankfurt am Main");
        serverGermany.setFlagName("germanyflag");
        serverGermany.setId(0);
        serverGermany.setUser("username219");
        serverGermany.setPassword("password219");
        serverList.add(serverGermany);

        ServerHolderConfiguration serverRussia = new ServerHolderConfiguration();
        serverRussia.setFileName("config_russia.ovpn");
        serverRussia.setCountry("Russia");
        serverRussia.setCity("Saint Petersburg");
        serverRussia.setFlagName("russiaflag");
        serverRussia.setId(1);
        serverRussia.setUser("vpnuser");
        serverRussia.setPassword("vpnpassword");
        serverList.add(serverRussia);
    }

    public void setServerList(ArrayList<ServerHolderConfiguration> serverList) {
        this.serverList = serverList;
    }

    public ArrayList<ServerHolderConfiguration> getServerList() {
        return serverList;
    }

    public ServerHolderConfiguration getServerById(int id) {
        Optional<ServerHolderConfiguration> optional = serverList.stream()
                .filter(e -> e.getId() == id)
                .findFirst();

        return optional.get();
    }
}
