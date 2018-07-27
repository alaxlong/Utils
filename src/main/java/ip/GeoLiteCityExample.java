package ip; /**
 * \* Created with IntelliJ IDEA.
 * \* User: baibing.shang
 * \* Date: 2018/7/20
 * \* Description:
 * \
 */
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

public class GeoLiteCityExample {
    public static final String GEO_LITE_FILE_LOCATION = "D:\\IdeaProjects\\All-Class\\src\\main\\resources\\assets\\GeoLiteCity.dat";

    public String getCityName(String ip) {
        try {
            LookupService lookupService = new LookupService(
                    GEO_LITE_FILE_LOCATION, LookupService.GEOIP_MEMORY_CACHE);
            Location location = lookupService.getLocation(ip);
            if (location != null) {
                return location.city;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public LookupService getLookUpService(String ip){
        try {
            LookupService lookupService = new LookupService(
                    GEO_LITE_FILE_LOCATION, LookupService.GEOIP_MEMORY_CACHE);
            return lookupService;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCityName(InetAddress inetAddress) {
        try {
            LookupService lookupService = new LookupService(
                    GEO_LITE_FILE_LOCATION, LookupService.GEOIP_MEMORY_CACHE);
            Location location = lookupService.getLocation(inetAddress);
            if (location != null) {
                return location.city;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void main(String[] args){
        GeoLiteCityExample example = new GeoLiteCityExample();
        String ip = "122.144.218.13";
        String cityName = example.getCityName(ip);
        // Hangzhou
        System.out.println(cityName);

        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            cityName = example.getCityName(inetAddress);
            // Hangzhou
            System.out.println(cityName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        LookupService lookupService = example.getLookUpService(ip);
        System.out.println(lookupService.getOrg(ip) + "***" + lookupService.getRegion(ip).region);
    }
}