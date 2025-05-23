package io.github.mrcomputer1.smileyplayertrader.util.database.statements;

import java.sql.ResultSet;

public interface StatementHandler {

    enum StatementType{
        CREATE_PRODUCT_TABLE,
        CREATE_META_TABLE,
        CREATE_SETTINGS_TABLE,

        /**
         * merchant (uuid),
         * product (nbt blob),
         * cost1 (nbt blob),
         * cost2 (nbt blob),
         * enabled (boolean),
         * available (boolean),
         * special_price (int),
         * priority (int),
         * hide_on_out_of_stock (boolean)
         */
        ADD_PRODUCT,

        /**
         * merchant (uuid)
         */
        FIND_PRODUCTS,

        /**
         * id (int)
         */
        DELETE_PRODUCT,

        /**
         * cost1 (nbt blob)
         * id (int)
         */
        SET_COST,

        /**
         * cost2 (nbt blob)
         * id (int)
         */
        SET_SECONDARY_COST,

        /**
         * product (nbt blob)
         * id (int)
         */
        SET_PRODUCT,

        /**
         * id (int)
         */
        ENABLE_PRODUCT,

        /**
         * id (int)
         */
        DISABLE_PRODUCT,

        /**
         * id (int)
         */
        GET_PRODUCT_BY_ID,

        /**
         * merchant (uuid)
         * LIMIT, OFFSET
         */
        FIND_PRODUCTS_IN_PAGES,

        /**
         * product (nbt blob)
         * cost (nbt blob)
         * cost2 (nbt blob)
         * special_price (int)
         * priority (int)
         * hide_on_out_of_stock (boolean)
         * purchase_limit (int)
         * id (int)
         */
        SET_PRODUCT_COST_COST2_SPECIALPRICE_PRIORITY_HIDEOUTOFSTOCK_PURCHASELIMIT,

        /**
         * id (int)
         */
        GET_ENABLED,

        /**
         * player (uuid)
         */
        LOAD_PLAYER_CONFIG,

        /**
         * player (uuid)
         */
        CREATE_DEFAULT_PLAYER_CONFIG,

        /**
         * trade_toggle (boolean)
         * combat_notice_toggle (boolean)
         * player (uuid)
         */
        UPDATE_PLAYER_CONFIG,

        /**
         * id (int)
         */
        HIDE_PRODUCT,

        /**
         * discount (int)
         * id (int)
         */
        SET_DISCOUNT,

        /**
         * priority (int)
         * id (int)
         */
        SET_PRIORITY,

        /**
         * amount (int)
         * id (int)
         */
        CHANGE_STORED_PRODUCT,

        /**
         * amount (int)
         * id (int)
         */
        CHANGE_STORED_COST,

        /**
         * amount (int)
         * id (int)
         */
        CHANGE_STORED_COST2,

        /**
         * amount (int)
         * id (int)
         */
        SET_STORED_COST,

        /**
         * amount (int)
         * id (int)
         */
        SET_STORED_COST2,

        /**
         * merchant (uuid)
         */
        FIND_PRODUCTS_WITH_EARNINGS,

        /**
         * merchant (uuid)
         */
        GET_UNCOLLECTED_EARNINGS,

        /**
         * hide_on_out_of_stock (boolean),
         * id (int)
         */
        SET_HIDE_ON_OUT_OF_STOCK,

        /**
         * START_ID, LIMIT, OFFSET
         */
        FIND_ALL_PRODUCTS_IN_PAGES,

        /**
         * id (int)
         */
        INCREMENT_PURCHASE_COUNT,

        /**
         * id (int)
         */
        RESET_PURCHASE_COUNT,

        /**
         * id (int), limit (int)
         */
        SET_PURCHASE_LIMIT,

        /**
         * id (int)
         */
        TOGGLE_UNLIMITED_SUPPLY,

        /**
         * merchant (uuid), enabled (bool), available (bool)
         */
        COUNT_PRODUCTS_FOR,

        /**
         * enabled (bool), available (bool)
         */
        COUNT_PRODUCTS_GLOBAL,

        /**
         * merchant (uuid)
         */
        COUNT_INACTIVE_PRODUCTS_FOR,

        COUNT_INACTIVE_PRODUCTS_GLOBAL,

        /**
         * merchant (uuid)
         */
        COUNT_ALL_PRODUCTS_FOR,

        COUNT_ALL_PRODUCTS_GLOBAL
    }

    void run(StatementType type, Object... objs);
    long runAndReturnInsertId(StatementType type, Object... objs);
    ResultSet get(StatementType type, Object... objs);

}
