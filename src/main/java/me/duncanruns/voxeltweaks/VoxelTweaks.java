package me.duncanruns.voxeltweaks;

import com.mamiyaotaru.voxelmap.VoxelConstants;
import com.mamiyaotaru.voxelmap.util.DimensionContainer;
import com.mamiyaotaru.voxelmap.util.Waypoint;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VoxelTweaks implements ModInitializer {
    public static final String MOD_ID = "voxel-tweaks";

    private static final Pattern XYZ_PATTERN = Pattern.compile("^.*?(-?\\d+(?:\\.?\\d+)?)[^\\d\\.]+?(-?\\d+(?:\\.?\\d+)?)[^\\d\\.]+?(-?\\d+(?:\\.?\\d+)?).*$");
    private static final Pattern XZ_PATTERN = Pattern.compile("^.*?(-?\\d+(?:\\.?\\d+)?)[^\\d\\.]+?(-?\\d+(?:\\.?\\d+)?).*$");

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static int[] getClipboardCoords() {
        String paste = MinecraftClient.getInstance().keyboard.getClipboard();
        return getCoords(paste);
    }

    public static int[] getCoords(String input) {
        boolean hasY;
        Matcher matcher = XYZ_PATTERN.matcher(input);
        if (!(hasY = matcher.matches())) {
            matcher = XZ_PATTERN.matcher(input);
            if (!matcher.matches()) return null;
        }
        int i = 1;
        return new int[]{
                MathHelper.floor(Double.parseDouble(matcher.group(i++))),
                hasY ? MathHelper.floor(Double.parseDouble(matcher.group(i++))) : Integer.MIN_VALUE,
                MathHelper.floor(Double.parseDouble(matcher.group(i)))
        };
    }

    @Override
    public void onInitialize() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("vhighlight")
                    .executes(c -> executeHighlightCommand(c, null))
                    .then(ClientCommandManager.argument("input", StringArgumentType.greedyString())
                            .executes(c -> executeHighlightCommand(c, StringArgumentType.getString(c, "input"))))
            );
            dispatcher.register(ClientCommandManager.literal("vwaypoint")
                    .then(ClientCommandManager.argument("name", StringArgumentType.word())
                            .executes(c -> executeWaypointCommand(c, null))
                            .then(ClientCommandManager.argument("input", StringArgumentType.greedyString())
                                    .executes(c -> executeWaypointCommand(c, StringArgumentType.getString(c, "input"))))));
        });
    }

    private static int executeHighlightCommand(CommandContext<FabricClientCommandSource> context, String input) {
        if(input == null) input = context.getSource().getPosition().toString();
        int[] coords = getCoords(input);
        if (coords == null) {
            context.getSource().sendError(Text.literal("Invalid input!"));
            return 0;
        }

        coords[1] = coords[1] == Integer.MIN_VALUE ? (int) context.getSource().getPosition().y : coords[1];

        ClientWorld world = context.getSource().getWorld();
        double coordinateScale = world.getDimension().coordinateScale();
        DimensionContainer dimension = VoxelConstants.getVoxelMapInstance().getDimensionManager().getDimensionContainerByWorld(world);
        VoxelConstants.getVoxelMapInstance().getWaypointManager().setHighlightedWaypoint(new Waypoint(
                        "temp highlight",
                        MathHelper.floor(coords[0] * coordinateScale),
                        MathHelper.floor(coords[2] * coordinateScale),
                        coords[1],
                        false,
                        0, 0, 0, "",
                        dimension.getStorageName(),
                        new TreeSet<>(Collections.singletonList(dimension))
                ),
                true);
        return 1;
    }

    private static int executeWaypointCommand(CommandContext<FabricClientCommandSource> context, String input) {
        if(input == null) input = context.getSource().getPosition().toString();
        int[] coords = getCoords(input);
        if (coords == null) {
            context.getSource().sendError(Text.literal("Invalid input!"));
            return 0;
        }

        coords[1] = coords[1] == Integer.MIN_VALUE ? (int) context.getSource().getPosition().y : coords[1];

        ClientWorld world = context.getSource().getWorld();
        double coordinateScale = world.getDimension().coordinateScale();
        DimensionContainer dimension = VoxelConstants.getVoxelMapInstance().getDimensionManager().getDimensionContainerByWorld(world);
        VoxelConstants.getVoxelMapInstance().getWaypointManager().addWaypoint(new Waypoint(
                StringArgumentType.getString(context, "name"),
                MathHelper.floor(coords[0] * coordinateScale),
                MathHelper.floor(coords[2] * coordinateScale),
                coords[1],
                true,
                1, 1, 1,
                "",
                dimension.getStorageName(),
                new TreeSet<>(Collections.singletonList(dimension))
        ));
        return 1;
    }
}