package me.flegacy.flms.mining.registry;

import me.flegacy.flms.FLMS;
import me.flegacy.flms.utils.FLMSException;
import me.flegacy.flms.utils.Text;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisteredBlock {

    private static final String DROPS_KEY = "drops";
    private static final String TYPE_KEY = "type";
    private static final String NAME_KEY = "display_name";
    private static final String HARDNESS_KEY = "hardness";

    private final List<ItemStack> drops = new ArrayList<>();
    public final Material type;
    private String displayName;
    private float hardness;

    public RegisteredBlock(Material material) {
        type = material;
        displayName = Text.format("&f" + Text.enumToDisplayName(type.toString()));
        hardness = type.getHardness();
    }

    public void setDisplayName(String name) {
        displayName = Text.format(name);
    }

    public void setHardness(float value) {
        if (value >= 0 && value <= 255)
            hardness = value;
        else throw new FLMSException("That hardness value isn't valid. It must be between 0 and 255.");
    }

    public void addDrop(ItemStack item) {
        if (item.getItemMeta() == null)
            throw new FLMSException("I can't add a drop that has no ItemMeta.");
        for (ItemStack drop : drops)
            if (drop.isSimilar(item)) {
                drop.setAmount(drop.getAmount() + item.getAmount());
                return;
            }
        drops.add(item);
    }

    public void removeDrop(ItemStack item) {
        drops.remove(item);
    }

    public List<ItemStack> getDropsList() {
        return List.copyOf(drops);
    }

    public String getDisplayName() {
        return displayName;
    }

    public float getHardness() {
        return hardness;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> serializedBlock = new HashMap<>();
        List<Map<String, Object>> serializedItemStacks = new ArrayList<>();
        drops.forEach((item) -> {
            serializedItemStacks.add(item.serialize());
        });
        serializedBlock.put(DROPS_KEY, serializedItemStacks);
        serializedBlock.put(TYPE_KEY, type);
        serializedBlock.put(NAME_KEY, displayName);
        serializedBlock.put(HARDNESS_KEY, hardness);

        return serializedBlock;
    }

    public static RegisteredBlock deserialize(FLMS plugin, Map<String, Object> serializedBlock) {
        {
            float hardness = (float) serializedBlock.get(HARDNESS_KEY);
            String displayName = (String) serializedBlock.get(NAME_KEY);
            Material type = (Material) serializedBlock.get(TYPE_KEY);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> serializedDrops = (List<Map<String, Object>>) serializedBlock.get(DROPS_KEY);
            List<ItemStack> drops = new ArrayList<>();
            serializedDrops.forEach((map) -> {
                drops.add(ItemStack.deserialize(map));
            });

            RegisteredBlock block = new RegisteredBlock(type);
            block.setDisplayName(displayName);
            block.setHardness(hardness);
            block.drops.addAll(drops);
            // TODO exception handling or something, Jesus

            return block;
        }
    }

}
