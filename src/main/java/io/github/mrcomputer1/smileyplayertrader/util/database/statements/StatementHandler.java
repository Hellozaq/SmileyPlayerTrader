package io.github.mrcomputer1.smileyplayertrader.util.database.statements;

import io.github.mrcomputer1.smileyplayertrader.util.database.AbstractDatabase;

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
         * enabled (boolean)
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
         * id (int)
         */
        SET_PRODUCT_COST_COST2,

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
        UPDATE_PLAYER_CONFIG
    }

    void run(StatementType type, Object... objs);
    ResultSet get(StatementType type, Object... objs);

}
