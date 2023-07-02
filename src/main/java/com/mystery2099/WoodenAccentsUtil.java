package com.mystery2099;

import com.mystery2099.datagen.BlockTagDataGen;

public class WoodenAccentsUtil {
    private WoodenAccentsUtil() {}
    private static BlockTagDataGen blockTagGen;

    public static BlockTagDataGen getBlockTagGen() {
        return blockTagGen;
    }

    public static void setBlockTagGen(BlockTagDataGen blockTagGen) {
        WoodenAccentsUtil.blockTagGen = blockTagGen;
    }
}
