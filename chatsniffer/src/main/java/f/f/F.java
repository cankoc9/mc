package f.f;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public final class F extends JavaPlugin implements Listener {
    Connection sqliteconn;
    static final String dbname="playermessageinfo";//your db name
    static final String tbname="sniffinfo";//your message info table
    static final String createtable=tbname+"(id serial primary key,name varchar(255),ip varchar(255),msg text)";
    static final String insertcommand="insert into "+tbname+"(name,ip,msg) values('%s','%s','%s');";
    static final String pass="";
    static final String user="root";
    public void setcon() throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        sqliteconn= DriverManager.getConnection("jdbc:mysql://localhost/",user,pass);
        Statement st=sqliteconn.createStatement();
        st.execute("create database if not exists "+dbname);
        st.execute("use "+dbname+";");
        st.execute("create table if not exists "+createtable);
    }
    @Override
    public void onEnable() {
        try{
            setcon();
            getServer().getPluginManager().registerEvents(this,this);
        }
        catch (Exception ex){
            getLogger().info(ex.getMessage());
        }
    }
    @EventHandler
    public void OnPlayerChat(AsyncPlayerChatEvent e){
        try{
            Statement st=sqliteconn.createStatement();
            st.execute("use "+dbname+";");
            String name=e.getPlayer().getPlayerListName();
            String ip=e.getPlayer().getAddress().toString();
            String msg=e.getMessage();
            st.execute(String.format(insertcommand,name,ip,msg));
        }
        catch (Exception ex){
            getLogger().info(ex.toString());
        }
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
