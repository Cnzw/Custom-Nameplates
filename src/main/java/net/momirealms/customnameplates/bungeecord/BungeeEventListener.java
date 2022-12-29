/*
 *  Copyright (C) <2022> <XiaoMoMi>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.momirealms.customnameplates.bungeecord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.neznamy.tab.shared.TAB;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.momirealms.customnameplates.objects.team.TABTeamHook;

import java.util.Objects;

public class BungeeEventListener implements Listener {

    private final Main plugin;

    public BungeeEventListener (Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onReceived(PluginMessageEvent event) {
        String channel = event.getTag();
        if (event.isCancelled() || !Objects.equals("customnameplates:cnp", channel)) {
            return;
        }
        ByteArrayDataInput dataInput = ByteStreams.newDataInput(event.getData());
        parseMessage(dataInput);
    }

    private void parseMessage(ByteArrayDataInput dataInput) {
        String playerName = dataInput.readUTF();
        String teamName = playerName;
        if (plugin.getBungeeConfig().isTab()) {
            teamName = TAB.getInstance().getPlayer(playerName).getTeamName();
            teamName = teamName == null ? playerName : teamName;
        }
        ProxiedPlayer proxiedPlayer = plugin.getProxy().getPlayer(playerName);
        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
        byteArrayDataOutput.writeUTF(playerName);
        byteArrayDataOutput.writeUTF(teamName);
        proxiedPlayer.getServer().sendData("customnameplates:cnp", byteArrayDataOutput.toByteArray());
    }
}