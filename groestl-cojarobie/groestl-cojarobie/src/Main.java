import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

    }

    public static int CycleP=1;
    public static int CycleQ=1;
    public static final int initialVector = 0x00000000000000000000000000000010;
    public static int[][] sBox = {
            {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
            {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
            {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
            {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
            {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
            {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
            {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
            {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
            {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
            {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
            {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
            {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
            {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
            {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
            {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
            {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}
    };



    public static int[][] permutationP(){
        int[][] matrixP = {
                {0x00, 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };

        return matrixP;
    }

    public static int[][] permutationQ(){
        int[][] matrixQ = {
                {0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF},
                {0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF},
                {0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF},
                {0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF},
                {0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF},
                {0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF},
                {0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF},
                {0xFF, 0xEF, 0xDF, 0xCF, 0xBF, 0xAF, 0x9F, 0x8F}
        };
        return matrixQ;
    }



    public static int[][] mapMessageToMatrixA(byte[] message, int blockSize) {
        byte[] paddedMessage = padMessage(message, blockSize);
        // Initialize matrix A
        int[][] matrixA = new int[8][8];

        int messageIndex = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                matrixA[j][i] = (int)paddedMessage[messageIndex];
                messageIndex++;
            }
        }

        return matrixA;
    }

    public static byte[] padMessage(byte[] message, int blockSize) {
        byte[] messageBytes = message;
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



    public static int[][] matrixB(){
        return new int[][] {
                {0x02, 0x02, 0x03, 0x04, 0x05, 0x03, 0x05, 0x07},
                {0x07, 0x02, 0x02, 0x03, 0x04, 0x05, 0x03, 0x05},
                {0x05, 0x07, 0x02, 0x02, 0x03, 0x04, 0x05, 0x03},
                {0x03, 0x05, 0x07, 0x02, 0x02, 0x03, 0x04, 0x05},
                {0x05, 0x03, 0x05, 0x07, 0x02, 0x02, 0x03, 0x04},
                {0x04, 0x05, 0x03, 0x05, 0x07, 0x02, 0x02, 0x03},
                {0x03, 0x04, 0x05, 0x03, 0x05, 0x07, 0x02, 0x02},
                {0x02, 0x03, 0x04, 0x05, 0x03, 0x05, 0x07, 0x02}
        };
    }

    public static int[][] roundConstantsP(int[][] matrixA){
        for (int j = 0; j < matrixA[0].length; j++) {
            matrixA[0][j] ^= CycleP;
        }
        CycleP++;
        return matrixA;
    }
    public static int[][] roundConstantsQ(int[][] matrixQ){
        for (int j = 0; j < matrixQ[0].length; j++) {
            matrixQ[0][j] ^= CycleQ;
        }
        CycleQ++;
        return matrixQ;
    }

    public static int[][] subBytes(int[][] matrix){
        for(int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                int row = matrix[i][j] & 0x0f;
                int column = matrix[i][j] & 0xf0;
                matrix[i][j]=sBox[row][column];
            }
        }
        return matrix;
    }

    public static int[][] shiftLeftP(int[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < i; j++) {
                int firstElement = matrix[i][0];
                System.arraycopy(matrix, i, matrix, 0, matrix.length - 1);
                matrix[i][matrix.length - 1] = firstElement;
            }
        }
        return matrix;
    }
    public static int[][] shiftLeftQ(int[][] matrix){
        int[] permutationVector = {1,3,5,7,0,2,4,6};
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < permutationVector[i]; j++) {
                int firstElement = matrix[i][0];
                System.arraycopy(matrix, i, matrix, 0, matrix.length - 1);
                matrix[i][matrix.length - 1] = firstElement;
            }
        }
        return matrix;
    }

    public static byte[][] mixBytes( byte[][] msg, int columns )  //mixBytes zaczerpnięte z języka C i przepisane na Jave
    {
        byte temp[] = new byte[8];
        for( int i = 0; i < columns; i++ )
        {
            for (int j = 0; j < 8; j++)
            {
                temp[j] = ( byte )(mul2(msg[(j + 0) % 8][i]) ^ mul2(msg[(j + 1) % 8][i]) ^
                        mul3(msg[(j + 2) % 8][i]) ^ mul4(msg[(j + 3) % 8][i]) ^
                        mul5(msg[(j + 4) % 8][i]) ^ mul3(msg[(j + 5) % 8][i]) ^
                        mul5(msg[(j + 6) % 8][i]) ^ mul7(msg[(j + 7) % 8][i]));
            }
            for( int j = 0; j < 8; j++ ) {
                msg[j][i] = temp[j];
            }
        }
        return msg;
    }
    public static byte mul1( byte b ) { return b ;}
    public static byte mul2( byte b ) { return ( byte )((0 != (b>>>7))?((b)<<1)^0x1b:((b)<<1)); }
    public static byte mul3( byte b ) { return ( byte )(mul2(b) ^ mul1(b)); }
    public static byte mul4( byte b ) { return ( byte )(mul2( mul2( b ))); }
    public static byte mul5( byte b ) { return ( byte )(mul4(b) ^ mul1(b)); }
    public static byte mul6( byte b ) { return ( byte )(mul4(b) ^ mul2(b)); }
    public static byte mul7( byte b ) { return ( byte )(mul4(b) ^ mul2(b) ^ mul1(b)); }

    public static byte[] PermutationP(String message, int blocksize){
        byte[] xoredinput = new byte[]{};
        byte[] messageinput = message.getBytes();
        byte[] iv = ByteBuffer.allocate(4).putInt(initialVector).array();

        for (int i = 0; i < messageinput.length; i++) {
            xoredinput[i] = (byte) (messageinput[i] ^ iv[i]);
        }
        byte[][] matrixAA = new byte[][]{};


        int [][] matrixA=shiftLeftP(subBytes(roundConstantsP(mapMessageToMatrixA(xoredinput,512))));
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA[i].length; j++) {
                matrixAA[i][j] = (byte) matrixA[i][j];
            }
        }
        mixBytes(matrixAA,8);

        mapMatrixToByteArray(matrixAA,blocksize);


        return xoredinput;
    }

    public static byte[] PermutationQ(String message, int blocksize){
        byte[] essa2 = new byte[]{};
        byte[] messageinput = message.getBytes();
        byte[][] matrixAA = new byte[][]{};

        int [][] matrixA=shiftLeftP(subBytes(roundConstantsP(mapMessageToMatrixA(messageinput,512))));
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA[i].length; j++) {
                matrixAA[i][j] = (byte) matrixA[i][j];
            }
        }
        mixBytes(matrixAA,8);

        essa2 =mapMatrixToByteArray(matrixAA,blocksize);


        return essa2;
    }

    public static byte[] functionF(String message){

        byte[] messaged = padMessage(message.getBytes(),512);
        for (int i = 0; i < messaged.length/64; i++) {
            PermutationQ(message,512);
            PermutationP(message,512);
        }


        return messaged;
    }

    public static byte[] mapMatrixToByteArray(byte[][] matrix, int blocksize){
        byte[] message = new byte[]{};
        int messageIndex = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                message[messageIndex]=matrix[j][i] ;
                messageIndex++;
            }
        }
        return message;
    }
}