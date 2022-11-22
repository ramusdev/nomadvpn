package com.rg.nomadvpn.model;

public enum ServerStatusEnum {
    RECONNECTING,
    DISCONNECTED,
    NONETWORK,
    CONNECTRETRY,
    NOPROCESS,
    VPN_GENERATE_CONFIG,
    WAIT,
    AUTH,
    GET_CONFIG,
    ASSIGN_IP,
    ADD_ROUTES,
    CONNECTED
}


