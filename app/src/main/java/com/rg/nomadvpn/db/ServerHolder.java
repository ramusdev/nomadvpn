package com.rg.nomadvpn.db;

import com.rg.nomadvpn.R;
import com.rg.nomadvpn.model.ServerHolderConfiguration;
import com.rg.nomadvpn.utils.MyApplicationContext;

import java.util.ArrayList;
import java.util.Optional;

public class ServerHolder {
    ArrayList<ServerHolderConfiguration> serverList = new ArrayList<>();
    private static ServerHolder instance = null;

    public ServerHolder() {
        populateServerList();
    }

    public static ServerHolder getInstance() {
        if (instance == null) {
            instance = new ServerHolder();
            return instance;
        } else {
            return instance;
        }
    }

    private void populateServerList() {

        ServerHolderConfiguration serverGermany = new ServerHolderConfiguration();
        serverGermany.setFileName("config_germany.ovpn");
        serverGermany.setCountry(MyApplicationContext.getAppContext().getResources().getString(R.string.country_germany));
        serverGermany.setCity(MyApplicationContext.getAppContext().getResources().getString(R.string.city_saarbrucken));
        serverGermany.setFlagName("germany_flag");
        serverGermany.setId(0);
        serverGermany.setIp("51.89.112.101");
        serverGermany.setUser("vpnusername219");
        serverGermany.setPassword("vpnpassword219");
        serverList.add(serverGermany);

        ServerHolderConfiguration serverNotherland = new ServerHolderConfiguration();
        serverNotherland.setFileName("config_notherland.ovpn");
        serverNotherland.setCountry(MyApplicationContext.getAppContext().getResources().getString(R.string.country_notherland));
        serverNotherland.setCity(MyApplicationContext.getAppContext().getResources().getString(R.string.city_amsterdam));
        serverNotherland.setFlagName("notherland_flag");
        serverNotherland.setId(1);
        serverNotherland.setIp("88.210.3.167");
        serverNotherland.setUser("vpnusername219");
        serverNotherland.setPassword("vpnpassword219");
        serverList.add(serverNotherland);

        ServerHolderConfiguration serverRussia = new ServerHolderConfiguration();
        serverRussia.setFileName("config_russia.ovpn");
        serverRussia.setCountry(MyApplicationContext.getAppContext().getResources().getString(R.string.country_russia));
        serverRussia.setCity(MyApplicationContext.getAppContext().getResources().getString(R.string.city_moscow));
        serverRussia.setFlagName("russia_flag");
        serverRussia.setId(2);
        serverRussia.setIp("45.91.8.50");
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
