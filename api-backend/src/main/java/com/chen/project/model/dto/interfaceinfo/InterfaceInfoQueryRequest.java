package com.chen.project.model.dto.interfaceinfo;


import com.chen.project.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author CSY
 */
//@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口主机
     */
    private String host;

    /**
     * 接口地址
     */
    private String url;
    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 创建人
     */
    private Long userId;

}
