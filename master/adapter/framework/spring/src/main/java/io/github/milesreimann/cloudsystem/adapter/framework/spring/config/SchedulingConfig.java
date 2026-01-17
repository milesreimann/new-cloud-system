package io.github.milesreimann.cloudsystem.adapter.framework.spring.config;

import io.github.milesreimann.cloudsystem.application.scheduling.strategy.filter.NodeStatusStrategy;
import io.github.milesreimann.cloudsystem.application.scheduling.strategy.filter.RequiredLabelStrategy;
import io.github.milesreimann.cloudsystem.application.scheduling.strategy.filter.ResourceAvailabilityStrategy;
import io.github.milesreimann.cloudsystem.application.scheduling.strategy.scoring.LeastLoadedScore;
import io.github.milesreimann.cloudsystem.application.scheduling.strategy.scoring.PreferredLabelScore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Miles R.
 * @since 16.01.2026
 */
@Configuration
public class SchedulingConfig {
    @Bean
    public ResourceAvailabilityStrategy resourceAvailabilityStrategy() {
        return new ResourceAvailabilityStrategy();
    }

    @Bean
    public RequiredLabelStrategy requiredLabelStrategy() {
        return new RequiredLabelStrategy();
    }

    @Bean
    public NodeStatusStrategy nodeStatusStrategy() {
        return new NodeStatusStrategy();
    }

    // Scores
    @Bean
    public PreferredLabelScore preferredLabelScore() {
        return new PreferredLabelScore();
    }

    @Bean
    public LeastLoadedScore leastLoadedScore() {
        return new LeastLoadedScore(0.6F, 0.4F);
    }
}