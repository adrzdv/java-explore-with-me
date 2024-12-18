package ru.practicum.stats.service;

import ru.practicum.statsdto.HitObject;
import ru.practicum.statsdto.HitObjectDto;
import ru.practicum.statsdto.ParamObject;

import java.util.List;

public interface StatsService {

    /**
     * A method for saving information about a request to an endpoint.
     *
     * @param object
     * @return
     */
    HitObject hit(HitObject object);

    /**
     * A method for getting statistic
     *
     * @param params Object of request parameters
     * @return List of HitObjectProjection
     */
    List<HitObjectDto> viewStats(ParamObject params);
}
