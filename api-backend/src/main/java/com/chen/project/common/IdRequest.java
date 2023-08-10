package com.chen.project.common;

import com.chen.project.constant.CommonConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求
 *
 * @author CSY
 */
@Data
public class IdRequest implements Serializable {

    /**
     * id
     */
    private long id;

    private static final long serialVersionUID = 1L;

}
