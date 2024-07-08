import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Groestl {
    public static void main(String[] args) {

        byte[][] array = {
                {(byte) 0xef, (byte) 0xca, (byte) 0xb7, (byte) 0x04, (byte) 0x09, (byte) 0x53, (byte) 0xd0, (byte) 0x51},
                {(byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0xaa},
                {(byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0xfb, (byte) 0x63},
                {(byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0xcd, (byte) 0x63, (byte) 0x63},
                {(byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63},
                {(byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63},
                {(byte) 0x63, (byte) 0x7c, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63},
                {(byte) 0x7c, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63, (byte) 0x63}
        };
        byte essa = (byte)0xff;
        System.out.println(essa);


        System.out.println(Arrays.deepToString(mixBytes(shiftLeftP(subBytes(roundConstantsP(array))),8)));


        System.out.println(groestl("abc",512));

    }

    public static final byte[] initialVector = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x00};

    //public static final int initialVector = 0x00000000000000000000000000000010;
    public static int CycleP=0;
    public static int CycleQ=0;
    public static byte[] Hi;
    public static int init = 0;
    public static byte[][] sBox = {
            {(byte) 0x63, (byte) 0x7c, (byte) 0x77, (byte) 0x7b, (byte) 0xf2, (byte) 0x6b, (byte) 0x6f, (byte) 0xc5, (byte) 0x30, (byte) 0x01, (byte) 0x67, (byte) 0x2b, (byte) 0xfe, (byte) 0xd7, (byte) 0xab, (byte) 0x76},
            {(byte) 0xca, (byte) 0x82, (byte) 0xc9, (byte) 0x7d, (byte) 0xfa, (byte) 0x59, (byte) 0x47, (byte) 0xf0, (byte) 0xad, (byte) 0xd4, (byte) 0xa2, (byte) 0xaf, (byte) 0x9c, (byte) 0xa4, (byte) 0x72, (byte) 0xc0},
            {(byte) 0xb7, (byte) 0xfd, (byte) 0x93, (byte) 0x26, (byte) 0x36, (byte) 0x3f, (byte) 0xf7, (byte) 0xcc, (byte) 0x34, (byte) 0xa5, (byte) 0xe5, (byte) 0xf1, (byte) 0x71, (byte) 0xd8, (byte) 0x31, (byte) 0x15},
            {(byte) 0x04, (byte) 0xc7, (byte) 0x23, (byte) 0xc3, (byte) 0x18, (byte) 0x96, (byte) 0x05, (byte) 0x9a, (byte) 0x07, (byte) 0x12, (byte) 0x80, (byte) 0xe2, (byte) 0xeb, (byte) 0x27, (byte) 0xb2, (byte) 0x75},
            {(byte) 0x09, (byte) 0x83, (byte) 0x2c, (byte) 0x1a, (byte) 0x1b, (byte) 0x6e, (byte) 0x5a, (byte) 0xa0, (byte) 0x52, (byte) 0x3b, (byte) 0xd6, (byte) 0xb3, (byte) 0x29, (byte) 0xe3, (byte) 0x2f, (byte) 0x84},
            {(byte) 0x53, (byte) 0xd1, (byte) 0x00, (byte) 0xed, (byte) 0x20, (byte) 0xfc, (byte) 0xb1, (byte) 0x5b, (byte) 0x6a, (byte) 0xcb, (byte) 0xbe, (byte) 0x39, (byte) 0x4a, (byte) 0x4c, (byte) 0x58, (byte) 0xcf},
            {(byte) 0xd0, (byte) 0xef, (byte) 0xaa, (byte) 0xfb, (byte) 0x43, (byte) 0x4d, (byte) 0x33, (byte) 0x85, (byte) 0x45, (byte) 0xf9, (byte) 0x02, (byte) 0x7f, (byte) 0x50, (byte) 0x3c, (byte) 0x9f, (byte) 0xa8},
            {(byte) 0x51, (byte) 0xa3, (byte) 0x40, (byte) 0x8f, (byte) 0x92, (byte) 0x9d, (byte) 0x38, (byte) 0xf5, (byte) 0xbc, (byte) 0xb6, (byte) 0xda, (byte) 0x21, (byte) 0x10, (byte) 0xff, (byte) 0xf3, (byte) 0xd2},
            {(byte) 0xcd, (byte) 0x0c, (byte) 0x13, (byte) 0xec, (byte) 0x5f, (byte) 0x97, (byte) 0x44, (byte) 0x17, (byte) 0xc4, (byte) 0xa7, (byte) 0x7e, (byte) 0x3d, (byte) 0x64, (byte) 0x5d, (byte) 0x19, (byte) 0x73},
            {(byte) 0x60, (byte) 0x81, (byte) 0x4f, (byte) 0xdc, (byte) 0x22, (byte) 0x2a, (byte) 0x90, (byte) 0x88, (byte) 0x46, (byte) 0xee, (byte) 0xb8, (byte) 0x14, (byte) 0xde, (byte) 0x5e, (byte) 0x0b, (byte) 0xdb},
            {(byte) 0xe0, (byte) 0x32, (byte) 0x3a, (byte) 0x0a, (byte) 0x49, (byte) 0x06, (byte) 0x24, (byte) 0x5c, (byte) 0xc2, (byte) 0xd3, (byte) 0xac, (byte) 0x62, (byte) 0x91, (byte) 0x95, (byte) 0xe4, (byte) 0x79},
            {(byte) 0xe7, (byte) 0xc8, (byte) 0x37, (byte) 0x6d, (byte) 0x8d, (byte) 0xd5, (byte) 0x4e, (byte) 0xa9, (byte) 0x6c, (byte) 0x56, (byte) 0xf4, (byte) 0xea, (byte) 0x65, (byte) 0x7a, (byte) 0xae, (byte) 0x08},
            {(byte) 0xba, (byte) 0x78, (byte) 0x25, (byte) 0x2e, (byte) 0x1c, (byte) 0xa6, (byte) 0xb4, (byte) 0xc6, (byte) 0xe8, (byte) 0xdd, (byte) 0x74, (byte) 0x1f, (byte) 0x4b, (byte) 0xbd, (byte) 0x8b, (byte) 0x8a},
            {(byte) 0x70, (byte) 0x3e, (byte) 0xb5, (byte) 0x66, (byte) 0x48, (byte) 0x03, (byte) 0xf6, (byte) 0x0e, (byte) 0x61, (byte) 0x35, (byte) 0x57, (byte) 0xb9, (byte) 0x86, (byte) 0xc1, (byte) 0x1d, (byte) 0x9e},
            {(byte) 0xe1, (byte) 0xf8, (byte) 0x98, (byte) 0x11, (byte) 0x69, (byte) 0xd9, (byte) 0x8e, (byte) 0x94, (byte) 0x9b, (byte) 0x1e, (byte) 0x87, (byte) 0xe9, (byte) 0xce, (byte) 0x55, (byte) 0x28, (byte) 0xdf},
            {(byte) 0x8c, (byte) 0xa1, (byte) 0x89, (byte) 0x0d, (byte) 0xbf, (byte) 0xe6, (byte) 0x42, (byte) 0x68, (byte) 0x41, (byte) 0x99, (byte) 0x2d, (byte) 0x0f, (byte) 0xb0, (byte) 0x54, (byte) 0xbb, (byte) 0x16}
    };


    public static String groestl(String message, int blocksize){
        for (int i = 0; i < 10; i++) {
            functionH(divideMessage(padMessage(message,blocksize),blocksize), blocksize);
        }

        System.out.println(Arrays.toString(functionOmega(Hi)));
        //return intArrayToHexString(bytesToInts(functionOmega(Hi)));
        return encodeHexString(functionOmega(Hi));
    }
    public static int[] bytesToInts(byte[] byteArray) {
        int[] intArray = new int[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            intArray[i]=byteArray[i]%256;
        }
        return intArray;
    }
    public static String intArrayToHexString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int value : array) {
            String hexValue = Integer.toHexString(value);
            sb.append(hexValue);
        }
        return sb.toString();
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

    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }
    public static String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString();
    }


    public static void functionH(ArrayList<byte[]> message, int blocksize){
        //byte[] iv = ByteBuffer.allocate(64).putInt(initialVector).array();
        //byte[] output = new byte[64];
        if(init==0){//
            Hi =functionF(message.get(0),initialVector,blocksize);
            init++;
        }
        else Hi =functionF(message.get(0),Hi,blocksize);
        for (int i = 0; i < message.size() -1; i++) {
            Hi = functionF(message.get(i+1), Hi, blocksize);
        }
    }

    public static byte[] functionOmega (byte[] message){
        byte[] outputofP = PermutationP(message,512);
        for (int i = 0; i < message.length; i++) {
            outputofP[i] = (byte) (message[i] ^ outputofP[i]);
        }

        return Arrays.copyOfRange(outputofP,0,outputofP.length/2);
    }
    public static byte[] functionF (byte[] message,byte[] iv, int blocksize){
        byte[] hi = new byte[64];

        for (int i = 0; i < message.length; i++) {
            hi[i] = (byte) (message[i] ^ iv[i]);
        }

        message =PermutationQ(message, blocksize);
        hi = PermutationP(hi, blocksize);

        for (int i = 0; i < message.length; i++) {
            hi[i] = (byte) (message[i] ^ iv[i] ^ hi[i]);
        }

        return hi;
    }

    public static byte[] PermutationP(byte[] message, int blocksize){


        byte [][] matrixA=mixBytes(shiftLeftP(subBytes(roundConstantsP(mapMessageToMatrixA(message,512)))),8);


        return mapMatrixToByteArray(matrixA,blocksize);
    }

    public static byte[] PermutationQ(byte[] message, int blocksize){


        byte [][] matrixA=mixBytes(shiftLeftQ(subBytes(roundConstantsQ(mapMessageToMatrixA(message,512)))),8);


        return mapMatrixToByteArray(matrixA,blocksize);
    }

    public static ArrayList<byte[]> divideMessage(byte[] byteArray, int subarraySize) {
        ArrayList<byte[]> dividedArray = new ArrayList<>();

        int totalSubarrays = ((byteArray.length*6)/ subarraySize) +1;

        for (int i = 0; i < totalSubarrays; i++) {
            int startIndex = i * 64;
            int endIndex = (i+1) * 64 ;

            byte[] subarray = new byte[64];

            subarray=Arrays.copyOfRange(byteArray,startIndex,endIndex);

            dividedArray.add(subarray);
        }

        return dividedArray;
    }

    public static byte[][] mapMessageToMatrixA(byte[] message, int blockSize) {
        // Initialize matrix A
        byte[][] matrixA = new byte[8][8];

        int messageIndex = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                matrixA[j][i] = message[messageIndex];
                messageIndex++;
            }
        }

        return matrixA;
    }
    public static byte[] padMessage(String message, int blockSize) {
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

        return paddedMessage;
    }





    public static byte[] mapMatrixToByteArray(byte[][] matrix, int blocksize){
        byte[] message = new byte[64];
        int messageIndex = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                message[messageIndex]=matrix[j][i] ;
                messageIndex++;
            }
        }
        return message;
    }


    public static byte[][] roundConstantsP(byte[][] matrixA){
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
    public static byte[][] roundConstantsQ(byte[][] matrixA){
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
                    matrixA[7][j] ^= CycleQ ^ (byte)matrixQ[i][j];
                }
                else matrixA[7][j] ^= (byte)matrixQ[i][j];
            }
        }
        CycleQ++;

        return matrixA;
    }

    public static byte[][] subBytes(byte[][] matrix){
        for(int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                int row = (matrix[i][j] & 0x0f);
                int column = (matrix[i][j] & 0xf0)/16;
                matrix[i][j]=sBox[column][row];
            }
        }
        return matrix;
    }

    public static byte[][] shiftLeftP(byte[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < i; j++) {
                byte firstElement = matrix[i][0];
                System.arraycopy(matrix[i], 1, matrix[i], 0, matrix[i].length - 1);
                matrix[i][matrix.length - 1] = firstElement;
            }
        }
        return matrix;
    }
    public static byte[][] shiftLeftQ(byte[][] matrix){
        int[] permutationVector = {1,3,5,7,0,2,4,6};
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < permutationVector[i]; j++) {
                byte firstElement = matrix[i][0];
                System.arraycopy(matrix[i], 1, matrix[i], 0, matrix[i].length - 1);
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
}


