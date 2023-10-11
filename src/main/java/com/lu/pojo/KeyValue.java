package com.lu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValue {
    public String key;
    public double value;

    @Override
    public String toString() {
        return "key=" + key + ", value=" + value;
    }
}
