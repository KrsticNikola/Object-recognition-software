/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpClasses;

/**
 *
 * @author nik
 */
public class Timer {

    long startTime;

    public void timerStart() {
        startTime = System.currentTimeMillis();

    }

    public void timerShow() {
        long elapsedTime = System.currentTimeMillis() - startTime;
//        long elapsedSeconds = elapsedTime / 1000;
        long elapsedmiliSeconds = elapsedTime;
        System.out.println(elapsedmiliSeconds + " Milisekundi");
    }

    public long getTime() {
        long elapsedTime = System.currentTimeMillis() - startTime;
//        long elapsedSeconds = elapsedTime / 1000;
        long elapsedMiliSeconds = elapsedTime;
//        System.out.println(elapsedSeconds + " Sekundi");
        return elapsedMiliSeconds;
    }

    public long getTimeSeconds() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedTime / 1000;
//        long elapsedMiliSeconds = elapsedTime;
//        System.out.println(elapsedSeconds + " Sekundi");
        return elapsedSeconds;
    }

}
