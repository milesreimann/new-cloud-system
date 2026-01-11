package io.github.milesreimann.cloudsystem.application.service;

import io.github.milesreimann.cloudsystem.api.model.Resources;
import io.github.milesreimann.cloudsystem.api.runtime.Node;
import io.github.milesreimann.cloudsystem.master.domain.model.ResourcesImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Miles R.
 * @since 11.01.2026
 */
public class NodeReservationService {
    private final Map<String, Resources> reservations = new ConcurrentHashMap<>();

    public boolean tryReserve(Node node, Resources reserve) {
        return reservations.compute(node.getName(), (_, current) -> {
            Resources reserved = current != null
                ? current
                : ResourcesImpl.empty();

            if (!canFitWithReservation(node, reserved, reserve)) {
                return reserved;
            }

            return reserved.add(reserve);
        }) != null;
    }

    public Resources getReserved(Node node) {
        return reservations.get(node.getName());
    }

    public void release(Node node, Resources r) {
        reservations.computeIfPresent(node.getName(),
            (_, current) -> current.subtract(r));
    }

    private boolean canFitWithReservation(Node node, Resources reserved, Resources reserve) {
        Resources available = node.getCapacity()
            .subtract(node.getUsage())
            .subtract(reserved);

        return available.fits(reserve);
    }
}