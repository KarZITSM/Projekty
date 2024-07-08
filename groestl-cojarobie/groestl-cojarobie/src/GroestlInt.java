import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GroestlInt {
    public static void main(String[] args) {
        System.out.println(groestl("abc",512));

        int[][] array = {
                {0xef, 0xca, 0xb7, 0x04, 0x09, 0x53, 0xd0, 0x51},
                {0x63, 0x63, 0x63, 0x63, 0x63, 0x63, 0x63, 0xaa},
                {0x63, 0x63, 0x63, 0x63, 0x63, 0x63, 0xfb, 0x63},
                {0x63, 0x63, 0x63, 0x63, 0x63, 0xcd, 0x63, 0x63},
                {0x63, 0x63, 0x63, 0x63, 0x63, 0x63, 0x63, 0x63},
                {0x63, 0x63, 0x63, 0x63, 0x63, 0x63, 0x63, 0x63},
                {0x63, 0x7c, 0x63, 0x63, 0x63, 0x63, 0x63, 0x63},
                {0x7c, 0x63, 0x63, 0x63, 0x63, 0x63, 0x63, 0x63}
        };

        int[][] byteArray = {
                {0x61, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
                {0x62, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
                {0x63, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
                {0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
                {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
                {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
                {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01},
                {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01}
        };
        System.out.println(mixBytes(array,8));
        System.out.println(Arrays.deepToString(roundConstantsP(byteArray)));
        //System.out.println(Arrays.deepToString(subBytes(roundConstantsP(byteArray))));
        //System.out.println(Arrays.deepToString(shiftLeftP(subBytes(roundConstantsP(byteArray)))));
        //System.out.println(Arrays.deepToString(mixBytes(subBytes(roundConstantsP(byteArray)),8)));


        System.out.println(Arrays.toString(padMessage("abc",512)));

//        System.out.println(groestl("uwu",512));
//        System.out.println(groestl("kurwa",512));
//        System.out.println(groestl("uwu1",512));
//        System.out.println(groestl("uwu2",512));
//        System.out.println(groestl("uwu3dfnbierwgouiwnvjnwrhobwtuobnaerhabvuhstrbhuwtbhntwbntrbnthvndfjnvijajnvioernvhuerbgjsgnbonfnvkjaerhbstrhbntrbnivihaernvjnerbjrtibtrbjihbvswhibvsirnbijwnbiqergwjrbvherwbvuttwny",512));
//        System.out.println(groestl("uwu3dfnbierwgouiwnvjnwrhodbwtuobnaerhabvuhstrbhuwtbhntwbntrbnthvndfjnvijajnvioernvhuerbgjsgnbonfnvkjaerhbstrhbntrbnivihaernvjnerbjrtibtrbjihbvswhibvsirnbijwnbiqergwjrbvherwbvuttwny",512));
//        System.out.println(groestl("uwu3dfnbierwgouiwnvjnwrhodbwtuobnaerhabvuhstrbhuwtbhntwbntrbnthvndfjnvijajnvioernvhuerbgjsgnbonfnvkjaerhbstrhbntrbnivihaernvjnerbjrtibtrbjihbvswhibvsirnbijwnbiqergwjrbvherwbvuttwnys",512));

        System.out.println(groestl("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq",512));
//        byte[][] msg = {{1,2,3,4,5,6,7,8},{1,2,3,4,5,6,7,8},{1,2,3,4,5,6,7,8},{1,2,3,4,5,6,7,8},{1,2,3,4,5,6,7,8},{1,2,3,4,5,6,7,8},{1,2,3,4,5,6,7,8},{1,2,3,4,5,6,7,8}};
//        System.out.println(Arrays.deepToString(mixBytes(msg,8)));
//        System.out.println(Arrays.deepToString(shiftLeftQ(msg)));
//        System.out.println(Arrays.deepToString(shiftLeftP(msg)));
//        System.out.println(Arrays.deepToString(subBytes(msg)));
//        System.out.println(Arrays.deepToString(roundConstantsP(msg)));
//        System.out.println(Arrays.deepToString(roundConstantsQ(msg)));

//        for (int i = 0; i < 100000; i++) {
//            String essa = new String();
//            essa = generateRandomString(1);
//        }
    }

    //public static final int initialVector = 0x00000000000000000000000000000010;
    public static final int[] initialVector = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x10,0x00};
    public static int CycleP=0;
    public static int CycleQ=0;
    public static int[] Hi;
    public static int init = 0;
    public static int[][] sBox = {
            { 0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76 },
            { 0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0 },
            { 0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15 },
            { 0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75 },
            { 0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84 },
            { 0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf },
            { 0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8 },
            { 0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2 },
            { 0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73 },
            { 0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb },
            { 0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79 },
            { 0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08 },
            { 0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a },
            { 0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e },
            { 0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf },
            { 0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16 }
    };




    public static String groestl(String message, int blocksize){
        for (int i = 0; i < 10; i++) {
            functionH(divideMessage(padMessage(message,blocksize),blocksize), blocksize);
        }


        return intArrToHex(functionOmega(Hi));
    }

    public static String byteArrayToHexString(byte[] byteArray) {
        BigInteger bigInteger = new BigInteger(1, byteArray);
        String hexString = bigInteger.toString(16);

        // Pad the hex string with leading zeros if needed
        int paddingLength = (byteArray.length * 2) - hexString.length();
        if (paddingLength > 0) {
            hexString = String.format("%0" + paddingLength + "d", 0) + hexString;
        }

        return hexString;
    }


public static String byteToUnsignedHex(int i) {
    String hex = Integer.toHexString(i);

    return hex;
}

    public static String intArrToHex(int[] arr) {
        StringBuilder builder = new StringBuilder(arr.length);
        for (int b : arr) {
            builder.append(byteToUnsignedHex(b));
        }
        return builder.toString();
    }



    public static void functionH(ArrayList<int[]> message, int blocksize){

        //byte[] output = new byte[64];
        if(init==0){
            Hi =functionF(message.get(0),initialVector,blocksize);
            init++;
        }
        else Hi =functionF(message.get(0),Hi,blocksize);
        for (int i = 0; i < message.size() - 1; i++) {
            Hi = functionF(message.get(i+1), Hi, blocksize);
        }
    }

    public static int[] functionOmega (int[] message){
        int[] outputofP = PermutationP(message,512);
        for (int i = 0; i < message.length; i++) {
            outputofP[i] =  (message[i] ^ outputofP[i]);
        }

        return Arrays.copyOfRange(outputofP,0,outputofP.length/2);
    }
    public static int[] functionF (int[] message,int[] iv, int blocksize){
        int[] hi = new int[64];

        for (int i = 0; i < message.length; i++) {
            hi[i] =  (message[i] ^ iv[i]);
        }

        message =PermutationQ(message, blocksize);
        hi = PermutationP(hi, blocksize);

        for (int i = 0; i < message.length; i++) {
            hi[i] =  (message[i] ^ iv[i] ^ hi[i]);
        }

        return hi;
    }

    public static int[] PermutationP(int[] message, int blocksize){


        int [][] matrixA=mixBytes(shiftLeftP(subBytes(roundConstantsP(mapMessageToMatrixA(message,512)))),8);


        return mapMatrixToByteArray(matrixA,blocksize);
    }

    public static int[] PermutationQ(int[] message, int blocksize){


        int [][] matrixA=mixBytes(shiftLeftQ(subBytes(roundConstantsQ(mapMessageToMatrixA(message,512)))),8);


        return mapMatrixToByteArray(matrixA,blocksize);
    }

    public static ArrayList<int[]> divideMessage(int[] byteArray, int subarraySize) {
        ArrayList<int[]> dividedArray = new ArrayList<>();

        int totalSubarrays = ((byteArray.length)/ subarraySize) +1;

        for (int i = 0; i < totalSubarrays; i++) {
            int startIndex = i * 64;
            int endIndex = (i+1) * 64 ;

            int[] subarray = new int[64];

            subarray=Arrays.copyOfRange(byteArray,startIndex,endIndex);

            dividedArray.add(subarray);
        }

        return dividedArray;
    }

    public static int[][] mapMessageToMatrixA(int[] message, int blockSize) {
        // Initialize matrix A
        int[][] matrixA = new int[8][8];

        int messageIndex = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                matrixA[j][i] = message[messageIndex];
                messageIndex++;
            }
        }

        return matrixA;
    }


//    public static int[] padMessage(String message, int blockSize) {
//        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
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
//        // Copy the message bytes to the padded message array
//        System.arraycopy(messageBytes, 0, paddedMessage, 0, messageBytes.length);
//
//        // Append '1' bit to the message
//        paddedMessage[messageBytes.length] = (byte) 0x80;
//
//        // Append '0' bits
//        for (int i = messageBytes.length + 1; i < paddedLength - 8; i++) {
//            paddedMessage[i] = 0;
//        }
//
//        // Append length representation
//        long lengthValue = (long) numBlocks;
//        for (int i = 7; i >= 0; i--) {
//            paddedMessage[paddedLength - 8 + i] = (byte) ((lengthValue >> (8 * i)) & 0xFF);
//        }
//        int[] paddingoutput = new int[paddedMessage.length];
//        for (int i = 0; i < paddedMessage.length; i++) {
//            paddingoutput[i]=paddedMessage[i];
//        }
//        return paddingoutput;
//    }

    public static int[] padMessage(String message, int blockSize) {
        StringBuilder sb = new StringBuilder();
        byte[] messageBytes = message.getBytes(StandardCharsets.US_ASCII);
        int messageLength = messageBytes.length * 8; // Convert message length to bits
        for (byte b : messageBytes) {
            for (int i = 7; i >= 0; i--) {
                int bit = (b >> i) & 0x01;
                sb.append(bit);
            }
        }

        // Calculate the number of bits needed for padding
        int paddingBits = (-messageLength - 65) % blockSize;
        if (paddingBits < 0) {
            paddingBits += blockSize;
        }

        // Calculate the number of blocks in the padded message
        int numBlocks = (messageLength + paddingBits + 65) / blockSize;

        // Calculate the length of the padded message in bytes
        int paddedLength = numBlocks * blockSize / 8;

        // Append '1' bit to the message
        sb.append("1");

        // Append '0' bits
        for (int i = 0; i < paddingBits; i++) {
            sb.append("0");
        }

        // Append length representation
        long lengthValue = numBlocks;
        String binaryRepresentation = Long.toBinaryString(lengthValue);
        int numZerosToAdd = 64 - binaryRepresentation.length();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < numZerosToAdd; i++) {
            sb2.append("0"); // Dodawanie zer
        }
        sb2.append(binaryRepresentation);
        String bitString = sb2.toString();

        sb.append(bitString);

        String paddedMessageString = sb.toString();

        byte[] paddedMessage = new byte[paddedMessageString.length() / 8]; // Tworzenie tablicy bajtów

        for (int i = 0; i < paddedMessageString.length(); i += 8) {
            String byteString = paddedMessageString.substring(i, i + 8); // Pobieranie kolejnych 8 bitów
            byte byteValue = (byte) Integer.parseInt(byteString, 2); // Konwersja na bajt
            paddedMessage[i / 8] = byteValue; // Dodawanie bajtu do tablicy bajtów
        }
        int[] array = new int[paddedMessage.length];
        for (int i = 0; i < paddedMessage.length; i++) {
            array[i]=paddedMessage[i];
        }

        return array;
    }


//    public static byte[] padMessage(String message, int blockSize) {
//        byte[] messageBytes = message.getBytes(StandardCharsets.US_ASCII);
//        int messageLength = messageBytes.length * 8; // Convert message length to bits
//
//        // Calculate the number of bits needed for padding
//        int paddingBits = (-messageLength - 65) % blockSize;
//        if (paddingBits < 0) {
//            paddingBits += blockSize;
//        }
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
//        // Copy the message bytes to the padded message array
//        System.arraycopy(messageBytes, 0, paddedMessage, 0, messageBytes.length);
//
//        // Append '1' bit to the message
//        paddedMessage[messageBytes.length] = (byte) 0x80;
//
//        // Append '0' bits
//        for (int i = messageBytes.length + 1; i < paddedLength - 8; i++) {
//            paddedMessage[i] = 0;
//        }
//
//        // Append length representation
//        long lengthValue = (messageLength + paddingBits + 65) / blockSize;
//        for (int i = 7; i >= 0; i--) {
//            paddedMessage[paddedLength - 8 + i] = (byte) ((lengthValue >> (8 * i)) & 0xFF);
//        }
//        for (int i = 0; i < blockSize/8; i++) {
//            int x = (int)paddedMessage[i];
//            paddedMessage[i]=(byte)(x%255);
//        }
//
//        return paddedMessage;
//    }


    public static int[] mapMatrixToByteArray(int[][] matrix, int blocksize){
        int[] message = new int[64];
        int messageIndex = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                message[messageIndex]=matrix[j][i] ;
                messageIndex++;
            }
        }
        return message;
    }


    public static int[][] roundConstantsP(int[][] matrixA){
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
        for (int j = 0; j < matrixA[0].length; j++) {
            matrixA[0][j] ^= CycleP ^ matrixP[0][j];
        }
        CycleP++;
        return matrixA;
    }
    public static int[][] roundConstantsQ(int[][] matrixA){
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
        for (int i = 0; i < matrixA[0].length; i++) {
            for (int j = 0; j < matrixQ[0].length; j++) {
                if(i==7) {
                    matrixA[7][j] ^= CycleQ ^ matrixQ[i][j];
                }
                else matrixA[7][j] ^= matrixQ[i][j];
            }
        }
        CycleQ++;

        return matrixA;
    }

    public static int[][] subBytes(int[][] matrix){
        for(int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                int row = (matrix[i][j] & 0x0f);
                int column = (matrix[i][j] & 0xf0)/16 ;
                matrix[i][j]=sBox[column][row];
            }
        }
        return matrix;
    }

    public static int[][] shiftLeftP(int[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < i; j++) {
                int firstElement = matrix[i][0];
                System.arraycopy(matrix[i], 1, matrix[i], 0, matrix[i].length - 1);
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
                System.arraycopy(matrix[i], 1, matrix[i], 0, matrix[i].length - 1);
                matrix[i][matrix.length - 1] = firstElement;
            }
        }
        return matrix;
    }

    public static int[][] mixBytes( int[][] message, int columns )  //mixBytes zaczerpnięte z języka C i przepisane na Jave
    {
        byte[][] msg = new byte[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                msg[i][j]=(byte)message[i][j];
            }
        }
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
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                message[i][j]=msg[i][j]%256;
            }
        }
        return message;
    }
    public static byte mul1( byte b ) { return b ;}
    public static byte mul2( byte b ) { return ( byte )((0 != (b>>>7))?((b)<<1)^0x1b:((b)<<1)); }
    public static byte mul3( byte b ) { return ( byte )(mul2(b) ^ mul1(b)); }
    public static byte mul4( byte b ) { return ( byte )(mul2( mul2( b ))); }
    public static byte mul5( byte b ) { return ( byte )(mul4(b) ^ mul1(b)); }
    public static byte mul6( byte b ) { return ( byte )(mul4(b) ^ mul2(b)); }
    public static byte mul7( byte b ) { return ( byte )(mul4(b) ^ mul2(b) ^ mul1(b)); }
}


