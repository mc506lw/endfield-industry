package top.mc506lw.rebar.endfield_industry.content.cloudstorage

import io.github.pylonmc.rebar.i18n.RebarArgument
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.inventory.ItemStack
import top.mc506lw.rebar.endfield_industry.EndfieldIndustry
import top.mc506lw.rebar.endfield_industry.content.powersystem.gui.PowerGridDisplayMode
import top.mc506lw.rebar.endfield_industry.content.powersystem.gui.PowerGridGuiBase
import xyz.xenondevs.invui.Click
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.item.AbstractItem
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.ItemWrapper
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object CloudStorageGui : Listener {
    
    private val logger = EndfieldIndustry.instance.logger
    
    private val itemKeyCache = ConcurrentHashMap<String, CachedItem>()
    private val actionCooldowns = ConcurrentHashMap<UUID, Long>()
    private const val COOLDOWN_MS = 200L
    
    data class CachedItem(
        val stack: ItemStack,
        val displayName: Component,
        val isRebarItem: Boolean
    )
    
    private fun isOnCooldown(playerId: UUID): Boolean {
        val lastAction = actionCooldowns[playerId] ?: 0L
        return System.currentTimeMillis() - lastAction < COOLDOWN_MS
    }
    
    private fun setCooldown(playerId: UUID) {
        actionCooldowns[playerId] = System.currentTimeMillis()
    }
    
    fun initialize() {
        logger.info("[CloudStorageGui] 初始化GUI系统")
        Bukkit.getPluginManager().registerEvents(this, EndfieldIndustry.instance)
        logger.info("[CloudStorageGui] GUI系统初始化完成")
    }
    
    fun createStorageItems(locationKey: String, page: Int): List<AbstractItem> {
        val items = CloudStorage.getItemsPage(locationKey, page)
        val result = mutableListOf<AbstractItem>()
        
        for (i in 0 until CloudStorage.ITEMS_PER_PAGE) {
            if (i < items.size) {
                val (itemKey, amount) = items[i]
                result.add(StorageItem(locationKey, itemKey, amount))
            } else {
                result.add(EmptyStorageItem(locationKey))
            }
        }
        
        return result
    }
    
    fun createPrevPageItem(locationKey: String, currentPage: Int): AbstractItem {
        val canPrev = currentPage > 0
        val material = if (canPrev) Material.ARROW else Material.GRAY_STAINED_GLASS_PANE
        
        return object : AbstractItem() {
            override fun getItemProvider(viewer: Player): ItemProvider {
                return ItemStackBuilder.of(material)
                    .name(Component.translatable("endfield-industry.gui.cloud_storage.prev_page"))
                    .lore(listOf(
                        if (canPrev) Component.translatable("endfield-industry.gui.cloud_storage.click_to_prev")
                        else Component.translatable("endfield-industry.gui.cloud_storage.first_page")
                    ))
            }

            override fun handleClick(clickType: ClickType, player: Player, click: Click) {
                if (canPrev) {
                    val newPage = currentPage - 1
                    setPlayerPage(player.uniqueId, newPage)
                    setPlayerLocationKey(player.uniqueId, locationKey)
                    player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
                    
                    val gui = getGuiForPlayer(player.uniqueId)
                    if (gui != null) {
                        refreshStorageView(gui, locationKey, newPage)
                    }
                }
            }
        }
    }
    
    fun createNextPageItem(locationKey: String, currentPage: Int): AbstractItem {
        val totalPages = maxOf(1, CloudStorage.getTotalPages(locationKey))
        val canNext = currentPage < totalPages - 1
        val material = if (canNext) Material.ARROW else Material.GRAY_STAINED_GLASS_PANE
        
        return object : AbstractItem() {
            override fun getItemProvider(viewer: Player): ItemProvider {
                return ItemStackBuilder.of(material)
                    .name(Component.translatable("endfield-industry.gui.cloud_storage.next_page"))
                    .lore(listOf(
                        if (canNext) Component.translatable("endfield-industry.gui.cloud_storage.click_to_next")
                        else Component.translatable("endfield-industry.gui.cloud_storage.last_page")
                    ))
            }

            override fun handleClick(clickType: ClickType, player: Player, click: Click) {
                if (canNext) {
                    val newPage = currentPage + 1
                    setPlayerPage(player.uniqueId, newPage)
                    setPlayerLocationKey(player.uniqueId, locationKey)
                    player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
                    
                    val gui = getGuiForPlayer(player.uniqueId)
                    if (gui != null) {
                        refreshStorageView(gui, locationKey, newPage)
                    }
                }
            }
        }
    }
    
    fun createInfoItem(locationKey: String, currentPage: Int): AbstractItem {
        val totalPages = maxOf(1, CloudStorage.getTotalPages(locationKey))
        val info = CloudStorage.getStorageInfo(locationKey)
        
        return object : AbstractItem() {
            override fun getItemProvider(viewer: Player): ItemProvider {
                return ItemStackBuilder.of(Material.PURPLE_STAINED_GLASS_PANE)
                    .name(Component.translatable("endfield-industry.gui.cloud_storage.info"))
                    .lore(listOf(
                        Component.translatable("endfield-industry.gui.cloud_storage.page_info")
                            .arguments(RebarArgument.of("current", currentPage + 1), RebarArgument.of("total", totalPages)),
                        Component.empty(),
                        Component.translatable("endfield-industry.gui.cloud_storage.total_items")
                            .arguments(RebarArgument.of("value", info.totalItems)),
                        Component.translatable("endfield-industry.gui.cloud_storage.total_amount")
                            .arguments(RebarArgument.of("value", info.totalAmount)),
                        Component.translatable("endfield-industry.gui.cloud_storage.max_capacity")
                            .arguments(RebarArgument.of("value", info.maxCapacity)),
                        Component.empty(),
                        Component.translatable("endfield-industry.gui.cloud_storage.shift_to_deposit")
                    ))
            }

            override fun handleClick(clickType: ClickType, player: Player, click: Click) {}
        }
    }
    
    fun refreshStorageView(gui: Gui, locationKey: String, page: Int) {
        val storageItems = createStorageItems(locationKey, page)
        for (i in storageItems.indices) {
            if (i < CloudStorage.ITEMS_PER_PAGE) {
                gui.setItem(i, storageItems[i])
            }
        }
        
        gui.setItem(45, createPrevPageItem(locationKey, page))
        gui.setItem(49, createInfoItem(locationKey, page))
        gui.setItem(53, createNextPageItem(locationKey, page))
    }
    
    fun getPlayerPage(playerId: UUID): Int {
        return top.mc506lw.rebar.endfield_industry.content.powersystem.gui.PlayerGuiSessionManager.getPage(playerId)
    }
    
    fun setPlayerPage(playerId: UUID, page: Int) {
        top.mc506lw.rebar.endfield_industry.content.powersystem.gui.PlayerGuiSessionManager.updatePage(playerId, page)
    }
    
    fun setPlayerLocationKey(playerId: UUID, locationKey: String) {
        top.mc506lw.rebar.endfield_industry.content.powersystem.gui.PlayerGuiSessionManager.updateLocationKey(playerId, locationKey)
    }
    
    fun setPlayerGui(playerId: UUID, gui: Gui) {
        val session = top.mc506lw.rebar.endfield_industry.content.powersystem.gui.PlayerGuiSessionManager.getSession(playerId)
        if (session != null) {
            session.gui = gui
        }
    }
    
    fun clearPlayerPage(playerId: UUID) {
        // 不再清除，由 PlayerGuiSessionManager 统一管理
    }
    
    fun getLocationKeyForPlayer(playerId: UUID): String? {
        return top.mc506lw.rebar.endfield_industry.content.powersystem.gui.PlayerGuiSessionManager.getLocationKey(playerId)
    }
    
    fun getGuiForPlayer(playerId: UUID): Gui? {
        return top.mc506lw.rebar.endfield_industry.content.powersystem.gui.PlayerGuiSessionManager.getGui(playerId)
    }
    
    private fun resolveItemFromKey(itemKey: String): CachedItem {
        val cached = itemKeyCache[itemKey]
        if (cached != null) {
            return cached
        }
        
        if (itemKey.startsWith("minecraft:")) {
            val materialName = itemKey.substring("minecraft:".length).split(":")[0]
            try {
                val material = Material.valueOf(materialName)
                val stack = ItemStack(material)
                val result = CachedItem(
                    stack = stack,
                    displayName = stack.displayName(),
                    isRebarItem = false
                )
                itemKeyCache[itemKey] = result
                return result
            } catch (e: IllegalArgumentException) {
                val fallback = ItemStack(Material.STONE)
                val result = CachedItem(
                    stack = fallback,
                    displayName = fallback.displayName(),
                    isRebarItem = false
                )
                itemKeyCache[itemKey] = result
                return result
            }
        }
        
        if (itemKey.startsWith("rebar:")) {
            val keyStr = itemKey.substring("rebar:".length)
            try {
                val key = org.bukkit.NamespacedKey.fromString(keyStr)
                if (key == null) {
                    logger.warning("[CloudStorageGui] 无法解析NamespacedKey: $keyStr")
                } else {
                    val schema = io.github.pylonmc.rebar.registry.RebarRegistry.ITEMS.get(key)
                    if (schema == null) {
                        logger.warning("[CloudStorageGui] 物品不在注册表中: $key")
                    } else {
                        val stack = schema.getItemStack()
                        val result = CachedItem(
                            stack = stack,
                            displayName = stack.displayName(),
                            isRebarItem = true
                        )
                        itemKeyCache[itemKey] = result
                        return result
                    }
                }
            } catch (e: Exception) {
                logger.warning("[CloudStorageGui] 解析Rebar物品异常: ${e.message}")
            }
        }
        
        val fallback = ItemStack(Material.STONE)
        val result = CachedItem(
            stack = fallback,
            displayName = fallback.displayName(),
            isRebarItem = false
        )
        itemKeyCache[itemKey] = result
        return result
    }
    
    private fun formatAmount(amount: Long): String {
        return when {
            amount >= 1_000_000_000 -> String.format("%.2fB", amount / 1_000_000_000.0)
            amount >= 1_000_000 -> String.format("%.2fM", amount / 1_000_000.0)
            amount >= 1_000 -> String.format("%.2fK", amount / 1_000.0)
            else -> amount.toString()
        }
    }
    
    class EmptyStorageItem(private val locationKey: String) : AbstractItem() {
        override fun getItemProvider(viewer: Player): ItemProvider {
            return ItemWrapper(ItemStack(Material.AIR))
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {
            if (isOnCooldown(player.uniqueId)) return
            
            val cursorItem = player.itemOnCursor
            val hasCursorItem = cursorItem != null && cursorItem.type != Material.AIR
            
            if (hasCursorItem && (clickType == ClickType.LEFT || clickType == ClickType.RIGHT)) {
                setCooldown(player.uniqueId)
                val insertAmount = if (clickType == ClickType.LEFT) cursorItem.amount.toLong() else 1L
                handleInsertItem(player, locationKey, cursorItem, insertAmount, isCursorItem = true)
            }
        }
    }
    
    class StorageItem(
        internal val locationKey: String,
        internal val itemKey: String,
        internal val amount: Long
    ) : AbstractItem() {
        override fun getItemProvider(viewer: Player): ItemProvider {
            val cached = resolveItemFromKey(itemKey)
            val isOverLimit = CloudStorage.isOverLimit(locationKey, itemKey)
            
            val lore = mutableListOf<Component>()
            lore.add(Component.translatable("endfield-industry.gui.cloud_storage.amount")
                .arguments(RebarArgument.of("value", formatAmount(amount))))
            
            if (isOverLimit) {
                lore.add(Component.empty())
                lore.add(Component.translatable("endfield-industry.gui.cloud_storage.over_limit"))
            }
            
            lore.add(Component.empty())
            lore.add(Component.translatable("endfield-industry.gui.cloud_storage.left_click_to_extract_stack"))
            lore.add(Component.translatable("endfield-industry.gui.cloud_storage.right_click_to_extract_one"))
            lore.add(Component.translatable("endfield-industry.gui.cloud_storage.shift_click_to_extract_all"))
            
            if (cached.isRebarItem) {
                val displayStack = cached.stack.clone()
                val meta = displayStack.itemMeta
                if (meta != null) {
                    meta.lore(lore)
                    displayStack.itemMeta = meta
                }
                return ItemWrapper(displayStack)
            } else {
                return ItemStackBuilder.of(cached.stack)
                    .name(cached.displayName)
                    .lore(lore)
            }
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {
            if (isOnCooldown(player.uniqueId)) return
            
            val cursorItem = player.itemOnCursor
            val hasCursorItem = cursorItem != null && cursorItem.type != Material.AIR
            
            when (clickType) {
                ClickType.LEFT, ClickType.RIGHT -> {
                    if (hasCursorItem) {
                        setCooldown(player.uniqueId)
                        val insertAmount = if (clickType == ClickType.LEFT) cursorItem.amount.toLong() else 1L
                        handleInsertItem(player, locationKey, cursorItem, insertAmount, isCursorItem = true)
                    } else {
                        setCooldown(player.uniqueId)
                        handleExtractToCursor(player, locationKey, itemKey, amount, clickType)
                    }
                }
                ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT -> {
                    if (!hasCursorItem) {
                        setCooldown(player.uniqueId)
                        handleExtractToInventory(player, locationKey, itemKey, amount)
                    }
                }
                else -> {}
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    fun onInventoryClose(event: InventoryCloseEvent) {
        // 不再清除，由 PlayerGuiSessionManager 统一管理
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        // 不再清除，由 PowerGridGuiBase 的 PlayerQuitEvent 处理
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    fun onPluginDisable(event: PluginDisableEvent) {
        if (event.plugin == EndfieldIndustry.instance) {
            itemKeyCache.clear()
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        
        val playerGui = getGuiForPlayer(player.uniqueId) ?: return
        
        val isPlayerViewingCloudStorage = playerGui.windows.any { it.viewer == player }
        if (!isPlayerViewingCloudStorage) return
        
        val locationKey = getLocationKeyForPlayer(player.uniqueId) ?: return
        
        val topInventory = event.view.topInventory
        val topInventorySize = topInventory.size
        val rawSlot = event.rawSlot
        
        val isPlayerInventoryClick = rawSlot >= topInventorySize
        
        if (!isPlayerInventoryClick) return
        
        val clickedItem = event.currentItem
        if (clickedItem == null || clickedItem.type == Material.AIR) return
        
        val isShiftClick = event.click == ClickType.SHIFT_LEFT || event.click == ClickType.SHIFT_RIGHT
        val isDropKey = event.click == ClickType.DROP || event.click == ClickType.CONTROL_DROP
        
        if (isShiftClick || isDropKey) {
            event.isCancelled = true
            
            val amountToInsert = when (event.click) {
                ClickType.CONTROL_DROP -> clickedItem.amount.toLong()
                else -> clickedItem.amount.toLong()
            }
            
            handleInsertItem(player, locationKey, clickedItem, amountToInsert)
        }
    }
    
    private fun handleInsertItem(player: Player, locationKey: String, item: ItemStack, amount: Long, isCursorItem: Boolean = false) {
        Bukkit.getScheduler().runTaskAsynchronously(EndfieldIndustry.instance, Runnable {
            val result = CloudStorage.insertItem(locationKey, item, amount)
            
            Bukkit.getScheduler().runTask(EndfieldIndustry.instance, Runnable {
                when (result) {
                    is CloudStorage.InsertResult.SUCCESS -> {
                        val actualAmount = result.amount.toInt()
                        
                        if (isCursorItem) {
                            if (actualAmount >= item.amount) {
                                player.setItemOnCursor(null)
                            } else {
                                val newAmount = item.amount - actualAmount
                                item.amount = newAmount
                                player.setItemOnCursor(item)
                            }
                        } else {
                            if (actualAmount >= item.amount) {
                                item.amount = 0
                            } else {
                                item.amount -= actualAmount
                            }
                        }
                        
                        player.playSound(player.location, Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f)
                        
                        val page = getPlayerPage(player.uniqueId)
                        val gui = getGuiForPlayer(player.uniqueId)
                        if (gui != null) {
                            refreshStorageView(gui, locationKey, page)
                        }
                    }
                    is CloudStorage.InsertResult.NOT_IN_WHITELIST -> {
                        player.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.not_in_whitelist"))
                        player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
                    }
                    is CloudStorage.InsertResult.CAPACITY_FULL -> {
                        player.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.capacity_full"))
                        player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
                    }
                    is CloudStorage.InsertResult.OVER_LIMIT -> {
                        player.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.over_limit"))
                        player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
                    }
                }
            })
        })
    }
    
    private fun handleExtractToCursor(player: Player, locationKey: String, itemKey: String, currentAmount: Long, clickType: ClickType) {
        val extractAmount = when (clickType) {
            ClickType.LEFT -> minOf(currentAmount, 64L)
            ClickType.RIGHT -> 1L
            else -> return
        }
        
        Bukkit.getScheduler().runTaskAsynchronously(EndfieldIndustry.instance, Runnable {
            val result = CloudStorage.extractItem(locationKey, itemKey, extractAmount)
            
            Bukkit.getScheduler().runTask(EndfieldIndustry.instance, Runnable {
                when (result) {
                    is CloudStorage.ExtractResult.SUCCESS -> {
                        val cached = resolveItemFromKey(itemKey)
                        val giveAmount = minOf(result.amount, Int.MAX_VALUE.toLong()).toInt()
                        val giveStack = cached.stack.clone()
                        giveStack.amount = giveAmount
                        
                        if (player.itemOnCursor == null || player.itemOnCursor!!.type == Material.AIR) {
                            player.setItemOnCursor(giveStack)
                        } else {
                            val leftover = player.inventory.addItem(giveStack)
                            if (leftover.isNotEmpty()) {
                                player.world.dropItem(player.location, leftover.values.first())
                            }
                        }
                        
                        player.playSound(player.location, Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f)
                        
                        val page = getPlayerPage(player.uniqueId)
                        val refreshGui = getGuiForPlayer(player.uniqueId)
                        if (refreshGui != null) {
                            refreshStorageView(refreshGui, locationKey, page)
                        }
                    }
                    is CloudStorage.ExtractResult.STORAGE_NOT_FOUND -> {
                        player.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.storage_not_found"))
                    }
                    is CloudStorage.ExtractResult.ITEM_NOT_FOUND -> {
                        player.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.item_not_found"))
                    }
                }
            })
        })
    }
    
    private fun handleExtractToInventory(player: Player, locationKey: String, itemKey: String, currentAmount: Long) {
        Bukkit.getScheduler().runTaskAsynchronously(EndfieldIndustry.instance, Runnable {
            val result = CloudStorage.extractItem(locationKey, itemKey, currentAmount)
            
            Bukkit.getScheduler().runTask(EndfieldIndustry.instance, Runnable {
                when (result) {
                    is CloudStorage.ExtractResult.SUCCESS -> {
                        val cached = resolveItemFromKey(itemKey)
                        var remaining = minOf(result.amount, Int.MAX_VALUE.toLong()).toInt()
                        
                        while (remaining > 0) {
                            val giveStack = cached.stack.clone()
                            giveStack.amount = minOf(remaining, giveStack.maxStackSize)
                            remaining -= giveStack.amount
                            
                            val leftover = player.inventory.addItem(giveStack)
                            if (leftover.isNotEmpty()) {
                                for (dropItem in leftover.values) {
                                    player.world.dropItem(player.location, dropItem)
                                }
                                break
                            }
                        }
                        
                        player.playSound(player.location, Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f)
                        
                        val page = getPlayerPage(player.uniqueId)
                        val refreshGui = getGuiForPlayer(player.uniqueId)
                        if (refreshGui != null) {
                            refreshStorageView(refreshGui, locationKey, page)
                        }
                    }
                    is CloudStorage.ExtractResult.STORAGE_NOT_FOUND -> {
                        player.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.storage_not_found"))
                    }
                    is CloudStorage.ExtractResult.ITEM_NOT_FOUND -> {
                        player.sendMessage(Component.translatable("endfield-industry.gui.cloud_storage.item_not_found"))
                    }
                }
            })
        })
    }
}
