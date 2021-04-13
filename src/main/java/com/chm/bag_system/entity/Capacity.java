package com.chm.bag_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @program: bag_system
 * @description
 * @author: BeiKe
 * @create: 2021-04-13 16:19
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class Capacity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer size;

    private Integer team;

    private String file;
}
