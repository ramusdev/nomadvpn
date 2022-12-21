package com.rg.nomadvpn.db;

import com.rg.nomadvpn.R;
import com.rg.nomadvpn.model.ServerHolderConfiguration;
import com.rg.nomadvpn.utils.MyApplicationContext;

import java.util.ArrayList;
import java.util.Optional;

public class ServerHolder {
    ArrayList<ServerHolderConfiguration> serverList = new ArrayList<>();

    public ServerHolder() {
        populateServerList();
    }

    private void populateServerList() {

        ServerHolderConfiguration serverNotherland = new ServerHolderConfiguration();
        serverNotherland.setFileName("config_notherland.ovpn");
        serverNotherland.setCountry(MyApplicationContext.getAppContext().getResources().getString(R.string.country_notherland));
        serverNotherland.setCity(MyApplicationContext.getAppContext().getResources().getString(R.string.city_amsterdam));
        serverNotherland.setFlagName("notherland_flag");
        serverNotherland.setId(0);
        serverNotherland.setUser("vpnusername219");
        serverNotherland.setPassword("vpnpassword219");
        serverList.add(serverNotherland);

        ServerHolderConfiguration serverRussia = new ServerHolderConfiguration();
        serverRussia.setFileName("config_russia.ovpn");
        serverRussia.setCountry(MyApplicationContext.getAppContext().getResources().getString(R.string.country_russia));
        serverRussia.setCity(MyApplicationContext.getAppContext().getResources().getString(R.string.city_moscow));
        serverRussia.setFlagName("russia_flag");
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
