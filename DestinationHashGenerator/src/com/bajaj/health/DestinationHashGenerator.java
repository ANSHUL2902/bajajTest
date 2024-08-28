package com.bajaj.health;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import java.io.File;
import java.io.IOException;

import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DestinationHashGenerator {
	 public static void main(String[] args) {
	        if (args.length != 2) {
	            System.err.println("Usage: java -jar DestinationHashGenerator.jar <PRN Number> <path to JSON file>");
	            System.exit(1);
	        }

	        String prnNumber = args[0];
	        String jsonFilePath = args[1];

	        try {
	            
	            ObjectMapper mapper = new ObjectMapper();
	            JsonNode root = mapper.readTree(new File(jsonFilePath));

	          
	            String destinationValue = findDestinationValue(root);
	            if (destinationValue == null) {
	                System.err.println("Key 'destination' not found in JSON file.");
	                System.exit(1);
	            }

	        
	            String randomString = generateRandomString(8);

	          
	            String toHash = prnNumber + destinationValue + randomString;

	            
	            String md5Hash = generateMD5Hash(toHash);

	           
	            System.out.println(md5Hash + ";" + randomString);

	        } catch (IOException | NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	    }

	    private static String findDestinationValue(JsonNode node) {
	        if (node.isObject()) {
	            for (JsonNode child : node) {
	                if (child.has("destination")) {
	                    return child.get("destination").asText();
	                }
	                String result = findDestinationValue(child);
	                if (result != null) {
	                    return result;
	                }
	            }
	        } else if (node.isArray()) {
	            for (JsonNode element : node) {
	                String result = findDestinationValue(element);
	                if (result != null) {
	                    return result;
	                }
	            }
	        }
	        return null;
	    }

	    private static String generateRandomString(int length) {
	        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	        Random random = new Random();
	        StringBuilder sb = new StringBuilder(length);
	        for (int i = 0; i < length; i++) {
	            int index = random.nextInt(characters.length());
	            sb.append(characters.charAt(index));
	        }
	        return sb.toString();
	    }

	    private static String generateMD5Hash(String input) throws NoSuchAlgorithmException {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] digest = md.digest(input.getBytes());
	        StringBuilder sb = new StringBuilder();
	        for (byte b : digest) {
	            sb.append(String.format("%02x", b));
	        }
	        return sb.toString();
	    }
}
