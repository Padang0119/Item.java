package test2.test2;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.text.SimpleDateFormat;
import java.util.*;
import java.text.DecimalFormat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class Test2 extends JavaPlugin implements Listener, CommandExecutor {
    @Override
    public void onEnable() {
        getLogger().info("플러그인이 활성화되었습니다.");
        getServer().getPluginManager().registerEvents(this, this);

        // TabListUpdater 클래스를 1초마다 실행
        TabListUpdater tabListUpdater = new TabListUpdater();
        Bukkit.getScheduler().runTaskTimer(this, tabListUpdater, 0L, 20L);
    }

    @Override
    public void onDisable() {
        getLogger().info("플러그인이 비활성화되었습니다.");
    }

    public static class TabListUpdater implements Runnable {
        private final DecimalFormat decimalFormat = new DecimalFormat("#.#");
        @Override
        public void run() {
            int playerCount = Bukkit.getOnlinePlayers().size();
            int maxPlayers = Bukkit.getMaxPlayers();
            double usedMemory = getUsedMemoryInMB();
            double maxMemory = getMaxMemoryInMB();
            double[] tps = getRecentTPS();
            String time = getTimeString();

            for (Player player : Bukkit.getOnlinePlayers()) {
                String[] headerAndFooterAndList = getHeaderAndFooterAndList(player, playerCount, maxPlayers, usedMemory, maxMemory, tps, time);
                String header = headerAndFooterAndList[0];
                String footer = headerAndFooterAndList[1];
                String list = headerAndFooterAndList[2];
                player.setPlayerListHeader(header);
                player.setPlayerListFooter(footer);
                player.setPlayerListName(list);
            }
        }
        private String[] getHeaderAndFooterAndList(Player player, int playerCount, int maxPlayers, double usedMemory, double maxMemory, double[] tps, String time) {
            StringBuilder header = new StringBuilder();
            StringBuilder footer = new StringBuilder();
            StringBuilder list = new StringBuilder();

            // 첫 번째 줄 (서버 이름, 온라인 플레이어 수 / 최대 플레이어 수)
            header.append("§8[§c❤§§r§8] §6✪ §eMOKALIN's Server §6✪ §8[§c❤§§r§8]\n");
            header.append("§e현재 접속자: §f").append(playerCount).append(" / ").append(maxPlayers);
            header.append("  §8|  ");
            int ping = player.getPing();
            header.append("§e서버 핑: §f").append(getPingColor(ping)).append(ping).append("ms\n");
            header.append("§8[§f§l§m          §b§l서버 §f§l접속 §b§l리스트§f§l§m          §r§8]\n");

            // 두 번째 줄 (TPS, 메모리 사용량, 시간)
            footer.append("\n");
            footer.append("§eTPS: §f").append(getTPSColor(tps[0])).append(decimalFormat.format(tps[0])).append("§r");
            footer.append("  §f").append(getTPSColor(tps[1])).append(decimalFormat.format(tps[1])).append("§r");
            footer.append("  §f").append(getTPSColor(tps[2])).append(decimalFormat.format(tps[2])).append("§r\n");
            footer.append("§e서버 메모리: §f").append(getMemoryColor(usedMemory, maxMemory)).append((int) usedMemory).append("MB §8/ §r").append((int) maxMemory).append("MB\n");
            footer.append("§e현재 시간: §f").append(time);

            // 세 번째 줄 (플레이어 체력)
            int playerHealth = (int) Math.ceil(player.getHealth());
            String healthColor = getHealthColor(playerHealth);
            int length = player.getName().length();
            int max_length = 12;
            int blank = max_length - length;
            list.append(player.getName());
            for (int i = 0;i < blank;i++) {
                list.append(" ");
            }
            list.append(healthColor).append(playerHealth).append(" ");

            String[] result = new String[3];
            result[0] = header.toString();
            result[1] = footer.toString();
            result[2] = list.toString();
            return result;
        }
        private String getHealthColor(int health) {
            if (health >= 18) {
                return "§a";
            } else if (health >= 16) {
                return "§2";
            } else if (health >= 14) {
                return "§e";
            } else if (health >= 12) {
                return "§6";
            } else if (health >= 8) {
                return "§c";
            } else if (health >= 4) {
                return "§4";
            } else {
                return "§0";
            }
        }
        private String getPingColor(int ping) {
            if (ping <= 50) {
                return "§a";
            } else if (ping <= 100) {
                return "§e";
            } else {
                return "§c";
            }
        }
        private String getTPSColor(double tps) {
            if (tps >= 19.0) {
                return "§a";
            } else if (tps >= 16.0) {
                return "§e";
            } else {
                return "§c";
            }
        }
        private String getMemoryColor(double usedMemory, double maxMemory) {
            double percentage = usedMemory / maxMemory * 100;
            if (percentage <= 60) {
                return "§a";
            } else if (percentage <= 80) {
                return "§e";
            } else {
                return "§c";
            }
        }
        private double[] getRecentTPS() {
            return Bukkit.getTPS();
        }
        private double getMaxMemoryInMB() {
            return Runtime.getRuntime().maxMemory() / 1048576.0;
        }
        private double getUsedMemoryInMB() {
            return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576.0;
        }
        private String getTimeString() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            return dateFormat.format(date);
        }
    }

    @EventHandler
    public void a1123(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        Location location = p.getLocation().add(0, 1, 0);
        if (p.isDead()) {
            p.spawnParticle(Particle.BLOCK_CRACK, location, 1500, 0, 0.7, 0, 0.5, Material.REDSTONE_BLOCK.createBlockData());
        }
    }
}
