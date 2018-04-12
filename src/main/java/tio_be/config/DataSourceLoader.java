package tio_be.config;

import com.alibaba.druid.wall.WallFilter;
import com.jfinal.plugin.druid.DruidPlugin;

import java.util.Properties;

/**
 * Created by Max on 2017/4/6.
 */
public class DataSourceLoader {
    private static final int DEFAULT_MAX_ACTIVE_CONNECTION = 15;

    public DataSourceLoader() {
    }

    public DruidPlugin loadDataSourceUsingDruid(Properties prop) {
        return this.loadDataSourceUsingDruid(prop, (String) null);
    }

    public DruidPlugin loadDataSourceUsingDruid(Properties prop, String configName) {
        String urlKey = configName == "url" ? configName : configName + ".url";
        String usernameKey = configName == "username" ? configName : configName + ".username";
        String passwordKey = configName == "password" ? configName : configName + ".password";
        String typeKey = configName == "type" ? configName : configName + ".type";
        String maxConnectionsKey = configName == "max_connections" ? configName : configName + ".max_connections";
        String url = prop.getProperty(urlKey);
        String username = prop.getProperty(usernameKey);
        String password = prop.getProperty(passwordKey);
        String dbType = prop.getProperty(typeKey);
        int maxActive = 15;

        try {
            maxActive = Integer.valueOf(prop.getProperty(maxConnectionsKey)).intValue();
        } catch (Exception var14) {
            ;
        }

        return this.loadDataSourceUsingDruid(url, username, password, dbType, maxActive);
    }

    public DruidPlugin loadDataSourceUsingDruid(String url, String username, String password, String dbType) {
        return this.loadDataSourceUsingDruid(url, username, password, dbType, 15);
    }

    public DruidPlugin loadDataSourceUsingDruid(String url, String username, String password, String dbType, int maxActive) {
        DruidPlugin druid = new DruidPlugin(url, username, password);
        druid.setMaxActive(maxActive);
        WallFilter wallFilter = new WallFilter();
        wallFilter.setDbType(dbType);
        druid.addFilter(wallFilter);
        return druid;
    }
}
