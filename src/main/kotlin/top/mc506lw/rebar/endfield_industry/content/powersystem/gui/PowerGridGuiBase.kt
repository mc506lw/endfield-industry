package top.mc506lw.rebar.endfield_industry.content.powersystem.gui

import io.github.pylonmc.rebar.block.base.RebarMultiblock
import io.github.pylonmc.rebar.i18n.RebarArgument
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.pylonmc.rebar.util.gui.GuiItems
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import top.mc506lw.rebar.endfield_industry.EndfieldIndustry
import top.mc506lw.rebar.endfield_industry.content.cloudstorage.CloudStorage
import top.mc506lw.rebar.endfield_industry.content.cloudstorage.CloudStorageGui
import top.mc506lw.rebar.endfield_industry.content.powersystem.devices.PowerDevice
import xyz.xenondevs.invui.Click
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.item.AbstractBoundItem
import xyz.xenondevs.invui.item.AbstractItem
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.ItemWrapper
import xyz.xenondevs.invui.window.Window
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque

abstract class PowerGridGuiBase(
    protected val device: PowerDevice
) {
    companion object {
        private const val CHART_ROWS = 5
        private const val CHART_COLS = 9
        private const val TOTAL_CHART_SLOTS = CHART_ROWS * CHART_COLS
        private const val HISTORY_SIZE = 9
        
        private val playerModes = ConcurrentHashMap<UUID, PowerGridDisplayMode>()
        private val playerGuiData = ConcurrentHashMap<UUID, MutableMap<String, Any>>()
        private var updateTaskId = -1
        private var listenerRegistered = false
        
        private val gridHistory = ConcurrentHashMap<UUID, ConcurrentLinkedDeque<PowerDataPoint>>()
        private val guiToBase = ConcurrentHashMap<Gui, PowerGridGuiBase>()
        
        data class PowerDataPoint(
            val timestamp: LocalDateTime,
            val totalCapacity: Int,
            val usedCapacity: Int,
            val availableCapacity: Int
        )
        
        fun isDeviceFormed(device: PowerDevice): Boolean {
            return if (device is RebarMultiblock) {
                device.isFormedAndFullyLoaded()
            } else {
                true
            }
        }
        
        fun getPlayerMode(playerId: UUID): PowerGridDisplayMode {
            return playerModes.getOrDefault(playerId, PowerGridDisplayMode.POWER_DATA)
        }
        
        fun setPlayerMode(playerId: UUID, mode: PowerGridDisplayMode) {
            playerModes[playerId] = mode
        }
        
        private fun registerPlayer(player: Player, gui: Gui, guiBase: PowerGridGuiBase) {
            playerGuiData[player.uniqueId] = mutableMapOf(
                "gui" to gui,
                "guiBase" to guiBase
            )
            ensureUpdateTaskRunning()
        }
        
        private fun unregisterPlayer(playerId: UUID) {
            playerGuiData.remove(playerId)
            playerModes.remove(playerId)
            CloudStorageGui.clearPlayerPage(playerId)
            if (playerGuiData.isEmpty()) {
                stopUpdateTask()
            }
        }
        
        private fun ensureListenerRegistered() {
            if (listenerRegistered) return
            listenerRegistered = true
            
            Bukkit.getPluginManager().registerEvents(object : Listener {
                @EventHandler(priority = EventPriority.MONITOR)
                fun onInventoryClose(event: InventoryCloseEvent) {
                    val player = event.player
                    if (player is Player && playerGuiData.containsKey(player.uniqueId)) {
                        unregisterPlayer(player.uniqueId)
                    }
                }
                
                @EventHandler(priority = EventPriority.MONITOR)
                fun onPlayerQuit(event: PlayerQuitEvent) {
                    unregisterPlayer(event.player.uniqueId)
                }
                
                @EventHandler(priority = EventPriority.MONITOR)
                fun onPluginDisable(event: PluginDisableEvent) {
                    if (event.plugin == EndfieldIndustry.instance) {
                        stopUpdateTask()
                        playerGuiData.clear()
                        playerModes.clear()
                        guiToBase.clear()
                    }
                }
            }, EndfieldIndustry.instance)
        }
        
        private fun isPowerGridGuiHolder(holder: InventoryHolder): Boolean {
            return holder is VirtualInventory || holder::class.qualifiedName?.contains("InvUI") == true
        }
        
        private fun ensureUpdateTaskRunning() {
            if (updateTaskId != -1) return
            
            updateTaskId = Bukkit.getScheduler().runTaskTimerAsynchronously(
                EndfieldIndustry.instance,
                Runnable {
                    updateAllGridHistories()
                    for ((playerId, data) in playerGuiData) {
                        try {
                            val gui = data["gui"] as? Gui ?: continue
                            val guiBase = data["guiBase"] as? PowerGridGuiBase ?: continue
                            val mode = getPlayerMode(playerId)
                            if (mode != PowerGridDisplayMode.CLOUD_STORAGE) {
                                Bukkit.getScheduler().runTask(EndfieldIndustry.instance, Runnable {
                                    guiBase.updateChartArea(gui, mode)
                                })
                            }
                        } catch (e: Exception) {
                            EndfieldIndustry.instance.logger.warning("Error updating GUI for player $playerId: ${e.message}")
                        }
                    }
                },
                20L,
                20L
            ).taskId
        }
        
        private fun updateAllGridHistories() {
            val gridManager = try {
                top.mc506lw.rebar.endfield_industry.content.powersystem.PowerGridManager.getInstance()
            } catch (e: IllegalStateException) {
                return
            }
            
            for ((gridId, grid) in gridManager.getAllGrids()) {
                val history = gridHistory.computeIfAbsent(gridId) { ConcurrentLinkedDeque() }
                val dataPoint = PowerDataPoint(
                    timestamp = LocalDateTime.now(),
                    totalCapacity = grid.totalCapacity,
                    usedCapacity = grid.usedCapacity,
                    availableCapacity = grid.availableCapacity
                )
                history.addLast(dataPoint)
                while (history.size > HISTORY_SIZE) {
                    history.removeFirst()
                }
            }
            
            val validGridIds = gridManager.getAllGrids().keys
            for (gridId in gridHistory.keys) {
                if (gridId !in validGridIds) {
                    gridHistory.remove(gridId)
                }
            }
        }
        
        private fun stopUpdateTask() {
            if (updateTaskId != -1) {
                Bukkit.getScheduler().cancelTask(updateTaskId)
                updateTaskId = -1
            }
        }
        
        internal fun initPlayerGui(player: Player, gui: Gui, guiBase: PowerGridGuiBase) {
            ensureListenerRegistered()
            playerGuiData[player.uniqueId] = mutableMapOf(
                "gui" to gui,
                "guiBase" to guiBase
            )
        }
        
        fun ensurePlayerRegistered(player: Player) {
            for ((gui, base) in guiToBase) {
                for (window in gui.windows) {
                    if (window.viewer == player) {
                        val existing = playerGuiData[player.uniqueId]
                        if (existing == null || existing["gui"] !== gui) {
                            initPlayerGui(player, gui, base)
                        }
                        return
                    }
                }
            }
        }
    }
    
    protected fun createChartItemsForMode(mode: PowerGridDisplayMode, playerId: UUID): List<AbstractItem> {
        return when (mode) {
            PowerGridDisplayMode.POWER_DATA -> createPowerChartItems()
            PowerGridDisplayMode.DEVICE_STATUS -> createDeviceDisplayItems()
            PowerGridDisplayMode.CLOUD_STORAGE -> createCloudStorageItems(playerId)
        }
    }
    
    private fun createPowerChartItems(): List<AbstractItem> {
        val grid = device.getGrid()
        val items = mutableListOf<AbstractItem>()
        
        if (grid == null) {
            val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            for (i in 0 until TOTAL_CHART_SLOTS) {
                items.add(SimpleChartItem(
                    ItemStack(Material.GRAY_STAINED_GLASS_PANE),
                    Component.text(currentTime),
                    listOf(Component.translatable("endfield-industry.gui.power_grid.not_connected"))
                ))
            }
            return items
        }
        
        val history = gridHistory.getOrDefault(grid.gridId, ConcurrentLinkedDeque())
        
        if (history.isEmpty()) {
            val now = LocalDateTime.now()
            for (i in 0 until HISTORY_SIZE) {
                val dataPoint = PowerDataPoint(
                    timestamp = now.minusSeconds((HISTORY_SIZE - 1 - i).toLong()),
                    totalCapacity = grid.totalCapacity,
                    usedCapacity = grid.usedCapacity,
                    availableCapacity = grid.availableCapacity
                )
                history.addLast(dataPoint)
            }
        }
        
        val historyList = history.toList()
        val maxCapacity = historyList.maxOfOrNull { it.totalCapacity }?.coerceAtLeast(1) ?: 1
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        
        for (row in 0 until CHART_ROWS) {
            for (col in 0 until CHART_COLS) {
                val dataIndex = col
                val dataPoint = if (dataIndex < historyList.size) historyList[dataIndex] else null
                
                if (dataPoint == null) {
                    items.add(SimpleChartItem(
                        ItemStack(Material.GRAY_STAINED_GLASS_PANE),
                        Component.translatable("endfield-industry.gui.power_grid.chart_no_data"),
                        listOf(Component.translatable("endfield-industry.gui.power_grid.chart_empty_slot"))
                    ))
                    continue
                }
                
                val timeAgo = historyList.size - 1 - dataIndex
                val timeLabel = if (timeAgo == 0) {
                    Component.translatable("endfield-industry.gui.power_grid.chart_now")
                } else {
                    Component.translatable("endfield-industry.gui.power_grid.chart_seconds_ago")
                        .arguments(RebarArgument.of("seconds", timeAgo))
                }
                
                val absoluteTime = dataPoint.timestamp.format(timeFormatter)
                val absoluteTimeComponent = Component.text(absoluteTime)
                
                val heightFromBottom = CHART_ROWS - row
                val totalCapacity = dataPoint.totalCapacity
                val usedCapacity = dataPoint.usedCapacity
                val availableCapacity = dataPoint.availableCapacity
                
                val yellowHeight = if (maxCapacity > 0 && totalCapacity > 0) {
                    ((totalCapacity.toDouble() / maxCapacity) * CHART_ROWS).toInt().coerceIn(1, CHART_ROWS)
                } else {
                    0
                }
                
                val blueHeight = if (maxCapacity > 0 && usedCapacity > 0) {
                    ((usedCapacity.toDouble() / maxCapacity) * CHART_ROWS).toInt().coerceIn(1, CHART_ROWS)
                } else {
                    0
                }
                
                val (material, lore) = when {
                    blueHeight > 0 && heightFromBottom <= blueHeight -> {
                        Pair(
                            Material.BLUE_STAINED_GLASS_PANE,
                            listOf(
                                timeLabel,
                                Component.translatable("endfield-industry.gui.power_grid.chart_consumption")
                                    .arguments(RebarArgument.of("value", usedCapacity)),
                                Component.translatable("endfield-industry.gui.power_grid.chart_total")
                                    .arguments(RebarArgument.of("value", totalCapacity)),
                                Component.translatable("endfield-industry.gui.power_grid.chart_available")
                                    .arguments(RebarArgument.of("value", availableCapacity))
                            )
                        )
                    }
                    yellowHeight > 0 && heightFromBottom <= yellowHeight -> {
                        Pair(
                            Material.YELLOW_STAINED_GLASS_PANE,
                            listOf(
                                timeLabel,
                                Component.translatable("endfield-industry.gui.power_grid.chart_capacity")
                                    .arguments(RebarArgument.of("value", totalCapacity)),
                                Component.translatable("endfield-industry.gui.power_grid.chart_available")
                                    .arguments(RebarArgument.of("value", availableCapacity))
                            )
                        )
                    }
                    else -> {
                        Pair(
                            Material.GRAY_STAINED_GLASS_PANE,
                            listOf(
                                timeLabel,
                                Component.translatable("endfield-industry.gui.power_grid.chart_empty_slot")
                            )
                        )
                    }
                }
                
                items.add(SimpleChartItem(ItemStack(material), absoluteTimeComponent, lore))
            }
        }
        
        return items
    }
    
    private fun createDeviceDisplayItems(): List<AbstractItem> {
        val grid = device.getGrid()
        val items = mutableListOf<AbstractItem>()
        
        if (grid == null) {
            for (i in 0 until TOTAL_CHART_SLOTS) {
                items.add(SimpleChartItem(ItemStack(Material.GRAY_STAINED_GLASS_PANE)))
            }
            return items
        }
        
        val cachedData = PowerGridCacheManager.getCachedDataOrCompute(grid)
        val deviceTypes = cachedData.deviceTypes
        
        val displayItems = mutableListOf<ItemStack>()
        
        for ((typeName, count) in deviceTypes) {
            var remaining = count
            while (remaining > 0) {
                val stackCount = remaining.coerceAtMost(64)
                val displayItem = createDeviceTypeDisplayItem(typeName, stackCount)
                displayItems.add(displayItem)
                remaining -= stackCount
            }
        }
        
        for (i in 0 until TOTAL_CHART_SLOTS) {
            if (i < displayItems.size) {
                items.add(DeviceDisplayItem(displayItems[i]))
            } else {
                items.add(SimpleChartItem(ItemStack(Material.GRAY_STAINED_GLASS_PANE)))
            }
        }
        
        return items
    }
    
    private fun createCloudStorageItems(playerId: UUID): List<AbstractItem> {
        val grid = device.getGrid()
        val items = mutableListOf<AbstractItem>()
        
        if (grid == null) {
            for (i in 0 until TOTAL_CHART_SLOTS) {
                items.add(SimpleChartItem(ItemStack(Material.GRAY_STAINED_GLASS_PANE)))
            }
            return items
        }
        
        val locationKey = CloudStorage.generateLocationKey(device.block.location)
        val page = CloudStorageGui.getPlayerPage(playerId)
        return CloudStorageGui.createStorageItems(locationKey, page)
    }
    
    private fun createDeviceTypeDisplayItem(typeName: String, count: Int): ItemStack {
        val (displayName, material) = when (typeName) {
            "PowerStationEmitter" -> Pair(
                Component.translatable("endfield-industry.gui.power_grid.device_power_station"),
                Material.COPPER_BLOCK
            )
            "RelayDiffuser" -> Pair(
                Component.translatable("endfield-industry.gui.power_grid.device_relay"),
                Material.LIGHTNING_ROD
            )
            "ProtocolCoreController" -> Pair(
                Component.translatable("endfield-industry.gui.power_grid.device_protocol_core"),
                Material.BEACON
            )
            "SeedExtractorController" -> Pair(
                Component.translatable("endfield-industry.gui.power_grid.device_seed_extractor"),
                Material.GRINDSTONE
            )
            "PlanterController" -> Pair(
                Component.translatable("endfield-industry.gui.power_grid.device_planter"),
                Material.COMPOSTER
            )
            else -> Pair(Component.text(typeName), Material.IRON_BLOCK)
        }
        
        return ItemStackBuilder.of(material)
            .name(displayName)
            .amount(count)
            .lore(listOf(
                Component.translatable("endfield-industry.gui.power_grid.device_count")
                    .arguments(RebarArgument.of("count", count))
            ))
            .build()
    }
    
    fun createGui(): Gui {
        ensureListenerRegistered()
        
        val grid = device.getGrid()
        if (grid != null) {
            val history = gridHistory.computeIfAbsent(grid.gridId) { ConcurrentLinkedDeque() }
            if (history.isEmpty()) {
                val now = LocalDateTime.now()
                for (i in 0 until HISTORY_SIZE) {
                    val dataPoint = PowerDataPoint(
                        timestamp = now.minusSeconds((HISTORY_SIZE - 1 - i).toLong()),
                        totalCapacity = grid.totalCapacity,
                        usedCapacity = grid.usedCapacity,
                        availableCapacity = grid.availableCapacity
                    )
                    history.addLast(dataPoint)
                }
            }
        }
        
        val gui = Gui.builder()
            .setStructure(
                ". . . . . . . . .",
                ". . . . . . . . .",
                ". . . . . . . . .",
                ". . . . . . . . .",
                ". . . . . . . . .",
                "# # p d g o b # #"
            )
            .addIngredient('#', GuiItems.background())
            .addIngredient('p', PowerDataItem(device, this))
            .addIngredient('d', DeviceStatusItem(device, this))
            .addIngredient('g', GridStatusItem(device))
            .addIngredient('o', OutputDataItem())
            .addIngredient('b', BaseStorageItem(device, this))
            .build()
        
        guiToBase[gui] = this
        
        val initialItems = createChartItemsForMode(PowerGridDisplayMode.POWER_DATA, UUID.randomUUID())
        for (i in initialItems.indices) {
            if (i < TOTAL_CHART_SLOTS) {
                gui.setItem(i, initialItems[i])
            }
        }
        
        if (grid == null) {
            Bukkit.getScheduler().runTaskLater(EndfieldIndustry.instance, Runnable {
                val newGrid = device.getGrid()
                if (newGrid != null) {
                    val newItems = createChartItemsForMode(PowerGridDisplayMode.POWER_DATA, UUID.randomUUID())
                    for (i in newItems.indices) {
                        if (i < TOTAL_CHART_SLOTS) {
                            gui.setItem(i, newItems[i])
                        }
                    }
                }
            }, 5L)
        }
        
        return gui
    }
    
    fun updateChartArea(gui: Gui, mode: PowerGridDisplayMode, playerId: UUID? = null) {
        val chartItems = createChartItemsForMode(mode, playerId ?: UUID.randomUUID())
        for (i in chartItems.indices) {
            if (i < TOTAL_CHART_SLOTS) {
                gui.setItem(i, chartItems[i])
            }
        }
    }
    
    fun updateControlBar(gui: Gui, mode: PowerGridDisplayMode, playerId: UUID) {
        when (mode) {
            PowerGridDisplayMode.CLOUD_STORAGE -> {
                val grid = device.getGrid()
                if (grid != null) {
                    val locationKey = CloudStorage.generateLocationKey(device.block.location)
                    val page = CloudStorageGui.getPlayerPage(playerId)
                    gui.setItem(45, CloudStorageGui.createPrevPageItem(locationKey, page))
                    gui.setItem(49, CloudStorageGui.createInfoItem(locationKey, page))
                    gui.setItem(53, CloudStorageGui.createNextPageItem(locationKey, page))
                }
            }
            else -> {
                gui.setItem(45, GuiItems.background())
                gui.setItem(49, GuiItems.background())
                gui.setItem(53, GuiItems.background())
            }
        }
    }
    
    class SimpleChartItem(
        private val item: ItemStack,
        private val customName: Component? = null,
        private val customLore: List<Component>? = null
    ) : AbstractItem() {
        override fun getItemProvider(viewer: Player): ItemProvider {
            val builder = ItemStackBuilder.of(item.type)
            if (customName != null) {
                builder.name(customName)
            }
            if (customLore != null) {
                builder.lore(customLore)
            }
            return ItemWrapper(builder.build())
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {}
    }
    
    class PowerDataItem(
        private val device: PowerDevice,
        private val guiBase: PowerGridGuiBase
    ) : AbstractBoundItem() {
        override fun getItemProvider(viewer: Player): ItemProvider {
            ensurePlayerRegistered(viewer)
            
            val grid = device.getGrid()
            return if (grid != null) {
                val cachedData = PowerGridCacheManager.getCachedDataOrCompute(grid)
                val statusKey = if (cachedData.isOverloaded) 
                    "endfield-industry.gui.power_grid.status_overloaded" 
                else 
                    "endfield-industry.gui.power_grid.status_normal"
                
                ItemStackBuilder.of(Material.GREEN_STAINED_GLASS_PANE)
                    .name(Component.translatable("endfield-industry.gui.power_grid.power_data"))
                    .lore(listOf(
                        Component.translatable("endfield-industry.gui.power_grid.status_display")
                            .arguments(RebarArgument.of("status", Component.translatable(statusKey))),
                        Component.translatable("endfield-industry.gui.power_grid.total_capacity")
                            .arguments(RebarArgument.of("value", cachedData.totalCapacity)),
                        Component.translatable("endfield-industry.gui.power_grid.used_capacity")
                            .arguments(RebarArgument.of("value", cachedData.usedCapacity)),
                        Component.translatable("endfield-industry.gui.power_grid.available_capacity")
                            .arguments(RebarArgument.of("value", cachedData.availableCapacity)),
                        Component.empty(),
                        Component.translatable("endfield-industry.gui.power_grid.click_to_view_chart")
                    ))
            } else {
                ItemStackBuilder.of(Material.GRAY_STAINED_GLASS_PANE)
                    .name(Component.translatable("endfield-industry.gui.power_grid.power_data"))
                    .lore(listOf(
                        Component.translatable("endfield-industry.gui.power_grid.not_connected")
                    ))
            }
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {
            setPlayerMode(player.uniqueId, PowerGridDisplayMode.POWER_DATA)
            initPlayerGui(player, getGui()!!, guiBase)
            val gui = getGui()
            if (gui != null) {
                guiBase.updateChartArea(gui, PowerGridDisplayMode.POWER_DATA, player.uniqueId)
                guiBase.updateControlBar(gui, PowerGridDisplayMode.POWER_DATA, player.uniqueId)
            }
        }
    }
    
    class DeviceStatusItem(
        private val device: PowerDevice,
        private val guiBase: PowerGridGuiBase
    ) : AbstractBoundItem() {
        override fun getItemProvider(viewer: Player): ItemProvider {
            val grid = device.getGrid()
            return if (grid != null) {
                val cachedData = PowerGridCacheManager.getCachedDataOrCompute(grid)
                ItemStackBuilder.of(Material.ORANGE_STAINED_GLASS_PANE)
                    .name(Component.translatable("endfield-industry.gui.power_grid.device_status"))
                    .lore(listOf(
                        Component.translatable("endfield-industry.gui.power_grid.supply_devices")
                            .arguments(RebarArgument.of("count", cachedData.deviceCount)),
                        Component.translatable("endfield-industry.gui.power_grid.consumer_devices")
                            .arguments(RebarArgument.of("count", cachedData.consumerCount)),
                        Component.empty(),
                        Component.translatable("endfield-industry.gui.power_grid.click_to_view_devices")
                    ))
            } else {
                ItemStackBuilder.of(Material.GRAY_STAINED_GLASS_PANE)
                    .name(Component.translatable("endfield-industry.gui.power_grid.device_status"))
                    .lore(listOf(
                        Component.translatable("endfield-industry.gui.power_grid.no_devices")
                    ))
            }
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {
            setPlayerMode(player.uniqueId, PowerGridDisplayMode.DEVICE_STATUS)
            initPlayerGui(player, getGui()!!, guiBase)
            val gui = getGui()
            if (gui != null) {
                guiBase.updateChartArea(gui, PowerGridDisplayMode.DEVICE_STATUS, player.uniqueId)
                guiBase.updateControlBar(gui, PowerGridDisplayMode.DEVICE_STATUS, player.uniqueId)
            }
        }
    }
    
    class GridStatusItem(private val device: PowerDevice) : AbstractItem() {
        override fun getItemProvider(viewer: Player): ItemProvider {
            val grid = device.getGrid()
            return if (grid != null) {
                val isFormed = isDeviceFormed(device)
                val statusKey = if (isFormed) 
                    "endfield-industry.gui.power_grid.structure_complete" 
                else 
                    "endfield-industry.gui.power_grid.structure_incomplete"
                
                ItemStackBuilder.of(Material.REDSTONE_BLOCK)
                    .name(Component.translatable("endfield-industry.gui.power_grid.grid_status"))
                    .lore(listOf(
                        Component.translatable("endfield-industry.gui.power_grid.grid_id")
                            .arguments(RebarArgument.of("id", grid.gridId.toString().substring(0, 8))),
                        Component.translatable("endfield-industry.gui.power_grid.structure_status")
                            .arguments(RebarArgument.of("status", Component.translatable(statusKey)))
                    ))
            } else {
                ItemStackBuilder.of(Material.REDSTONE_BLOCK)
                    .name(Component.translatable("endfield-industry.gui.power_grid.grid_status"))
                    .lore(listOf(
                        Component.translatable("endfield-industry.gui.power_grid.grid_id_not_connected")
                    ))
            }
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {
        }
    }
    
    class OutputDataItem : AbstractItem() {
        override fun getItemProvider(viewer: Player): ItemProvider {
            return ItemStackBuilder.of(Material.LIME_STAINED_GLASS_PANE)
                .name(Component.translatable("endfield-industry.gui.power_grid.output_data"))
                .lore(listOf(
                    Component.translatable("endfield-industry.gui.power_grid.coming_soon")
                ))
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {
        }
    }
    
    class BaseStorageItem(
        private val device: PowerDevice,
        private val guiBase: PowerGridGuiBase
    ) : AbstractBoundItem() {
        override fun getItemProvider(viewer: Player): ItemProvider {
            val grid = device.getGrid()
            return if (grid != null) {
                val locationKey = CloudStorage.generateLocationKey(device.block.location)
                val info = CloudStorage.getStorageInfo(locationKey)
                ItemStackBuilder.of(Material.PURPLE_STAINED_GLASS_PANE)
                    .name(Component.translatable("endfield-industry.gui.power_grid.base_storage"))
                    .lore(listOf(
                        Component.translatable("endfield-industry.gui.cloud_storage.total_items")
                            .arguments(RebarArgument.of("value", info.totalItems)),
                        Component.translatable("endfield-industry.gui.cloud_storage.total_amount")
                            .arguments(RebarArgument.of("value", info.totalAmount)),
                        Component.empty(),
                        Component.translatable("endfield-industry.gui.power_grid.click_to_view_storage")
                    ))
            } else {
                ItemStackBuilder.of(Material.GRAY_STAINED_GLASS_PANE)
                    .name(Component.translatable("endfield-industry.gui.power_grid.base_storage"))
                    .lore(listOf(
                        Component.translatable("endfield-industry.gui.power_grid.not_connected")
                    ))
            }
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {
            val grid = device.getGrid()
            if (grid != null) {
                val locationKey = CloudStorage.generateLocationKey(device.block.location)
                PowerGridGuiBase.setPlayerMode(player.uniqueId, PowerGridDisplayMode.CLOUD_STORAGE)
                CloudStorageGui.setPlayerPage(player.uniqueId, 0)
                CloudStorageGui.setPlayerLocationKey(player.uniqueId, locationKey)
                val gui = getGui()
                if (gui != null) {
                    CloudStorageGui.setPlayerGui(player.uniqueId, gui)
                }
                PowerGridGuiBase.initPlayerGui(player, getGui()!!, guiBase)
                val gui2 = getGui()
                if (gui2 != null) {
                    guiBase.updateChartArea(gui2, PowerGridDisplayMode.CLOUD_STORAGE, player.uniqueId)
                    guiBase.updateControlBar(gui2, PowerGridDisplayMode.CLOUD_STORAGE, player.uniqueId)
                }
            }
        }
    }
    
    class DeviceDisplayItem(private val displayItem: ItemStack) : AbstractItem() {
        override fun getItemProvider(viewer: Player): ItemProvider {
            return ItemWrapper(displayItem)
        }

        override fun handleClick(clickType: ClickType, player: Player, click: Click) {}
    }
}
