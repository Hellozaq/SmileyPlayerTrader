package io.github.mrcomputer1.smileyplayertrader.util.database;

import io.github.mrcomputer1.smileyplayertrader.SmileyPlayerTrader;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;

public class SQLiteDatabase extends AbstractDatabase {

    private Connection conn = null;
    private long insertId = -1;

    private boolean useReturningRowId;

    public SQLiteDatabase(File name){
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + name.getAbsolutePath());
            useReturningRowId = !conn.getMetaData().supportsGetGeneratedKeys();
        } catch (SQLException e) {
            SmileyPlayerTrader.getInstance().getLogger().severe("Failed to open/create SQLite3 database. Disabling...");
            Bukkit.getPluginManager().disablePlugin(SmileyPlayerTrader.getInstance());
            e.printStackTrace();
        }
    }

    private void setValues(PreparedStatement stmt, Object... objs){
        for(int i = 0; i < objs.length; i++){
            try {
                stmt.setObject(i + 1, objs[i]);
            } catch (SQLException e) {
                SmileyPlayerTrader.getInstance().getLogger().severe("Failed to set a value in an SQLite3 statement.");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run(String sql, Object... objs){
        this.logSQLStatement("run", sql, objs);

        try {
            if (!isConnected()) {
                SmileyPlayerTrader.getInstance().getLogger().severe("Failed to run statement as there is no database connection.");
                return;
            }

            PreparedStatement stmt;
            if (useReturningRowId) {
                stmt = this.conn.prepareStatement(sql);
            } else {
                stmt = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            }

            setValues(stmt, objs);
            stmt.execute();

            if (useReturningRowId) {
                ResultSet s = stmt.getResultSet();
                if(s != null && s.next()){
                    this.insertId = s.getLong(1);
                }
            } else {
                ResultSet s = stmt.getGeneratedKeys();
                if(s.next()){
                    this.insertId = s.getLong(1);
                }
            }

            stmt.close();

        }catch(SQLException e){
            SmileyPlayerTrader.getInstance().getLogger().severe("Failed to execute SQLite3 statement.");
            e.printStackTrace();
        }
    }

    @Override
    public long runAndReturnInsertId(String sql, Object... objs) {
        if(useReturningRowId) {
            this.run(sql + " RETURNING rowid", objs);
        } else {
            this.run(sql, objs);
        }
        return this.insertId;
    }

    @Override
    public ResultSet get(String sql, Object... objs){
        this.logSQLStatement("get", sql, objs);

        try{
            if(!isConnected()){
                SmileyPlayerTrader.getInstance().getLogger().severe("Failed to run statement as there is no database connection.");
                return null;
            }
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            setValues(stmt, objs);
            return stmt.executeQuery();
        } catch (SQLException e) {
            SmileyPlayerTrader.getInstance().getLogger().severe("Failed to execute SQLite3 query statement.");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isConnected(){
        try {
            return this.conn != null && !this.conn.isClosed();
        } catch (SQLException e) {
            SmileyPlayerTrader.getInstance().getLogger().warning("Failed to check connection status, assuming not connected!");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void close(){
        if(!isConnected()){
            SmileyPlayerTrader.getInstance().getLogger().severe("Failed to close connection as not connected!");
            return;
        }
        try {
            this.conn.close();
        } catch (SQLException e) {
            SmileyPlayerTrader.getInstance().getLogger().severe("Failed to close connection.");
            e.printStackTrace();
        }
    }

    @Override
    protected void upgrade(int version) {
        if(version == 1){
            run("ALTER TABLE products RENAME TO " + this.getDatabasePrefix() + "products");
        }else if(version == 2){
            run("CREATE TABLE IF NOT EXISTS " + this.getDatabasePrefix() + "settings (" +
                    "player TEXT NOT NULL," +
                    "trade_toggle BOOLEAN DEFAULT 1 NOT NULL," +
                    "combat_notice_toggle BOOLEAN DEFAULT 1 NOT NULL)");
        }else if(version == 3){
            run("ALTER TABLE " + this.getDatabasePrefix() + "products ADD COLUMN available BOOLEAN DEFAULT 1 NOT NULL");
            run("ALTER TABLE " + this.getDatabasePrefix() + "products ADD COLUMN special_price INTEGER DEFAULT 0 NOT NULL");
        }else if(version == 4){
            run("ALTER TABLE " + this.getDatabasePrefix() + "products ADD COLUMN priority INTEGER DEFAULT 0 NOT NULL");
            run("ALTER TABLE " + this.getDatabasePrefix() + "products ADD COLUMN stored_product INTEGER DEFAULT 0 NOT NULL");
            run("ALTER TABLE " + this.getDatabasePrefix() + "products ADD COLUMN stored_cost INTEGER DEFAULT 0 NOT NULL");
            run("ALTER TABLE " + this.getDatabasePrefix() + "products ADD COLUMN stored_cost2 INTEGER DEFAULT 0 NOT NULL");
            run("ALTER TABLE " + this.getDatabasePrefix() + "products ADD COLUMN hide_on_out_of_stock INTEGER DEFAULT 0 NOT NULL");
        }else if(version == 5){
            run("ALTER TABLE " + this.getDatabasePrefix() + "products ADD COLUMN purchase_limit INTEGER DEFAULT -1 NOT NULL");
            run("ALTER TABLE " + this.getDatabasePrefix() + "products ADD COLUMN purchase_count INTEGER DEFAULT 0 NOT NULL");
        }else if(version == 6){
            run("ALTER TABLE " + this.getDatabasePrefix() + "products ADD COLUMN unlimited_supply BOOLEAN DEFAULT 0 NOT NULL");
        }
    }

}
