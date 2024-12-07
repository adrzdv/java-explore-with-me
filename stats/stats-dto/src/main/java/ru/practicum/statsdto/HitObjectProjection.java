package ru.practicum.statsdto;

/**
 * Projection of HitObjects for displaying statistic
 */

public interface HitObjectProjection {

    /**
     * Returns app name of HitObject
     *
     * @return App name
     */
    String getApp();

    /**
     * Returns uri of HitObject
     *
     * @return Uri name
     */
    String getUri();

    /**
     * Returns the number of requests
     *
     * @return Hits number
     */
    int getHits();
}
