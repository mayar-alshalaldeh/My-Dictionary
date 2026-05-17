package application;

import java.util.Random;

public class Generator {
	
	    private String[] subjects = {"Boy","Cat","Student"};
	    private String[] verbs = {"eat","read","see"};
	    private String[] objects = {"apple","book","car"};

	    public String generateEnglish() {
	        Random r = new Random();
	        return subjects[r.nextInt(3)] + " " +
	               verbs[r.nextInt(3)] + " " +
	               objects[r.nextInt(3)];
	    }
	}


