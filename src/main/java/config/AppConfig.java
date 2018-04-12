package config;

import models.*;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import controllers.HelloWorldController;
import controllers.UserController;

/**
 * @author Jieying Xu
 */
public class AppConfig extends JFinalConfig {

    public static void main(String[] args) {
        // Launching under IDEA environment
        JFinal.start("src/main/webapp", 80, "/");
    }

    @Override
    public void configConstant(Constants me) {
        me.setDevMode(true);
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/hello", HelloWorldController.class);
        me.add("/user", UserController.class);
    }

    @Override
    public void configEngine(Engine me) {}

    @Override
    public void configPlugin(Plugins me) {
        DataSourceLoader dsl = new DataSourceLoader();
        DruidPlugin dp = dsl.loadDataSourceUsingDruid(PropKit.use("datasource.properties").getProperties(), "master");
        me.add(dp);

        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        me.add(arp);
        arp.addMapping("user", User.class);
        arp.addMapping("brand", Brand.class);
        arp.addMapping("category", Category.class);
        arp.addMapping("commodity", Commodity.class);
        arp.addMapping("favorite_list", "user_id, commodity_id", FavoriteList.class);
        arp.addMapping("tp_shop", TpShop.class);
        arp.addMapping("tp_user", TpUser.class);
        arp.addMapping("viewing_history", ViewingHistory.class);
    }

    @Override
    public void configInterceptor(Interceptors me) {}

    @Override
    public void configHandler(Handlers me) {}
}
