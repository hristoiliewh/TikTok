//package com.tiktok.tiktok.util;
//
//import com.maxmind.geoip2.DatabaseReader;
//import com.maxmind.geoip2.exception.GeoIp2Exception;
//import java.io.File;
//import java.io.IOException;
//import java.net.InetAddress;
//
//public class GeoIP2Reader {
//    private static final String DB_PATH = "geo/GeoIP2-City.mmdb";
//    private static DatabaseReader reader = null;
//
//    public static synchronized DatabaseReader getReader() throws IOException {
//        if (reader == null) {
//            File database = new File(DB_PATH);
//            reader = new DatabaseReader.Builder(database).build();
//        }
//        return reader;
//    }
//
//    public static String getRegionByIp(String ip) {
//        try {
//            InetAddress ipAddress = InetAddress.getByName(ip);
//            return getReader().city(ipAddress).getMostSpecificSubdivision().getName();
//        } catch (IOException | GeoIp2Exception e) {
//            //TODO Handle exception
//        }
//        return null;
//    }
//}
//
