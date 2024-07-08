//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//
//public class pADDING {
//    public static byte[] padMessage(String message, int blockSize) {
//        byte[] messageBytes = message.getBytes(StandardCharsets.US_ASCII);
//        int messageLength = messageBytes.length * 8; // number of bits
//
//        // Calculate the number of bits needed for padding
//        int paddingBits = (-messageLength - 65) % blockSize;
//
//        // Calculate the number of blocks in the padded message
//        int numBlocks = (messageLength + paddingBits + 65) / blockSize;
//
//        // Calculate the length of the padded message in bytes
//        int paddedLength = numBlocks * blockSize / 8;
//
//        // Create the padded message array
//        byte[] paddedMessage = new byte[paddedLength];
//
//        // Append '1' bit to the message
//        paddedMessage[0] = (byte) 0x80;
//
//        // Copy the message bytes to the padded message array
//        System.arraycopy(messageBytes, 0, paddedMessage, 1, messageBytes.length);
//
//        // Append '0' bits
//        Arrays.fill(paddedMessage, messageBytes.length + 1, paddedLength - 8, (byte) 0);
//
//        // Append length representation
//        long lengthValue = (long) numBlocks;
//        for (int i = 7; i >= 0; i--) {
//            paddedMessage[paddedLength - 8 + i] = (byte) ((lengthValue >> (8 * i)) & 0xFF);
//        }
//
//        return paddedMessage;
//    }
//
//
//
//    public static void main(String[] args) {
//        System.out.println(Arrays.toString(padMessage("abc",512)));
//    }
//}
