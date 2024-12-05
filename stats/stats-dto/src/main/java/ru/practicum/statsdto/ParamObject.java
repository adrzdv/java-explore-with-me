package ru.practicum.statsdto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Object for collecting all request parameters in one
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParamObject {

    String start;
    String end;
    List<String> uris;
    Boolean unique;

}
