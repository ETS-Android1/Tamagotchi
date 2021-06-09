package com.example.tamagotchi.mvc2;

import android.os.Handler;

import com.example.tamagotchi.etats.Etat;
import com.example.tamagotchi.etats.EtatFatigueNotStonks;
import com.example.tamagotchi.etats.EtatFatigueStonks;
import com.example.tamagotchi.etats.EtatHeureuxNotStonks;
import com.example.tamagotchi.etats.EtatHeureuxStonks;
import com.example.tamagotchi.etats.EtatSleepRough;
import com.example.tamagotchi.tools.CryptoList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Random;

public class Pnl2_Mdl extends Observable {

    private int m_cash;
    private int m_energy;
    private Etat m_etat;
    private String m_phrase;
    private Handler m_Handler = new Handler();

    public Pnl2_Mdl(){
        m_cash = 100;
        m_energy = 100;
        m_etat = new EtatHeureuxStonks();

        setChanged();
        notifyObservers();

        m_Handler.postDelayed(mToastRunnable, 500);
    }

    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            passTime();
            m_Handler.postDelayed(this, 500);
        }
    };

    void nouvelEtat()
    {
        m_etat.etapeSuivante(this);
    }

    public void changeEtat(int etatNumber){
        if(m_etat != null){
            m_etat = null;
        }

        switch (etatNumber){
            case 1: // heureux & stonks
                m_etat = new EtatHeureuxStonks();
                break;
            case 2: // heureux mais notstonks
                m_etat = new EtatHeureuxNotStonks();
                break;
            case 3: // pas heureux mais stonks
                m_etat = new EtatFatigueStonks();
                break;
            case 4: // pas heureux & notstonks
                m_etat = new EtatFatigueNotStonks();
                break;
            case 5: // mort
                m_etat = new EtatSleepRough();
                break;
        }
    }

    public void work(){
        m_cash += 2;
        if(m_cash > 100){
            m_cash = 100;
        }
        nouvelEtat();
    }

    public void sleep(){
        m_energy += 5;
        if(m_energy < 100){
            m_energy = 100;
        }
        nouvelEtat();
    }

    public void giveCrypto(int value){
        m_cash += value;
        if(m_cash > 100){
            m_cash = 100;
        }
        nouvelEtat();
    }

    public void invest(String namePlayer, String nameTama){
        final List<CryptoList> list = Collections.unmodifiableList(Arrays.asList(CryptoList.values()));

        Random random = new Random(); // creating Random object

        int value = random.nextInt(15);
        CryptoList randomCrypto = list.get(random.nextInt(list.size()));

        if(random.nextBoolean() == false) { // displaying a random boolean
            // down
            value *= -1;
        }

        giveCrypto(value);

        if(value < 0){
            m_phrase = nameTama + " a achteté la crypto-monnaie " + randomCrypto.getName() + ", c'était un mauvais coup vous avez perdu de l'argent MR." + namePlayer;
        } else {
            m_phrase = nameTama + "Votre joueur a achteté la crypto-monnaie " + randomCrypto.getName() + ", c'était un bon coup vous avez gagné de l'argent MR." + namePlayer;
        }

        nouvelEtat();
    }

    public void passTime(){
        if(m_energy > 0)
            m_energy--;
        if(m_cash > 0)
            m_cash--;
        nouvelEtat();
        setChanged();
        notifyObservers();
    }

    public int getM_cash() { return m_cash; }

    public int getM_energy() { return m_energy; }

    public Etat getM_etat() { return m_etat; }

    public String getM_phrase() { return m_phrase; }

}
