package com.rg.nomadvpn.model;

public enum ServerStatusEnum {
    VPN_GENERATE_CONFIG,
    WAIT,
    AUTH,
    GET_CONFIG,
    ASSIGN_IP,
    ADD_ROUTES,
    CONNECTED
}
