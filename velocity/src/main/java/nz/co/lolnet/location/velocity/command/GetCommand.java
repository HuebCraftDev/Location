/*
 * Copyright 2018 lolnet.co.nz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nz.co.lolnet.location.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.Components;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import nz.co.lolnet.location.api.Location;
import nz.co.lolnet.location.api.data.User;
import nz.co.lolnet.location.common.command.AbstractCommand;
import nz.co.lolnet.location.velocity.VelocityPlugin;
import nz.co.lolnet.location.velocity.util.VelocityToolbox;

import java.util.List;

public class GetCommand extends AbstractCommand {
    
    public GetCommand() {
        addAlias("get");
        setPermission("location.command.get");
        setUsage("<Player>");
    }
    
    @Override
    public void execute(Object object, List<String> arguments) {
        CommandSource source = (CommandSource) object;
        if (arguments.isEmpty()) {
            source.sendMessage(VelocityToolbox.getTextPrefix().append(TextComponent.of("Invalid arguments: " + getUsage(), TextColor.RED)));
            return;
        }
        
        String username = arguments.remove(0);
        Player player = VelocityPlugin.getInstance().getProxy().getPlayer(username).orElse(null);
        if (player == null || !player.isActive()) {
            source.sendMessage(VelocityToolbox.getTextPrefix().append(TextComponent.of(username + " is not online", TextColor.RED)));
            return;
        }
        
        User user = Location.getInstance().getUser(player.getUniqueId()).orElse(null);
        if (user == null) {
            source.sendMessage(VelocityToolbox.getTextPrefix().append(TextComponent.of(player.getUsername() + " not found", TextColor.RED)));
            return;
        }
        
        TextComponent.Builder textBuilder = TextComponent.builder("");
        textBuilder.append(TextComponent.of(user.getUsername(), TextColor.BLUE).decoration(TextDecoration.BOLD, true))
                .append(TextComponent.of(" (" + user.getProtocolVersion() + ")")).append(Components.newline());
        
        textBuilder.append(TextComponent.of("Position: ", TextColor.DARK_GRAY))
                .append(TextComponent.of(user.getX() + ", " + user.getY() + ", " + user.getZ(), TextColor.WHITE)).append(Components.newline());
        
        textBuilder.append(TextComponent.of("Rotation: ", TextColor.DARK_GRAY))
                .append(TextComponent.of(user.getYaw() + ", " + user.getPitch(), TextColor.WHITE)).append(Components.newline());
        
        textBuilder.append(TextComponent.of("Server: ", TextColor.DARK_GRAY))
                .append(TextComponent.of(user.getServer() + " (" + user.getDimension() + ")", TextColor.WHITE));
        
        source.sendMessage(textBuilder.build());
    }
}