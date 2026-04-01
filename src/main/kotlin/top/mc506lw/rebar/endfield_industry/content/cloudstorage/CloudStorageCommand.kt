package top.mc506lw.rebar.endfield_industry.content.cloudstorage

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.block.Container
import org.bukkit.entity.Player
import top.mc506lw.rebar.endfield_industry.EndfieldIndustry

object CloudStorageCommand {
    
    fun register() {
        val command = Commands.literal("ei")
            .requires { it.sender.hasPermission("endfield-industry.admin") }
            
            .then(Commands.literal("whitelist")
                .then(Commands.literal("add")
                    .executes(this::handleWhitelistAdd)
                )
                .then(Commands.literal("list")
                    .executes(this::handleWhitelistList)
                )
                .then(Commands.literal("import")
                    .executes(this::handleWhitelistImport)
                )
            )
            .then(Commands.literal("storage")
                .then(Commands.literal("info")
                    .then(Commands.argument("gridId", StringArgumentType.word())
                        .executes(this::handleStorageInfo)
                    )
                )
            )
            .then(Commands.literal("reload")
                .executes(this::handleReload)
            )
            .build()
        
        EndfieldIndustry.instance.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { commands ->
            commands.registrar().register(command)
        }
    }
    
    private fun handleWhitelistAdd(context: CommandContext<CommandSourceStack>): Int {
        val sender = context.source.sender
        if (sender !is Player) {
            sender.sendMessage(Component.text("只有玩家可以使用此命令"))
            return Command.SINGLE_SUCCESS
        }
        
        val itemInHand = sender.inventory.itemInMainHand
        if (itemInHand.type == Material.AIR) {
            sender.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.need_item_in_hand"))
            return Command.SINGLE_SUCCESS
        }
        
        val itemKey = CloudStorage.generateItemKey(itemInHand)
        if (CloudStorage.addToWhitelist(itemKey)) {
            sender.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.whitelist_added")
                .arguments(Component.text(itemInHand.type.name)))
        } else {
            sender.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.whitelist_already_exists"))
        }
        
        return Command.SINGLE_SUCCESS
    }
    
    private fun handleWhitelistList(context: CommandContext<CommandSourceStack>): Int {
        val sender = context.source.sender
        val whitelist = CloudStorage.getWhitelist()
        
        if (whitelist.isEmpty()) {
            sender.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.whitelist_empty"))
        } else {
            sender.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.whitelist_list")
                .arguments(Component.text(whitelist.size.toString())))
            whitelist.take(10).forEach { itemKey ->
                sender.sendMessage(Component.text(" - $itemKey"))
            }
            if (whitelist.size > 10) {
                sender.sendMessage(Component.text(" ... 还有 ${whitelist.size - 10} 项"))
            }
        }
        
        return Command.SINGLE_SUCCESS
    }
    
    private fun handleWhitelistImport(context: CommandContext<CommandSourceStack>): Int {
        val sender = context.source.sender
        if (sender !is Player) {
            sender.sendMessage(Component.text("只有玩家可以使用此命令"))
            return Command.SINGLE_SUCCESS
        }
        
        val targetBlock = sender.getTargetBlockExact(5)
        if (targetBlock == null || targetBlock.state !is Container) {
            sender.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.need_target_container"))
            return Command.SINGLE_SUCCESS
        }
        
        val container = targetBlock.state as Container
        val items = container.inventory.contents.filterNotNull()
        
        var count = 0
        for (item in items) {
            val itemKey = CloudStorage.generateItemKey(item)
            if (CloudStorage.addToWhitelist(itemKey)) {
                count++
            }
        }
        
        sender.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.whitelist_imported")
            .arguments(Component.text(count.toString())))
        
        return Command.SINGLE_SUCCESS
    }
    
    private fun handleStorageInfo(context: CommandContext<CommandSourceStack>): Int {
        val sender = context.source.sender
        val gridIdStr = StringArgumentType.getString(context, "gridId")
        val gridId = try {
            java.util.UUID.fromString(gridIdStr)
        } catch (e: IllegalArgumentException) {
            sender.sendMessage(Component.text("无效的电网ID格式"))
            return Command.SINGLE_SUCCESS
        }
        
        val info = CloudStorage.getStorageInfo(gridId)
        sender.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.storage_info_header")
            .arguments(Component.text(gridIdStr)))
        sender.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.total_items")
            .arguments(Component.text(info.totalItems.toString())))
        sender.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.total_amount")
            .arguments(Component.text(info.totalAmount.toString())))
        sender.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.max_capacity")
            .arguments(Component.text(info.maxCapacity.toString())))
        
        return Command.SINGLE_SUCCESS
    }
    
    private fun handleReload(context: CommandContext<CommandSourceStack>): Int {
        val sender = context.source.sender
        CloudStorage.reloadConfig()
        sender.sendMessage(Component.text("配置已重新加载"))
        return Command.SINGLE_SUCCESS
    }
}
