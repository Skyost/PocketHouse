package fr.skyost.pockethouse.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.BlockPopulator;

public class EmptyGenerator extends ChunkGenerator {
	
	@Override
	public List<BlockPopulator> getDefaultPopulators(final World world) {
		return new ArrayList<BlockPopulator>();
	}
	
	@Override
	public boolean canSpawn(final World world, final int x, final int z) {
		return true;
	}
	
	@Override
	public final byte[][] generateBlockSections(final World world, final Random random, final int chunkx, final int chunkz, final BiomeGrid biomes) {
		return new byte[world.getMaxHeight() / 16][];
	}
	
}