package com.lu.javabean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamplePath {
    private String path;
    private String range;
    private String resultHref;
}
