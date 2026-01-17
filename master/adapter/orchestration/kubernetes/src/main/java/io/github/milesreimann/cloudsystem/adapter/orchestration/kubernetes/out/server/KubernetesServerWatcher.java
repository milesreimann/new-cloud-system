package io.github.milesreimann.cloudsystem.adapter.orchestration.kubernetes.out.server;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;

/**
 * @author Miles R.
 * @since 10.01.2026
 */
public class KubernetesServerWatcher implements Watcher<Pod> {
    public KubernetesServerWatcher(KubernetesClient kubernetesClient) {
        kubernetesClient.pods().watch(this);
    }

    @Override
    public void eventReceived(Action action, Pod resource) {
        if (action == Action.MODIFIED) {
            System.out.println(resource.getMetadata().getName() + " " + resource.getStatus().getPhase());
        } else if (action == Action.ERROR) {
            System.out.println("oh no");
        }
    }

    @Override
    public void onClose(WatcherException cause) {

    }
}
