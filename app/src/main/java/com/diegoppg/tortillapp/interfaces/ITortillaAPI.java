package com.diegoppg.tortillapp.interfaces;

import com.diegoppg.tortillapp.modelo.Tortilla;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ITortillaAPI {
    public void addTortilla(String name, double latitude, double longitude);

    public CompletableFuture<List<Tortilla>> listTortillas();
}
