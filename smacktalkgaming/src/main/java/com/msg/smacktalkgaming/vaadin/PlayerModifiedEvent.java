package com.msg.smacktalkgaming.vaadin;

import java.io.Serializable;
import com.msg.smacktalkgaming.backend.domain.Player;

public class PlayerModifiedEvent implements Serializable {

    private final Player Player;

    public PlayerModifiedEvent(Player p) {
        this.Player = p;
    }

    public Player getPlayer() {
        return Player;
    }
    
}
