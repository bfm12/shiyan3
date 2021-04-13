package com.chm.bag_system.toolClass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @program: bag_system
 * @description
 * @author: BeiKe
 * @create: 2021-04-13 19:21
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Scatter {

    private String fileName;

    private String group;

    private String type;
}
