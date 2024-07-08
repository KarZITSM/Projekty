import java.nio.charset.StandardCharsets;

public class testshit {
    private static final int BLOCK_SIZE = 64; // Rozmiar bloku w bajtach

    public static byte[][] mapMessageToMatrixA(String message) {
        byte[][] matrixA = new byte[8][8];

        int messageIndex = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //matrixA[j][i] = (byte) message.substring(messageIndex,messageIndex+8);
                messageIndex=messageIndex+8;
            }
        }

        return matrixA;
    }


    public static String padMessage(String message, int blockSize) {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        int messageLength = messageBytes.length * 8; // Convert message length to bits

        // Calculate the number of bits needed for padding
        int paddingBits = (-messageLength - 65) % blockSize;
        if (paddingBits < 0) {
            paddingBits += blockSize;
        }

        // Calculate the number of blocks in the padded message
        int numBlocks = (messageLength + paddingBits + 65) / blockSize;

        // Convert the message to binary representation
        StringBuilder binaryMessage = new StringBuilder();
        for (byte b : messageBytes) {
            String binaryByte = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            binaryMessage.append(binaryByte);
        }

        // Append '1' bit to the message
        binaryMessage.append("1");

        // Append '0' bits
        int numZeroBits = paddingBits - 1;
        for (int i = 0; i < numZeroBits; i++) {
            binaryMessage.append("0");
        }

        // Append length representation
        String lengthRepresentation = String.format("%64s", Long.toBinaryString(numBlocks)).replace(' ', '0');
        binaryMessage.append(lengthRepresentation);

        return binaryMessage.toString();
    }

    public static void main(String[] args) {
        String message = "Hello, world!";
        int blockSize = 512; // Block size in bits

        String paddedMessage = padMessage(message, blockSize);
        System.out.println("Padded Message: " + paddedMessage);

        byte[][] matrixA = mapMessageToMatrixA(paddedMessage);

        // Wyświetlenie macierzy A
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA[i].length; j++) {
                System.out.print(matrixA[i][j] + " ");
            }
            System.out.println();
        }


    }
}

