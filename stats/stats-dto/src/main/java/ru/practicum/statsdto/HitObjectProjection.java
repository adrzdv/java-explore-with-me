package ru.practicum.statsdto;

/**
 * Projection of HitObjects for displaying statistic
 */

public interface HitObjectProjection {

    String getApp();

    String getUri();

    int getHits();
}
