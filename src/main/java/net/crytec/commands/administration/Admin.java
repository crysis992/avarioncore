/*
 * ************************************************************************
 *
 * AvarionCraft.de - Created at 08.12.19, 20:22	 by crysis992
 *  __________________
 *
 * [2016] - [2019] AvarionCraft.de
 * All Rights Reserved.
 * net.crytec.commands.administration.Admin can not be copied and/or distributed without the express
 *  permission of crysis992
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of AvarionCraft.de and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to AvarionCraft.de
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from AvarionCraft.de.
 *
 */

package net.crytec.commands.administration;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import net.crytec.AvarionCore;
import net.crytec.libs.commons.utils.item.ItemBuilder;
import net.crytec.util.F;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.LootContextParameters;
import net.minecraft.server.v1_15_R1.LootTableInfo;
import net.minecraft.server.v1_15_R1.TileEntity;
import net.minecraft.server.v1_15_R1.WorldServer;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

@CommandAlias("admin")
@CommandPermission("ct.developer")
public class Admin extends BaseCommand implements Listener {

  public Admin() {
    Bukkit.getPluginManager().registerEvents(this, AvarionCore.getPlugin());
  }

  @Default
  public void sendHelp(final CommandIssuer issuer, final CommandHelp help) {
    help.showHelp(issuer);

  }

  @Subcommand("conversation")
  public void conversation(final Player player) {

    final ConversationFactory factory = new ConversationFactory(AvarionCore.getPlugin());
    factory.withFirstPrompt(new TestConv());
    factory.withLocalEcho(false);
    factory.withPrefix(new ConversationPrefix() {

      @Override
      public String getPrefix(final ConversationContext context) {
        return "§2[Blub] ";
      }
    });

    player.beginConversation(factory.buildConversation(player));
  }

  @Subcommand("unloadChunks")
  public void unloadChunks(final Player player) {

    for (final Chunk chunk : player.getWorld().getLoadedChunks()) {
      if (chunk.equals(player.getChunk())) {
        continue;
      }
      chunk.unload(true);
    }


  }

  @Subcommand("font")
  public void font(final Player player, final int id) {

    final StringBuilder string = new StringBuilder();
    string.append("Folgendes Icon für ID ").append(id).append(":").append((char) id);
    player.sendMessage(string.toString());


  }

  //	@Subcommand("anvil")
//	public void anvilDebug(Player player) {
//
//		new AnvilGUI(player, "Name", (p, l) -> {
//			player.sendMessage("Dein Input: " + l);;
//			return null;
//		});
//	}

//	@EventHandler
//	public void replaceArrow(EntityShootBowEvent event) {
//		LivingEntity player = event.getEntity();
//
//		World world = ((CraftWorld) player.getWorld()).getHandle();
//		EntityItemProjectile launch = new EntityItemProjectile(world, Material.CARROT);
//		launch.setPosition(player.getEyeLocation().getX(), player.getEyeLocation().getY() - 0.25, player.getEyeLocation().getZ());
//		world.addEntity(launch);
//
//		launch.getBukkitEntity().setVelocity(event.getProjectile().getVelocity());
//
//		event.setProjectile(launch.getBukkitEntity());
//	}

//	@Subcommand("monster")
//	public void monsterDebug(Player player) {

//		World world = ((CraftWorld) player.getWorld()).getHandle();

  // EntityPowderMerchant merch = new EntityPowderMerchant(world);
  //
  // world.addEntity(merch, SpawnReason.CUSTOM);
  //
  // merch.getBukkitLivingEntity().teleport(player.getLocation());

//		 Snowball sb = (Snowball)
//		 player.getWorld().spawnEntity(player.getLocation(),
//		 EntityType.SNOWBALL);

//		 ((CraftSnowball) sb).getHandle().gra
  //
  // net.minecraft.server.v1_15_R1.Entity launch = new
  // TestProjectile(world);
  // world.addEntity(launch);
  //
  // launch.setPosition(player.getEyeLocation().getX(),
  // player.getEyeLocation().getY(), player.getEyeLocation().getZ());
  // launch.getBukkitEntity().setVelocity(player.getEyeLocation().getDirection().multiply(1.2).normalize());

//	}

  @Subcommand("modeldata")
  public void modelDataView(final Player player) {
    if (player.getInventory().getItemInMainHand() == null) {
      player.sendMessage(F.error("Du musst ein Item in der Hand halten."));
      return;
    }

    final Inventory inventory = Bukkit.createInventory(player, 54);

    for (int i = 0; i < 54; i++) {
      inventory.addItem(new ItemBuilder(player.getInventory().getItemInMainHand().clone()).lore("§fModelData: §6" + i).build());
    }

    player.openInventory(inventory);
  }

  @Subcommand("givemodel")
  public void modelDataView(final Player player, final int data) {
    if (player.getInventory().getItemInMainHand() == null) {
      player.sendMessage(F.error("Du musst ein Item in der Hand halten."));
      return;
    }

    final ItemStack item = new ItemBuilder(player.getInventory().getItemInMainHand()).setModelData(data).build();
    player.getInventory().setItemInMainHand(item);
  }
//
//	@Subcommand("hologram")
//	public void hologram(Player sender, String line) {
//
//		PhoenixHologramManager mgr = PhoenixAPI.get().getHologramManager();
//		PhoenixHologram hologram = mgr.createHologram(sender.getLocation().clone().add(0, 1, 0));
//
//		hologram.appendTextLine("§6Test Zeile");
//		hologram.appendTextLine("§6Ich bin");
//		hologram.appendTextLine("§6ein Test");
//		hologram.appendTextLine("§6THologram :D");
//
//		hologram.appendItemLine(new ItemStack(Material.ACACIA_BOAT));
//
//		hologram.getHologramLine(3).registerClickAction(player -> {
//			player.sendMessage("Blub");
//		});
//
//		hologram.setClickable();
//
//		hologram.showTo(sender);
//		hologram.setPlayerFilter(player -> player.isOp());
//
//	}

  @Subcommand("attribute")
  public void attributeTest(final Player sender) {

    final ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
    final ItemMeta meta = item.getItemMeta();

    // Angriffsgeschwindigkeit auf 2 setzen
    meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "test-speed", 2, Operation.ADD_NUMBER, EquipmentSlot.HAND));

    // Das Attribute für Schaden entfernen
    meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);

    // Schaden auf 3 setzen
    meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "test-damage", 3, Operation.ADD_NUMBER, EquipmentSlot.HAND));

    meta.setDisplayName("§2Eigenes Schwert");

    item.setItemMeta(meta);

    sender.getInventory().addItem(item);

  }

  @Subcommand("break")
  public void test(final Player sender) {

    final RayTraceResult result = sender.rayTraceBlocks(10);

    if (result == null || result.getHitBlock() == null) {
      return;
    }

    final Block block = result.getHitBlock();

    final WorldServer ws = ((CraftWorld) block.getWorld()).getHandle();
    final IBlockData data = ((CraftBlockState) block.getState()).getHandle();
    final BlockPosition pos = ((CraftBlock) block).getPosition();
    final TileEntity blockEnt = ws.getTileEntity(pos);

    final net.minecraft.server.v1_15_R1.ItemStack item = CraftItemStack.asNMSCopy(sender.getInventory().getItemInMainHand());

    final LootTableInfo.Builder builder = new LootTableInfo.Builder(ws).set(LootContextParameters.POSITION, pos).set(LootContextParameters.BLOCK_STATE, data)
        .setOptional(LootContextParameters.BLOCK_ENTITY, blockEnt).setOptional(LootContextParameters.TOOL, item);

    final List<ItemStack> stacks = data.a(builder).stream().map(original -> CraftItemStack.asBukkitCopy(original)).collect(Collectors.toList());

    stacks.forEach(i -> sender.getInventory().addItem(i));

  }

//  @Subcommand("kill")
//  public void killEntity(final Player sender) {
//
//    final RayTraceResult result = sender.getWorld().rayTrace(sender.getEyeLocation(), sender.getEyeLocation().getDirection(), 8D, FluidCollisionMode.NEVER, false, 1D,
//        e -> !e.getUniqueId().equals(sender.getUniqueId()));
//
//    if (result == null || result.getHitEntity() == null) {
//
//      sender.sendMessage("No Entity found.");
//      return;
//    }
//
//    final Entity target = result.getHitEntity();
//
//    final net.minecraft.server.v1_15_R1.Entity NMStarget = ((CraftEntity) target).getHandle();
//    final net.minecraft.server.v1_15_R1.Entity issuer = ((CraftEntity) sender).getHandle();
//
//    final WorldServer ws = ((CraftWorld) sender.getWorld()).getHandle();
//
//    final LootTableInfo.Builder builder = new LootTableInfo.Builder(ws).set(LootContextParameters.LAST_DAMAGE_PLAYER, (EntityHuman) issuer)
//        .set(LootContextParameters.DAMAGE_SOURCE, DamageSource.MAGIC).set(LootContextParameters.THIS_ENTITY, NMStarget)
//        .setOptional(LootContextParameters.DIRECT_KILLER_ENTITY, issuer).setOptional(LootContextParameters.KILLER_ENTITY, issuer)
//        .set(LootContextParameters.POSITION, new BlockPosition(NMStarget.getPositionVector()));
//
//    final LootTable table = issuer.getMinecraftServer().getLootTableRegistry().getLootTable(((EntityLiving) NMStarget).cG());
//    final List<ItemStack> drops = table.populateLoot(builder.build(LootContextParameterSets.ENTITY)).stream().map(original -> CraftItemStack.asBukkitCopy(original))
//        .collect(Collectors.toList());
//
//    drops.forEach(i -> sender.getInventory().addItem(i));
//
//  }

  @Subcommand("blockstatemeta")
  public void giveBlockStateMeta(final Player sender) {

    final ItemStack item = new ItemStack(Material.SPAWNER);

    final BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();

    final CreatureSpawner state = (CreatureSpawner) meta.getBlockState();
    state.setSpawnedType(EntityType.CAT);
    state.setSpawnRange(8);
    state.setDelay(30);

    state.getBlockData();

    meta.setBlockState(state);

    item.setItemMeta(meta);

    sender.getInventory().addItem(item);

    final ItemStack log = new ItemStack(Material.ACACIA_LOG);
    final ItemMeta logMeta = log.getItemMeta();

    final BlockData logData = Material.ACACIA_LOG.createBlockData();
    ((Orientable) logData).setAxis(Axis.X);
    ((BlockDataMeta) logMeta).setBlockData(logData);
    log.setItemMeta(logMeta);

    sender.getInventory().addItem(log);
  }

  @Subcommand("blockdatameta")
  public void giveBlockDataMeta(final Player sender) {

    final ItemStack log = new ItemStack(Material.ACACIA_LOG);
    final BlockDataMeta meta = (BlockDataMeta) log.getItemMeta();

    final Orientable data = (Orientable) Material.ACACIA_LOG.createBlockData();
    data.setAxis(Axis.X);

    meta.setBlockData(data);
    log.setItemMeta(meta);

    sender.getInventory().addItem(log);
  }

}
