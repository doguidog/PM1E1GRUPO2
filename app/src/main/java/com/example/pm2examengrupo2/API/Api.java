package com.example.pm2examengrupo2.API;

public class Api {
    private static final String ipaddress = "elkinhn.online";
    public static final String StringHttp = "https://";
    private static final String GetEmple = "/APIEG2/listacontactos.php";
    private static final String GetBuscar = "/APIEG2/listasinglecontacto.php?nombre=";
    private static final String setUpdate = "/APIEG2/actualizarcontacto.php";
    private static final String CreateUsuario = "/APIEG2/crearcontacto.php";
    public static final String EndPointGetContact = StringHttp + ipaddress + GetEmple;
    public static final String EndPointGetBuscarContact = StringHttp + ipaddress + GetBuscar;
    public static final String EndPointSetUpdateContact = StringHttp + ipaddress + setUpdate;
    public static final String EndPointCreateUsuario = StringHttp + ipaddress + CreateUsuario;
}
