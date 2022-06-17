package io.github.mrcomputer1.smileyplayertrader.command;

import io.github.mrcomputer1.smileyplayertrader.SmileyPlayerTrader;
import io.github.mrcomputer1.smileyplayertrader.util.CommandUtil;
import io.github.mrcomputer1.smileyplayertrader.util.I18N;
import io.github.mrcomputer1.smileyplayertrader.util.database.statements.StatementHandler;
import io.github.mrcomputer1.smileyplayertrader.versions.VersionSupport;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepositCommand implements ICommand{
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(I18N.translate("&cYou must be running this command from a player."));
            return;
        }

        if(!SmileyPlayerTrader.getInstance().getConfig().getBoolean("itemStorage.enable", true)){
            sender.sendMessage(I18N.translate("&cItem storage is not enabled."));
            return;
        }

        if(args.length < 1){
            sender.sendMessage(I18N.translate("&cBad Syntax! &f/spt deposit <id>"));
            return;
        }

        long id;
        try{
            id = Long.parseLong(args[0]);
        }catch (NumberFormatException e){
            sender.sendMessage(I18N.translate("&cInvalid Number!"));
            return;
        }

        if(CommandUtil.isNotAuthorized(sender, id)){
            sender.sendMessage(I18N.translate("&cWhoops! You are not authorized to edit others products!"));
            return;
        }

        Player p = (Player) sender;

        if (p.getInventory().getItemInMainHand().getType().isAir()) {
            sender.sendMessage(I18N.translate("&cYou must be holding an item in your main hand!"));
            return;
        }

        ItemStack hand = p.getInventory().getItemInMainHand();

        ResultSet set = SmileyPlayerTrader.getInstance().getStatementHandler().get(StatementHandler.StatementType.GET_PRODUCT_BY_ID, id);
        try {
            if (set.next()) {
                byte[] productBytes = set.getBytes("product");
                if(productBytes == null){
                    sender.sendMessage(I18N.translate("&cThis item does not match the type of the product."));
                    return;
                }
                ItemStack product = VersionSupport.byteArrayToItemStack(productBytes);

                if(hand.isSimilar(product)){
                    int amountOfProduct = hand.getAmount() / product.getAmount();
                    int itemAmount = amountOfProduct * product.getAmount();

                    int limit = SmileyPlayerTrader.getInstance().getConfig().getInt("itemStorage.productStorageLimit", -1);
                    if(limit != -1 && set.getInt("stored_product") + amountOfProduct > limit){
                        sender.sendMessage(I18N.translate("&cYou cannot store more than %0% of a product.", limit));
                        return;
                    }

                    // Add to storage
                    SmileyPlayerTrader.getInstance().getStatementHandler().run(
                            StatementHandler.StatementType.CHANGE_STORED_PRODUCT,
                            amountOfProduct, id
                    );

                    // Remove from hand
                    hand.setAmount(hand.getAmount() - itemAmount);

                    sender.sendMessage(I18N.translate("&aDeposited %0% of %1%.", itemAmount, product.getType()));
                }else{
                    sender.sendMessage(I18N.translate("&cThis item does not match the type of the product."));
                }
            }
        } catch (SQLException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
