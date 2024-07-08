import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class testgowna {

    public static byte[][] mapMessageToMatrixA(String message, int blockSize) {
        byte[] paddedMessage = padMessage(message, blockSize);
        // Initialize matrix A
        byte[][] matrixA = new byte[8][8];

        int messageIndex = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                matrixA[j][i] = paddedMessage[messageIndex];
                messageIndex++;
            }
        }

        return matrixA;
    }

    public static byte[] padMessage(String message, int blockSize) {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        int messageLength = messageBytes.length * 8; // Convert message length to bits

        // Calculate the number of bits needed for padding
        int paddingBits = (-messageLength - 65) % blockSize;
        if (paddingBits < 0) {
            paddingBits += blockSize;
        }

        // Calculate the number of blocks in the padded message
        int numBlocks = (messageLength + paddingBits + 65) / blockSize;

        // Calculate the length of the padded message in bytes
        int paddedLength = numBlocks * blockSize / 8;

        // Create the padded message array
        byte[] paddedMessage = new byte[paddedLength];

        // Copy the message bytes to the padded message array
        System.arraycopy(messageBytes, 0, paddedMessage, 0, messageBytes.length);

        // Append '1' bit to the message
        paddedMessage[messageBytes.length] = (byte) 0b10000000;

        // Append '0' bits
        for (int i = messageBytes.length + 1; i < paddedLength - 8; i++) {
            paddedMessage[i] = 0;
        }

        // Append length representation
        long lengthValue = numBlocks;
        for (int i = 7; i >= 0; i--) {
            paddedMessage[paddedLength - 8 + i] = (byte) ((lengthValue >> (8 * i)) & 0xFF);
        }

        return paddedMessage;
    }

    public static void main(String[] args) {
        String message = "Hello, world!";
        int blockSize = 512; // Block size in bits

        System.out.println(Arrays.toString(padMessage(message,blockSize)));

//        String paddedMessage = padMessage(message, blockSize);
//        System.out.println("Padded Message: " + paddedMessage);

        byte[][] matrixA = mapMessageToMatrixA(message, blockSize);

        // Wyświetlenie macierzy A
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA[i].length; j++) {
                System.out.print(matrixA[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println(".........................");


    }
}
