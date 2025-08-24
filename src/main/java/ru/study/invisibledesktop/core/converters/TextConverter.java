package ru.study.invisibledesktop.core.converters;

public interface TextConverter {

    default String formatID(long id) {
        // Define the expanded emoji range across different categories
        int emojiStart = 0x2600; // Start of broad emoji range (including weather, symbols, etc.)
        int emojiRangeSize = 0x26FF - emojiStart; // Range size from start to end of emoji block

        // Split the long into four 16-bit parts
        int[] parts = new int[4];
        parts[0] = (int) (id & 0xFFFF);           // Lowest 16 bits
        parts[1] = (int) ((id >> 16) & 0xFFFF);   // Next 16 bits
        parts[2] = (int) ((id >> 32) & 0xFFFF);   // Next 16 bits
        parts[3] = (int) ((id >> 48) & 0xFFFF);   // Highest 16 bits

        // Convert each part to an emoji within the expanded emoji range
        StringBuilder emojis = new StringBuilder();
        for (int part : parts) {
            int emojiCodePoint = emojiStart + (part % emojiRangeSize); // Map part to an emoji within range
            emojis.append(new String(Character.toChars(emojiCodePoint)));
        }

        return emojis.toString();
    }

}
