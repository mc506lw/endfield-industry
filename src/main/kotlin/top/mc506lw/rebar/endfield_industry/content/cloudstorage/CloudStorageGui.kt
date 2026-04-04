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
    
    private val playerPages = ConcurrentHashMap<UUID, Int>()
    private val playerLocationKeys = ConcurrentHashMap<UUID, String>()
    private val playerGuis = ConcurrentHashMap<UUID, Gui>()
    private val itemKeyCache = ConcurrentHashMap<String, CachedItem>()
    
    data class CachedItem(
        val stack: ItemStack,
        val displayName: Component,
        val isRebarItem: Boolean
    )
    
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
                result.add(SimpleItem(ItemStack(Material.AIR)))
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
                    playerPages[player.uniqueId] = newPage
                    playerLocationKeys[player.uniqueId] = locationKey
                    player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
                    
                    val gui = playerGuis[player.uniqueId]
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
                    playerPages[player.uniqueId] = newPage
                    playerLocationKeys[player.uniqueId] = locationKey
                    player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
                    
                    val gui = playerGuis[player.uniqueId]
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
        return playerPages.getOrDefault(playerId, 0)
    }
    
    fun setPlayerPage(playerId: UUID, page: Int) {
        playerPages[playerId] = page
    }
    
    fun setPlayerLocationKey(playerId: UUID, locationKey: String) {
        playerLocationKeys[playerId] = locationKey
    }
    
    fun setPlayerGui(playerId: UUID, gui: Gui) {
        playerGuis[playerId] = gui
    }
    
    fun clearPlayerPage(playerId: UUID) {
        playerPages.remove(playerId)
        playerLocationKeys.remove(playerId)
        playerGuis.remove(playerId)
    }
    
    fun getLocationKeyForPlayer(playerId: UUID): String? {
        return playerLocationKeys[playerId]
    }
    
    fun getGuiForPlayer(playerId: UUID): Gui? {
        return playerGuis[playerId]
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
                if (key != null) {
                    val schema = io.github.pylonmc.rebar.registry.RebarRegistry.ITEMS.get(key)
                    if (schema != null) {
                        val stack = schema.getItemStack()
                        val translationKey = "${key.namespace}.${key.key}.name"
                        val result = CachedItem(
                            stack = stack,
                            displayName = Component.translatable(translationKey),
                            isRebarItem = true
                        )
                        itemKeyCache[itemKey] = result
                        return result
                    }
                }
            } catch (e: Exception) {
                // Ignore
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
    
    class SimpleItem(private val item: ItemStack) : AbstractItem() {
        override fun getItemProvider(viewer: Player): ItemProvider {
            return ItemWrapper(item)
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {}
    }
    
    class StorageItem(
        private val locationKey: String,
        private val itemKey: String,
        private val amount: Long
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
            lore.add(Component.translatable("endfield-industry.gui.cloud_storage.left_click_to_extract_one"))
            lore.add(Component.translatable("endfield-industry.gui.cloud_storage.right_click_to_extract_stack"))
            lore.add(Component.translatable("endfield-industry.gui.cloud_storage.shift_click_to_extract_all"))
            
            return ItemStackBuilder.of(cached.stack.type)
                .name(cached.displayName)
                .lore(lore)
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {
            val extractAmount = when (clickType) {
                ClickType.LEFT -> 1L
                ClickType.RIGHT -> 64L
                ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT -> amount
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
                            
                            val leftover = player.inventory.addItem(giveStack)
                            if (leftover.isNotEmpty()) {
                                player.world.dropItem(player.location, leftover.values.first())
                            }
                            
                            player.playSound(player.location, Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f)
                            
                            val page = getPlayerPage(player.uniqueId)
                            val gui = getGuiForPlayer(player.uniqueId)
                            if (gui != null) {
                                refreshStorageView(gui, locationKey, page)
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
    
    @EventHandler(priority = EventPriority.MONITOR)
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player
        if (player is Player) {
            clearPlayerPage(player.uniqueId)
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        clearPlayerPage(event.player.uniqueId)
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    fun onPluginDisable(event: PluginDisableEvent) {
        if (event.plugin == EndfieldIndustry.instance) {
            playerPages.clear()
            playerLocationKeys.clear()
            playerGuis.clear()
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val mode = PowerGridGuiBase.getPlayerMode(player.uniqueId)
        if (mode != PowerGridDisplayMode.CLOUD_STORAGE) return
        
        val locationKey = getLocationKeyForPlayer(player.uniqueId) ?: return
        
        if (event.clickedInventory == player.inventory) {
            val clickedItem = event.currentItem
            if (clickedItem != null && clickedItem.type != Material.AIR) {
                if (event.click == ClickType.SHIFT_LEFT || event.click == ClickType.SHIFT_RIGHT) {
                    event.isCancelled = true
                    
                    Bukkit.getScheduler().runTaskAsynchronously(EndfieldIndustry.instance, Runnable {
                        val result = CloudStorage.insertItem(locationKey, clickedItem, clickedItem.amount.toLong())
                        
                        Bukkit.getScheduler().runTask(EndfieldIndustry.instance, Runnable {
                            when (result) {
                                is CloudStorage.InsertResult.SUCCESS -> {
                                    val actualAmount = result.amount.toInt()
                                    if (actualAmount >= clickedItem.amount) {
                                        event.currentItem = null
                                    } else {
                                        clickedItem.amount -= actualAmount
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
            }
        }
    }
}
