package main;

import game.AstralBastralGame;

import javax.xml.crypto.Data;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.rmi.server.ExportException;

public class Main {

    public static void main(String[] args) {
        AstralBastralGame game = new AstralBastralGame();
        long startTime = System.nanoTime();
        game.makeTurn();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println(duration / 1000);
        startTime = System.nanoTime();
        game.makeTurn();
        endTime = System.nanoTime();

        duration = (endTime - startTime);
        System.out.println(duration / 1000);
        startTime = System.nanoTime();
        game.makeTurn();
        endTime = System.nanoTime();

        duration = (endTime - startTime);
        System.out.println(duration / 1000);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.write(10);
            out.write(12);
            out.flush();
            byte[] a = stream.toByteArray();
            stream.reset();
            out.write(20);
            out.write(22);
            out.flush();
            byte[] b = stream.toByteArray();
            for (int i = 0; i < a.length; i++) {
                System.out.println(a[i]);
                System.out.println(b[i]);
            }
        }
        catch(Exception e) {

        }
    }
}
