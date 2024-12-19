package ru.practicum.stats;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.statsdto.HitObject;
import ru.practicum.stats.service.StatsService;
import ru.practicum.statsdto.HitObjectDto;
import ru.practicum.statsdto.ParamObject;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@EntityScan(basePackages = "ru.practicum.statsdto")
class StatsServiceTests {

    @Autowired
    private StatsService service;
    @Autowired
    private EntityManager em;
    HitObject objOne;
    HitObject objTwo;

    @BeforeEach
    void setUp() {
        objOne = HitObject.builder()
                .app("some-app-test")
                .uri("/some/uri/1")
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();

        objTwo = HitObject.builder()
                .app("some-app-test")
                .uri("/some/test/1")
                .ip("127.0.0.127")
                .timestamp(LocalDateTime.now().plusDays(15L))
                .build();
    }

    @Test
    void saveNewHit() {

        HitObject dbObjOne = service.hit(objOne);
        HitObject dbObjTwo = service.hit(objTwo);

        TypedQuery<HitObject> objOneQue = em.createQuery("Select h from HitObject h where h.id = :id",
                HitObject.class);
        HitObject iqo = objOneQue.setParameter("id", dbObjOne.getId()).getSingleResult();

        TypedQuery<HitObject> objTwoQue = em.createQuery("Select h from HitObject h where h.id = :id",
                HitObject.class);
        HitObject iqt = objTwoQue.setParameter("id", dbObjTwo.getId()).getSingleResult();

        assertAll("Verify first object",
                () -> assertThat(iqo.getId(), equalTo(objOne.getId())),
                () -> assertThat(iqo.getApp(), equalTo(objOne.getApp())),
                () -> assertThat(iqo.getUri(), equalTo(objOne.getUri())),
                () -> assertThat(iqo.getIp(), equalTo(objOne.getIp())),
                () -> assertThat(iqo.getTimestamp(), equalTo(objOne.getTimestamp())));

        assertAll("Verify second object",
                () -> assertThat(iqt.getId(), equalTo(objTwo.getId())),
                () -> assertThat(iqt.getApp(), equalTo(objTwo.getApp())),
                () -> assertThat(iqt.getUri(), equalTo(objTwo.getUri())),
                () -> assertThat(iqt.getIp(), equalTo(objTwo.getIp())),
                () -> assertThat(iqt.getTimestamp(), equalTo(objTwo.getTimestamp())));

    }

    @Test
    void getTestStats() throws BadRequestException {

        HitObject dbObjOne = service.hit(objOne);
        HitObject dbObjTwo = service.hit(objTwo);

        String start = "2024-01-01T00:00:00";
        String end = "2025-12-31T00:00:00";

        ParamObject params = ParamObject.builder()
                .start(start)
                .end(end)
                .build();

        List<HitObjectDto> res = service.viewStats(params);

        assertThat(res.size(), equalTo(2));

        params.setUnique(true);

        res = service.viewStats(params);

        assertThat(res.size(), equalTo(2));

        params.setUris(List.of("/some/uri/1"));

        res = service.viewStats(params);

        assertThat(res.size(), equalTo(1));

    }

}